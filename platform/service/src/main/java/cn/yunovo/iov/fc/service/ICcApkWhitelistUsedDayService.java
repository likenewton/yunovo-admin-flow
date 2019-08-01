package cn.yunovo.iov.fc.service;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.entity.CcApkWhitelistUsedDay;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bill
 * @since 2019-07-25
 */
public interface ICcApkWhitelistUsedDayService extends IService<CcApkWhitelistUsedDay> {

	
	CcApkWhitelistUsedDay getToDayApkWhitelistUsedTotal(String iccid, Integer day);

}
