package lik.kindle.cap.service.impl;

import lik.kindle.cap.model.HttpResult;
import lik.kindle.cap.service.HttpClientService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author langyi
 * @date 2017/1/27.
 */
@Service
public class HttpClientServiceImpl implements HttpClientService {
    private static Log logger = LogFactory.getLog(HttpClientServiceImpl.class);
    private static final String CHARSET = "UTF-8";
    private final CloseableHttpClient httpClient;
    private final RequestConfig requestConfig;

    @Autowired
    public HttpClientServiceImpl(CloseableHttpClient httpClient, RequestConfig requestConfig) {
        this.httpClient = httpClient;
        this.requestConfig = requestConfig;
    }


    private String entityToString(HttpEntity resEntity) throws IOException {
        String charset = null;
        final ContentType contentType = ContentType.get(resEntity);
        if (contentType != null && contentType.getCharset() != null) {
            charset = contentType.getCharset().displayName();
        }
        String body = EntityUtils.toString(resEntity, charset);
        if (charset == null) {
            String s = body.toLowerCase();
            Matcher matcher = Pattern.compile("<meta[^>]*charset\\s*=\\s*([\\S]*).*\"").matcher(s);
            if (matcher.find()) {
                charset = matcher.group(1);
                body = new String(body.getBytes(HTTP.DEF_CONTENT_CHARSET), charset);
            }
        }
        return body;
    }

    @Override
    public String doGet(String url) {
        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(this.requestConfig);
            response = this.httpClient.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = entityToString(response.getEntity());
            }
            EntityUtils.consume(resEntity);
            return result;
        } catch (Exception e) {
            logger.error("请求错误,请求的URL地址为:" + url, e);
            return null;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.warn("", e);
            }
        }
    }

    @Override
    public String doGet(String url, Map<String, String> params) throws IOException {
        if (params != null && params.size() > 0) {
            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity;
            ArrayList<NameValuePair> parameters = new ArrayList<>();
            for (String name : params.keySet()) {
                parameters.add(new BasicNameValuePair(name, params.get(name)));
            }
            formEntity = new UrlEncodedFormEntity(parameters, CHARSET);
            // 将请求实体设置到httpPost对象中
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(formEntity.getContent(), CHARSET));
            url += "?" + bufferedReader.readLine();
            bufferedReader.close();
        }
        logger.debug("get url:" + url);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = this.httpClient.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            String result = null;
            if (resEntity != null) {
                result = EntityUtils.toString(response.getEntity(), CHARSET);
            }
            EntityUtils.consume(resEntity);
            return result;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public HttpResult doPost(String url, Map<String, String> params) throws IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);

        if (params != null && params.size() > 0) {
            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity;
            ArrayList<NameValuePair> parameters = new ArrayList<>();
            for (String name : params.keySet()) {
                parameters.add(new BasicNameValuePair(name, params.get(name)));
            }
            formEntity = new UrlEncodedFormEntity(parameters, CHARSET);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }
        logger.debug("post param:" + EntityUtils.toString(httpPost.getEntity()));
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = this.httpClient.execute(httpPost);
            return new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), CHARSET));
        } finally {
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                EntityUtils.consume(resEntity);
                response.close();
            }
        }
    }

    @Override
    public HttpResult doPost(String url, String json) throws IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);

        if (json != null) {
            // 构造一个form表单式的实体
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(stringEntity);
        }

        return execute(httpPost);
    }

    @Override
    public HttpResult doPost(String url, Map<String, String> HeaderMap, String json, boolean isSupportHttps) throws IOException {
        // 创建http POST请求
        CloseableHttpClient closeableHttpClient = httpClient;
        if (isSupportHttps) {
            closeableHttpClient = createSSLClientDefault();
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);

        if (HeaderMap != null) {
            for (Map.Entry<String, String> entry : HeaderMap.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        if (json != null) {
            // 构造一个form表单式的实体
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(stringEntity);
        }
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = closeableHttpClient.execute(httpPost);
            return new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), CHARSET));
        } finally {
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                EntityUtils.consume(resEntity);
                response.close();
            }
            closeableHttpClient.close();
        }
    }


    /**
     * Https接口调用创建CloseableHttpClient方法
     */
    private CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            logger.error("create CloseableHttpClient error ", e);
        }
        return HttpClients.createDefault();
    }

    @Override
    public HttpResult doPost(String url) throws IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);

        return execute(httpPost);
    }

    private HttpResult execute(HttpUriRequest request) throws IOException {
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(request);
            return new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), CHARSET));
        } finally {
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                EntityUtils.consume(resEntity);
                response.close();
            }
        }
    }


}
