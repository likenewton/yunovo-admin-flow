package cn.yunovo.iov.fc.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcStatsMonthService;
import cn.yunovo.iov.fc.service.ICcUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path="/api/select", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value="/api/select", tags="下拉框接口列表")
public class SelectController extends BaseController{
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcStatsMonthService iCcStatsMonthService;
	
	public static List<SelectBean> CARD_TYPES = new ArrayList<>();
	
	static {
		
		SelectBean temp = new SelectBean();
		temp.setLabel("智网吉林11位卡");
		temp.setValue("1");
		CARD_TYPES.add(temp);
		
		temp = new SelectBean();
		temp.setLabel("智网科技 JASPER");
		temp.setValue("2");
		CARD_TYPES.add(temp);
		
		temp = new SelectBean();
		temp.setLabel("吉林长春 JASPER");
		temp.setValue("3");
		CARD_TYPES.add(temp);
		
		temp = new SelectBean();
		temp.setLabel("智网定向流量卡");
		temp.setValue("4");
		CARD_TYPES.add(temp);
		
		temp = new SelectBean();
		temp.setLabel("吉林长春 新APN");
		temp.setValue("5");
		CARD_TYPES.add(temp);
	}

	@ApiOperation(notes="获取机构下拉列表接口", value = "获取机构下拉列表接口")
	@RequestMapping(path="/api/select/orgs", method= {RequestMethod.GET})
	public Result<List<SelectBean>> orgs() {
		
		LoginInfo info = this.getLoginBaseInfo();
		
		CcUser ccUser = iCcUserService.findUserOrgAndOrgpos(info.getLoginName());
		if(ccUser == null) {
			return ResultUtil.success("ok", null);
		}
		
		List<SelectBean> selects = iCcOrgService.select(ccUser.getOrg_id(), ccUser.getOrgpos());
		return ResultUtil.success(selects);
	}
	
	@ApiOperation(notes="获取卡商列表接口", value = "获取卡商列表接口")
	@RequestMapping(path="/api/select/cardTypes", method= {RequestMethod.GET})
	public Result<List<SelectBean>> cardTypes() {
		
		return ResultUtil.success("ok", CARD_TYPES);
	}
	
	@ApiOperation(notes="月度统计-月份下拉数据接口", value = "月度统计-月份下拉数据接口")
	@RequestMapping(path="/api/select/stats/getMonths", method= {RequestMethod.GET})
	public Result<List<SelectBean>> statsMonths() {
		
		LoginInfo info = this.getLoginBaseInfo();
		List<SelectBean> months = iCcStatsMonthService.monthSelect(info);
		return ResultUtil.success("ok", months);
	}
	
	
}
