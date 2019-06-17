package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcStatsDay;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 流量卡日流量使用情况统计表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcStatsDayService extends IService<CcStatsDay> {

	public PageData<CcStatsDay, Object> getDayUsePage(PageForm pageForm, Integer card_id, LoginInfo info);
}