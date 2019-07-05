package cn.yunovo.iov.fc.web.controller.gprs;

import java.io.IOException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcRealname;
import cn.yunovo.iov.fc.model.entity.CcResetLog;
import cn.yunovo.iov.fc.model.form.RealnameForm;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;
import cn.yunovo.iov.fc.service.ICcRealnameService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags="业务管理-流量卡实名接口列表")
@RequestMapping(path="/api/realname", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
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
	
	@ApiOperation(value="业务管理-流量卡实名列表接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "card_iccid", value = "卡iccid", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "status", value = "审批状态", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "申请时间-开始日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "申请时间-结束日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query")
	})
	@RequestMapping(path="/export",method= {RequestMethod.GET, RequestMethod.POST})
	public void export(PageForm form, String card_iccid, Integer org_id, String date_start,
			String date_end, Integer status) throws IOException {
		
		iCcRealnameService.getItemsPageExport(org_id, card_iccid, date_start, date_end, status, this.getLoginBaseInfo());
	}
	
	@ApiOperation(value="业务管理-流量卡实名审批接口")
	@RequestMapping(path="/audit",method= {RequestMethod.POST})
	public Result<String> audit(@RequestBody RealnameForm form) {

		form.validate();
		boolean isOk = iCcRealnameService.audit(form, this.getLoginBaseInfo());
		if(!isOk) {
			log.warn("[audit][实名审核失败]params={}", JSONObject.toJSONString(form));
			return ResultUtil.build(-1, "实名审批失败！");
		}else {
			return ResultUtil.successCN(null);
		}
	}
	
	@ApiOperation(value="业务管理-流量卡实名解除")
	@RequestMapping(path="/unbind",method= {RequestMethod.POST})
	public Result<String> unbind(@RequestBody RealnameForm form) {

		form.validate(UpdateGroupValidate.class);
		boolean isOk = iCcRealnameService.unbind(form, this.getLoginBaseInfo());
		if(!isOk) {
			log.warn("[unbind][实名认证解绑异常，请重试！]params={}", form.buildJsonString());
			return ResultUtil.build(-1, "实名认证解绑异常，请重试！");
		}else {
			return ResultUtil.successCN(null);
		}
		
	}
	
}
