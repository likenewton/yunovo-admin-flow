package cn.yunovo.iov.fc.dao;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.SellPayResultBean;
import cn.yunovo.iov.fc.model.result.CardTotalByOrgidInfoBean;
import cn.yunovo.iov.fc.model.result.CardUsedResultBean;
import cn.yunovo.iov.fc.model.result.PayDetailResultBean;
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
	
	//public Long getItemsPageCount(@Param("card_iccid")String card_iccid, @Param("card_type")Integer card_type, @Param("org_id")Integer org_id, @Param("max_unused")Integer max_unused, @Param("unicom_diff")Integer unicom_diff, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
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
	
	/**
	 * 机构充值
	 * @param page
	 * @param org_id
	 * @param date_start
	 * @param date_end
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<PayDetailResultBean> getPayDetailPage(IPage<PayDetailResultBean> page, @Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end,  @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 流量卡列表查询
	 * @param page
	 * @param card_iccid 
	 * @param card_type
	 * @param org_id
	 * @param date_start
	 * @param date_end
	 * @param time_expire
	 * @param unicom_stop
	 * @param status
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<CcGprsCard> queryCardListPage(IPage<CcGprsCard> page, @Param("card_iccid")String card_iccid, @Param("card_type")Integer card_type, @Param("org_id")Integer org_id, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("time_expire")Integer time_expire, @Param("unicom_stop")Integer unicom_stop, @Param("status")Integer status, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	
	/**
	 * 机构流量卡数据统计
	 * @param page
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public List<CardTotalByOrgidInfoBean> cardTotalByOrgidGroup(IPage<CardTotalByOrgidInfoBean> page, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 首页-
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public HashMap<String, Object> getCardCase(@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * 获取非云智设备激活设备数
	 * @param orgpos
	 * @param orgs
	 * @return
	 */
	public Integer getUnicomTotal(@Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	/**
	 * SELECT * FROM cc_gprs_card WHERE card_iccid = #{card_iccid}
	 * 通过iccid 获取流量卡信息
	 * @param card_iccid
	 * @return
	 */
	public CcGprsCard getByIccid(@Param("card_iccid")String card_iccid);
	
	/**
	 * 兼容19位ICCID
	 * SELECT * FROM cc_gprs_card WHERE card_iccid like "#{card_iccid}%"
	 * @param card_iccid
	 * @return
	 */
	public CcGprsCard getByLikeIccid(@Param("card_iccid")String card_iccid);
	
	public CcGprsCard getByCardSn(@Param("card_sn")String card_sn);
	
	public CcGprsCard getGprsCardByIccid(@Param("card_iccid")String card_iccid);
}
