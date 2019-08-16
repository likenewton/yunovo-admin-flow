package cn.yunovo.iov.fc.web.controller.gprs;


import java.util.List;

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
import cn.yunovo.iov.fc.model.entity.CcOnoffLog;
import cn.yunovo.iov.fc.service.ICcOnoffLogService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="统计分析-停卡日志")
@RequestMapping(path="/api/gprs/onoffLog", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OnoffLogController extends BaseController{

	@Autowired
	private ICcOnoffLogService iCcOnoffLogService;
	
	@ApiOperation(value="统计分析-停卡日志")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_type", value = "卡商类型id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_id", value = "流量卡id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query")})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	@OpLog(opType=OpTypeEnum.QUERY, opName="统计分析-停卡日志")
	public Result<PageData<CcOnoffLog, Object>> onoffLogs(Integer org_id, Integer card_type, String card_iccid, PageForm page, Integer card_id){
	
		PageData<CcOnoffLog, Object>  result = iCcOnoffLogService.getItems(page, card_iccid, card_type, org_id, card_id, this.getLoginBaseInfo());
		return ResultUtil.success(result);
	}
	
	@ApiOperation(value="统计分析-停卡日志明细")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "card_id", value = "流量卡id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query")})
	@RequestMapping(path="/detail",method= {RequestMethod.GET, RequestMethod.POST})
	@OpLog(opType=OpTypeEnum.QUERY, opName="统计分析-停卡日志明细")
	public Result<List<CcOnoffLog>> detail(Integer card_id, String card_iccid){
		
		return ResultUtil.success(iCcOnoffLogService.stopDetail(card_id, card_iccid, this.getLoginBaseInfo()));
	}
	
}
