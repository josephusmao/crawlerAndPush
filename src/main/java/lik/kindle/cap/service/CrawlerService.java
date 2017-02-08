package lik.kindle.cap.service;

import lik.kindle.cap.model.BodyParam;
import lik.kindle.cap.model.Crawler;
import lik.kindle.cap.model.CrawlerBody;
import lik.kindle.cap.model.ListParam;

import java.net.MalformedURLException;

/**
 * @author langyi
 * @date 2017/1/27.
 */
public interface CrawlerService {

    void crawler(CrawlerBody param, BodyParam bodyParam);

    Crawler crawler(String url, ListParam listParam) throws MalformedURLException;

}
