package cn.yunovo.iov.fc.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.sunshine.dcda.system.service.model.CooperateOrganVo;
import org.sunshine.dcda.view.system.viewcomponent.ICooperateOrganViewComponent;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcUser;
import cn.yunovo.iov.fc.service.ICcCurrencyService;
import cn.yunovo.iov.fc.service.ICcGprsBatchService;
import cn.yunovo.iov.fc.service.ICcGprsPackService;
import cn.yunovo.iov.fc.service.ICcGprsPayService;
import cn.yunovo.iov.fc.service.ICcLanguageService;
import cn.yunovo.iov.fc.service.ICcNationService;
import cn.yunovo.iov.fc.service.ICcNotifyService;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.service.ICcStatsMonthService;
import cn.yunovo.iov.fc.service.ICcUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path="", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value="/api/select", tags="下拉框接口列表")
public class SelectController extends BaseController{
	
	@Autowired
	private ICcOrgService iCcOrgService;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcStatsMonthService iCcStatsMonthService;
	
	@Autowired
	private ICcNotifyService iCcNotifyService;
	
	@Autowired
	private ICcGprsPayService iCcGprsPayService;
	
	@Autowired
	private ICcGprsPackService iCcGprsPackService;
	
	@Autowired
	private ICcCurrencyService iCcCurrencyService;
	
	@Autowired
	private ICcLanguageService iCcLanguageService;
	
	@Autowired
	private ICcNationService iCcNationService;
	
	@Autowired
	private ICcGprsBatchService iCcGprsBatchService;
	
	@Resource
	private ICooperateOrganViewComponent cooperateOrganViewComponent;
	
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
		if(ccUser == null || ccUser.getOrg_id() == null || ccUser.getOrg_id() == 0) {
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
	
	@ApiOperation(notes="统计分析-通知或来源下拉数据接口", value = "统计分析-通知或来源下拉数据接口")
	@RequestMapping(path="/api/select/notifyFrom/select", method= {RequestMethod.GET})
	public Result<List<SelectBean>> getNotifyFrom() {
		
		List<SelectBean> months = iCcNotifyService.select();
		return ResultUtil.success("ok", months);
	}
	
	@ApiOperation(notes="财务报表-付款方式", value = "财务报表-付款方式")
	@RequestMapping(path="/api/select/payMethod/select", method= {RequestMethod.GET})
	public Result<List<SelectBean>> getPayMethod() {
		
		List<SelectBean> months = iCcGprsPayService.select();
		return ResultUtil.success("ok", months);
	}
	
	@ApiOperation(notes="财务报表-套餐流量", value = "财务报表-套餐流量")
	@RequestMapping(path="/api/select/pack", method= {RequestMethod.GET})
	public Result<List<SelectBean>> getPacks() {
		
		List<SelectBean> months = iCcGprsPackService.select(this.getLoginBaseInfo());
		return ResultUtil.success("ok", months);
	}
	
	@ApiOperation(notes="系统设置-货币下拉", value = "系统设置-货币下拉")
	@RequestMapping(path="/api/select/currency", method= {RequestMethod.GET})
	public Result<List<SelectBean>> currency() {
		
		List<SelectBean> currency = iCcCurrencyService.select();
		return ResultUtil.success("ok", currency);
	}
	
	@ApiOperation(notes="系统设置-系统语言下拉", value = "系统设置-系统语言")
	@RequestMapping(path="/api/select/language", method= {RequestMethod.GET})
	public Result<List<SelectBean>> language() {
		
		List<SelectBean> currency = iCcLanguageService.select();
		return ResultUtil.success("ok", currency);
	}
	
	@ApiOperation(notes="系统设置-国家区域下拉", value = "系统设置-国家区域下拉")
	@RequestMapping(path="/api/select/nation", method= {RequestMethod.GET})
	public Result<List<SelectBean>> nation(Integer parent) {
		
		List<SelectBean> nation = iCcNationService.select(parent);
		return ResultUtil.success("ok", nation);
	}
	
	@ApiOperation(notes="有效周期下拉", value = "有效周期下拉")
	@RequestMapping(path="/api/select/liveMonth", method= {RequestMethod.GET})
	public Result<List<SelectBean>> liveMonth() {
		
		List<SelectBean> nation = iCcGprsBatchService.select();
		return ResultUtil.success("ok", nation);
	}
	
	@ApiOperation(notes="月度充值-充值月份下拉", value = "月度充值-充值月份下拉")
	@RequestMapping(path="/api/select/payMonths", method= {RequestMethod.GET})
	public Result<List<SelectBean>> payMonths() {
		
		List<SelectBean> nation = iCcGprsPayService.monthSelect(this.getLoginBaseInfo());
		return ResultUtil.success("ok", nation);
	}
	
	private final String DEVICE_ORGS_FORMAT = "%s(%s)";
	
	@ApiOperation(notes="车联网机构下拉", value = "车联网机构下拉")
	@RequestMapping(path="/api/select/deviceOrgs", method= {RequestMethod.GET})
	public Result<List<SelectBean>> deviceOrgs() {
		
		List<SelectBean> orgs  = null;
		List<CooperateOrganVo> organList = null;
		try {
			organList = cooperateOrganViewComponent.queryCooperateOrganList(null, null);
		} catch (Exception e) {
			log.error("[deviceOrgs][exception]exception={}", ExceptionUtils.getStackTrace(e));
		}
		
		if(!CollectionUtils.isEmpty(organList)) {
			orgs = organList.stream().map(new Function<CooperateOrganVo, SelectBean>() {

				@Override
				public SelectBean apply(CooperateOrganVo t) {
					SelectBean select2 = new SelectBean();
					select2.setLabel(String.format(DEVICE_ORGS_FORMAT, t.getCooOrganName(), t.getCode()));
					select2.setValue(t.getCode());
					return select2;
				}
			}).collect(Collectors.toList());
		}
		return ResultUtil.success("ok", orgs);
	}
	
}
