package cn.yunovo.iov.fc.web.controller.gprs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcResetLog;
import cn.yunovo.iov.fc.model.result.CardResetForm;
import cn.yunovo.iov.fc.model.result.CardRestBean;
import cn.yunovo.iov.fc.service.ICcResetLogService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="业务管理-卡重置接口列表")
@RequestMapping(path="/api/card/reset", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GprsCardResetController extends BaseController{

	@Autowired
	private ICcResetLogService iCcResetLogService;
	
	@ApiOperation(value="业务管理-流量卡重置历史查询接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "重置时间-开始日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "重置时间-结束日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query")
			})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcResetLog, Object>> list(PageForm form, String card_iccid, Integer org_id, String date_start,
			String date_end) {
		
		PageData<CcResetLog, Object>  data = iCcResetLogService.getItemsPage(form, org_id, card_iccid, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value="业务管理-流量卡重置接口")
	@RequestMapping(path="/gprs",method= {RequestMethod.POST})
	public Result<List<CardRestBean>> cardReset(@RequestBody CardResetForm form) {
		form.validate();
		List<CardRestBean>  data = iCcResetLogService.cardReset(form, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	
}
