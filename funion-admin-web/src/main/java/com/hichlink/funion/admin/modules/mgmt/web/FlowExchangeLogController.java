package com.hichlink.funion.admin.modules.mgmt.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.aspire.webbas.core.web.BaseController;
import com.hichlink.funion.common.entity.FlowExchangeLog;
import com.hichlink.funion.common.service.FlowExchangeLogService;

/**
 * 
 * <b>Title：</b>FlowExchangeLogController.java<br/>
 * <b>Description：</b> <br/>
 * <b>@author： </b>oceanzhuang<br/>
 * <b>@date：</b>2015-11-28 14:10:24<br/>
 * <b>Copyright (c) 2015 ASPire Tech.</b>
 * 
 */
@Controller
@RequestMapping("/flowExchangeLog")
public class FlowExchangeLogController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(FlowExchangeLogController.class);
	@Autowired
	@Qualifier("flowExchangeLogService")
	private FlowExchangeLogService flowExchangeLogService;

	@RequestMapping(value = "/query.ajax")
	@ResponseBody
	public Map<String, Object> pageQuery(Page<FlowExchangeLog> page) {
		Page<FlowExchangeLog> list = flowExchangeLogService.pageQuery(page);
		return super.page(list);
	}

	@RequestMapping(value = "/add.ajax", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(@ModelAttribute("flowExchangeLog") FlowExchangeLog data) {
		flowExchangeLogService.saveAndUpdate(data);
		return super.success("新增成功");
	}

	@RequestMapping(value = "/delete.ajax")
	@ResponseBody
	public Map<String, Object> delete(Long logId) {
		flowExchangeLogService.delete(logId);
		return super.success("删除成功");
	}

	@RequestMapping(value = "/get.ajax")
	@ResponseBody
	public Map<String, Object> get(Long logId) {
		FlowExchangeLog data = flowExchangeLogService.get(logId);
		return super.success(data);
	}

	/**
	 * 更新的时候需额外传递updId,值跟主键值一样,被@ModelAttribute注释的方法会在此controller每个方法执行前被执行，
	 * 要谨慎使用
	 */
	@ModelAttribute("flowExchangeLog")
	public void getForUpdate(@RequestParam(value = "updId", required = false) Long updId, Model model) {
		if (null != updId) {
			model.addAttribute("flowExchangeLog", flowExchangeLogService.get(updId));
		} else {
			model.addAttribute("flowExchangeLog", new FlowExchangeLog());
		}
	}
}