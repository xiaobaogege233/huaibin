package com.anbang.qipai.huaibinmajiang.plan.bean;

import lombok.Data;

@Data
public class MemberPowerBalance {
	private String id;// 账户id
	private String memberId;
	private String lianmengId;
	private double balanceAfter;

}
