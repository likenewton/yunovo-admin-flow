package cn.yunovo.iov.fc.dao;

import cn.yunovo.iov.fc.model.entity.CcRealname;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 流量卡实名制表 Mapper 接口
 * </p>
 *
 * @author bill
 * @since 2019-06-14
 */
public interface ICcRealnameMapper extends BaseMapper<CcRealname> {

	List<CcRealname> getItemsPage(IPage<CcRealname> page, @Param("org_id")Integer org_id, @Param("card_iccid")String card_iccid, @Param("date_start")String date_start, @Param("date_end")String date_end, @Param("status")Integer status, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public Integer getRlnameTotalByStatus(@Param("cdi_status")Integer cdi_status, @Param("orgpos")String orgpos, @Param("orgs")String[] orgs);
	
	public CcRealname getByCardId(@Param("card_id")Integer card_id);
}
