package lik.kindle.cap.service.impl;

import lik.kindle.cap.model.*;
import lik.kindle.cap.service.CrawlerService;
import lik.kindle.cap.service.ManagerService;
import lik.kindle.cap.service.StorageService;
import lik.kindle.cap.service.UrlParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author langyi
 * @date 2017/1/27.
 */
@Service
public class ManagerServiceImpl implements ManagerService {
    private static Logger log = LoggerFactory.getLogger(ManagerServiceImpl.class);

    //临时信息放置到别的地方
    private final static ConcurrentHashMap<String, CrawlerTask> crawlerTaskMap = new ConcurrentHashMap<>();

    //todo@langyi 每个网站使用单独的线程池来爬取
    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    private CrawlerService crawlerService;

    @Qualifier("fileStorageServiceImpl")
    @Autowired
    private StorageService fileStorageService;
    @Qualifier("urlParamServiceImpl")
    @Autowired
    private UrlParamService urlParamService;

    @Override
    public String submitCrawlerTask(final String url) throws MalformedURLException {
        String uuid = UUID.randomUUID().toString();
        final CrawlerTask crawlerTask = new CrawlerTask();
        ListParam listParam = urlParamService.generateListPram(url);
        final BodyParam bodyParam = urlParamService.generateBodyPram(url);
        final Crawler crawler = crawlerService.crawler(url, listParam);
        crawlerTask.setCrawler(crawler);
        crawlerTaskMap.put(uuid, crawlerTask);
        if (!crawler.isSuccess()) {
            crawlerTask.setFinishNum(-1);
            return uuid;
        }
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                List<CrawlerBody> bodyList = crawler.getCrawlerBodyList();
                final File path = new File("/Users/likan/Desktop/", crawler.getName());
                for (final CrawlerBody crawlerBody : bodyList) {
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                crawlerService.crawler(crawlerBody, bodyParam);
                                try {
                                    fileStorageService.storage(crawlerBody, path);
                                } catch (Exception e) {
                                    log.warn("save fail", e);
                                }
                            } finally {
                                crawlerTask.stepFinishNum();

                                if (crawlerTask.isFinish()) {
                                    fileStorageService.storageToc(crawler,path);
                                }
                            }
                        }
                    });
                }
            }
        });
        return uuid;
    }

    @Override
    public CrawlerTask getCrawlerTask(String uuid) {
        return crawlerTaskMap.get(uuid);
    }


}
