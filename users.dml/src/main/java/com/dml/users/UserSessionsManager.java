package com.dml.users;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户会话管理
 *
 * @author neo
 */
public class UserSessionsManager {

	private String id;

	private Map<String, UserSession> idSessionMap = new ConcurrentHashMap<>();

	private Map<String, Set<String>> userIdSessionIdsMap = new ConcurrentHashMap<>();

	public void addSession(UserSession session) {
		idSessionMap.put(session.getId(), session);
		if (session.getUserId() != null) {
			Set<String> sessionIds = userIdSessionIdsMap.get(session.getUserId());
			if (sessionIds == null) {
				sessionIds = new HashSet<>();
				userIdSessionIdsMap.put(session.getUserId(), sessionIds);
			}
			sessionIds.add(session.getId());
		}
	}

	public void removeSession(UserSession session) {
		idSessionMap.remove(session.getId());
		if (session.getUserId() != null) {
			userIdSessionIdsMap.get(session.getUserId()).remove(session.getId());
		}
	}

	public Set<String> getSessionIds() {
		return idSessionMap.keySet();
	}

	public void createSessionForUser(String userId, String sessionId, long createTime, long keepaliveTime) {
		UserSession session = new UserSession();
		session.setId(sessionId);
		session.setUserId(userId);
		session.setCreateTime(createTime);
		session.setTimeoutTime(createTime + keepaliveTime);
		addSession(session);
	}

	/**
	 * 独占模式会踢除该用户的其他会话
	 * 
	 * @param userId
	 * @param sessionId
	 * @param createTime
	 * @param keepaliveTime
	 */
	public void createEngrossSessionForUser(String userId, String sessionId, long createTime, long keepaliveTime) {
		removeSessionsForUser(userId);
		UserSession session = new UserSession();
		session.setId(sessionId);
		session.setUserId(userId);
		session.setCreateTime(createTime);
		session.setTimeoutTime(createTime + keepaliveTime);
		addSession(session);
	}

	public Set<String> getSessionIdsForUsers(Set<String> userIdSet) {
		Set<String> idSet = new HashSet<>();
		userIdSet.forEach(userId -> {
			Set<String> sessionIds = userIdSessionIdsMap.get(userId);
			if (sessionIds != null) {
				idSet.addAll(sessionIds);
			}
		});
		return idSet;
	}

	public void removeSessionsForUser(String userId) {
		Set<String> sessionIds = userIdSessionIdsMap.get(userId);
		if (sessionIds != null) {
			sessionIds.forEach((sessionId) -> {
				idSessionMap.remove(sessionId);
			});
			sessionIds.clear();
		}
	}

	public Set<UserSession> checkAndRemoveOTSessions(long currentTime) {
		Set<UserSession> removed = new HashSet<>();
		Iterator<UserSession> i = idSessionMap.values().iterator();
		while (i.hasNext()) {
			UserSession session = i.next();
			if (session.isTimeout(currentTime)) {
				idSessionMap.remove(session.getId());
				Set<String> sessionIds = userIdSessionIdsMap.get(session.getUserId());
				if (sessionIds != null) {
					sessionIds.remove(session.getId());
				}
				removed.add(session);
			}
		}
		return removed;
	}

	/**
	 * 登录的会话转为游客会话
	 * 
	 * @param sessionId
	 */
	public void toPlanSession(String sessionId) {
		UserSession userSession = idSessionMap.get(sessionId);
		if (userSession != null) {
			userSession.setUserId(null);
			Set<String> sessionIds = userIdSessionIdsMap.get(userSession.getUserId());
			if (sessionIds != null) {
				sessionIds.remove(sessionId);
			}
		}
	}

	/**
	 * 游客会话转为登录的会话
	 * 
	 * @param sessionId
	 * @param userId
	 */
	public void toUserSession(String sessionId, String userId) {
		UserSession userSession = idSessionMap.get(sessionId);
		if (userSession != null) {
			userSession.setUserId(userId);
			Set<String> sessionIds = userIdSessionIdsMap.get(userId);
			if (sessionIds == null) {
				sessionIds = new HashSet<>();
				userIdSessionIdsMap.put(userId, sessionIds);
			}
			sessionIds.add(sessionId);
		}
	}

	public String getUserIdBySessionId(String sessionId) {
		UserSession session = idSessionMap.get(sessionId);
		if (session != null) {
			return session.getUserId();
		} else {
			return null;
		}
	}

	public void updateUserSession(String sessionId, long currentTime, long sessionKeepTime) {
		UserSession session = idSessionMap.get(sessionId);
		if (session != null) {
			session.setTimeoutTime(currentTime + sessionKeepTime);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
