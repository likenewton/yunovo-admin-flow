package cn.yunovo.iov.fc.api.model;


import java.util.Date;

public class AppKey implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// 主键ID
	private String id;

	// appKey
	private String appKey;

	// appSecret
	private String appSecret;
	private String generativeAlgorithm;

	// 状态：-1=删除，0=停用，1=启用
	private Integer status;

	// 说明
	private String remark;

	// 创建人
	private String createPerson;

	// 创建日期
	private Date createTime;

	// 最后修改人
	private String updatePerson;

	// 最后修改日期
	private Date updateTime;
	
	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdatePerson() {
		return updatePerson;
	}

	public void setUpdatePerson(String updatePerson) {
		this.updatePerson = updatePerson;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getGenerativeAlgorithm() {
		return generativeAlgorithm;
	}

	public void setGenerativeAlgorithm(String generativeAlgorithm) {
		this.generativeAlgorithm = generativeAlgorithm;
	}

	@Override
	public String toString() {
		return "AppKey [" + "id=" + id + ",appKey=" + appKey + ",appSecret=" + appSecret + ",status=" + status
				+ ",remark=" + remark + ",createPerson=" + createPerson + ",createTime=" + createTime + ",updatePerson="
				+ updatePerson + ",updateTime=" + updateTime + "]";
	}
}