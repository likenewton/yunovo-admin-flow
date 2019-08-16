package cn.yunovo.iov.fc.web.controller.gprs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.common.utils.log.OpLog;
import cn.yunovo.iov.fc.common.utils.log.OpTypeEnum;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsPay;
import cn.yunovo.iov.fc.model.result.MonthPayReportResultBean;
import cn.yunovo.iov.fc.model.result.OrgPayReportResultBean;
import cn.yunovo.iov.fc.model.result.PayCountResultBean;
import cn.yunovo.iov.fc.model.result.PayDetailResultBean;
import cn.yunovo.iov.fc.model.result.PayListChartDataResultBean;
import cn.yunovo.iov.fc.model.result.PayListTotalResulBean;
import cn.yunovo.iov.fc.model.result.PayPackResultBean;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.service.ICcGprsPayService;
import cn.yunovo.iov.fc.service.ICcGprsSnapService;
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
	
	@Autowired
	private ICcGprsSnapService iCcGprsSnapService;
	
	@OpLog(opType=OpTypeEnum.QUERY, opName="财务报表-充值总额")
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
	
	@OpLog(opType=OpTypeEnum.QUERY, opName="财务报表-机构充值统计")
	@ApiOperation(value = "财务报表-机构充值统计")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pay_method", value = "付款方式", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path = "/orgPayReport", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<OrgPayReportResultBean, OrgPayReportResultBean>> orgPayReport(PageForm pageForm, Integer org_id, String date_start, String date_end, Integer pay_method) {
		
		PageData<OrgPayReportResultBean, OrgPayReportResultBean>  data = iCcGprsPayService.getOrgPayReportPage(pageForm, org_id, pay_method, date_start, null, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@OpLog(opType=OpTypeEnum.QUERY, opName="财务报表-充值明细")
	@ApiOperation(value = "财务报表-充值明细")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "充值时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "充值时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pay_sn", value = "订单编号", required = false, dataType = "String", paramType = "query"),
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
	
	@OpLog(opType=OpTypeEnum.DOWNLOAD, opName="财务报表-充值明细导出")
	@ApiOperation(value = "财务报表-充值明细导出")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "充值时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "充值时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pay_sn", value = "订单编号", required = false, dataType = "String", paramType = "query"),
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
	@RequestMapping(path = "/getPayListPageExport", method = { RequestMethod.GET, RequestMethod.POST })
	public void getPayListPageExport(Integer org_id, String pay_sn, String card_iccid, Integer card_type, String transfer_id, Double gprs_amount, String pay_from, Short pay_method, Short is_paid, String date_start, String date_end, String  paid_start, String paid_end) throws IOException {
		iCcGprsPayService.getPayListPageExport(org_id, pay_sn, card_iccid, null, card_type, transfer_id, gprs_amount, pay_from, pay_method, is_paid, date_start, date_end, paid_start, paid_end, this.getLoginBaseInfo());
	}
	
	
	@OpLog(opType=OpTypeEnum.DOWNLOAD, opName="财务报表-充值快照明细导出")
	@ApiOperation(value = "财务报表-充值快照明细导出")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "充值时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "充值时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "流量卡号", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "card_type", value = "卡商类型", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "pay_from", value = "订单来源", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "paid_start", value = "付款时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "paid_end", value = "付款时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pay_method", value = "付款方式", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path = "/getPaySnapExport", method = { RequestMethod.GET, RequestMethod.POST })
	public void getPaySnapExport(Integer org_id, String card_iccid, Integer card_type, String pay_from, Short pay_method, String date_start, String date_end, String  paid_start, String paid_end) throws IOException {
		iCcGprsSnapService.export(org_id, card_iccid, card_type, pay_from, pay_method, date_start, date_end, paid_start, paid_end, this.getLoginBaseInfo());
	}
	
	@OpLog(opType=OpTypeEnum.QUERY, opName="财务报表-机构充值明细")
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
	
	
	@OpLog(opType=OpTypeEnum.QUERY, opName="财务报表-套餐充值统计")
	@ApiOperation(value = "财务报表-套餐充值统计")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pay_method", value = "付款方式", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path = "/payPack", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<PayPackResultBean, PayPackResultBean>> getPayPackPage(PageForm pageForm, Short pay_method,Integer org_id, String date_start, String date_end) {
		
		PageData<PayPackResultBean, PayPackResultBean>  data = iCcGprsPayService.getPayPackPage(pageForm, pay_method, org_id, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@OpLog(opType=OpTypeEnum.QUERY, opName="财务报表-充值月度统计")
	@ApiOperation(value = "财务报表-充值月度统计")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "mdate", value = "统计月份 YYYY-MM", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path = "/monthReport", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<MonthPayReportResultBean, OrgPayReportResultBean>> getMonthCountPage(PageForm pageForm, Integer org_id, String mdate) {
		
		PageData<MonthPayReportResultBean, OrgPayReportResultBean>  data = iCcGprsPayService.getMonthCountPage(pageForm, org_id, mdate, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@OpLog(opType=OpTypeEnum.QUERY, opName="财务报表-充值与分析统计图表")
	@ApiOperation(value = "财务报表-充值与分析统计图表")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "充值时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "充值时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pay_sn", value = "订单编号", required = false, dataType = "String", paramType = "query"),
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
	@RequestMapping(path = "/paylog/chart", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<Map<String, List<PayListChartDataResultBean>>> getPaylogChart(Integer org_id, String pay_sn, String card_iccid, Integer card_type, String transfer_id, Double gprs_amount, String pay_from, Short pay_method, Short is_paid, String date_start, String date_end, String  paid_start, String paid_end) {
		
		Map<String, List<PayListChartDataResultBean>>  data = iCcGprsPayService.getPaylogChart(org_id, pay_sn, card_iccid, null, card_type, transfer_id, gprs_amount, pay_from, pay_method, is_paid, date_start, date_end, paid_start, paid_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
}
