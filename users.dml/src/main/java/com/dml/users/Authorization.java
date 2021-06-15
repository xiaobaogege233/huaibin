package com.dml.users;

/**
 * 用户的一个授权。用于用户登录，用户登录的时候需要传递授权请求参数给系统，系统验证授权成功就是登录成功。
 * 
 * @author neo
 *
 */
public interface Authorization {

	/**
	 * 生成代表该auth的key
	 * 
	 * @return 生成的key
	 */
	AuthKey createAuthKey();

	/**
	 * 授权计算
	 * 
	 * @param parameters
	 *            计算授权需要的参数
	 * @throws AuthException
	 *             授权失败
	 */
	void auth(String... parameters) throws AuthException;

}
