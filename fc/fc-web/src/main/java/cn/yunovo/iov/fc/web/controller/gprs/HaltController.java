package cn.yunovo.iov.fc.web.controller.gprs;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="统计分析-已停卡况")
@RequestMapping(path="/api/gprs/halt", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HaltController extends BaseController{

	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@ApiOperation(value="统计分析-已停卡况查询接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_type", value = "卡商类型id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "time_expire", value = "过期时间，空表示所有，1 代表未过期，0 代表已过期", required = false, dataType = "int",paramType = "query")})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcGprsCard, HashMap<String, Double>>> getHaltPage(Integer org_id, Integer card_type, String card_iccid, Integer time_expire, PageForm page){
	
		PageData<CcGprsCard, HashMap<String, Double>>  result = iCcGprsCardService.getHaltPage(page, card_iccid, card_type, org_id, time_expire, this.getLoginBaseInfo());
		return ResultUtil.success(result);
	}
	
	@ApiOperation(value="统计分析-已停卡况查询导出接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_type", value = "卡商类型id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "time_expire", value = "过期时间，空表示所有，1 代表未过期，0 代表已过期", required = false, dataType = "int",paramType = "query")})
	@RequestMapping(path="/export",method= {RequestMethod.GET, RequestMethod.POST})
	public void export(Integer org_id, Integer card_type, String card_iccid, Integer time_expire) throws IOException{
		
		iCcGprsCardService.getHaltPageExport(card_iccid, card_type, org_id, time_expire, this.getLoginBaseInfo());
	}
	
}
