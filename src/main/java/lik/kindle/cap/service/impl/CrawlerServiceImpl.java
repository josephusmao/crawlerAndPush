package lik.kindle.cap.service.impl;

import lik.kindle.cap.model.BodyParam;
import lik.kindle.cap.model.Crawler;
import lik.kindle.cap.model.CrawlerBody;
import lik.kindle.cap.model.ListParam;
import lik.kindle.cap.service.CrawlerService;
import lik.kindle.cap.service.HttpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author langyi
 * @date 2017/1/27.
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {
    private static Logger log = LoggerFactory.getLogger(CrawlerServiceImpl.class);
    @Autowired
    private HttpClientService httpClientService;

    @Override
    public void crawler(CrawlerBody param, BodyParam bodyParam) {
        if (param.isSuccess()) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            String html = httpClientService.doGet(param.getUrl());
            if (html != null) {
                int temp = html.indexOf(bodyParam.getBodyBegin());
                if (temp > 0) {
                    String body = html.substring(temp + bodyParam.getBodyBegin().length());
                    temp = body.indexOf(bodyParam.getBodyEnd());
                    if (temp > 0) {
                        body = body.substring(0, temp);
                        param.setBody(body);
                        System.out.println(body);
                        return;
                    }
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                //
            }
        }
        System.out.println("爬取失败!");
    }


    @Override
    public Crawler crawler(String url, ListParam listParam) throws MalformedURLException {
        ArrayList<CrawlerBody> crawlerBodies = new ArrayList<>();
        CrawlerBody crawlerBody = new CrawlerBody();
        crawlerBody.setUrl(url);
        crawler(crawlerBody, listParam);
        if (crawlerBody.isSuccess()) {
            URL tempUrl = new URL(url);
            String host = tempUrl.getProtocol() + "://" + tempUrl.getHost();
            String hri = host + tempUrl.getPath();
            if (crawlerBody.isSuccess()) {
                Pattern pattern = Pattern.compile(listParam.getReg());
                Matcher matcher = pattern.matcher(crawlerBody.getBody());
                int num = 1;
                while (matcher.find() && matcher.groupCount() == 2) {
                    CrawlerBody param = new CrawlerBody();
                    param.setNum(num++);
                    param.setTitle(matcher.group(listParam.getTitleNum()));
                    param.setUrl(matcher.group(listParam.getUrlNum()));
                    if (!param.getUrl().startsWith("http:")){
                        if (param.getUrl().startsWith("/")) {
                            param.setUrl(host + param.getUrl());
                        }else{
                            param.setUrl(hri + param.getUrl());
                        }
                    }
                    crawlerBodies.add(param);
                }
            }
        }
        Crawler crawler = new Crawler();
        crawler.setCrawlerBodyList(crawlerBodies);
        crawler.setName(crawlerBody.getTitle() == null ? "小说" : crawlerBody.getTitle());
        return crawler;
    }

}
