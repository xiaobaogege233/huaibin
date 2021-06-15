package com.anbang.qipai.huaibinmajiang.plan.dao.mongodb;

import com.anbang.qipai.huaibinmajiang.plan.bean.MemberPowerBalance;
import com.anbang.qipai.huaibinmajiang.plan.dao.MemberPowerBalanceDao;
import com.anbang.qipai.huaibinmajiang.plan.dao.mongodb.repository.MemberPowerBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongdbMemberPowerBalanceDao implements MemberPowerBalanceDao {

    @Autowired
    private MemberPowerBalanceRepository memberPowerBalanceRepository;

    @Override
    public void save(MemberPowerBalance memberPowerBalance) {
        memberPowerBalanceRepository.save(memberPowerBalance);
    }

    @Override
    public MemberPowerBalance findByMemberIdAndLianmengId(String memberId, String lianmengId) {
        return memberPowerBalanceRepository.findOneByMemberIdAndLianmengId(memberId, lianmengId);
    }

}
