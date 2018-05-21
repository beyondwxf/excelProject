package com.hexun.zh.datafilter.common.utils;

import com.hexun.zh.datafilter.common.utils.http.HttpClientPool;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http 请求工具类
 * 
 * @author zhoudong
 * 
 */
public final class HTTPUtils {

	private final static String charset = "UTF-8";

	/**
	 * 发送get请求
	 * @author zhoudong
	 * @return
	 */
	public static String sendGet(String url,int ... timeout) {
		HttpGet httpGet = new HttpGet(url);
		HttpClientPool.config(httpGet,timeout);

		return execute(httpGet);
	}

	/**
	 * 发送get请求
	 * @author zhoudong
	 * @return
	 */
	public static String sendGet(String url, Map<?, ?> param,int ... timeout) {
		// 1、处理参数
		StringBuilder sb = new StringBuilder(url);
		sb.append("?");
		for(Map.Entry<?, ?> entry : param.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		if(sb.toString().endsWith("&") || sb.toString().endsWith("?")){
			sb.deleteCharAt(sb.length()-1);
		}

		return sendGet(sb.toString(),timeout);
	}
	
	/**
     * 向指定 URL 发送POST方法的请求(2018-01-04升级到httpPool)
     * @author zhoudong
	 * @param timeout 单位秒
     */
    public static String sendPost(String url, Map<?, ?> param,int ... timeout) {
		// 1、处理参数
		UrlEncodedFormEntity uefEntity = processParams(param);

		// 2、准备请求
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(uefEntity);
		HttpClientPool.config(httpPost,timeout);

		return execute(httpPost);
    }

	/**
	 * 处理参数
	 * @param param
	 * @return
	 */
	private static UrlEncodedFormEntity processParams(Map<?, ?> param){
		List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
		for(Map.Entry<?, ?> entry : param.entrySet()) {
			formParams.add(new BasicNameValuePair(String.valueOf(entry.getKey()),String.valueOf(entry.getValue())));
		}
		UrlEncodedFormEntity uefEntity = null;
		try {
			uefEntity = new UrlEncodedFormEntity(formParams,charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return uefEntity;
	}

	/**
	 * 执行请求
	 * @param httpRequestBase
	 * @return
	 */
    private static String execute(HttpRequestBase httpRequestBase){
		CloseableHttpResponse response = null;
		String result = null;
		try {
			response = HttpClientPool.getHttpClient().execute(httpRequestBase);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, charset);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(response);
		}
		return result;
	}

	/**
	 * 资源过期 2018-01-04 周栋
	 * @param url
	 * @param param
	 * @return
	 */
    @Deprecated
    public static String apiPost(String url,String param) {
        return sendPost(url+ "?" + param,new HashMap<>());
    }
    
    /**
	 * 该方式内部实现已经去掉，直接调用sendPost
	 */
    @Deprecated
	public static String postURL(String address,Map<String, Object> param) {
		return sendPost(address,param);
	}
	
	/** 向指定URL发送Get请求，返回字节数组
	 * @param sUrl  		请求url地址
	 * @param timeout  	超时时间（秒）
	 * @return 字节数组
	 * @throws Exception 
	 * @author zhaominghao add by 20161213
	 */	
	public static byte[] GetByte(String sUrl, int timeout){    
		HttpURLConnection conn = null;
		
		try {    
	        URL url = new URL(sUrl);    
	        conn = (HttpURLConnection)url.openConnection();    
	        conn.setRequestMethod("GET");    
	        conn.setConnectTimeout(timeout * 1000);    
	        InputStream inStream = conn.getInputStream();    
	        byte data[] = readInputStream(inStream);  

	        return data;
		} 
		catch (Exception e) {    
			e.printStackTrace();    
			if (conn != null) {
				conn.disconnect();
			}
			return null;
	    }   
	}	

	/** 从输入流中读取字节数据
	 * @param inStream  	输入流
	 * @return 字节数组
	 * @throws Exception 
	 * @author zhaominghao add by 20161213
	 */	
	private static byte[] readInputStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();    
		byte[] buffer = new byte[2048];    
		int len = 0;    
		while( (len=inStream.read(buffer)) != -1 ){    
			outStream.write(buffer, 0, len);    
	    }    
	     
		inStream.close();    
	    return outStream.toByteArray();    
	}

    /**
     * 发送post请求
     * @param url
     *            发送请求的 URL
     * @param jsonData
     *            请求参数，原生的JSON 字符串。
     * @return 所代表远程资源的响应结果
     */
    public static  String sendPost(String url, String jsonData){
		String data = jsonData;
		String response = null;
		try {
			CloseableHttpClient httpclient = null;
	        CloseableHttpResponse httpresponse = null;
	        try {
	        	httpclient = HttpClientPool.getHttpClient();
	            HttpPost httppost = new HttpPost(url);
				StringEntity stringentity = new StringEntity(data,
	                     ContentType.create("application/json", charset));
	            httppost.setEntity(stringentity);
	            httpresponse = httpclient.execute(httppost);
	            response = EntityUtils
	                     .toString(httpresponse.getEntity());

			} catch (Exception e) {
				e.printStackTrace();
			}finally{
	        	IOUtils.closeQuietly(httpresponse);
			}

		} catch (Exception e) {
			 e.printStackTrace();
		}
		return response;
	}
	/**
	 * 发送post请求
	 * @param url
	 *            发送请求的 URL
	 * @param jsonData
	 *            请求参数，原生的JSON 字符串。
	 * @param timeOut
	 *            超时时间，单位毫秒
	 * @return 所代表远程资源的响应结果
	 */
	public static  String sendPost(String url, String jsonData,int timeOut){
		String data = jsonData;
		String response = null;
		try {
			CloseableHttpClient httpclient = null;
			CloseableHttpResponse httpresponse = null;
			try {
				httpclient = HttpClientPool.getHttpClient();
				HttpPost httppost = new HttpPost(url);
				RequestConfig requestConfig = RequestConfig.custom()
						.setConnectTimeout(timeOut).setConnectionRequestTimeout(timeOut)
						.setSocketTimeout(timeOut).build();
				httppost.setConfig(requestConfig);
				StringEntity stringentity = new StringEntity(data,
						ContentType.create("application/json", charset));
				httppost.setEntity(stringentity);
				httpresponse = httpclient.execute(httppost);
				response = EntityUtils
						.toString(httpresponse.getEntity());

			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				IOUtils.closeQuietly(httpresponse);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 发送get请求（带cookie）
	 * @author zhaominghao
	 * @return
	 * @throws Exception 
	 */
	public static String sendGet(String url, Map<? , ?> param, String cookies, int readTimeout) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        
        if(param != null){
            sb.append("?");
            for(Map.Entry<?, ?> entry : param.entrySet()) {
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            if(sb.length() > 0)
            	sb.deleteCharAt(sb.length()-1);
        }
        
		StringBuilder result = new StringBuilder();
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			System.out.println("HTTPUtils.sendGet，发送http请求：" + sb.toString());
			URL get = new URL(sb.toString());
			connection = (HttpURLConnection) get.openConnection();

			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			
	        //Cookie设置
	        if (StringUtils.isNotBlank(cookies)) {  
	    	   connection.setRequestProperty("Cookie", cookies);  
	        }    
			
			//设置超时时间
			connection.setConnectTimeout(10 * 1000);
			connection.setReadTimeout(readTimeout * 1000);
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lines;
			while ((lines = reader.readLine()) != null) {
				result.append(lines);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(reader);
			if (connection != null) {
				connection.disconnect();
			}
		}
		return result.toString();
	}
  
    
	/**
	 * 说明：获取HTTP请求的完整URL
	 * 返回值：
	 * @author zhaominghao
	 * @param request http请求
	 * @throws UnsupportedEncodingException 
	 */
	public static String GetFullUrl(HttpServletRequest request) throws UnsupportedEncodingException{
		StringBuilder sb = new StringBuilder();
		sb.append(request.getRequestURL().toString());
		if(!StringUtils.isBlank(request.getQueryString())){
			sb.append("?");
			sb.append(request.getQueryString());
		}
		
		return URLEncoder.encode(sb.toString(), charset);
	}    

	/**
	 * 说明：获取HTTP上一次请求的完整URL
	 * 返回值：
	 * @author zhaominghao
	 * @param request http请求
	 * @throws UnsupportedEncodingException 
	 */
	public static String GetLastFullUrl(HttpServletRequest request) throws UnsupportedEncodingException{
		StringBuilder sb = new StringBuilder();
		sb.append(request.getHeader("Referer"));
		return URLEncoder.encode(sb.toString(), charset);
	}    

	/**
	 * 发送get请求（不捕获异常）
	 * @author zhaominghao
	 * @return
	 * @throws IOException 
	 */
	public static String sendHttpGet(String url, Map<? , ?> param, int readTimeout) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append("?");
        
        for(Map.Entry<?, ?> entry : param.entrySet()) {
        	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if(sb.length() > 0)
        	sb.deleteCharAt(sb.length()-1);

        return sendGet(sb.toString(),readTimeout);
	}
	
	/**
	 * 发送get请求
	 * @author zhaominghao
	 * @return
	 */
	@Deprecated
	public static String sendHttpGet(String url, int readTimeout) {
		return sendGet(url, readTimeout);
	}

	/**
     * 发送post请求
     * @author zhaominghao
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
	 * @throws IOException 
     */
    public static String sendHttpPost(String url, Map<?, ?> param, int readTimeout) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			//设置超时时间
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(readTimeout * 1000);
            
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<?, ?> entry : param.entrySet()) {
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            if(sb.length() > 0)
            	sb.deleteCharAt(sb.length()-1);
            
            // 发送请求参数
            out.print(sb);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        }
        //使用finally块来关闭输出流、输入流
        finally{
        	IOUtils.closeQuietly(out);
        	IOUtils.closeQuietly(in);
        }
        return result.toString();
    }    

}
