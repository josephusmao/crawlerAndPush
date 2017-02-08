package test.lik.kindle.cap.service.impl.impl;

import lik.kindle.cap.model.BodyParam;
import lik.kindle.cap.model.CrawlerBody;
import lik.kindle.cap.model.ListParam;
import lik.kindle.cap.service.CrawlerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.velocity.LikVelocityLayoutViewResolver;
import test.BaseTest;

import java.util.List;

/**
 * @author langyi
 * @date 2017/1/27.
 */
public class CrawlerServiceImplTest extends BaseTest{


    @Autowired
    private LikVelocityLayoutViewResolver velocityLayoutViewResolver;

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

        List<CrawlerBody> crawlerBodies = crawlerService.crawler("http://www.qu.la/book/176/", listParam).getCrawlerBodyList();

        for (int i = 0; i < 30; i++) {
            CrawlerBody crawlerBody = crawlerBodies.get(i);
            crawlerService.crawler(crawlerBody, bodyParam);
        }

        for (int i = 0; i < 30; i++) {
            CrawlerBody crawlerBody = crawlerBodies.get(i);
            System.out.println(crawlerBody);
        }

    }

    @Test
    public void crawlerBody() throws Exception {
        CrawlerBody param = new CrawlerBody();
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

        List<CrawlerBody> crawlerBodies = crawlerService.crawler("http://www.qu.la/book/176/", listParam).getCrawlerBodyList();

        for (int i = 0; i < 30; i++) {
            System.out.println(crawlerBodies.get(i));
        }
    }

    @Test
    public void testVelocityWrite() throws Exception {
        CrawlerBody param = new CrawlerBody();
        BodyParam bodyParam = new BodyParam();
        param.setUrl("http://www.qu.la/book/176/143150.html");
        bodyParam.setBodyBegin("<div id=\"content\"><script>readx();</script>");
        bodyParam.setBodyEnd("</div>");
        param.setNum(2);
        param.setTitle("49329847839dh");
        crawlerService.crawler(param,bodyParam);

        ModelMap modelMap = new ModelMap();
        modelMap.put("title", param.getTitle());
        modelMap.put("body", param.getBody());
        String html = velocityLayoutViewResolver.merge("view/mobi", modelMap);
        System.out.println(html);
    }
}