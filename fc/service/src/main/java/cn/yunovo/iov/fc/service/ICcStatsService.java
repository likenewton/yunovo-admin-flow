package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcStats;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 各项参数统计表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-03
 */
public interface ICcStatsService extends IService<CcStats> {

	public PageData<CcStats,Object> getItemsPage(PageForm pageForm, Integer org_id, String date_start, String date_end, LoginInfo info);
	
}
