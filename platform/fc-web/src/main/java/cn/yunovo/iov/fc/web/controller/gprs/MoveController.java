package cn.yunovo.iov.fc.web.controller.gprs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.common.utils.log.OpLog;
import cn.yunovo.iov.fc.common.utils.log.OpTypeEnum;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsMove;
import cn.yunovo.iov.fc.model.form.GprsMoveForm;
import cn.yunovo.iov.fc.service.ICcGprsMoveService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="业务管理-流量迁移接口列表")
@RequestMapping(path="/api/gprs/move", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MoveController extends BaseController{

	@Autowired
	private ICcGprsMoveService iCcGprsMoveService;
	
	@ApiOperation(value="业务管理-流量卡重置历史查询接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "old_card_iccid", value = "老卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "迁移时间-开始日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "迁移时间-结束日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query")
			})
	@OpLog(opType=OpTypeEnum.QUERY, opName="业务管理-流量卡重置历史查询接口")
	@RequestMapping(path="/history",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcGprsMove, Object>> list(PageForm form, String card_iccid, Integer org_id, String date_start,
			String date_end, String old_card_iccid) {
		
		PageData<CcGprsMove, Object>  data = iCcGprsMoveService.getItemsPage(form, org_id, card_iccid, old_card_iccid, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@OpLog(opType=OpTypeEnum.UPDATE, opName="业务管理-流量卡重置")
	@ApiOperation(value="业务管理-流量卡重置")
	@RequestMapping(path="/",method= {RequestMethod.POST})
	public Result<String> move(@RequestBody GprsMoveForm form) {
		
		form.validate();
		iCcGprsMoveService.move(form, this.getLoginBaseInfo());
		return ResultUtil.successCN(null);
	}
	
}
