package cn.yunovo.iov.fc.web.controller.gprs;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.common.utils.web.WebRequestUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcStatsMonth;
import cn.yunovo.iov.fc.service.ICcStatsMonthService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags="统计分析-月度用量接口列表")
@RequestMapping(path="/api/gprs/stats", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class StatsController extends BaseController{

	@Autowired
	private ICcStatsMonthService iCcStatsMonthService;
	
	@ApiOperation(value="统计分析-月度用量查询接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "card_type", value = "卡商类型id", required = false, dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "mdate", value = "查询月份", required = false, dataType = "Integer",paramType = "query")})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcStatsMonth, Map<String,Object>>> query(Integer org_id, Integer card_type, String card_iccid, String mdate, PageForm page){
		
		PageData<CcStatsMonth, Map<String,Object>> pageInfo =  iCcStatsMonthService.getItemsPage(org_id, card_type, card_iccid, mdate, page, this.getLoginBaseInfo());
		return ResultUtil.build(0, "ok",pageInfo);
	}
	
	@ApiOperation(value="统计分析-月度用量导出接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "card_type", value = "卡商类型id", required = false, dataType = "Integer",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "mdate", value = "查询月份", required = false, dataType = "Integer",paramType = "query")})
	@RequestMapping(path="/export",method= {RequestMethod.GET})
	public void export(Integer org_id, Integer card_type, String card_iccid, String mdate){
		
		try {
			iCcStatsMonthService.export(org_id, card_type, card_iccid, mdate, this.getLoginBaseInfo());
		} catch (IOException e) {
			log.error("[StatsController.export][exception]params={},exception={}", JSONObject.toJSONString(WebRequestUtil.request().getParameterMap()), ExceptionUtils.getStackTrace(e));
		}
	}
	
}
