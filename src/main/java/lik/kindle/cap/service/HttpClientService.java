package lik.kindle.cap.service;

import lik.kindle.cap.model.HttpResult;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.Map;

/**
 * @author langyi
 * @date 2017/1/27.
 */
public interface HttpClientService {
    /**
     * 执行get请求
     * @param url
     * @return
     */
    public String doGet(String url);

    /**
     * 执行带参数的get请求
     * @param url
     * @return
     */
    public String doGet(String url,Map<String,String> params) throws IOException;

    /**
     * 执行不带带参数的post请求
     * @param url
     * @return
     */
    public HttpResult doPost(String url)throws ClientProtocolException, IOException;
    /**
     * 执行带参数的post请求
     * @param url
     * @return
     */
    public HttpResult doPost(String url, Map<String,String> params) throws IOException;
    /**
     * 执行json参数的的post请求
     * @param url
     * @return
     */
    public HttpResult doPost(String url,String json)throws ClientProtocolException, IOException ;

    public HttpResult doPost(String url,Map<String,String> headerMap,String json,boolean isSupportHttps) throws IOException ;
}
