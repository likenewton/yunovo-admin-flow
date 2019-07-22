package cn.yunovo.iov.fc.web.controller.system;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcNation;
import cn.yunovo.iov.fc.model.form.NationForm;
import cn.yunovo.iov.fc.model.form.group.DeleteGroupValidate;
import cn.yunovo.iov.fc.model.form.group.InsertGroupValidate;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;
import cn.yunovo.iov.fc.service.ICcNationService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="系统设置-国家区域接口列表")
@RequestMapping(path="/api/system", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class NationController extends BaseController{

	@Autowired
	private ICcNationService iCcNationService;
	
	@RequestMapping(path="/nations/", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-国家区域查询列表")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "ntid", value = "区域id", required = false, dataType = "int",paramType = "query")
			})
	public Result<PageData<CcNation, List<CcNation>>> getNationsPage(Integer ntid, PageForm pageForm) {
		
		ntid = (ntid == null ? 0 : ntid);
		PageData<CcNation, List<CcNation>> data =  iCcNationService.getNationsPage(pageForm, ntid, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@RequestMapping(path="/nations/detail", method= {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value="系统设置-国家区域详情接口")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "ntid", value = "区域id", required = false, dataType = "int",paramType = "query")
			})
	public Result<CcNation> getById(Integer ntid) {
		
		CcNation data =  iCcNationService.getById(ntid);
		return ResultUtil.success(data);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(path="/nations/insert", method= {RequestMethod.POST})
	@ApiOperation(value="系统设置-国家区域新增接口")
	public Result<?> insert(@RequestBody NationForm form) {
		
		form.validate(InsertGroupValidate.class);
		
		if(form.getParent() == null) {
			form.setParent(0);
		}
		
		CcNation entity = new CcNation();
		BeanUtils.copyProperties(form, entity);
		
		iCcNationService.save(entity);
		return ResultUtil.successCN(null);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(path="/nations/update", method= {RequestMethod.POST})
	@ApiOperation(value="系统设置-国家区域修改接口")
	public Result<?> update(@RequestBody NationForm form) {
		
		form.validate(UpdateGroupValidate.class);
		CcNation entity = new CcNation();
		BeanUtils.copyProperties(form, entity);
		
		entity.setParent(null);
		
		iCcNationService.updateById(entity);
		return ResultUtil.successCN(null);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(path="/nations/delete", method= {RequestMethod.POST})
	@ApiOperation(value="系统设置-国家区域删除接口")
	public Result<?> delete(@RequestBody NationForm form) {
		
		form.validate(DeleteGroupValidate.class);
		iCcNationService.removeByIds(Arrays.asList(form.getNtids()));
		return ResultUtil.successCN(null);
	}
	
	
}
