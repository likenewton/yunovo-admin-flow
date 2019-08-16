package cn.yunovo.iov.fc.web.controller.gprs;

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
import cn.yunovo.iov.fc.model.entity.CcNotify;
import cn.yunovo.iov.fc.service.ICcNotifyService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="统计分析-通知来源")
@RequestMapping(path="/api/gprs/notifyFrom", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class NotifyFromController extends BaseController{

	@Autowired
	private ICcNotifyService iCcNotifyService;
	
	@ApiOperation(value = "统计分析-通知来源")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "导卡时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "导卡时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "ntf_type", value = "通知或来源", required = false, dataType = "String", paramType = "query")
			})
	@OpLog(opType=OpTypeEnum.QUERY, opName="统计分析-通知来源")
	@RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<CcNotify, Object>> getItemsPage(PageForm pageForm, String ntf_type, String date_start, String date_end) {
		
		PageData<CcNotify, Object> data = iCcNotifyService.getItemsPage(pageForm, ntf_type, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@OpLog(opType=OpTypeEnum.QUERY, opName="统计分析-通知来源(机构明细)")
	@ApiOperation(value = "统计分析-通知来源(机构明细)")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "ntf_date", value = "统计日期", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "ntf_type", value = "通知或来源", required = false, dataType = "String", paramType = "query")
			})
	@RequestMapping(path = "/org", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<CcNotify, Object>> getItemsOrgPage(PageForm pageForm, String ntf_type, String ntf_date, Integer org_id) {
		
		PageData<CcNotify, Object> data = iCcNotifyService.getItemsOrgPage(pageForm, ntf_type, org_id, ntf_date, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
}
