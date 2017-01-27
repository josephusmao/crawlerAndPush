package lik.kindle.cap.model;

/**
 * @author langyi
 * @date 2017/1/27.
 */
public class ListParam extends BodyParam{
    private String reg;
    private int titleNum = 2;
    private int urlNum = 1;


    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public int getTitleNum() {
        return titleNum;
    }

    public void setTitleNum(int titleNum) {
        this.titleNum = titleNum;
    }

    public int getUrlNum() {
        return urlNum;
    }

    public void setUrlNum(int urlNum) {
        this.urlNum = urlNum;
    }
}
