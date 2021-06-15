package com.dml.users;

/**
 * 三方授权，总是成功的授权。对于三方登录，其实授权由三方服务器完成，这个类是个用于三方授权的永远成功的代理
 * 
 * @author neo
 *
 */
public class ThirdAuthorization implements Authorization {

	private String publisher;

	private String uuid;

	@Override
	public void auth(String... parameters) throws AuthException {
	}

	@Override
	public AuthKey createAuthKey() {
		PublisherUniqueStringAuthKey key = new PublisherUniqueStringAuthKey();
		key.setPublisher(publisher);
		key.setUuid(uuid);
		return key;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
