package lik.kindle.cap.model;

/**
 * @author langyi
 * @date 2017/1/27.
 */
public class CrawlerParam {
    private String url;
    private String title;
    private int num;
    private String body;

    @Override
    public String toString() {
        return num + "\t[" + title + "][" + url + "]:" + (isSuccess() ? body.substring(0, 10) : "fail");
    }

    public boolean isSuccess(){
        return body != null;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
