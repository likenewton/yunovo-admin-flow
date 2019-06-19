package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcGprsAllotMapper;
import cn.yunovo.iov.fc.model.entity.CcGprsAllot;
import cn.yunovo.iov.fc.service.FcConstant;
import cn.yunovo.iov.fc.service.ICcGprsAllotService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 流量分配表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
@Service
public class CcGprsAllotServiceImpl extends ServiceImpl<ICcGprsAllotMapper, CcGprsAllot> implements ICcGprsAllotService {

	@Autowired
	private ICcGprsAllotMapper iCcGprsAllotMapper;
	
	
	public boolean gprsAllot(Integer card_id) {
		
		String month_end_type_cacheKey = FcConstant.memResKey(FcConstant.MONTH_END_TYPE_CACHEKEY);
		//获取未过期的流量套餐分配情况列表，根据此列表分配当月可使用的流量套餐
		List<CcGprsAllot>  unAllotGprsPack = iCcGprsAllotMapper.getUnAllotGprsPack(card_id, NumberUtils.createInteger(month_end_type_cacheKey));
		if(CollectionUtils.isEmpty(unAllotGprsPack)) {
			
			return false; //没有流量套餐可分配
		}
		
		/**
		 * 判断是否为月结日，并且卡是昨天（在非车机上）激活，那么流量套餐需要从上个月开始分配
		 */
		if(unAllotGprsPack.get(0).getCard_type() == 1) { //吉林联通
			
		}else {//JASPER物联卡
			
		}
		
		return true;
		
	}
	
}
