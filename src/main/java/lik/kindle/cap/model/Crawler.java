package lik.kindle.cap.model;

import java.util.List;

/**
 * @author langyi
 * @date 2017/2/8.
 */
public class Crawler {
    private String name;
    private List<CrawlerBody> crawlerBodyList;

    public boolean isSuccess(){
        return crawlerBodyList != null && crawlerBodyList.size() > 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CrawlerBody> getCrawlerBodyList() {
        return crawlerBodyList;
    }

    public void setCrawlerBodyList(List<CrawlerBody> crawlerBodyList) {
        this.crawlerBodyList = crawlerBodyList;
    }
}
