package cn.yunovo.iov.fc.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.form.PayForm;
import cn.yunovo.iov.fc.model.form.SystemParamsForm;
import cn.yunovo.iov.fc.service.ICcSettingService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="系统设置-系统参数设置")
@RequestMapping(path="/api/system", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ParamsController extends BaseController{

	@Autowired
	private ICcSettingService iCcSettingService;
	
	@RequestMapping(path="/params/", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-系统参数详情")
	public Result<SystemParamsForm> detail() {
		SystemParamsForm data = iCcSettingService.systemParams();
		return ResultUtil.success(data);
	}
	
	@RequestMapping(path="/params/update", method= {RequestMethod.POST})
	@ApiOperation(value="系统设置-系统参数详情编辑")
	public Result<Object> update(@RequestBody SystemParamsForm form) {
		
		form.validate();
		iCcSettingService.updateSystemParams(form, this.getLoginBaseInfo());
		return ResultUtil.successCN(null);
	}
}
