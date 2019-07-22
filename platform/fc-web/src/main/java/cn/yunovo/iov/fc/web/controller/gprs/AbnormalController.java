package cn.yunovo.iov.fc.web.controller.gprs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.sunshine.dcda.system.service.model.SystemResourceVo;

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
@Api(tags="统计分析-用量异常")
@RequestMapping(path="/api/gprs/abnormal", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AbnormalController extends BaseController{

	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@ApiOperation(value="统计分析-用量异常查询接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_type", value = "卡商类型id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "max_unused", value = "剩余流量", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "unicom_diff", value = "日差异流量", required = false, dataType = "int",paramType = "query")})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcGprsCard, Object>> getItemsPage(Integer org_id, Integer card_type, String card_iccid, Integer max_unused,Integer unicom_diff, PageForm page) {
	
		PageData<CcGprsCard, Object> data = iCcGprsCardService.getItemsPage(page, card_iccid, card_type, org_id, max_unused, unicom_diff, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value="统计分析-用量异常导出接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_type", value = "卡商类型id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "max_unused", value = "剩余流量", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "unicom_diff", value = "日差异流量", required = false, dataType = "int",paramType = "query")})
	@RequestMapping(path="/export",method= {RequestMethod.GET, RequestMethod.POST})
	public void export(Integer org_id, Integer card_type, String card_iccid, Integer max_unused,Integer unicom_diff) throws IOException {
	
		iCcGprsCardService.getItemsPageExport(card_iccid, card_type, org_id, max_unused, unicom_diff, this.getLoginBaseInfo());
	}
	
}
