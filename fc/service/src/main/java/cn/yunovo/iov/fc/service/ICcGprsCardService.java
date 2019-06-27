package cn.yunovo.iov.fc.service;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsCard;
import cn.yunovo.iov.fc.model.entity.SellPayResultBean;
import cn.yunovo.iov.fc.model.form.CardOnoffForm;
import cn.yunovo.iov.fc.model.result.CardDetailInfoBean;
import cn.yunovo.iov.fc.model.result.CardTotalByOrgidInfoBean;
import cn.yunovo.iov.fc.model.result.CardUsedResultBean;
import cn.yunovo.iov.fc.model.result.PayDetailResultBean;
import cn.yunovo.iov.fc.model.result.UnicomDataBean;
import cn.yunovo.iov.fc.model.result.UnicomStatResultBean;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * GPRS流量卡信息表 服务类
 * </p>
 *
 * @author bill
 * @since 2019-05-30
 */
public interface ICcGprsCardService extends IService<CcGprsCard> {

	
	public Map<String, String> getCard_type();
	
	public PageData<CcGprsCard, HashMap<String, Double>> getHaltPage(PageForm pageForm, String card_iccid,
			Integer card_type, Integer org_id, Integer time_expire, LoginInfo info);
	
	/**
	 * 用量异常查询接口
	 * @param pageForm
	 * @param card_iccid
	 * @param card_type
	 * @param org_id
	 * @param max_unused
	 * @param unicom_diff
	 * @param info
	 * @return
	 */
	public PageData<CcGprsCard, Object>  getItemsPage(PageForm pageForm, String card_iccid,
			Integer card_type, Integer org_id, Integer max_unused, Integer unicom_diff, LoginInfo info);
	
	/**
	 * 统计分析-续费数据
	 * @param pageForm
	 * @param org_id 机构id
	 * @param date_start
	 * @param date_end
	 * @param info
	 * @return
	 */
	public PageData<SellPayResultBean, Object> getSellPayPage(PageForm pageForm, Integer org_id, String date_start, String date_end, LoginInfo info);
	
	/**
	 * 统计分析-流量卡使用情况查询
	 * @param pageForm
	 * @param org_id
	 * @param date_start
	 * @param date_end
	 * @param info
	 * @return
	 */
	public PageData<CardUsedResultBean, CardUsedResultBean> getCardUsedPage(PageForm pageForm, Integer org_id, String date_start, String date_end, LoginInfo info);
	
	
	/**
	 * 统计分析-联通情况
	 * @param pageForm
	 * @param org_id
	 * @param date_start
	 * @param date_end
	 * @param jstart
	 * @param jend
	 * @param info
	 * @return
	 */
	public PageData<UnicomStatResultBean, UnicomStatResultBean> getUnicomStatPage(PageForm pageForm, Integer org_id, String date_start, String date_end, String jstart, String jend,LoginInfo info);
	
	/**
	 * 统计分析-联通情况
	 * @param pageForm
	 * @param org_id
	 * @param date_start
	 * @param date_end
	 * @param jstart
	 * @param jend
	 * @param info
	 * @return
	 */
	public PageData<PayDetailResultBean, PayDetailResultBean> getPayDetailPage(PageForm pageForm, Integer org_id, String date_start, String date_end, LoginInfo info);

	/**
	 * 业务管理-流量卡列表接口
	 * @param form
	 * @param card_iccid
	 * @param org_id
	 * @param date_start
	 * @param date_end
	 * @param time_expire
	 * @param unicom_stop
	 * @param status
	 * @param info
	 * @param card_type
	 * @return
	 */
	PageData<CcGprsCard, Object> queryCardListPage(PageForm form, String card_iccid, Integer org_id, String date_start,
			String date_end, Integer time_expire, Integer unicom_stop, Integer status, LoginInfo info,
			Integer card_type);

	
	/**
	 * 流量卡基本信息
	 * @param card_id
	 * @return
	 */
	CardDetailInfoBean detailByCardId(Integer card_id);
	
	/**
	 * 统计机构流量卡数
	 * @param form
	 * @param info
	 * @return
	 */
	PageData<CardTotalByOrgidInfoBean, Object> cardTotalByOrgidGroupPage(PageForm form, LoginInfo info);

	/**
	 * 通过ICCID获取流量卡数据, 如果缓存里有流量卡数据，则优先使用缓存
	 * @param iccid  流量卡iccid
	 * @return CcGprsCard对象
	 */
	CcGprsCard getByIccid(String iccid);

	/**
	 * 更新流量卡信息
	 * @param card 需更新的流量卡数据
	 * @return bool 成功true失败false
	 */
	public boolean updateCard(CcGprsCard card);

	String gprsFormat(Double gprs);

	/**
	 * 同步联通数据
	 * @param card_sn gprs.cc_gprs_card.card_sn
	 */
	public UnicomDataBean syncUnicomData(String card_sn, LoginInfo loginBaseInfo);

	public boolean onoff(CardOnoffForm form, LoginInfo loginBaseInfo);
	
	
	
}
