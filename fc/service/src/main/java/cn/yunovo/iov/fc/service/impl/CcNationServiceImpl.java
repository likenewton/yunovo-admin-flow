package cn.yunovo.iov.fc.service.impl;

import cn.yunovo.iov.fc.dao.ICcNationMapper;
import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcNation;
import cn.yunovo.iov.fc.service.ICcNationService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 国家区域表 服务实现类
 * </p>
 *
 * @author bill
 * @since 2019-06-11
 */
@Service
public class CcNationServiceImpl extends ServiceImpl<ICcNationMapper, CcNation> implements ICcNationService {

	@Override
	public PageData<CcNation, List<CcNation>> getNationsPage(PageForm pageForm, Integer ntid, LoginInfo loginInfo) {
		
		PageData<CcNation, List<CcNation>> pageData = new PageData<>(); 
		
		Page<CcNation> page = new Page<>();
		
		if(ArrayUtils.isEmpty(pageForm.getAscs()) && ArrayUtils.isEmpty(pageForm.getDescs())) {
			page.setAsc("ntname");
		}else {
			page.setAsc(pageForm.getAscs());
			page.setDesc(pageForm.getDescs());
		}
		
		QueryWrapper<CcNation> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent", ntid == null ? 0 : ntid);
		
		IPage<CcNation> records =  this.baseMapper.selectPage(page, queryWrapper);
		
		pageData.setPage((Page<CcNation>) records);
		pageData.setOther(this.getNation(ntid));
		
		return pageData;
	}
	
	@Override
	public CcNation getById(Integer ntid) {
		
		if(ntid == null) {
			return null;
		}

		return this.getBaseMapper().selectById(ntid);
		
	}

	@Override
	public List<CcNation> getNation(Integer ntid) {
		
		if(ntid == null) {
			return null;
		}
		
		List<CcNation> result = new ArrayList<>();
		
		this._getNation(ntid, result);
		return result;
	}

	private void _getNation(Integer ntid, List<CcNation> result) {
		
		CcNation temp = this.getById(ntid);
		
		if(temp != null) {
			result.add(temp);
			this._getNation(temp.getParent(), result);
		}
		
	}

}
