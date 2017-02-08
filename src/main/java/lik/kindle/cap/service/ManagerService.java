package lik.kindle.cap.service;

import lik.kindle.cap.model.CrawlerTask;

import java.net.MalformedURLException;

/**
 * @author langyi
 * @date 2017/1/27.
 */
public interface ManagerService {
    String submitCrawlerTask(String url) throws MalformedURLException;

    CrawlerTask getCrawlerTask(String uuid);
}
