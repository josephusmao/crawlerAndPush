package test.lik.kindle.cap.service.impl;

import lik.kindle.cap.model.BodyParam;
import lik.kindle.cap.model.CrawlerBody;
import lik.kindle.cap.service.CrawlerService;
import lik.kindle.cap.service.impl.FileStorageServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import test.BaseTest;

import java.io.File;

/**
 * @author langyi
 * @date 2017/2/8.
 */
public class FileStorageServiceImplTest extends BaseTest {


    @Qualifier("crawlerServiceImpl")
    @Autowired
    private CrawlerService crawlerService;
    @Qualifier("fileStorageServiceImpl")
    @Autowired
    private FileStorageServiceImpl fileStorageService;


    @Test
    public void storage() throws Exception {
        CrawlerBody param = new CrawlerBody();
        BodyParam bodyParam = new BodyParam();
        param.setUrl("http://www.qu.la/book/176/143150.html");
        bodyParam.setBodyBegin("<div id=\"content\"><script>readx();</script>");
        bodyParam.setBodyEnd("</div>");
        param.setNum(2);
        param.setTitle("49329847839dh");
        crawlerService.crawler(param,bodyParam);

        fileStorageService.storage(param, new File("/Users/likan/Desktop/test/"));

    }

}