package com.anbang.qipai.huaibinmajiang.plan.dao.mongodb.repository;

import com.anbang.qipai.huaibinmajiang.plan.bean.MemberPowerBalance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberPowerBalanceRepository extends MongoRepository<MemberPowerBalance, String> {

	MemberPowerBalance findOneByMemberIdAndLianmengId(String memberId, String lianmengId);
}
