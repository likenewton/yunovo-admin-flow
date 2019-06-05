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
	
	@ApiOperation(value = "统计分析-充值总额")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<CcGprsPay, CcGprsPay>> payReport(PageForm pageForm, Integer org_id, String date_start, String date_end) {
		
		PageData<CcGprsPay, CcGprsPay>  data = iCcGprsPayService.getPayCountPage(pageForm, org_id, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "统计分析-机构充值统计")
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
	
	
	
}
