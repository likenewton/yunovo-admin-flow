package cn.yunovo.iov.fc.service;

import java.util.HashMap;

import cn.yunovo.iov.fc.model.LoginInfo;
import cn.yunovo.iov.fc.model.entity.CcStats;

public interface  IHomeService {
	
	public HashMap<String, HashMap<String, Object>> getData(LoginInfo loginInfo) throws Exception;

	CcStats siminfo(LoginInfo info) throws Exception;
}
