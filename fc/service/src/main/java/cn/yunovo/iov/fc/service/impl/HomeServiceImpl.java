package cn.yunovo.iov.fc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.yunovo.iov.fc.dao.ICcGprsCardMapper;
import cn.yunovo.iov.fc.dao.ICcGprsPackMapper;
import cn.yunovo.iov.fc.dao.ICcGprsPayMapper;
import cn.yunovo.iov.fc.dao.ICcOnoffLogMapper;
import cn.yunovo.iov.fc.dao.ICcRealnameMapper;
import cn.yunovo.iov.fc.dao.ICcStatsMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.entity.CcStats;
import cn.yunovo.iov.fc.service.ICcUserService;
import cn.yunovo.iov.fc.service.IHomeService;

@Service
public class HomeServiceImpl implements IHomeService{

	@Autowired
	public ICcOnoffLogMapper iCcOnoffLogMapper;
	
	@Autowired
	private ICcGprsPayMapper iCcGprsPayMapper;
	
	@Autowired
	private ICcGprsCardMapper iCcGprsCardMapper;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Autowired
	private ICcStatsMapper iCcStatsMapper;
	
	@Autowired
	private ICcRealnameMapper iCcRealnameMapper;
	
	@Autowired
	private ICcGprsPackMapper iCcGprsPackMapper;
	
	@Override
	public HashMap<String, HashMap<String, Object>> getData(LoginInfo loginInfo) throws InterruptedException, ExecutionException{
		
		String orgpos = iCcUserService.getOrgpos(loginInfo.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			
			return null;
		}
		
		////获取今日停卡数量
		ExecutorService exc = Executors.newFixedThreadPool(7);
		FutureTask<HashMap<String, Object>> data1 = new FutureTask<>(new Callable<HashMap<String, Object>>() {
			@Override
			public HashMap<String, Object> call() throws Exception {
				
				return iCcOnoffLogMapper.getStopData(orgpos, orgpos.split(","));
			}

			
		});
		exc.execute(data1);
		
		//获取机构下的充值与返利情况
		FutureTask<HashMap<String, Object>> data2 = new FutureTask<>(new Callable<HashMap<String, Object>>() {

			@Override
			public HashMap<String, Object> call() throws Exception {
				return iCcGprsCardMapper.getCardCase(orgpos, orgpos.split(","));
			}
			
		});
		exc.execute(data2);
		
		//获取今日激活数量
		FutureTask<HashMap<String, Object>> data3 = new FutureTask<>(new Callable<HashMap<String, Object>>() {

			@Override
			public HashMap<String, Object> call() throws Exception {
				return iCcGprsPayMapper.getJRActiveData(orgpos, orgpos.split(","));
			}
			
		});
		exc.execute(data3);
		
		//获取当月续费机构下的当月流量卡统计
		FutureTask<HashMap<String, Object>> data4 = new FutureTask<>(new Callable<HashMap<String, Object>>() {

			@Override
			public HashMap<String, Object> call() throws Exception {
				String data = DateFormatUtils.format(new Date(), "yyyy-MM");
				return iCcGprsPayMapper.getDYPayCase(data + "-01 00:00:00", orgpos, orgpos.split(","));
			}
			
		});
		exc.execute(data4);
		
		//获取机构下的充值与返利情况
		FutureTask<HashMap<String, Object>> data5 = new FutureTask<>(new Callable<HashMap<String, Object>>() {

			@Override
			public HashMap<String, Object> call() throws Exception {
				
				return iCcGprsPayMapper.getPayCase(null, null, orgpos, orgpos.split(","));
			}

			
		});
		exc.execute(data5);
		
		
		//获取今日充值统计
		final String  now_date_start = DateFormatUtils.format(new Date(), "yyyy-MM-dd") + " 00:00:00";
		final String  now_date_end = DateFormatUtils.format(new Date(), "yyyy-MM-dd") + " 23:59:59";
		FutureTask<HashMap<String, Object>> data6 = new FutureTask<>(new Callable<HashMap<String, Object>>() {

			@Override
			public HashMap<String, Object> call() throws Exception {
				
				return iCcGprsPayMapper.getPayCase(now_date_start, now_date_end, orgpos, orgpos.split(","));
			}
			
		});
		exc.execute(data6);
		
		//获取上月续费机构下的当月流量卡统计
		String syMonth = DateFormatUtils.format(DateUtils.addMonths(new Date(), -1),"yyyy-MM");
		FutureTask<HashMap<String, Object>> data7 = new FutureTask<>(new Callable<HashMap<String, Object>>() {

			@Override
			public HashMap<String, Object> call() throws Exception {
				
				return iCcGprsPayMapper.getSYPayCase(syMonth + "-01 00:00:00", syMonth+"-31 23:59:59", orgpos, orgpos.split(","));
			}

			
		});
		exc.execute(data7);
		exc.shutdown();
		HashMap<String, HashMap<String, Object>> returnData = new HashMap<>();
		
		returnData.put("cres", data2.get());
		returnData.put("pres", data5.get());
		
		HashMap<String, Object> tres = new HashMap<>();
		tres.putAll(data3.get());
		tres.putAll(data6.get());
		tres.putAll(data1.get());
		returnData.put("tres", tres);
		
		returnData.put("dy_pres",data4.get());
		returnData.put("sy_pres",data7.get());
		
		return returnData;
	}
	
	@Override
	public CcStats siminfo(LoginInfo info) throws Exception {
		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			
			return null;
		}
		Page<CcStats> page = new Page<>(1, 1);
		page.setSearchCount(false);
		page.setDesc("stats_date");
		List<CcStats> data = iCcStatsMapper.getItemsPage(page, null, null, null, orgpos, orgpos.split(","));
		
		////获取今日停卡数量
		ExecutorService exc = Executors.newFixedThreadPool(3);
		FutureTask<CcStats> data1 = new FutureTask<>(new Callable<CcStats>() {
			@Override
			public CcStats call() throws Exception {
				
				List<CcStats> items = iCcStatsMapper.getItemsPage(page, null, null, null, orgpos, orgpos.split(","));
				if(CollectionUtils.isEmpty(items)) {
					return new CcStats();
				}else {
					return items.get(0);
				}
			}
			
		});
		exc.execute(data1);
		
		//获取机构下的充值与返利情况
		FutureTask<Integer> data2 = new FutureTask<>(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				Integer data = iCcGprsCardMapper.getUnicomTotal(orgpos, orgpos.split(","));
				return data;
			}
			
		});
		exc.execute(data2);
		
		//获取今日激活数量
		FutureTask<Integer> data3 = new FutureTask<>(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return iCcRealnameMapper.getRlnameTotalByStatus(2, orgpos, orgpos.split(","));
			}
			
		});
		exc.execute(data3);
		
		exc.shutdown();
		CcStats stats = data1.get();	
		
		stats.setUnicom_total(data2.get());
		stats.setRlname_total(data3.get());
		
		return stats;
	}
	
	@Override
	public 	Map<String, Integer> rl2pack(LoginInfo info) {
		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			
			return null;
		}
		//待实名数
		Integer rlnameTotal = iCcRealnameMapper.getRlnameTotalByStatus(0, orgpos, orgpos.split(","));
		
		//套餐数
		Integer packNum = iCcGprsPackMapper.getPackNum(orgpos, orgpos.split(","));
		
		Map<String, Integer> data = new HashMap<>();
		data.put("rlname_num", rlnameTotal);
		data.put("pack_num", packNum);
		return data;
	}
	
}
