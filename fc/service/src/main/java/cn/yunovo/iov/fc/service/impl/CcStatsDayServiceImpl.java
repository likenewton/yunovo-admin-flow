package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.dao.ICcStatsDayMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCardLog;
import cn.yunovo.iov.fc.model.entity.CcStatsDay;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcStatsDayService;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 流量卡日流量使用情况统计表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
public class CcStatsDayServiceImpl extends ServiceImpl<ICcStatsDayMapper, CcStatsDay> implements ICcStatsDayService {

	@Autowired
	private ICcStatsDayMapper ICcStatsDayMapper;
	
	private final String CARD_DAY_USE_CACHEKEY = "CARD-DAYUSE#%s#%s#%s";
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
	@Override
	public PageData<CcStatsDay, Object> getDayUsePage(PageForm pageForm, Integer card_id, LoginInfo info) {
		
		Page<CcStatsDay> page = pageForm.build(CcStatsDay.class, null, null);
		page.setDesc("stats_date");
		String cacheKey = String.format(CARD_DAY_USE_CACHEKEY, card_id,page.getCurrent(),page.getSize());
		cacheKey = FcConstant.memResKey(cacheKey);
		PageData<CcStatsDay, Object> returnData = null;
		
		//先从缓存总获取，如果未命中缓存则溯源
		String cache = jedisPoolUtil.get(cacheKey);
		
		if(StringUtils.isEmpty(cache)) {
			
			returnData = new PageData<>();
			List<CcStatsDay> records = ICcStatsDayMapper.getDayUsePage(page, card_id);
			page.setRecords(records);
			returnData.setPage(page);
			
			if(!CollectionUtils.isEmpty(records)) {
				jedisPoolUtil.setEx(cacheKey, returnData);
			}
		}else {
			returnData = JSONObject.parseObject(cache, PageData.class);
		}
		return returnData;
	}

}
