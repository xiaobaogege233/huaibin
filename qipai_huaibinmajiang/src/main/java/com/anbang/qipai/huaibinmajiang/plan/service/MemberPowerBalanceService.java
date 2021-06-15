package com.anbang.qipai.huaibinmajiang.plan.service;

import com.anbang.qipai.huaibinmajiang.plan.bean.MemberPowerBalance;
import com.anbang.qipai.huaibinmajiang.plan.dao.MemberPowerBalanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberPowerBalanceService {

    @Autowired
    private MemberPowerBalanceDao memberPowerBalanceDao;

    public void save(MemberPowerBalance memberPowerBalance) {
        memberPowerBalanceDao.save(memberPowerBalance);
    }

    public MemberPowerBalance findByMemberIdAndLianmengId(String memberId, String lianmengId) {
        return memberPowerBalanceDao.findByMemberIdAndLianmengId(memberId, lianmengId);
    }
}
