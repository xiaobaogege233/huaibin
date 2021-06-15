package com.dml.users;

/**
 * 基于帐号密码的授权
 * 
 * @author neo
 *
 */
public class AccountPasswordAuthorization implements Authorization {

	private String account;

	private String password;

	@Override
	public void auth(String... parameters) throws AuthException {
		String inputPassword = parameters[0];
		if (!inputPassword.equals(password)) {
			throw new WrongPasswordException();
		}
	}

	@Override
	public AuthKey createAuthKey() {
		StringAuthKey key = new StringAuthKey();
		key.setKey(account);
		return key;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
