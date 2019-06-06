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
import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.result.OrgPayReportResultBean;
import cn.yunovo.iov.fc.model.result.PayCountResultBean;
import cn.yunovo.iov.fc.model.result.PayDetailResultBean;
import cn.yunovo.iov.fc.model.result.PayListTotalResulBean;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcGprsPayService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="财务报表模块接口列表")
@RequestMapping(path="/api/gprs/report", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ReportController extends BaseController{

	@Autowired
	private ICcGprsPayService iCcGprsPayService;
	
	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@ApiOperation(value = "财务报表-充值总额")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<PayCountResultBean, PayCountResultBean>> payReport(PageForm pageForm, Integer org_id, String date_start, String date_end) {
		
		PageData<PayCountResultBean, PayCountResultBean>  data = iCcGprsPayService.getPayCountPage(pageForm, org_id, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "财务报表-机构充值统计")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pay_method", value = "付款方式", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path = "/orgPayReport", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<OrgPayReportResultBean, OrgPayReportResultBean>> payReport(PageForm pageForm, Integer org_id, String date_start, String date_end, Integer pay_method) {
		
		PageData<OrgPayReportResultBean, OrgPayReportResultBean>  data = iCcGprsPayService.getOrgPayReportPage(pageForm, org_id, pay_method, date_start, null, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "财务报表-充值明细")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "充值时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "充值时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pay_sn", value = "付款方式", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "流量卡号", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "card_type", value = "卡商类型", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "transfer_id", value = "支付流水号", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "gprs_amount", value = "套餐流量", required = false, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "pay_from", value = "订单来源", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "is_paid", value = "支付状态", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "paid_start", value = "付款时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "paid_end", value = "付款时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pay_method", value = "付款方式", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path = "/getPayListPage", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<CcGprsPay, PayListTotalResulBean>> getPayListPage(PageForm pageForm, Integer org_id, String pay_sn, String card_iccid, Integer card_type, String transfer_id, Double gprs_amount, String pay_from, Short pay_method, Short is_paid, String date_start, String date_end, String  paid_start, String paid_end) {
		
		PageData<CcGprsPay, PayListTotalResulBean>  data = iCcGprsPayService.getPayListPage(pageForm, org_id, pay_sn, card_iccid, null, card_type, transfer_id, gprs_amount, pay_from, pay_method, is_paid, date_start, date_end, paid_start, paid_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "财务报表-机构充值明细")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path = "/payDetail", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<PayDetailResultBean, PayDetailResultBean>> getPayDetailPage(PageForm pageForm, Integer org_id, String date_start, String date_end) {
		
		PageData<PayDetailResultBean, PayDetailResultBean>  data = iCcGprsCardService.getPayDetailPage(pageForm, org_id, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
}
