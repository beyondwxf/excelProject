package com.hexun.zh.datafilter.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hexun.zh.datafilter.common.utils.BaseResponse;
import com.hexun.zh.datafilter.common.utils.Digest;
import com.hexun.zh.datafilter.common.utils.Encrypt;
import com.hexun.zh.datafilter.common.utils.HTTPUtils;
import com.hexun.zh.datafilter.common.utils.ParameterUtils;
import com.hexun.zh.datafilter.common.utils.PropertyUtil;
import com.hexun.zh.datafilter.common.utils.StringUtils;
import com.hexun.zh.datafilter.service.PointService;
@Service
public class PointServiceImpl implements PointService {

	@Override
	public BaseResponse commonOperation(HttpServletRequest req) {
		BaseResponse result = new BaseResponse();
		String pointType = req.getParameter("pointType");
		String pointOperation = req.getParameter("pointOperation");
		String userid = req.getParameter("userid");
		String amout = req.getParameter("amout");
		if(StringUtils.isBlank(pointType) || StringUtils.isBlank(pointOperation) || StringUtils.isBlank(userid)) {
			result.setErrorMgs("参数不正确!");
		}else if(("ADD".equalsIgnoreCase(pointOperation)||"MINUS".equalsIgnoreCase(pointOperation))&(StringUtils.isBlank(amout))) {
			result.setErrorMgs("参数不正确!");
		}
		if("GOLD".equalsIgnoreCase(pointType)) {
			result.setRespData(pointG(pointType, pointOperation, userid, amout)); 
		}else {
			result.setRespData(pointBS(pointType, pointOperation, userid, amout));
		}
		result.setRespCode("T");
		return result;
	}
	
	
	
	
	
	/**
	 * 操作牛币 神币
	 * @return
	 */
	private String pointBS(String pointType,String pointOperation,String userid,String amout) {
		String result = "没有返回";
		if("BULL".equalsIgnoreCase(pointType)) {
			pointType = "BULL_MONEY";
		}else if("SUPGOD".equalsIgnoreCase(pointType)) {
			pointType = "SUPGOD_MONEY";
		}
		if("QUERY".equalsIgnoreCase(pointOperation)) {
			result = pointsQuery(pointType, userid);
		}else if("ADD".equalsIgnoreCase(pointOperation)) {
			result = pointsAdd(pointType, userid, amout);
		}else if("MINUS".equalsIgnoreCase(pointOperation)) {
			result = pointsMinus(pointType, userid, amout);
		}else if("RESET".equalsIgnoreCase(pointOperation)) {
			result = pointsReset(pointType, userid);
		}else {
			result = "没有返回";
		}
		
		return result;
	}

	
	/**
	 * 操作金币
	 * @return
	 */
	private String pointG(String pointType,String pointOperation,String userid,String amout) {
		String result = "没有返回";
		if("QUERY".equalsIgnoreCase(pointOperation)) {
			result = queryGold(userid);
		}else if("ADD".equalsIgnoreCase(pointOperation)) {
			result = addGold(userid, amout);
		}else if("MINUS".equalsIgnoreCase(pointOperation)) {
			result = minusGold(userid, amout);
		}else {
			 result = "没有返回";
		}
		
		return result;
	}
	
	
	
    public  String pointsAdd(String pointType,String uid,String amount){
    	String partner_id = PropertyUtil.getProperty("point.partner_id");
		String md5Key = PropertyUtil.getProperty("point.md5Key");
		String url = PropertyUtil.getProperty("point.url");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("access_channel","ALL");
        map.put("input_charset", "UTF-8");
        map.put("memo","测试");
        map.put("event_call","后台手动跑批");
        map.put("access_channel","H5");
        
        map.put("partner_id",partner_id);
        map.put("points_amount",amount);
        map.put("points_type",pointType);
        map.put("request_no",UUID.randomUUID().toString());
        map.put("user_id",uid);
        System.out.println("请求的参数:"+ParameterUtils.createSignText(map));
        String sign = Encrypt.getMD5(ParameterUtils.createSignText(map) + md5Key);
        map.put("sign",sign);
        map.put("sign_type","MD5");
        System.out.println("sign:"+sign);
       
        String result = HTTPUtils.sendPost(url + "/pointsGrant", JSON.toJSONString(map));

        return result;
    }
    
