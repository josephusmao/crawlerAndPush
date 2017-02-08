package lik.kindle.cap.model;

/**
 * @author langyi
 * @date 2017/2/8.
 */
public class CrawlerTask {
    private Crawler crawler;

    private transient int finishNum;

    public boolean isFinish(){
        return crawler != null && (finishNum < 0 || finishNum == crawler.getCrawlerBodyList().size());
    }

    public void stepFinishNum(){
        finishNum++;
    }

    public void setFinishNum(int finishNum) {
        this.finishNum = finishNum;
    }

    public int getFinishNum() {
        return finishNum;
    }

    public Crawler getCrawler() {
        return crawler;
    }

    public void setCrawler(Crawler crawler) {
        this.crawler = crawler;
    }
}
