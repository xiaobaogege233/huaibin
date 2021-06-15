package com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao;

import com.dml.mpgame.game.player.GamePlayerState;

public class PlayerXiapiao implements GamePlayerState {

	public static final String name = "PlayerXiapiao";

	@Override
	public String name() {
		return name;
	}

}
