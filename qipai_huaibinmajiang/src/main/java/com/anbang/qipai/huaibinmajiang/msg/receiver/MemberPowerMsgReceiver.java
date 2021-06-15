package com.anbang.qipai.huaibinmajiang.msg.receiver;

import com.anbang.qipai.huaibinmajiang.msg.channel.sink.MemberPowerBalanceSink;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.CommonMO;
import com.anbang.qipai.huaibinmajiang.plan.bean.MemberPowerBalance;
import com.anbang.qipai.huaibinmajiang.plan.service.MemberPowerBalanceService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.util.Map;

@EnableBinding(MemberPowerBalanceSink.class)
public class MemberPowerMsgReceiver {

	@Autowired
	private MemberPowerBalanceService memberPowerBalanceService;

	private Gson gson = new Gson();

	@StreamListener(MemberPowerBalanceSink.MEMBERPOWERBALANCE)
	public void recordMemberPowerRecordDbo(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		Map map = gson.fromJson(json, Map.class);
		if ("accounting".equals(msg)) {
			String accountId = (String) map.get("accountId");
			String memberId = (String) map.get("memberId");
			String lianmengId = (String) map.get("lianmengId");
			double balanceAfter = ((Double) map.get("balance"));
			MemberPowerBalance memberPowerBalance = new MemberPowerBalance();
			memberPowerBalance.setId(accountId);
			memberPowerBalance.setMemberId(memberId);
			memberPowerBalance.setLianmengId(lianmengId);
			memberPowerBalance.setBalanceAfter(balanceAfter);
			memberPowerBalanceService.save(memberPowerBalance);
		}
	}
}
