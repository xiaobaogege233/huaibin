package com.dml.users;

/**
 * 用户和系统的一次会话
 * 
 * @author neo
 *
 */
public class UserSession {

	/**
	 * session id 通常就是客户端程序理解的 token
	 */
	private String id;

	/**
	 * 登录以后才会有值。游客状态userId是null
	 */
	private String userId;

	private long createTime;

	private long timeoutTime;

	/**
	 * 检测session过期
	 * 
	 * @param currentTime
	 * @return
	 */
	public boolean isTimeout(long currentTime) {
		return currentTime > timeoutTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getTimeoutTime() {
		return timeoutTime;
	}

	public void setTimeoutTime(long timeoutTime) {
		this.timeoutTime = timeoutTime;
	}

	public boolean equals(Object o) {
		if (o instanceof UserSession) {
			return ((UserSession) o).id.equals(id);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return id.hashCode();
	}

}
