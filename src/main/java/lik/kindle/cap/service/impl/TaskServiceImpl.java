package lik.kindle.cap.service.impl;

import lik.kindle.cap.service.CrawlerService;
import lik.kindle.cap.service.StorageService;
import lik.kindle.cap.service.TaskService;
import lik.kindle.cap.service.UrlParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author langyi
 * @date 2017/1/27.
 */
@Service
public class TaskServiceImpl implements TaskService {
    private static Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private CrawlerService crawlerService;

    @Qualifier("fileStorageServiceImpl")
    @Autowired
    private StorageService fileStorageService;
    @Qualifier("urlParamServiceImpl")
    @Autowired
    private UrlParamService urlParamService;

    @Override
    public void crawlerAndSave(String url) throws Exception {
        //todo@langyi get listParam


    }
}
