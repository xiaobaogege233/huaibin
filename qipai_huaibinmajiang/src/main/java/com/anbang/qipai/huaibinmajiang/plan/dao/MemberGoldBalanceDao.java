package com.anbang.qipai.huaibinmajiang.plan.dao;

import com.anbang.qipai.huaibinmajiang.plan.bean.MemberGoldBalance;

public interface MemberGoldBalanceDao {

	void save(MemberGoldBalance memberGoldBalance);

	MemberGoldBalance findByMemberId(String memberId);
}
