package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcStatsDay;
import cn.yunovo.iov.fc.model.entity.CcStatsMonth;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 流量卡月流量使用情况统计表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
public interface ICcStatsMonthService extends IService<CcStatsMonth> {

	public List<String> getMonths(LoginInfo info);
	
	public List<SelectBean> monthSelect(LoginInfo info);
	
	public PageData<CcStatsMonth, Map<String,Object>> getItemsPage(Integer org_id, Integer card_type, String card_iccid, String mdate,PageForm page, LoginInfo info);
	
	public void export(Integer org_id, Integer card_type, String card_iccid, String mdate, LoginInfo info) throws IOException;
	
	public PageData<CcStatsMonth, Object> getMonthUsePage(PageForm pageForm, Integer card_id, LoginInfo info);
}
