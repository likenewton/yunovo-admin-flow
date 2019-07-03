package cn.yunovo.iov.fc.web.controller.gprs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.yunovo.iov.fc.common.utils.Result;
import cn.yunovo.iov.fc.common.utils.ResultUtil;
import cn.yunovo.iov.fc.model.PageData;
import cn.yunovo.iov.fc.model.PageForm;
import cn.yunovo.iov.fc.model.entity.CcGprsBatch;
import cn.yunovo.iov.fc.model.entity.CcResetLog;
import cn.yunovo.iov.fc.model.form.CcGprsBatchForm;
import cn.yunovo.iov.fc.model.result.BatchSaveResultBean;
import cn.yunovo.iov.fc.service.ICcGprsBatchService;
import cn.yunovo.iov.fc.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags="业务管理-批次管理接口列表")
@RequestMapping(path="/api/gprs/batch", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class BatchController extends BaseController{

	@Autowired
	private ICcGprsBatchService iCcGprsBatchService;
	
	@ApiOperation(value="业务管理-流量卡重置历史查询接口")
	@ApiImplicitParams(value = { 
			@ApiImplicitParam(name = "org_id", value = "机构id", required = false, dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "batch_sn", value = "批次号", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_start", value = "重置时间-开始日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query"),
			@ApiImplicitParam(name = "date_end", value = "重置时间-结束日期（YYYY-MM-DD）", required = false, dataType = "String",paramType = "query")
			})
	@RequestMapping(path="/history",method= {RequestMethod.GET, RequestMethod.POST})
	public Result<PageData<CcGprsBatch, Object>> list(PageForm form, String batch_sn, Integer org_id, String date_start,
			String date_end) {
		
		PageData<CcGprsBatch, Object>  data = iCcGprsBatchService.getItemsPage(form, org_id, batch_sn, date_start, date_end, this.getLoginBaseInfo());
		return ResultUtil.success(data);
	}
	
	@ApiOperation(value="业务管理-流量卡批次新增接口")
	@RequestMapping(path="/insert",method= {RequestMethod.POST})
	public Result<BatchSaveResultBean> insert(@ModelAttribute CcGprsBatchForm form) {
		
		BatchSaveResultBean result = iCcGprsBatchService.saveBatch(form, this.getLoginBaseInfo());
		return ResultUtil.successCN(result);
	}
}
