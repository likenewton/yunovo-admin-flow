package cn.yunovo.iov.fc.dao;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcGprsCard;

/**
 * <p>
 * GPRS流量卡信息表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
public interface ICcGprsCardMapper extends BaseMapper<CcGprsCard> {

	public List<CcGprsCard> getHaltPage(IPage<CcGprsCard> page, @Param("card_iccid")String card_iccid, @Param("card_type")Integer card_type, @Param("org_id")Integer org_id, @Param("time_expire")Integer time_expire,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public Long getHaltPageCount(@Param("card_iccid")String card_iccid, @Param("card_type")Integer card_type, @Param("org_id")Integer org_id, @Param("time_expire")Integer time_expire,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);

	public HashMap<String, Double>  getHaltTotal(@Param("card_iccid")String card_iccid, @Param("card_type")Integer card_type, @Param("org_id")Integer org_id, @Param("time_expire")Integer time_expire,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
}
