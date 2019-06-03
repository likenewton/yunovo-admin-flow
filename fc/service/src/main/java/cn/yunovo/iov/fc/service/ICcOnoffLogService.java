package cn.yunovo.iov.fc.service;


import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcOnoffLog;

/**
 * <p>
 * 停卡日志 服务类
 * </p>
 *
 * @author bill
 * @since 2019-06-01
 */
public interface ICcOnoffLogService extends IService<CcOnoffLog> {

	
	public PageData<CcOnoffLog, Object> getItems(PageForm pageForm, String card_iccid,
			Integer card_type, Integer org_id, LoginInfo info);
	
	public List<CcOnoffLog> stopDetail(Integer card_id, String card_iccid, LoginInfo info);
}
