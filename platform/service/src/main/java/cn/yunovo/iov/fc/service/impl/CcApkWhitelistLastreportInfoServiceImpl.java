package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.DateUtil;
import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.dao.ICcApkWhitelistLastreportInfoMapper;
import cn.yunovo.iov.fc.model.entity.CcApkWhitelistLastreportInfo;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcApkWhitelistLastreportInfoService;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流量卡最后一次应用白名单消耗上报信息 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-07-29
 */
@Slf4j
@Service
public class CcApkWhitelistLastreportInfoServiceImpl extends ServiceImpl<ICcApkWhitelistLastreportInfoMapper, CcApkWhitelistLastreportInfo> implements ICcApkWhitelistLastreportInfoService {

	private final String LAST_REPORT_INFO_KEY = "APKWHITE-LAST-REPORT-%s";
	
	@Autowired
	private ICcApkWhitelistLastreportInfoMapper iCcApkWhitelistLastreportInfoMapper;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	@Override
	public CcApkWhitelistLastreportInfo getLastReportInfoInCache(String iccid) {
		
		String cacheKey = FcConstant.memResKey(String.format(LAST_REPORT_INFO_KEY, iccid));
		String data = jedisPoolUtil.get(cacheKey);
		CcApkWhitelistLastreportInfo info = null;
		if(StringUtils.isEmpty(data)) {
			info = this.getLastReportInfoInDB(iccid);
		}else {
			info = JSONObject.parseObject(data, CcApkWhitelistLastreportInfo.class);
		}
		
		return info;
		
	}
	
	@Override
	public CcApkWhitelistLastreportInfo getLastReportInfoInDB(String iccid) {
		
		QueryWrapper<CcApkWhitelistLastreportInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("iccid", iccid);
		
		return this.getOne(queryWrapper, false);
		
	}

	@Override
	public boolean saveInfo(Integer card_id, String iccid, Long nonce, Long org_gprs_month, Long yunovo_gprs_month, String sn, Double used_total) {

		CcApkWhitelistLastreportInfo info = new CcApkWhitelistLastreportInfo();
		info.setIccid(iccid);
		info.setCard_id(card_id);
		info.setCreate_datetime(DateUtil.nowStr());
		info.setNonce(nonce);
		info.setSn(sn);
		info.setCount(1L);
		info.setOrg_gprs_month(org_gprs_month);
		info.setYunovo_gprs_month(yunovo_gprs_month);
		info.setUsed_total(used_total == null ? 0 : used_total);
		boolean isOk = this.save(info);
		if(isOk) {
			return this.cacheInof(info);
		}
		return isOk;
	}
	
	

	private boolean cacheInof(CcApkWhitelistLastreportInfo info) {
		
		if(info == null) {
			return false;
		}
		String cacheKey = FcConstant.memResKey(String.format(LAST_REPORT_INFO_KEY, info.getIccid()));
		try {
			return retBool(jedisPoolUtil.setEx(cacheKey, JSONObject.toJSONString(info)));
		} catch (Exception e) {
			log.error("[cacheInof][exception]params={},exception={}", JSONObject.toJSONString(info), ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	@Override
	public boolean updateInfo(CcApkWhitelistLastreportInfo info) {

		if(info == null || info.getId() == null) {
			log.warn("[updateInfo][data is null]params={}", JSONObject.toJSONString(info));
			return false;
		}
		
		iCcApkWhitelistLastreportInfoMapper.updateInfoById(info);
		return this.cacheInof(info);
		
	}
}
