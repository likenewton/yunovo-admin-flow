package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcGprsValueMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsValue;
import cn.yunovo.iov.fc.model.entity.CcOrg;
import cn.yunovo.iov.fc.model.entity.CcResetLog;
import cn.yunovo.iov.fc.model.result.GprsAllotResultBean;
import cn.yunovo.iov.fc.service.ICcGprsValueService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 流量值表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
public class CcGprsValueServiceImpl extends ServiceImpl<ICcGprsValueMapper, CcGprsValue> implements ICcGprsValueService {

	@Autowired
	private ICcGprsValueMapper iCcGprsValueMapper;
	
	@Override
	public PageData<GprsAllotResultBean, Object> getAllotPage(PageForm pageForm, Integer card_id, LoginInfo info) {

		Page<GprsAllotResultBean> page = pageForm.build(GprsAllotResultBean.class, null, "V.time_added");
		PageData<GprsAllotResultBean, Object> returnData = new PageData<>();
		List<GprsAllotResultBean> records = iCcGprsValueMapper.getAllotPage(page, card_id);
		page.setRecords(records);
		returnData.setPage(page);
		return returnData;
	}
	
	@Override
	public Map<Integer, CcGprsValue> allotValueMapForHowmonth(Integer card_id, Integer allot_id, Integer[] how_month){
		
		List<CcGprsValue>  data = iCcGprsValueMapper.getByCardidAndAllotidAndHowmonth(card_id, allot_id, how_month);
		if(CollectionUtils.isEmpty(data)) {
			return Collections.emptyMap();
		}
		
		Map<Integer, CcGprsValue> map = data.stream().collect(Collectors.toMap(CcGprsValue::getHow_month, Function.identity()));
		
		return map;
	}

}
