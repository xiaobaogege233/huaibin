package com.dml.mpgame.game.extend.multipan.player;

import com.dml.mpgame.game.player.GamePlayerState;

public class PlayerReadyToStartNextPan implements GamePlayerState {

	public static final String name = "PlayerReadyToStartNextPan";

	@Override
	public String name() {
		return name;
	}
}