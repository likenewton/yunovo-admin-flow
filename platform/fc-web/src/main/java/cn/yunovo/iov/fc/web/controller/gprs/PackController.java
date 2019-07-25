package cn.yunovo.iov.fc.web.controller.gprs;

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
import cn.yunovo.iov.fc.model.entity.CcGprsPack;
import cn.yunovo.iov.fc.model.form.GprsPackForm;
import cn.yunovo.iov.fc.model.form.group.DeleteGroupValidate;
import cn.yunovo.iov.fc.model.form.group.InsertGroupValidate;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;
import cn.yunovo.iov.fc.service.ICcGprsPackService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="业务管理-套餐管理接口列表")
@RequestMapping(path="/api/gprs/pack", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PackController extends BaseController{

	@Autowired
	private ICcGprsPackService iCcGprsPackService;
	
	@ApiOperation(value="业务管理-套餐查询接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query")
			})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcGprsPack, Object>> list(PageForm form, Integer org_id) {
		
		PageData<CcGprsPack, Object>  data = iCcGprsPackService.getItemsPage(form, org_id, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value="业务管理-套餐新增接口")
	@RequestMapping(path="/insert",method= {RequestMethod.POST})
	public Result<String> insert(@RequestBody GprsPackForm form) {
		
		form.validate(InsertGroupValidate.class);
		form.setAllot_value(GprsPackForm.computeAllotValue(form.getGprs_amount(), form.getAllot_month()));
		iCcGprsPackService.save(form, this.getLoginBaseInfo());
		return ResultUtil.successCN(null);
	}
	
	@ApiOperation(value="业务管理-套餐修改接口")
	@RequestMapping(path="/update",method= {RequestMethod.POST})
	public Result<String> update(@RequestBody GprsPackForm form) {
		
		form.validate(UpdateGroupValidate.class);
		form.setAllot_value(GprsPackForm.computeAllotValue(form.getGprs_amount(), form.getAllot_month()));
		iCcGprsPackService.update(form, this.getLoginBaseInfo());
		return ResultUtil.successCN(null);
	}
	
	@ApiOperation(value="业务管理-套餐详情")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "pack_id", value = "套餐id", required = false, dataType = "int",paramType = "query")
			})
	@RequestMapping(path="/detail",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<CcGprsPack> detail(Integer pack_id) {
		
		CcGprsPack pack = iCcGprsPackService.detail(pack_id, this.getLoginBaseInfo());
		return ResultUtil.successCN(pack);
	}
	
	@ApiOperation(value="业务管理-套餐停用")
	@RequestMapping(path="/stop",method= {RequestMethod.POST})
	public Result<Object> stop(@RequestBody GprsPackForm form) {
		
		form.stopValidate();
		iCcGprsPackService.stop(form, this.getLoginBaseInfo());
		return ResultUtil.successCN(null);
	}
}
