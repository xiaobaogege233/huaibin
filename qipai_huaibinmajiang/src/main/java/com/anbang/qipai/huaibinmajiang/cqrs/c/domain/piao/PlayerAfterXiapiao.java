package com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao;

import com.dml.mpgame.game.player.GamePlayerState;

public class PlayerAfterXiapiao implements GamePlayerState {

	public static final String name = "PlayerAfterXiapiao";

	@Override
	public String name() {
		return name;
	}

}
