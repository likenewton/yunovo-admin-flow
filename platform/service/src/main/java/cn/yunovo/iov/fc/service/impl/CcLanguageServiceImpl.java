package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.JedisPoolUtil;
import cn.yunovo.iov.fc.dao.ICcLanguageMapper;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcLanguage;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcLanguageService;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 语言表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-13
 */
@Service
public class CcLanguageServiceImpl extends ServiceImpl<ICcLanguageMapper, CcLanguage> implements ICcLanguageService {

	@Autowired
	private ICcLanguageMapper iCcLanguageMapper;
	
	@Autowired
	private JedisPoolUtil jedisPoolUtil;
	
    public List<CcLanguage> getCurrencies(){
		
		return iCcLanguageMapper.getItemsPage(null);
	}
	
	public JSONObject languageMap(Boolean noCache) {
		
		noCache = noCache == null ? false : noCache;
		List<CcLanguage> temp1= null;
		
		String cache = null, cacheKey = FcConstant.memResKey("language");
		JSONObject result = null;
		if(!noCache) {
			cache = jedisPoolUtil.get(cacheKey);
		}
		if(StringUtils.isEmpty(cache)) {
			result = new JSONObject();
			temp1 = this.getCurrencies();
			if(CollectionUtils.isEmpty(temp1)) {
				return result;
			}
			
			for (CcLanguage ccLanguage : temp1) {
				
				result.put(ccLanguage.getCode(), ccLanguage);
			}
			
			jedisPoolUtil.setEx(cacheKey, JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue));
		}else {
			result = JSONObject.parseObject(cache);
		}
		
		return result;
	}
	
	@Override
	public List<SelectBean> select(){
		
		JSONObject data = this.languageMap(null);
		if(CollectionUtils.isEmpty(data)) {
			return null;
		}
		
		Set<Entry<String, Object>> entrySet = data.entrySet();
		Iterator<Entry<String, Object>> it = entrySet.iterator();
		Entry<String, Object> temp = null;
		List<SelectBean> select = new ArrayList<>();
		while(it.hasNext()) {
			temp = it.next();
			select.add(new SelectBean(data.getJSONObject(temp.getKey()).getString("name"), temp.getKey()));
		}
		return select;
	}
}
