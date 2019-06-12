package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.common.utils.BusinessException;
import cn.yunovo.iov.fc.dao.ICcExtensionMapper;
import cn.yunovo.iov.fc.dao.ICcSettingMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.entity.CcExtension;
import cn.yunovo.iov.fc.model.entity.CcSetting;
import cn.yunovo.iov.fc.model.form.PayForm;
import cn.yunovo.iov.fc.model.result.PayInfoBean;
import cn.yunovo.iov.fc.service.ICcExtensionService;
import cn.yunovo.iov.fc.service.ICcSettingService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
	
	@Autowired
	private ICcSettingService iCcSettingService;
	
	@Autowired
	private ICcSettingMapper iCcSettingMapper;
	
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

	@Override
	public int paymentInstall(String type) {
		
		if(type == null) {
			return 0;
		}
		
		//查询已安装的支付方式
		QueryWrapper<CcExtension> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("type", "payment");
		queryWrapper.eq("code", type);
		
		if(!pays.containsKey(type)) {
			throw new BusinessException(-1, "系统提示：无效的支付方式");
		}
		
		List<CcExtension> selectList = iCcExtensionMapper.selectList(queryWrapper);
		
		
		if(!CollectionUtils.isEmpty(selectList)) {
			throw new BusinessException(-1, "系统提示：该支付方式已安装, 请勿重复安装");
		}
		
		CcExtension entity =  new CcExtension();
		entity.setType("payment");
		entity.setCode(type);
		return iCcExtensionMapper.insert(entity);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,transactionManager="clwTransactionManager")
	public int paymentUninstall(String type) {
		
		if(type == null) {
			return 0;
		}
		
		//查询已安装的支付方式
		QueryWrapper<CcExtension> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("type", "payment");
		queryWrapper.eq("code", type);
				
		List<CcExtension> selectList = iCcExtensionMapper.selectList(queryWrapper);
		if(CollectionUtils.isEmpty(selectList)) {
			throw new BusinessException(-1, "系统提示：未找到对应的支付方式");
		}
		
		UpdateWrapper<CcExtension> wrapper = new UpdateWrapper<>();
		wrapper.eq("type", "payment");
		wrapper.eq("code", type);
		iCcExtensionMapper.delete(wrapper);
		
		UpdateWrapper<CcSetting> deleteWrapper = new UpdateWrapper<>();
		deleteWrapper.eq("group", type);
		iCcSettingMapper.deleteByGroup(type);
		
		return 1;
	}

	@Override
	public Map<String, String> paymentDetail(String type) {
		
		QueryWrapper<CcSetting> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("group", type);
		List<CcSetting> selectList = iCcSettingMapper.queryList(type, null, null);
		
		Map<String, String> setting = new HashMap<>();
		if(CollectionUtils.isEmpty(selectList)) {
			return setting;
		}
		
		for (CcSetting ccSetting : selectList) {
			setting.put(ccSetting.getKey(), ccSetting.getValue());
		}
		
		return setting;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED,transactionManager="clwTransactionManager")
	public int paymentUpdate(PayForm form, LoginInfo loginBaseInfo) {
		
		iCcSettingService.editSetting(form.getType(), form.build());
		return 1;
	}
	
	
	
	
}
