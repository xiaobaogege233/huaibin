package test.dml.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.dml.users.AuthException;
import com.dml.users.AuthorizationAlreadyExistsException;
import com.dml.users.AuthorizationNotFoundException;
import com.dml.users.UserNotFoundException;
import com.dml.users.WrongPasswordException;

public class UsersTest {

	@Test
	public void doTest() {

		long currentTime = 0;

		MemberService memberService = new MemberService();
		// 注册新会员
		try {
			memberService.addMember("1", "郑成栋1", "neo", "123");
		} catch (Exception e) {
			assertTrue(false);
		}

		// 确认新会员注册成功
		Member member1 = memberService.getMemberById("1");
		assertEquals("郑成栋1", member1.getName());

		// 用新注册的会员登录_账号打错
		try {
			memberService.login("nee", "123", "0", currentTime, 1000 * 10);
		} catch (AuthorizationNotFoundException e) {
			assertTrue(true);
		} catch (AuthException e) {
			assertTrue(false);
		}

		// 用新注册的会员登录_密码打错
		try {
			memberService.login("neo", "111", "0", currentTime, 1000 * 10);
		} catch (AuthorizationNotFoundException e) {
			assertTrue(false);
		} catch (AuthException e) {
			assertTrue(e instanceof WrongPasswordException);
		}

		// 用新注册的会员登录_成功
		try {
			memberService.login("neo", "123", "0", currentTime, 1000 * 10);
		} catch (AuthorizationNotFoundException | AuthException e) {
			assertTrue(false);
		}

		// 得到当前登录会员的会员名
		member1 = memberService.getMemberByToken("0");
		assertEquals("郑成栋1", member1.getName());

		// 同一登录名再去注册
		try {
			memberService.addMember("2", "郑成栋2", "neo", "123");
		} catch (Exception e) {
			assertTrue(e instanceof AuthorizationAlreadyExistsException);
		}

		// 绑定三方，比如微信
		String wechatOpenId = "asfd09asdg";
		try {
			memberService.thirdCreateAuthForUser("1", wechatOpenId, "wechat");
		} catch (UserNotFoundException | AuthorizationAlreadyExistsException e) {
			assertTrue(false);
		}

		// 三方登录
		try {
			memberService.thirdLogin("wechat", wechatOpenId, "1", currentTime, 1000 * 10);
		} catch (AuthorizationNotFoundException | AuthException e) {
			assertTrue(false);
		}

		// 验证两个会话（同时登录不踢模式）
		member1 = memberService.getMemberByToken("1");
		assertEquals("郑成栋1", member1.getName());
		member1 = memberService.getMemberByToken("0");
		assertEquals("郑成栋1", member1.getName());

		// 三方注册，比如qq
		String qqOpenId = "fdghjhkl";
		try {
			memberService.thirdAddMember("2", "郑成栋2", "qq", qqOpenId);
		} catch (Exception e) {
			assertTrue(false);
		}

		// 确认新会员注册成功
		Member member2 = memberService.getMemberById("2");
		assertEquals("郑成栋2", member2.getName());

		// 绑定另一个三方，比如微信
		String wechatOpenId2 = "qwewqlkhdfgpor";
		try {
			memberService.thirdCreateAuthForUser("2", wechatOpenId2, "wechat");
		} catch (UserNotFoundException | AuthorizationAlreadyExistsException e) {
			assertTrue(false);
		}

		// qq三方登录
		try {
			memberService.thirdLogin("qq", qqOpenId, "2", currentTime, 1000 * 10);
		} catch (AuthorizationNotFoundException | AuthException e) {
			assertTrue(false);
		}
		// 验证登录
		member2 = memberService.getMemberByToken("2");
		assertEquals("郑成栋2", member2.getName());

		// 微信三方登录（排他模式）
		try {
			memberService.engrossThirdLogin("qq", qqOpenId, "3", currentTime, 1000 * 10);
		} catch (AuthorizationNotFoundException | AuthException e) {
			assertTrue(false);
		}

		// 验证登录
		member2 = memberService.getMemberByToken("3");
		assertEquals("郑成栋2", member2.getName());
		// 验证登录被踢
		member2 = memberService.getMemberByToken("2");
		assertNull(member2);

		// 业务活动导致session延长
		currentTime += (1000 * 5);
		memberService.updateUserSession("3", currentTime, 1000 * 10);

		// session 超时
		currentTime += (1000 * 6);
		memberService.getUserSessionsManager().checkAndRemoveOTSessions(currentTime);
		member1 = memberService.getMemberByToken("1");
		assertNull(member1);
		member2 = memberService.getMemberByToken("3");
		assertEquals("郑成栋2", member2.getName());

	}

}
