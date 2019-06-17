package cn.yunovo.iov.fc.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.entity.CcStats;
import cn.yunovo.iov.fc.service.IHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path="/api/home", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value="/api/home", tags="首页接口列表")
public class HomeController extends BaseController{

	@Autowired
	private IHomeService homeService;
	
	@ApiOperation(notes="获取数据看板数据接口", value = "获取数据看板数据接口")
	@RequestMapping(path="/payCase", method= {RequestMethod.GET})
	public Result<HashMap<String, HashMap<String, Object>>> getPayDetail() throws Exception {
		
		HashMap<String, HashMap<String, Object>> data = homeService.getData(this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(notes="首页-SIM卡统计", value = "首页-SIM卡统计")
	@RequestMapping(path="/siminfo", method= {RequestMethod.GET})
	public Result<CcStats> siminfo() throws Exception {
		
		CcStats data = homeService.siminfo(this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(notes="首页-数据总览", value = "首页-数据总览")
	@RequestMapping(path="/rl2pack", method= {RequestMethod.GET})
	public Result<Map<String, Integer>> rl2pack() throws Exception {
		
		Map<String, Integer> data = homeService.rl2pack(this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
}
