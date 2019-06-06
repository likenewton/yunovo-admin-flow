package cn.yunovo.iov.fc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.yunovo.iov.fc.dao.ICcGprsPackMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.SelectBean;
import cn.yunovo.iov.fc.model.entity.CcGprsPack;
import cn.yunovo.iov.fc.service.ICcGprsPackService;
import cn.yunovo.iov.fc.service.ICcUserService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 流量充值套餐表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-05
 */
@Service
public class CcGprsPackServiceImpl extends ServiceImpl<ICcGprsPackMapper, CcGprsPack> implements ICcGprsPackService {

	@Autowired
	private ICcGprsPackMapper iCcGprsPackMapper;
	
	@Autowired
	private ICcUserService iCcUserService;
	
	@Override
	public List<SelectBean> select(LoginInfo info) {
		
		String orgpos = iCcUserService.getOrgpos(info.getLoginName());
		if (StringUtils.isEmpty(orgpos)) {
			return null;
		}
		
		List<CcGprsPack> packs = iCcGprsPackMapper.getPack(orgpos, orgpos.split(","));
		
		if(CollectionUtils.isEmpty(packs)) {
			return null;
		}
		
		List<SelectBean> result = new ArrayList<>();
		for (CcGprsPack ccGprsPack : packs) {
			result.add(new SelectBean(ccGprsPack.getGprs_amount(),ccGprsPack.getGprs_amount()));
		}
		
		return result;
	}

}
