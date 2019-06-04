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
import cn.yunovo.iov.fc.model.entity.CcStats;
import cn.yunovo.iov.fc.service.ICcStatsService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="统计分析-流量卡运营统计")
@RequestMapping(path="/api/gprs/payOnline", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PayOnlineController extends BaseController{

	@Autowired
	private ICcStatsService iCcStatsService;
	
	@ApiOperation(value = "统计分析-流量卡运营统计")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "date_start", value = "导卡时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "导卡时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query")
			})
	@RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<CcStats, Object>> getItemsPage(PageForm pageForm, String date_start, String date_end) {
		
		PageData<CcStats, Object> data = iCcStatsService.getItemsPage(pageForm, null, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "统计分析-流量卡运营统计(机构明细)")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "stats_date", value = "统计日期 YYYY-MM-DD", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query")
			})
	@RequestMapping(path = "/org", method = { RequestMethod.GET, RequestMethod.POST })
	public Result<PageData<CcStats, Object>> getItemsOrgPage(PageForm pageForm, Integer org_id, String stats_date) {
		
		PageData<CcStats, Object> data = iCcStatsService.getItemsOrgPage(pageForm, org_id, stats_date, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
}
