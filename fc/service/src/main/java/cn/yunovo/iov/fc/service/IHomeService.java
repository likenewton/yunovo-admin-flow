package cn.yunovo.iov.fc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.entity.CcStats;
import cn.yunovo.iov.fc.model.result.HomeChartDataBean;
import cn.yunovo.iov.fc.model.result.HomeSimTrendInfoBean;

public interface  IHomeService {
	
	public HashMap<String, HashMap<String, Object>> getData(LoginInfo loginInfo) throws Exception;

	CcStats siminfo(LoginInfo info) throws Exception;

	Map<String, Integer> rl2pack(LoginInfo info);

	List<HomeSimTrendInfoBean> simTrend(Integer stype, String date_start, String date_end, LoginInfo info);

	Map<String, List<HomeChartDataBean>> topupTrend(Integer stype, String date_start, String date_end, LoginInfo info);

	Map<String, List<HomeChartDataBean>> orderTrend(Integer stype, String date_start, String date_end, LoginInfo info);

	Map<String, List<HomeChartDataBean>> gprsTrend(Integer stype, String date_start, String date_end, LoginInfo info);

	Map<String, List<HomeChartDataBean>> priceTrend(Integer stype, String date_start, String date_end, LoginInfo info);
}
