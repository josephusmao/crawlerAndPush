package test.lik.kindle.cap.service.impl;

import lik.kindle.cap.model.CrawlerTask;
import lik.kindle.cap.service.ManagerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import test.BaseTest;

/**
 * @author langyi
 * @date 2017/2/8.
 */
public class ManagerServiceImplTest extends BaseTest {


    @Qualifier("managerServiceImpl")
    @Autowired
    private ManagerService managerService;

    @Test
    public void submitCrawlerTask() throws Exception {
        String uuid = managerService.submitCrawlerTask("http://www.qu.la/book/26979/");
        CrawlerTask crawlerTask = managerService.getCrawlerTask(uuid);


        while (!crawlerTask.isFinish()) {
            Thread.sleep(10000);
        }

        System.out.println("num:" + crawlerTask.getFinishNum());

    }

}