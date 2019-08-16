package cn.yunovo.iov.fc.web.controller.gprs;

import java.io.IOException;

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
import cn.yunovo.iov.fc.model.result.CardUsedResultBean;
import cn.yunovo.iov.fc.model.result.UnicomStatResultBean;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "统计分析-联通情况")
@RequestMapping(path = "/api/gprs/unicomStat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UnicomStatController extends BaseController {

	@Autowired
	private ICcGprsCardService iCcGprsCardService;

	@ApiOperation(value = "统计分析-联通情况")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "导卡时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "导卡时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "jstart", value = "激活时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "jend", value = "激活时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query") })
	@RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
	@OpLog(opType=OpTypeEnum.QUERY,opName="统计分析-联通情况")
	public Result<PageData<UnicomStatResultBean, UnicomStatResultBean>> getCardUsedPage(PageForm pageForm,
			Integer org_id, String date_start, String date_end, String jstart, String jend) {

		PageData<UnicomStatResultBean, UnicomStatResultBean> data = iCcGprsCardService.getUnicomStatPage(pageForm,
				org_id, date_start, date_end, jstart, jend, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value = "统计分析-联通流量卡统计导出")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "导卡时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "导卡时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "jstart", value = "激活时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "jend", value = "激活时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query") })
	@RequestMapping(path = "/export", method = { RequestMethod.GET, RequestMethod.POST })
	@OpLog(opType=OpTypeEnum.DOWNLOAD,opName="统计分析-联通流量卡统计导出")
	public void export(Integer org_id, String date_start, String date_end, String jstart, String jend) throws IOException {

		iCcGprsCardService.getUnicomStatPageExport(org_id, date_start, date_end, jstart, jend, this.getLoginBaseInfo());
	}
	
	@ApiOperation(value = "统计分析-已激活联通流量卡统计导出")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "导卡时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "导卡时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "jstart", value = "激活时间-开始日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "2 已激活,非2 未激活", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "jend", value = "激活时间-结束日期 YYYY-MM-DD", required = false, dataType = "String", paramType = "query") })
	@RequestMapping(path = "/cardExport", method = { RequestMethod.GET, RequestMethod.POST })
	@OpLog(opType=OpTypeEnum.DOWNLOAD,opName="统计分析-已激活联通流量卡统计导出")
	public void cardExport(Integer org_id, String date_start, String date_end, String jstart, String jend, Integer type) throws IOException {
		
		iCcGprsCardService.cardExport(org_id, date_start, date_end, jstart, jend, type, this.getLoginBaseInfo());
	}

}
