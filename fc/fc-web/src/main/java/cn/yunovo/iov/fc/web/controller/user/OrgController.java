package cn.yunovo.iov.fc.web.controller.user;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.form.OrgForm;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags="用户权限-机构管理")
@RequestMapping(path="/api/user/org", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class OrgController extends BaseController{

	@Autowired
	private ICcOrgService iCcOrgService;
	
	
	@ApiOperation(value="用户权限-机构管理列表")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query")
			})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcOrg, Object>> getListPage(Integer org_id, PageForm pageForm) {
		
		PageData<CcOrg, Object> data = iCcOrgService.getListPage(pageForm, org_id, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value="用户权限-机构新增")
	@RequestMapping(path="/insert",method= {RequestMethod.POST})
	public Result<Object> insert(@RequestBody OrgForm org){
		
		int result = iCcOrgService.insert(org, this.getLoginBaseInfo());
		
		if(result < 1) {
			
			log.warn("[OrgController.insert][warn]params={},username={},msg={}", JSONObject.toJSONString(org), this.getLoginBaseInfo().getLoginName(),"机构新增失败");
			return ResultUtil.build(-1, "新增失败");
		}
		
		return ResultUtil.build(0, "新增成功");
	}
	
	@ApiOperation(value="用户权限-机构修改")
	@RequestMapping(path="/update",method= {RequestMethod.POST})
	public Result<Object> update(@RequestBody OrgForm org){
		
		int result = iCcOrgService.update(org, this.getLoginBaseInfo());
		
		if(result < 1) {
			
			log.warn("[OrgController.update][warn]params={},username={},msg={}", JSONObject.toJSONString(org), this.getLoginBaseInfo().getLoginName(),"机构信息修改失败");
			return ResultUtil.build(-1, "修改失败");
		}
		
		return ResultUtil.build(0, "修改成功");
	}
	
	@ApiOperation(value="用户权限-机构删除")
	@RequestMapping(path="/delete",method= {RequestMethod.POST})
	public Result<Object> delete(@RequestBody OrgForm org){
		
		int result = iCcOrgService.delete(org.getOrgs(), this.getLoginBaseInfo());
		
		if(result < 1) {
			
			log.warn("[OrgController.update][warn]params={},username={},msg={}", JSONObject.toJSONString(org), this.getLoginBaseInfo().getLoginName(),"机构信息删除失败");
			return ResultUtil.build(-1, "删除失败");
		}
		
		return ResultUtil.build(0, "删除成功");
	}
	
	
}
