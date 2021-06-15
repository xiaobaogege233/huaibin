package com.anbang.qipai.huaibinmajiang.plan.dao;

import com.anbang.qipai.huaibinmajiang.plan.bean.MemberPowerBalance;

public interface MemberPowerBalanceDao {

	void save(MemberPowerBalance memberPowerBalance);

	MemberPowerBalance findByMemberIdAndLianmengId(String memberId, String lianmengId);
}
