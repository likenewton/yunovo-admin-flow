package cn.yunovo.iov.fc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.dao.ICcApkWhitelistUsedDayMapper;
import cn.yunovo.iov.fc.model.entity.CcApkWhitelistUsedDay;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcApkWhitelistUsedDayService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-07-25
 */
@Service
public class CcApkWhitelistUsedDayServiceImpl extends ServiceImpl<ICcApkWhitelistUsedDayMapper, CcApkWhitelistUsedDay> implements ICcApkWhitelistUsedDayService {

	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	private final String APKWHITELIST_DAY_USED_KEY = "APKWHITELIST-DAY-USED-%s-%s";
	
	@Override
	public CcApkWhitelistUsedDay getToDayApkWhitelistUsedTotal(String iccid, Integer day) {
		
		if(StringUtils.isEmpty(iccid) || day == null) {
			return null;
		}
		
		String key = FcConstant.memResKey(String.format(APKWHITELIST_DAY_USED_KEY, day, iccid));
		String cacheStr = jedisPoolUtil.get(key);
		CcApkWhitelistUsedDay data = null;
		if(StringUtils.isEmpty(cacheStr)) {
			
			QueryWrapper<CcApkWhitelistUsedDay> queryWarp = new QueryWrapper<>();
			queryWarp.eq("iccid", iccid);
			queryWarp.eq("day", day);
			data = this.getOneInfo(queryWarp);
			
			if(data != null) {
				jedisPoolUtil.setEx(key, JSONObject.toJSONString(data));
			}
			
			return data;
		}else {
			
			return JSONObject.parseObject(cacheStr, CcApkWhitelistUsedDay.class);
		}
		
	}

	private CcApkWhitelistUsedDay getOneInfo(QueryWrapper<CcApkWhitelistUsedDay> queryWarp) {
		
		if(queryWarp == null || queryWarp.isEmptyOfWhere()) {
			return null;
		}
		return this.getOne(queryWarp,false);
	}

}