    public String pointsMinus(String pointType,String user_id,String points_amount) {
    	String partner_id = PropertyUtil.getProperty("point.partner_id");
		String md5Key = PropertyUtil.getProperty("point.md5Key");
		String url = PropertyUtil.getProperty("point.url");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("input_charset","UTF-8");
        map.put("partner_id",partner_id);
        map.put("points_type",pointType);
        map.put("user_id",user_id);
        map.put("memo","扣减测试");
        map.put("request_no", UUID.randomUUID().toString());
        map.put("points_amount",points_amount);
        map.put("access_channel","ALL");

        String sign = Encrypt.getMD5(ParameterUtils.createSignText(map) + md5Key);
        map.put("sign",sign);
        map.put("sign_type","MD5");
        
        System.out.println(JSON.toJSONString(map));
        
        String result = HTTPUtils.sendPost(url + "/commonPointsDecrease", JSON.toJSONString(map));
       

        System.out.println(result);

        return result;
    }
    
    
    public String pointsReset(String pointType,String user_id) {
    	String partner_id = PropertyUtil.getProperty("point.partner_id");
		String md5Key = PropertyUtil.getProperty("point.md5Key");
		String url = PropertyUtil.getProperty("point.url");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("input_charset","UTF-8");
        map.put("partner_id",partner_id);
        map.put("points_type",pointType);
        map.put("user_id",user_id);
        map.put("memo","积分重置");
        map.put("request_no", UUID.randomUUID().toString());
        map.put("access_channel","ALL");

        String sign = Encrypt.getMD5(ParameterUtils.createSignText(map) + md5Key);
        map.put("sign",sign);
        map.put("sign_type","MD5");
        
        System.out.println(JSON.toJSONString(map));
        
        String result = HTTPUtils.sendPost(url + "/pointsReset", JSON.toJSONString(map));
       

        System.out.println(result);

        return result;
    }
    
    public String pointsQuery(String pointType,String userid) {
    	String partner_id = PropertyUtil.getProperty("point.partner_id");
		String md5Key = PropertyUtil.getProperty("point.md5Key");
		String url = PropertyUtil.getProperty("point.url");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("input_charset", "UTF-8");
        map.put("partner_id",partner_id);
        map.put("points_type",pointType);
        map.put("user_id",userid);

        String sign = Encrypt.getMD5(ParameterUtils.createSignText(map) + md5Key);
        map.put("sign",sign);
        map.put("sign_type","MD5");

         String result = HTTPUtils.sendPost(url+"/pointsQuery", JSON.toJSONString(map));

        System.out.println(result);
        return result;
    }
    
    private String  addGold(String userId,String money){
    	String url = PropertyUtil.getProperty("point.gold.url");
		String key = PropertyUtil.getProperty("point.gold.key");
		String platform = PropertyUtil.getProperty("point.gold.platform");
		
      String operate = "0";
      String memo = "测试";
      Map<String,String> map = new HashMap<>();
      map.put("platform",platform);
      map.put("type", "Play");
      map.put("operate","0"); // 0-增金币 1-减金币
      map.put("money",money);
      map.put("userId",userId);
      map.put("comment",memo);
      map.put("code", Digest.hmacSign((userId + map.get("platform") + map.get("type") + operate+ money), key));
      
      System.out.println("** 提交到金币系统数据:"+JSON.toJSONString(map));
      String result = HTTPUtils.sendPost(url+"/app/coinhandler",map,10*1000);
      System.out.println("** 调用金币系统返回值:"+result);
      if(StringUtils.isBlank(result) || !JSON.parseObject(result).getString("status").equals("SUCCESS")){
          System.out.println("** 增加金币失败：{"+JSON.parseObject(result).getString("moreInfo")+"}，用户id:{"+userId+"}:");
      } else{
          System.out.println("** 成功，用户id:"+userId);
      }
      return result;
  }
    
    private String minusGold(String userId,String money){
		String url = PropertyUtil.getProperty("point.gold.url");
		String key = PropertyUtil.getProperty("point.gold.key");
		String platform = PropertyUtil.getProperty("point.gold.platform");
      String operate = "1";
      String memo = "测试";
      Map<String,String> map = new HashMap<>();
      map.put("platform",platform);
      map.put("type", "Play");
      map.put("operate",operate); // 0-增金币 1-减金币
      map.put("money",money);
      map.put("userId",userId);
      map.put("comment",memo);
      map.put("code", Digest.hmacSign((userId + map.get("platform") + map.get("type") + operate+ money), key));
      
      System.out.println("** 提交到金币系统数据:"+JSON.toJSONString(map));
      String result = HTTPUtils.sendPost(url+"/app/coinhandler",map,10*1000);
      System.out.println("** 调用金币系统返回值:"+result);
      if(StringUtils.isBlank(result) || !JSON.parseObject(result).getString("status").equals("SUCCESS")){
          System.out.println("** 增加金币失败：{"+JSON.parseObject(result).getString("moreInfo")+"}，用户id:{"+userId+"}:");
      } else{
          System.out.println("** 成功，用户id:"+userId);
      }
      return result;
  }
    
    private String queryGold(String userId){
    	String url = PropertyUtil.getProperty("point.gold.url");
		String key = PropertyUtil.getProperty("point.gold.key");
		String platform = PropertyUtil.getProperty("point.gold.platform");
        Map<String,String> map = new HashMap<>();
        map.put("platform",platform);
        map.put("userId",userId);
        map.put("code", Digest.hmacSign((map.get("platform")+userId), key));
        
        System.out.println("** 提交到金币系统数据:"+JSON.toJSONString(map));
        String result = HTTPUtils.sendPost(url+"/getUserCoin",map,10*1000);
        System.out.println("** 调用金币系统返回值:"+result);
        return result;
    }
}
