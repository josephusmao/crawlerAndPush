package lik.kindle.cap.service.impl;

import lik.kindle.cap.model.BodyParam;
import lik.kindle.cap.model.ListParam;
import lik.kindle.cap.service.UrlParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author langyi
 * @date 2017/1/27.
 */
@Service
public class UrlParamServiceImpl implements UrlParamService {
    private static Logger log = LoggerFactory.getLogger(UrlParamServiceImpl.class);

    @Override
    public ListParam generateListPram(String url) {
        //todo@langyi
        ListParam listParam = new ListParam();
        listParam.setReg("<a\\s*href\\s*=\"([^\"]*)\"\\s*>([^<]*)<\\/a>");
        listParam.setBodyBegin("\t\t\t\t<dl>");
        listParam.setBodyEnd("\t\t\t\t</dl>");
        return listParam;
    }

    @Override
    public BodyParam generateBodyPram(String url) {
        //todo@langyi
        BodyParam bodyParam = new BodyParam();
        bodyParam.setBodyBegin("<div id=\"content\"><script>readx();</script>");
        bodyParam.setBodyEnd("</div>");
        return bodyParam;
    }
}
