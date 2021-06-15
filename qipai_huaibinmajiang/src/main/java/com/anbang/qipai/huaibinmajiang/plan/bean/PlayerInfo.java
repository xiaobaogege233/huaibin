package com.anbang.qipai.huaibinmajiang.plan.bean;

import lombok.Data;

@Data
public class PlayerInfo {
	private String id;
	private String nickname;
	private String gender;// 会员性别:男:male,女:female
	private String headimgurl;
	private boolean vip;

}
