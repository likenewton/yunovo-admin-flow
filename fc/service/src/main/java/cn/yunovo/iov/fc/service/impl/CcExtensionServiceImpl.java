package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcExtensionMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.entity.CcExtension;
import cn.yunovo.iov.fc.model.result.PayInfoBean;
import cn.yunovo.iov.fc.service.ICcExtensionService;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 延伸表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-11
 */
@Service
@ConfigurationProperties(prefix="fc.gprs")
public class CcExtensionServiceImpl extends ServiceImpl<ICcExtensionMapper, CcExtension> implements ICcExtensionService {

	@Autowired
	private ICcExtensionMapper iCcExtensionMapper;
	
	private Map<String, Map<String, String>> pays;

	@Override
	public Map<String, Map<String, String>> getPays() {
		return pays;
	}

	public void setPays(Map<String, Map<String, String>> pays) {
		this.pays = pays;
	}
	
	@Override
	public List<PayInfoBean> getPaymentItems(LoginInfo info){
		
		if(CollectionUtils.isEmpty(pays)) {
			return null;
		}
		
		//查询已安装的支付方式
		QueryWrapper<CcExtension> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("type", "payment");
		
		List<CcExtension> selectList = iCcExtensionMapper.selectList(queryWrapper);
		
		List<String> installed = new ArrayList<>();
		if(selectList == null) {
			
			
		}else {
			for (CcExtension ccExtension : selectList) {
				installed.add(ccExtension.getCode());
			}
		}
		
		Set<Entry<String, Map<String, String>>> entrySet = pays.entrySet();
		Iterator<Entry<String, Map<String, String>>> it = entrySet.iterator();
		Entry<String, Map<String, String>> temp = null;
		Map<String, String> value = null;
		List<PayInfoBean> result = new ArrayList<>();
		while(it.hasNext()) {
			temp = it.next();
			value = temp.getValue();
			result.add(new PayInfoBean(temp.getKey(),value.get("link_url"),value.get("form_url"),value.get("icon_url"),value.get("name"),installed.contains(temp.getKey())));
		}
		
		return result;
	}
	
	
	
	
}
