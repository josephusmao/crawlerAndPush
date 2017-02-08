package lik.kindle.cap.service;

import lik.kindle.cap.model.Crawler;
import lik.kindle.cap.model.CrawlerBody;

import java.io.File;

/**
 * @author langyi
 * @date 2017/2/8.
 */
public interface StorageService {
    void storage(CrawlerBody body, File path) throws Exception;

    void storageToc(Crawler crawler, File path);
}
