package com.hexun.zh.datafilter.controller;

import com.hexun.zh.datafilter.common.controller.DefaultBaseController;
import com.hexun.zh.datafilter.common.page.Page;
import com.hexun.zh.datafilter.common.utils.BaseResponse;
import com.hexun.zh.datafilter.common.utils.ExcelCPUtils;
import com.hexun.zh.datafilter.common.utils.PropertyUtil;
import com.hexun.zh.datafilter.common.utils.StringUtils;
import com.hexun.zh.datafilter.entity.Feedback;
import com.hexun.zh.datafilter.service.DataFilterService;
import com.hexun.zh.datafilter.service.InventoryStatisticsService;
import com.hexun.zh.datafilter.service.PointService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面
 * @author wangxuefei
 *
 */
@Scope("prototype")
@Controller
public class pointOperationController extends DefaultBaseController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	private PointService pointService;
	
	
	/**
	 * 操作积分数据
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="pointOperation",method={RequestMethod.GET,RequestMethod.POST})
	public 	@ResponseBody BaseResponse loadCPData(HttpServletRequest req){
		return pointService.commonOperation(req);
	}
	
	/**
	 * 简单操作
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="easyOperation",method={RequestMethod.GET,RequestMethod.POST})
	public 	ModelAndView easyOperation(HttpServletRequest req) {
		return getModelAndView("easyOperation");
	}
	
}
