package cn.yunovo.iov.fc.web.controller.gprs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCardLog;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.entity.CcStatsDay;
import cn.yunovo.iov.fc.model.entity.CcStatsMonth;
import cn.yunovo.iov.fc.model.form.CardOnoffForm;
import cn.yunovo.iov.fc.model.result.CardDetailInfoBean;
import cn.yunovo.iov.fc.model.result.CardTotalByOrgidInfoBean;
import cn.yunovo.iov.fc.model.result.GprsAllotResultBean;
import cn.yunovo.iov.fc.model.result.PayListTotalResulBean;
import cn.yunovo.iov.fc.model.result.UnicomDataBean;
import cn.yunovo.iov.fc.service.ICcCardLogService;
import cn.yunovo.iov.fc.service.ICcGprsAllotService;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcGprsPayService;
import cn.yunovo.iov.fc.service.ICcGprsValueService;
import cn.yunovo.iov.fc.service.ICcStatsDayService;
import cn.yunovo.iov.fc.service.ICcStatsMonthService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="业务管理-流量卡列表")
@RequestMapping(path="/api/gprs/card", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GprsCardController extends BaseController{

	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@Autowired
	private ICcGprsPayService iCcGprsPayService;
	
	@Autowired
	private ICcGprsValueService iCcGprsValueService;
	
	@Autowired
	private ICcCardLogService iCcCardLogService;
	
	@Autowired
	private ICcStatsDayService iCcStatsDayService;
	
	@Autowired
	private ICcStatsMonthService iCcStatsMonthService;
	
	@ApiOperation(value="业务管理-流量卡列表")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_type", value = "卡商类型id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "激活时间-开始日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "激活时间-结束日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "time_expire", value = "是否已过期,0(已过期) 1(未过期)", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "unicom_stop", value = "是否在联通已停卡0（正常）1（停用）", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "status", value = "激活状态0（未激活）1（已激活）", required = false, dataType = "int",paramType = "query")})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcGprsCard, Object>> list(PageForm form, String card_iccid, Integer org_id, String date_start,
			String date_end, Integer time_expire, Integer unicom_stop, Integer status,
			Integer card_type) {
		
		PageData<CcGprsCard, Object>  data = iCcGprsCardService.queryCardListPage(form, card_iccid, org_id, date_start, date_end, time_expire, unicom_stop, status, this.getLoginBaseInfo(), card_type);
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "业务管理-流量卡充值明细")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "充值时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "充值时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "card_id", value = "流量卡id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "pay_from", value = "订单来源", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "is_paid", value = "支付状态", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "paid_start", value = "付款时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "paid_end", value = "付款时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pay_method", value = "付款方式", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path="/payDetail",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcGprsPay, PayListTotalResulBean>> cardPayDetail(PageForm pageForm, Integer card_id, String pay_from, Short pay_method, Short is_paid, String date_start, String date_end, String paid_start, String paid_end) {
		
		PageData<CcGprsPay, PayListTotalResulBean>  data = null;
		if(card_id == null) {
			return ResultUtil.build(-1, "请选择您要查询的流量卡");
		}
		data = iCcGprsPayService.getPayListPage(pageForm, null, null, null, card_id, null, null, null, pay_from, pay_method, is_paid, date_start, date_end, paid_start, paid_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "业务管理-流量分配详情")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "card_id", value = "流量卡id", required = true, dataType = "int", paramType = "query")
	})
	@RequestMapping(path="/gprsAllotList",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<GprsAllotResultBean, Object>> gprsAllotList(PageForm pageForm, Integer card_id) {
		
		PageData<GprsAllotResultBean, Object>  data = null;
		if(card_id == null) {
			return ResultUtil.build(-1, "请选择您要查询的流量卡");
		}
		data = iCcGprsValueService.getAllotPage(pageForm, card_id, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "业务管理-流量卡生命周期日志")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "card_id", value = "流量卡id", required = true, dataType = "int", paramType = "query")
	})
	@RequestMapping(path="/logList",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcCardLog, Object>> logList(PageForm pageForm, Integer card_id) {
		
		PageData<CcCardLog, Object>  data = null;
		if(card_id == null) {
			return ResultUtil.build(-1, "请选择您要查询的流量卡");
		}
		data = iCcCardLogService.logList(pageForm, card_id, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "业务管理-流量卡日使用情况")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "card_id", value = "流量卡id", required = true, dataType = "int", paramType = "query")
	})
	@RequestMapping(path="/dayUse",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcStatsDay, Object>> dayUsePage(PageForm pageForm, Integer card_id) {
		
		PageData<CcStatsDay, Object>  data = null;
		if(card_id == null) {
			return ResultUtil.build(-1, "请选择您要查询的流量卡");
		}
		data = iCcStatsDayService.getDayUsePage(pageForm, card_id, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "业务管理-流量卡月使用情况")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "card_id", value = "流量卡id", required = true, dataType = "int", paramType = "query")
	})
	@RequestMapping(path="/monthUse",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcStatsMonth, Object>> monthUsePage(PageForm pageForm, Integer card_id) {
		
		PageData<CcStatsMonth, Object>  data = null;
		if(card_id == null) {
			return ResultUtil.build(-1, "请选择您要查询的流量卡");
		}
		data = iCcStatsMonthService.getMonthUsePage(pageForm, card_id, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "业务管理-流量卡基本信息")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "card_id", value = "流量卡id", required = true, dataType = "int", paramType = "query")
	})
	@RequestMapping(path="/detail",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<CardDetailInfoBean> detail(PageForm pageForm, Integer card_id) {
		
		CardDetailInfoBean  data = null;
		if(card_id == null) {
			return ResultUtil.build(-1, "请选择您要查询的流量卡");
		}
		data = iCcGprsCardService.detailByCardId(card_id);
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "业务管理-机构流量卡统计")
	@RequestMapping(path="/cardTotalByOrgidGroup",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CardTotalByOrgidInfoBean, Object>> cardTotalByOrgidGroupPage(PageForm pageForm) {
		
		PageData<CardTotalByOrgidInfoBean, Object> data = iCcGprsCardService.cardTotalByOrgidGroupPage(pageForm, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "业务管理-数据同步")
	@RequestMapping(path="/syncUnicomData",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<UnicomDataBean> syncUnicomData(String card_sn) {
		
		UnicomDataBean bean = iCcGprsCardService.syncUnicomData(card_sn, this.getLoginBaseInfo());
		return ResultUtil.success(bean);
	}
	
	@ApiOperation(value = "业务管理-流量卡管理(开卡/停卡)")
	@RequestMapping(path="/onoff",method= {RequestMethod.POST})
	public Result<UnicomDataBean> onoff(CardOnoffForm form) {
		
		boolean isSuccess = iCcGprsCardService.onoff(form, this.getLoginBaseInfo());
		if(isSuccess) {
			return ResultUtil.successCN(null);
		}else {
			
			return ResultUtil.build(-1, "操作失败");
		}
	}
	
}
