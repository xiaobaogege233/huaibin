package com.anbang.qipai.huaibinmajiang.msg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.huaibinmajiang.msg.channel.source.MemberGoldsSource;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.CommonMO;

@EnableBinding(MemberGoldsSource.class)
public class MemberGoldsMsgService {
	@Autowired
	private MemberGoldsSource memberGoldsSource;

	public void withdraw(String memberId, int amount, String textSummary) {
		CommonMO mo = new CommonMO();
		mo.setMsg("withdraw");
		Map data = new HashMap();
		data.put("memberId", memberId);
		data.put("amount", amount);
		data.put("textSummary", textSummary);
		mo.setData(data);
		memberGoldsSource.memberGoldsAccounting().send(MessageBuilder.withPayload(mo).build());
	}

	public void giveGoldToMember(String memberId, int amount, String textSummary) {
		CommonMO mo = new CommonMO();
		mo.setMsg("givegoldtomember");
		Map data = new HashMap();
		data.put("memberId", memberId);
		data.put("amount", amount);
		data.put("textSummary", textSummary);
		mo.setData(data);
		memberGoldsSource.memberGoldsAccounting().send(MessageBuilder.withPayload(mo).build());
	}
}
