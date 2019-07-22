package cn.yunovo.iov.fc.dao;

import cn.yunovo.iov.fc.model.entity.CcGprsValue;
import cn.yunovo.iov.fc.model.result.GprsAllotResultBean;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 流量值表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcGprsValueMapper extends BaseMapper<CcGprsValue> {

	public List<GprsAllotResultBean> getAllotPage(IPage<GprsAllotResultBean> page, @Param("card_id")Integer card_id);
	
	public List<CcGprsValue> getByCardidAndAllotidAndHowmonth(@Param("card_id")Integer card_id, @Param("allot_id")Integer allot_id, @Param("how_month")Integer[] how_month);
	
	public List<String> getIccidByCardtypeAndHowmonth(@Param("card_type")Integer card_type, @Param("how_month")Integer how_month);
	
	public List<CcGprsValue> getGprsInfoByCardidAndHowmonth(@Param("card_id")Integer card_id,@Param("card_type")Integer card_type, @Param("how_month")Integer how_month);
	
	/**
	 * 获取对应card_id 所有流量的最后失效日期
	 * @param card_id 流量卡id
	 * @param how_month 月份yyyyMM
	 * @return
	 */
	public String getMaxTimeExpireByCardidAndHowMonth(@Param("card_id")Integer card_id, @Param("how_month")Integer how_month);
	
	/**
	 * 计算该卡的总剩余流量与过期时间
	 * @param card_id
	 * @param how_month
	 * @return
	 */
	public CcGprsValue sumBalanceDvalByCardidAndHowMonth(@Param("card_id")Integer card_id, @Param("how_month")Integer how_month);
	
	public int updateHowmonthByCardidAndHowMonth(@Param("card_id")Integer card_id, @Param("month")Integer month);
}
