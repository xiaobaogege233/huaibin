package test.dml.users;

import java.util.HashMap;
import java.util.Map;

import com.dml.users.AccountPasswordAuthorization;
import com.dml.users.AuthException;
import com.dml.users.AuthorizationAlreadyExistsException;
import com.dml.users.AuthorizationNotFoundException;
import com.dml.users.PublisherUniqueStringAuthKey;
import com.dml.users.StringAuthKey;
import com.dml.users.ThirdAuthorization;
import com.dml.users.UserNotFoundException;
import com.dml.users.UserSessionsManager;
import com.dml.users.UsersManager;

public class MemberService {

	private UsersManager usersManager = new UsersManager();// 这里只是演示，正式环境需要从repository取出。

	private UserSessionsManager userSessionsManager = new UserSessionsManager();// 这里只是演示，正式环境需要从repository取出。

	private Map<String, Member> members = new HashMap<>();

	public void addMember(String memberId, String realName, String loginName, String password) throws Exception {
		AccountPasswordAuthorization auth = new AccountPasswordAuthorization();
		auth.setAccount(loginName);
		auth.setPassword(password);

		usersManager.createUserWithAuth(memberId, auth);
		Member member = new Member();
		member.setId(memberId);
		member.setName(realName);
		members.put(memberId, member);
	}

	public void thirdAddMember(String memberId, String realName, String publisher, String openId) throws Exception {
		ThirdAuthorization auth = new ThirdAuthorization();
		auth.setPublisher(publisher);
		auth.setUuid(openId);

		usersManager.createUserWithAuth(memberId, auth);
		Member member = new Member();
		member.setId(memberId);
		member.setName(realName);
		members.put(memberId, member);
	}

	public Member getMemberById(String id) {
		return members.get(id);
	}

	/**
	 * @param loginName
	 * @param password
	 * @throws AuthException
	 * @throws AuthorizationNotFoundException
	 */
	public void login(String loginName, String password, String token, long currentTime, long sessionKeepTime)
			throws AuthorizationNotFoundException, AuthException {
		StringAuthKey ak = new StringAuthKey();
		ak.setKey(loginName);
		String userId = usersManager.authAndGetUserId(ak, password);
		userSessionsManager.createSessionForUser(userId, token, currentTime, sessionKeepTime);
	}

	public void thirdLogin(String publisher, String openId, String token, long currentTime, int sessionKeepTime)
			throws AuthorizationNotFoundException, AuthException {
		PublisherUniqueStringAuthKey ak = new PublisherUniqueStringAuthKey();
		ak.setPublisher(publisher);
		ak.setUuid(openId);
		String userId = usersManager.authAndGetUserId(ak);
		userSessionsManager.createSessionForUser(userId, token, currentTime, sessionKeepTime);
	}

	public void engrossThirdLogin(String publisher, String openId, String token, long currentTime, int sessionKeepTime)
			throws AuthorizationNotFoundException, AuthException {
		PublisherUniqueStringAuthKey ak = new PublisherUniqueStringAuthKey();
		ak.setPublisher(publisher);
		ak.setUuid(openId);
		String userId = usersManager.authAndGetUserId(ak);
		userSessionsManager.createEngrossSessionForUser(userId, token, currentTime, sessionKeepTime);
	}

	public void thirdCreateAuthForUser(String userId, String openId, String publisher)
			throws UserNotFoundException, AuthorizationAlreadyExistsException {
		ThirdAuthorization auth = new ThirdAuthorization();
		auth.setPublisher(publisher);
		auth.setUuid(openId);
		usersManager.addAuthForUser(userId, auth);
	}

	public UserSessionsManager getUserSessionsManager() {
		return userSessionsManager;
	}

	public Member getMemberByToken(String token) {
		String memberId = userSessionsManager.getUserIdBySessionId(token);
		return members.get(memberId);
	}

	public void updateUserSession(String token, long currentTime, int sessionKeepTime) {
		userSessionsManager.updateUserSession(token, currentTime, sessionKeepTime);
	}

}
