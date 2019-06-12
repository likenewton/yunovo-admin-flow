package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcCurrencyMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcCurrency;
import cn.yunovo.iov.fc.service.ICcCurrencyService;
import cn.yunovo.iov.fc.service.ICcSettingService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 货币表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-12
 */
@Service
public class CcCurrencyServiceImpl extends ServiceImpl<ICcCurrencyMapper, CcCurrency> implements ICcCurrencyService {

	@Autowired
	private ICcCurrencyMapper iCcCurrencyMapper;
	
	@Autowired
	public ICcSettingService iCcSettingService;
	
	@Override
	public PageData<CcCurrency, Object> getItemsPage(PageForm form, LoginInfo info) {
		
		Page<CcCurrency> page = form.build(CcCurrency.class, null, null);
		
		List<CcCurrency> records = iCcCurrencyMapper.getItemsPage(page);
		if(!CollectionUtils.isEmpty(records)) {
			Map<String, String> systemConfigMap = iCcSettingService.systemConfigMap();
			
			for (CcCurrency ccCurrency : records) {
				
				ccCurrency.setIs_default(StringUtils.equals(ccCurrency.getCode(), systemConfigMap.get("config_currency")));
			}
		}
		page.setRecords(records);
		PageData<CcCurrency, Object> data = new PageData<>();
		data.setPage(page);
		
		return data;
	}

}
