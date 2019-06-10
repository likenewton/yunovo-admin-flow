package cn.yunovo.iov.fc.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.service.ICcOrgService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags="用户权限-机构管理")
@RequestMapping(path="/api/user/org", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
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
	
	
}
