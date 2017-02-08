package lik.kindle.cap.service.impl;

import lik.kindle.cap.model.Crawler;
import lik.kindle.cap.model.CrawlerBody;
import lik.kindle.cap.service.StorageService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.velocity.LikVelocityLayoutViewResolver;

import java.io.File;

/**
 * @author langyi
 * @date 2017/2/8.
 */
@Service
public class FileStorageServiceImpl implements StorageService {
    private static Logger log = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    @Autowired
    private LikVelocityLayoutViewResolver velocityLayoutViewResolver;

    @Override
    public void storage(CrawlerBody body, File path) throws Exception {
        if (!body.isSuccess()) {
            return;
        }
        mkdirs(path);
        String bodyString = toSaveString(body);
        File file = new File(path, body.getNum() + ".html");
        FileUtils.write(file, bodyString,"utf8");
    }

    @Override
    public void storageToc(Crawler crawler, File path)  {
        try {
            mkdirs(path);
            saveFile(crawler, path, "view/mobi_toc", "toc.html");
            saveFile(crawler, path, "view/mobi_ncx", "toc.ncx");
            saveFile(crawler, path, "view/mobi_opf", "xs.opf");
        } catch (Exception e) {
            log.error("save file", e);
        }
    }

    private void saveFile(Crawler crawler, File path, String viewName, String fileName) throws Exception {
        String listString = toSaveString(crawler, viewName);
        File file = new File(path, fileName);
        FileUtils.write(file, listString, "utf8");
    }

    private void mkdirs(File path) throws Exception {
        if (!path.isDirectory()) {
            synchronized (path) {
                if (!path.mkdirs()) {
                    throw new Exception("创建目录失败！");
                }
            }
        }
    }

    private String toSaveString(CrawlerBody body) throws Exception {
        ModelMap modelMap = new ModelMap();
        modelMap.put("title", body.getTitle());
        modelMap.put("body", body.getBody());
        return velocityLayoutViewResolver.merge("view/mobi", modelMap);
    }

    private String toSaveString(Crawler crawler,String view) throws Exception {
        ModelMap modelMap = new ModelMap();
        modelMap.put("crawler", crawler);
        return velocityLayoutViewResolver.merge(view, modelMap);
    }
}
