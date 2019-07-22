package cn.yunovo.iov.fc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.yunovo.iov.fc.model.entity.CcGprsSnap;
import cn.yunovo.iov.fc.model.export.CcGprsSnapExportBean;

/**
 * <p>
 * GPRS流量快照表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-07-07
 */
public interface ICcGprsSnapMapper extends BaseMapper<CcGprsSnap> {

	public List<CcGprsSnapExportBean> getPaySnapExport(@Param("org_id")Integer org_id,@Param("card_iccid")String card_iccid, @Param("card_type")Integer card_type, @Param("pay_from")String pay_from, @Param("pay_method")Short pay_method, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("paid_start")String  paid_start, @Param("paid_end")String paid_end, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
}
