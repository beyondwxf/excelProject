package com.hexun.zh.datafilter.common.utils.http;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * httpclient
 * @author zhoudong
 *
 */
public class HttpClientPool {
    private static PoolingHttpClientConnectionManager connectionManager = null;
    static{
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(400);
        connectionManager.setDefaultMaxPerRoute(50);
    }
    public static CloseableHttpClient getHttpClient(){
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(globalConfig).build();
        return client;
    }

    /**
     * 配置信息
     * @param httpRequestBase
     */
    public static void config(HttpRequestBase httpRequestBase,int ... timeout) {
        httpRequestBase.setHeader("User-Agent", "Mozilla/5.0");
        httpRequestBase.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpRequestBase.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");//"en-US,en;q=0.5");
        httpRequestBase.setHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");

        int connTimeout = 10000;    // 默认超时时间，10秒
        if(timeout.length > 0) connTimeout = timeout[0] * 1000;

        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connTimeout)
                .setConnectTimeout(connTimeout)
                .setSocketTimeout(connTimeout)
                .build();

        httpRequestBase.setConfig(requestConfig);

    }

}