package cn.yunovo.iov.fc.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.entity.CcStats;
import cn.yunovo.iov.fc.model.result.HomeChartDataBean;
import cn.yunovo.iov.fc.model.result.HomeSimTrendInfoBean;
import cn.yunovo.iov.fc.service.IHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
	
	@ApiOperation(notes="首页-sim卡使用趋势", value = "首页-sim卡使用趋势")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "date_start", value = "统计日期-开始日期（YYYY-MM-DD）", required = true, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "统计日期-结束日期（YYYY-MM-DD）", required = true, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "stype", value = "统计区间，0 天、1 月、 2 年 、0 周", required = true, dataType = "int",paramType = "query")
			})
	@RequestMapping(path="/simTrend", method= {RequestMethod.GET})
	public Result<List<HomeSimTrendInfoBean>> simTrend(Integer stype, String date_start, String date_end) throws Exception {
		
		if(StringUtils.isEmpty(date_start) || StringUtils.isEmpty(date_end)) {
			ResultUtil.build(-1, "请输入正确的统计时间区间");
		}
		
		if(stype < 0 || stype > 2) {
			stype = 0;
		}
		List<HomeSimTrendInfoBean> data = homeService.simTrend(stype, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(notes="首页-充值趋势", value = "首页-充值趋势")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "date_start", value = "统计日期-开始日期（YYYY-MM-DD）", required = true, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "统计日期-结束日期（YYYY-MM-DD）", required = true, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "stype", value = "统计区间，0 天、1 月、 2 年 、0 周", required = true, dataType = "int",paramType = "query")
			})
	@RequestMapping(path="/topupTrend", method= {RequestMethod.GET})
	public Result<Map<String, List<HomeChartDataBean>>> topupTrend(Integer stype, String date_start, String date_end) throws Exception {
		
		if(StringUtils.isEmpty(date_start) || StringUtils.isEmpty(date_end)) {
			ResultUtil.build(-1, "请输入正确的统计时间区间");
		}
		
		if(stype < 0 || stype > 2) {
			stype = 0;
		}
		Map<String, List<HomeChartDataBean>> data = homeService.topupTrend(stype, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(notes="首页-订单趋势", value = "首页-订单趋势")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "date_start", value = "统计日期-开始日期（YYYY-MM-DD）", required = true, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "统计日期-结束日期（YYYY-MM-DD）", required = true, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "stype", value = "统计区间，0 天、1 月、 2 年 、0 周", required = true, dataType = "int",paramType = "query")
			})
	@RequestMapping(path="/orderTrend", method= {RequestMethod.GET})
	public Result<Map<String, List<HomeChartDataBean>>> orderTrend(Integer stype, String date_start, String date_end) throws Exception {
		
		if(StringUtils.isEmpty(date_start) || StringUtils.isEmpty(date_end)) {
			ResultUtil.build(-1, "请输入正确的统计时间区间");
		}
		
		if(stype < 0 || stype > 2) {
			stype = 0;
		}
		Map<String, List<HomeChartDataBean>> data = homeService.orderTrend(stype, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(notes="首页-流量消耗趋势", value = "首页-流量消耗趋势")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "date_start", value = "统计日期-开始日期（YYYY-MM-DD）", required = true, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "统计日期-结束日期（YYYY-MM-DD）", required = true, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "stype", value = "统计区间，0 天、1 月、 2 年 、0 周", required = true, dataType = "int",paramType = "query")
			})
	@RequestMapping(path="/gprsTrend", method= {RequestMethod.GET})
	public Result<Map<String, List<HomeChartDataBean>>> gprsTrend(Integer stype, String date_start, String date_end) throws Exception {
		
		if(StringUtils.isEmpty(date_start) || StringUtils.isEmpty(date_end)) {
			ResultUtil.build(-1, "请输入正确的统计时间区间");
		}
		
		if(stype < 0 || stype > 2) {
			stype = 0;
		}
		Map<String, List<HomeChartDataBean>> data = homeService.gprsTrend(stype, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(notes="首页-客单价趋势", value = "首页-客单价趋势")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "date_start", value = "统计日期-开始日期（YYYY-MM-DD）", required = true, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "统计日期-结束日期（YYYY-MM-DD）", required = true, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "stype", value = "统计区间，0 天、1 月、 2 年 、0 周", required = true, dataType = "int",paramType = "query")
	})
	@RequestMapping(path="/priceTrend", method= {RequestMethod.GET})
	public Result<Map<String, List<HomeChartDataBean>>> priceTrend(Integer stype, String date_start, String date_end) throws Exception {
		
		if(StringUtils.isEmpty(date_start) || StringUtils.isEmpty(date_end)) {
			ResultUtil.build(-1, "请输入正确的统计时间区间");
		}
		
		if(stype < 0 || stype > 2) {
			stype = 0;
		}
		Map<String, List<HomeChartDataBean>> data = homeService.priceTrend(stype, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
}
