package test.lik.kindle.cap.service.impl.impl;

import lik.kindle.cap.model.BodyParam;
import lik.kindle.cap.model.CrawlerParam;
import lik.kindle.cap.model.ListParam;
import lik.kindle.cap.service.CrawlerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.BaseTest;

import java.util.List;

/**
 * @author langyi
 * @date 2017/1/27.
 */
public class CrawlerServiceImplTest extends BaseTest{


    @Autowired
    private CrawlerService crawlerService;

    @Test
    public void crawler() throws Exception {
        BodyParam bodyParam = new BodyParam();
        bodyParam.setBodyBegin("<div id=\"content\"><script>readx();</script>");
        bodyParam.setBodyEnd("</div>");

        ListParam listParam = new ListParam();
        listParam.setReg("<a\\s*href\\s*=\"([^\"]*)\"\\s*>([^<]*)<\\/a>");
        listParam.setBodyBegin("\t\t\t\t<dl>");
        listParam.setBodyEnd("\t\t\t\t</dl>");

        List<CrawlerParam> crawlerParams = crawlerService.crawler("http://www.qu.la/book/176/", listParam);

        for (int i = 0; i < 30; i++) {
            CrawlerParam crawlerParam = crawlerParams.get(i);
            crawlerService.crawler(crawlerParam, bodyParam);
        }

        for (int i = 0; i < 30; i++) {
            CrawlerParam crawlerParam = crawlerParams.get(i);
            System.out.println(crawlerParam);
        }

    }

    @Test
    public void crawlerBody() throws Exception {
        CrawlerParam param = new CrawlerParam();
        BodyParam bodyParam = new BodyParam();
        param.setUrl("http://www.qu.la/book/176/143150.html");
        bodyParam.setBodyBegin("<div id=\"content\"><script>readx();</script>");
        bodyParam.setBodyEnd("</div>");
        param.setNum(2);
        param.setTitle("49329847839dh");
        crawlerService.crawler(param,bodyParam);
    }

    @Test
    public void crawLerList() throws Exception {
        ListParam listParam = new ListParam();
        listParam.setReg("<a\\s*href\\s*=\"([^\"]*)\"\\s*>([^<]*)<\\/a>");
        listParam.setBodyBegin("\t\t\t\t<dl>");
        listParam.setBodyEnd("\t\t\t\t</dl>");

        List<CrawlerParam> crawlerParams = crawlerService.crawler("http://www.qu.la/book/176/", listParam);

        for (int i = 0; i < 30; i++) {
            System.out.println(crawlerParams.get(i));
        }
    }
}