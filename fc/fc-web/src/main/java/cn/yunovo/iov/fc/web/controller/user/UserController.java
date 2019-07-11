package cn.yunovo.iov.fc.web.controller.user;


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
import cn.yunovo.iov.fc.model.form.UserForm;
import cn.yunovo.iov.fc.model.form.group.DeleteGroupValidate;
import cn.yunovo.iov.fc.model.form.group.InsertGroupValidate;
import cn.yunovo.iov.fc.model.form.group.UpdateGroupValidate;
import cn.yunovo.iov.fc.model.result.UserResultBean;
import cn.yunovo.iov.fc.service.ICcUserService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(tags="用户权限-用户机构接口列表")
@RequestMapping(path="/api/user", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController extends BaseController{

	@Autowired
	private ICcUserService iCcUserService;
	
	
	@ApiOperation(value="用户权限-用户信息查询")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "所属机构", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "username", value = "用户账号", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "firstname", value = "真实姓名", required = false, dataType = "String",paramType = "query"),
			})
	@RequestMapping(path="/",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<UserResultBean, Object>> getListPage(Integer org_id, PageForm pageForm, String firstname, String username) {
		
		PageData<UserResultBean, Object> data = iCcUserService.userListPage(pageForm, username, firstname, org_id, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	
	@ApiOperation(value="用户权限-用户信息修改")
	@RequestMapping(path="/edit",method= {RequestMethod.POST})
	public Result<Object> edit(@RequestBody UserForm form) {
		
		form.validate(InsertGroupValidate.class);
		boolean isOk = iCcUserService.edit(form, getLoginBaseInfo());
		if(isOk) {
			log.info("[edit][用户信息修改成功]params={},user={}", form.buildJsonString(), this.getLoginBaseInfo().buildJsonString());
			return ResultUtil.successCN(null);
		}else {
			log.warn("[edit][用户信息修改失败]params={},user={}", form.buildJsonString(), this.getLoginBaseInfo().buildJsonString());
			return ResultUtil.build(-1, "操作失败");
		}
	}
	
	@ApiOperation(value="用户权限-用户启用停用修改")
	@RequestMapping(path="/onoff",method= {RequestMethod.POST})
	public Result<Object> onoff(@RequestBody UserForm form) {
		
		form.validate(UpdateGroupValidate.class);
		boolean isOk = iCcUserService.onoff(form, getLoginBaseInfo());
		if(isOk) {
			log.info("[onoff][用户信息修改成功]params={},user={}", form.buildJsonString(), this.getLoginBaseInfo().buildJsonString());
			return ResultUtil.successCN(null);
		}else {
			log.warn("[onoff][用户信息修改失败]params={},user={}", form.buildJsonString(), this.getLoginBaseInfo().buildJsonString());
			return ResultUtil.build(-1, "操作失败");
		}
	}
	
	@ApiOperation(value="用户权限-用户信息删除")
	@RequestMapping(path="/del",method= {RequestMethod.POST})
	public Result<Object> del(@RequestBody UserForm form) {
		
		form.validate(DeleteGroupValidate.class);
		boolean isOk = iCcUserService.onoff(form, getLoginBaseInfo());
		if(isOk) {
			log.info("[del][用户信息删除成功]params={},user={}", form.buildJsonString(), this.getLoginBaseInfo().buildJsonString());
			return ResultUtil.successCN(null);
		}else {
			log.warn("[del][用户信息删除失败]params={},user={}", form.buildJsonString(), this.getLoginBaseInfo().buildJsonString());
			return ResultUtil.build(-1, "操作失败");
		}
	}
}
