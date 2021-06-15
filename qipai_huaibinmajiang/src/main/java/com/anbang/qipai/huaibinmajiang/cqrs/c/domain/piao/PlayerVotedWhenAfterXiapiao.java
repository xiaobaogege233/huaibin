package com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao;

import com.dml.mpgame.game.player.GamePlayerState;

public class PlayerVotedWhenAfterXiapiao implements GamePlayerState {

	public static final String name = "PlayerVotedWhenAfterXiapiao";

	@Override
	public String name() {
		return name;
	}

}
