package cn.yunovo.iov.fc.web.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path="/api/home", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value="/api/home", tags="首页接口列表")
public class HomeController extends BaseController{

	@Autowired
	private HomeService homeService;
	
	@ApiOperation(notes="获取数据看板数据接口", value = "获取数据看板数据接口")
	@RequestMapping(path="/payCase", method= {RequestMethod.GET})
	public Result<HashMap<String, HashMap<String, Object>>> getPayDetail() throws Exception {
		
		HashMap<String, HashMap<String, Object>> data = homeService.getData(this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
}
