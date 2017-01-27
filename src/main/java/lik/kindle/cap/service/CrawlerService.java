package lik.kindle.cap.service;

import lik.kindle.cap.model.BodyParam;
import lik.kindle.cap.model.CrawlerParam;
import lik.kindle.cap.model.ListParam;

import java.net.MalformedURLException;
import java.util.List;

/**
 * @author langyi
 * @date 2017/1/27.
 */
public interface CrawlerService {

    void crawler(CrawlerParam param, BodyParam bodyParam);

    List<CrawlerParam> crawler(String url, ListParam listParam) throws MalformedURLException;

}
