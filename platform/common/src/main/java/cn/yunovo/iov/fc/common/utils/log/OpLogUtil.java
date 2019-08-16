package cn.yunovo.iov.fc.common.utils.log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpLogUtil {

	public static void info(LogBean logBean) {
		
		if(logBean == null) {
			
			log.warn("[oplog]log 数据为空");
			return ;
		}
		
		log.info("[oplog]{}", JSONObject.toJSONString(logBean, SerializerFeature.WriteMapNullValue));
		
	}
	
}
