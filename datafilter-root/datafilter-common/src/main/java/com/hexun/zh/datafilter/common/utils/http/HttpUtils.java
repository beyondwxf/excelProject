package com.hexun.zh.datafilter.common.utils.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 此类不让用，全部方法为private，请使用../HTTPUtils.java
 * @author zhoudong
 */
public class HttpUtils {

    /**
     * Get请求
     * @param url
     * @return
     */
    private static String sendGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        HttpClientPool.config(httpGet);

        CloseableHttpResponse response = null;
        String result = null;
        try {
            response = HttpClientPool.getHttpClient().execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(response);
        }
        return result;
    }

    /**
     * Post请求
     * @param url
     * @return
     */
    private static String sendPost(String url, Map<?, ?> param) {
        // 1、处理参数
        List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
        for(Map.Entry<?, ?> entry : param.entrySet()) {
            formParams.add(new BasicNameValuePair(String.valueOf(entry.getKey()),String.valueOf(entry.getValue())));
        }
        UrlEncodedFormEntity uefEntity = null;
        try {
            uefEntity = new UrlEncodedFormEntity(formParams);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 2、准备请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(uefEntity);
        HttpClientPool.config(httpPost);

        CloseableHttpResponse response = null;
        String result = null;
        // 3、返回
        try {
            response = HttpClientPool.getHttpClient().execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(response);
        }
        return result;
    }

}
