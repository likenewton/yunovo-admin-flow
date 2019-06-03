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
import cn.yunovo.iov.fc.model.result.CardUsedResultBean;
import cn.yunovo.iov.fc.service.ICcGprsCardService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="统计分析-累计用量")
@RequestMapping(path="/api/gprs/cardUsed", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CardUsedController extends BaseController{

	@Autowired
	private ICcGprsCardService iCcGprsCardService;
	
	@ApiOperation(value="统计分析-累计用量")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "开始日期 YYYY-MM-DD", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "结束日期 YYYY-MM-DD", required = false, dataType = "String",paramType = "query")})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CardUsedResultBean, CardUsedResultBean>> getCardUsedPage(PageForm pageForm, Integer org_id, String date_start, String date_end) {
		
		PageData<CardUsedResultBean, CardUsedResultBean>  data = iCcGprsCardService.getCardUsedPage(pageForm, org_id, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
}
