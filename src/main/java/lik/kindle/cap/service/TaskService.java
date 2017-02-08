package lik.kindle.cap.service;

import java.net.MalformedURLException;

/**
 * @author langyi
 * @date 2017/1/27.
 */
public interface TaskService {
    void crawlerAndSave(String url) throws MalformedURLException, Exception;
}
