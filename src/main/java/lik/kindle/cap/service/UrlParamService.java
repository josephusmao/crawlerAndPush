package lik.kindle.cap.service;

import lik.kindle.cap.model.BodyParam;
import lik.kindle.cap.model.ListParam;

/**
 * @author langyi
 * @date 2017/1/27.
 */
public interface UrlParamService {
    ListParam generateListPram(String url);

    BodyParam generateBodyPram(String url);
}

