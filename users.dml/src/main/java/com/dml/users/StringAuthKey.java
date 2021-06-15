package com.dml.users;

/**
 * 一个字符串表达的授权key。比如账号密码的授权，用账号来表授权的key。
 * 
 * @author neo
 *
 */
public class StringAuthKey implements AuthKey {

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean equals(Object o) {
		if (o instanceof StringAuthKey) {
			StringAuthKey sak = (StringAuthKey) o;
			return sak.key.equals(key);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return key.hashCode();
	}

}
