package com.dml.mpgame.game.player;

public class PlayerReadyToStart implements GamePlayerState {

	public static final String name = "PlayerReadyToStart";

	@Override
	public String name() {
		return name;
	}
}