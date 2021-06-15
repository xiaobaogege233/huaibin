package com.anbang.qipai.huaibinmajiang.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.huaibinmajiang.plan.bean.MemberGoldBalance;

public interface MemberGoldBalanceRepository extends MongoRepository<MemberGoldBalance, String> {

	MemberGoldBalance findOneByMemberId(String memberId);
}
