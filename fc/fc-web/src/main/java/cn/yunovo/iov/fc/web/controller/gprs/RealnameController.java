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
import cn.yunovo.iov.fc.model.entity.CcRealname;
import cn.yunovo.iov.fc.model.entity.CcResetLog;
import cn.yunovo.iov.fc.service.ICcRealnameService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="业务管理-流量卡实名接口列表")
@RequestMapping(path="/api/realname", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RealnameController extends BaseController{

	@Autowired
	private ICcRealnameService iCcRealnameService;
	
	@ApiOperation(value="业务管理-流量卡实名列表接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "status", value = "审批状态", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "申请时间-开始日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "申请时间-结束日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query")
			})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcRealname, Object>> list(PageForm form, String card_iccid, Integer org_id, String date_start,
			String date_end, Integer status) {
		
		PageData<CcRealname, Object>  data = iCcRealnameService.getItemsPage(form, org_id, card_iccid, date_start, date_end, status, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
}
