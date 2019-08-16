package cn.yunovo.iov.fc.web.controller.system;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import cn.yunovo.iov.fc.model.form.PayForm;
import cn.yunovo.iov.fc.model.result.PayInfoBean;
import cn.yunovo.iov.fc.service.ICcExtensionService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags="系统设置-支付方式")
@RequestMapping(path="/api/system", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PaysController extends BaseController{

	@Autowired
	private ICcExtensionService iCcExtensionService;
	
	@OpLog(opType=OpTypeEnum.QUERY, opName="系统设置-支付方式查询")
	@RequestMapping(path="/pays/", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式查询")
	public Result<List<PayInfoBean>> getItems() {
		
		List<PayInfoBean> info = iCcExtensionService.getPaymentItems(this.getLoginBaseInfo());
		return ResultUtil.success(info);
		
	}
	
	@OpLog(opType=OpTypeEnum.INSERT, opName="系统设置-支付方式(安装)")
	@RequestMapping(path="/pays/install", method= {RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式(安装)")
	public Result<Object> install(@RequestBody PayForm form) {
		
		if(StringUtils.isEmpty(form.getType())) {
			return ResultUtil.build(400, "系统提示：请选择您要安装的支付方式");
		}
		
		iCcExtensionService.paymentInstall(form.getType());
		return ResultUtil.successCN(null);
	}
	
	@OpLog(opType=OpTypeEnum.DELETE, opName="系统设置-支付方式(卸载)")
	@RequestMapping(path="/pays/uninstall", method= {RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式(卸载)")
	public Result<Object> uninstall(@RequestBody PayForm form) {
		if(StringUtils.isEmpty(form.getType())) {
			return ResultUtil.build(400, "系统提示：请选择您要卸载的支付方式");
		}
		
		iCcExtensionService.paymentUninstall(form.getType());
		return ResultUtil.successCN(null);
	}
	
	@OpLog(opType=OpTypeEnum.QUERY, opName="系统设置-支付方式(详情)")
	@RequestMapping(path="/pays/detail", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式(详情)")
	public Result<Map<String, String>> detail(@ApiParam(value="支付方式")String type) {
		Map<String, String> data = iCcExtensionService.paymentDetail(type);
		return ResultUtil.success(data);
	}
	
	@OpLog(opType=OpTypeEnum.UPDATE, opName="系统设置-支付方式(编辑)")
	@RequestMapping(path="/pays/update", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式(编辑)")
	public Result<Object> update(@RequestBody PayForm form) {
		
		form.validate();
		iCcExtensionService.paymentUpdate(form,this.getLoginBaseInfo());
		return ResultUtil.successCN(null);
		
	}
}
