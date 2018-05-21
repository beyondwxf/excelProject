package com.hexun.zh.datafilter.common.utils.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map;

/**
 * 
 * <p>http请求示例，请求json</p>
 * @author yangli
 * @version $Id: HttpClientUtils.java, v 0.1 2017年8月7日 上午10:06:58 yangli Exp $
 */
public class HttpClientUtils {

	public static  String sendPost(String url, Map<String,Object> paraMap){
		String data = JSON.toJSONString(paraMap);
		String response = null;
		try {
			CloseableHttpClient httpclient = null;
	        CloseableHttpResponse httpresponse = null;
	        try {
	        	httpclient = HttpClients.createDefault();
	            HttpPost httppost = new HttpPost(url);
	            StringEntity stringentity = new StringEntity(data,
	                     ContentType.create("application/json", "UTF-8"));
	            httppost.setEntity(stringentity);
	            httpresponse = httpclient.execute(httppost);
	            response = EntityUtils
	                     .toString(httpresponse.getEntity());
	            
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(httpclient != null) {
	                 httpclient.close();
	            }
	            if (httpresponse != null) {
	                 httpresponse.close();
	            }
			}
	        
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return response;
	}
}
