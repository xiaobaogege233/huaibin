package com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao;

import com.dml.mpgame.game.GameState;

public class VotingWhenXiapiao implements GameState {

	public static final String name = "VotingWhenXiaPiao";

	@Override
	public String name() {
		return name;
	}

}
