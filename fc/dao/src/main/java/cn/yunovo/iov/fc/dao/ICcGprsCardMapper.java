package cn.yunovo.iov.fc.dao;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.SellPayResultBean;
import cn.yunovo.iov.fc.model.result.CardUsedResultBean;
import cn.yunovo.iov.fc.model.result.UnicomStatResultBean;

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
	
	public HashMap<String, Double>  getHaltTotal(@Param("card_iccid")String card_iccid, @Param("card_type")Integer card_type, @Param("org_id")Integer org_id, @Param("time_expire")Integer time_expire,@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public List<CcGprsCard> getItemsPage(IPage<CcGprsCard> page, @Param("card_iccid")String card_iccid, @Param("card_type")Integer card_type, @Param("org_id")Integer org_id, @Param("max_unused")Integer max_unused, @Param("unicom_diff")Integer unicom_diff, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public Long getItemsPageCount(@Param("card_iccid")String card_iccid, @Param("card_type")Integer card_type, @Param("org_id")Integer org_id, @Param("max_unused")Integer max_unused, @Param("unicom_diff")Integer unicom_diff, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 获取机构售卡数量与支付情况
	 * @param org_id
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<SellPayResultBean> getSellPayPage(IPage<SellPayResultBean> page, @Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 统计分析-流量卡使用统计
	 * @param page
	 * @param org_id
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<CardUsedResultBean> getCardUsedPage(IPage<CardUsedResultBean> page, @Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);

	/**
	 * 统计分析-流量卡使用统计（总计）
	 * @return
	 */
	public CardUsedResultBean getCardUsedTotal(@Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 统计分析-联通情况统计
	 * @return
	 */
	public List<UnicomStatResultBean> getUnicomStatPage(IPage<UnicomStatResultBean> page, @Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("jstart")String jstart, @Param("jend")String jend,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 统计分析-联通情况统计（总计）
	 * @return
	 */
	public UnicomStatResultBean getUnicomStatTotal(@Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("jstart")String jstart, @Param("jend")String jend,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
}
