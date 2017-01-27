package lik.kindle.cap.service.impl.impl;

import lik.kindle.cap.model.BodyParam;
import lik.kindle.cap.model.CrawlerParam;
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
import java.util.List;
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
    public void crawler(CrawlerParam param,BodyParam bodyParam) {
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
        System.out.println("爬取失败!");
    }


    @Override
    public List<CrawlerParam> crawler(String url, ListParam listParam) throws MalformedURLException {
        ArrayList<CrawlerParam> crawlerParams = new ArrayList<>();
        CrawlerParam crawlerParam = new CrawlerParam();
        crawlerParam.setUrl(url);
        crawler(crawlerParam, listParam);
        URL tempUrl = new URL(url);
        String host = tempUrl.getProtocol() + "://" + tempUrl.getHost();
        String hri = host + tempUrl.getPath();
        if (crawlerParam.isSuccess()) {
            Pattern pattern = Pattern.compile(listParam.getReg());
            Matcher matcher = pattern.matcher(crawlerParam.getBody());
            int num = 1;
            while (matcher.find() && matcher.groupCount() == 2) {
                CrawlerParam param = new CrawlerParam();
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
                crawlerParams.add(param);
            }
        }
        return crawlerParams;
    }

}
