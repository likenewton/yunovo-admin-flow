package cn.yunovo.iov.fc.service;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.yunovo.iov.fc.model.entity.CcApkWhitelistLastreportInfo;

/**
 * <p>
 * 流量卡最后一次应用白名单消耗上报信息 服务类
 * </p>
 *
 * @author bill
 * @since 2019-07-29
 */
public interface ICcApkWhitelistLastreportInfoService extends IService<CcApkWhitelistLastreportInfo> {

	
	
	/**
	 * 
	 * @param iccid 流量卡iccid
	 * @return 
	 */
	CcApkWhitelistLastreportInfo getLastReportInfoInCache(String iccid);

	/**
	 * 从缓存中获取对应流量卡最后一次白名单流量消耗上报信息
	 * @param iccid
	 * @return
	 */
	CcApkWhitelistLastreportInfo getLastReportInfoInDB(String iccid);

	boolean saveInfo(Integer card_id, String iccid, Long nonce, Long org_gprs_month, Long yunovo_gprs_month, String sn, Double used_total);

	boolean updateInfo(CcApkWhitelistLastreportInfo info);

}
