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
import cn.yunovo.iov.fc.model.form.PayForm;
import cn.yunovo.iov.fc.model.result.PayInfoBean;
import cn.yunovo.iov.fc.service.ICcExtensionService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags="系统设置-支付方式")
@RequestMapping(path="/api/system", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PaysController extends BaseController{

	@Autowired
	private ICcExtensionService iCcExtensionService;
	
	@RequestMapping(path="/pays/", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式查询")
	public Result<List<PayInfoBean>> getItems() {
		
		List<PayInfoBean> info = iCcExtensionService.getPaymentItems(this.getLoginBaseInfo());
		return ResultUtil.success(info);
		
	}
	
	@RequestMapping(path="/pays/install", method= {RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式(安装)")
	public Result<Object> install(@RequestBody PayForm form) {
		
		if(StringUtils.isEmpty(form.getType())) {
			return ResultUtil.build(400, "系统提示：请选择您要安装的支付方式");
		}
		
		iCcExtensionService.paymentInstall(form.getType());
		return ResultUtil.successCN(null);
	}
	
	@RequestMapping(path="/pays/uninstall", method= {RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式(卸载)")
	public Result<Object> uninstall(@RequestBody PayForm form) {
		if(StringUtils.isEmpty(form.getType())) {
			return ResultUtil.build(400, "系统提示：请选择您要卸载的支付方式");
		}
		
		iCcExtensionService.paymentUninstall(form.getType());
		return ResultUtil.successCN(null);
	}
	
	@RequestMapping(path="/pays/detail", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式(详情)")
	public Result<Map<String, String>> detail(@ApiParam(value="支付方式")String type) {
		Map<String, String> data = iCcExtensionService.paymentDetail(type);
		return ResultUtil.success(data);
	}
	
	@RequestMapping(path="/pays/edit", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-支付方式(编辑)")
	public void edit(@RequestBody PayForm form) {
		
		
	}
}
