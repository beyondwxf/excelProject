package com.hexun.zh.datafilter.controller;

import com.hexun.zh.datafilter.common.controller.DefaultBaseController;
import com.hexun.zh.datafilter.common.page.Page;
import com.hexun.zh.datafilter.common.utils.BaseResponse;
import com.hexun.zh.datafilter.common.utils.StringUtils;
import com.hexun.zh.datafilter.entity.Feedback;
import com.hexun.zh.datafilter.service.DataFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面
 * @author zhoudong
 *
 */
@Scope("prototype")
@Controller
public class IndexController extends DefaultBaseController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	private DataFilterService dataFilterService;

	/**
	 * 首页
	 * @param req
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value="index",method={RequestMethod.GET,RequestMethod.POST})
	public 	ModelAndView index(HttpServletRequest req) throws Exception {
		return getModelAndView("index");
	}

	/**
	 * 导入页面
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="to_upload",method={RequestMethod.GET,RequestMethod.POST})
	public 	ModelAndView toUpload(HttpServletRequest req) {
		return getModelAndView("upload_excel");
	}
	/**
	 * 导入页面
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="bi",method={RequestMethod.GET,RequestMethod.POST})
	public 	ModelAndView bi(HttpServletRequest req) {
		return getModelAndView("bi");
	}

	/**
	 * 加载全部数据
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="load",method={RequestMethod.GET,RequestMethod.POST})
	public 	@ResponseBody Page<Feedback> load(HttpServletRequest req) {

		int pageNo = StringUtils.isBlank(req.getParameter("pageNo")) ? 1 : Integer.parseInt(req.getParameter("pageNo"));
		int pageSize = StringUtils.isBlank(req.getParameter("pageSize")) ? 10 : Integer.parseInt(req.getParameter("pageSize"));

		Page<Feedback> page = dataFilterService.findDataByPage("findDataByPage",new HashMap<>(),pageNo,pageSize);

		return page;
	}

	/**
	 * 加载线图数据
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="load_line_chat",method={RequestMethod.GET,RequestMethod.POST})
	public 	@ResponseBody BaseResponse loadLineChat(HttpServletRequest req){
		return dataFilterService.loadLineChat(req);
	}

	/**
	 * 加载条形数据
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="load_bar_chat",method={RequestMethod.GET,RequestMethod.POST})
	public 	@ResponseBody BaseResponse loadBarChat(HttpServletRequest req) {
		return dataFilterService.loadBarChat(req);
	}

	/**
	 * 加载饼图数据
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="load_pie_chat",method={RequestMethod.GET,RequestMethod.POST})
	public 	@ResponseBody BaseResponse loadPieChat(HttpServletRequest req) {
		return dataFilterService.loadPieChat(req);
	}
}
