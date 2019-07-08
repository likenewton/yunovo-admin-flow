package cn.yunovo.iov.fc.service;

import java.io.IOException;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.entity.CcGprsSnap;

/**
 * <p>
 * GPRS流量快照表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-07-07
 */
public interface ICcGprsSnapService extends IService<CcGprsSnap> {

	/**
	 * 快照导出
	 */
	void export(Integer org_id, String card_iccid, Integer card_type, String pay_from, Short pay_method,
			String date_start, String date_end, String paid_start, String paid_end, LoginInfo info) throws IOException;

}
