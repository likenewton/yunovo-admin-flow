package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcGprsAllotMapper;
import cn.yunovo.iov.fc.model.entity.CcGprsAllot;
import cn.yunovo.iov.fc.service.ICcGprsAllotService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

	
	public void gprsAllot(Integer card_id) {
		
		
		//获取未过期的流量套餐分配情况列表，根据此列表分配当月可使用的流量套餐
		
		
		
		
	}
	
}
