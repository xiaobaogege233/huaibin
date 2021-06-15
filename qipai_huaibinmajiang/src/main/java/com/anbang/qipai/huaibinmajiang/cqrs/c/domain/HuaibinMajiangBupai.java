package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.player.action.mo.MopaiReason;

public class HuaibinMajiangBupai implements MopaiReason {

	public static final String name = "bupai";

	@Override
	public String getName() {
		return name;
	}

}
