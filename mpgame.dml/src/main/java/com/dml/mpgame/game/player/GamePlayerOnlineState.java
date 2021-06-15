package com.dml.mpgame.game.player;

public enum GamePlayerOnlineState {
	online, offline;
	private static GamePlayerOnlineState[] array = GamePlayerOnlineState.values();

	public static GamePlayerOnlineState valueOf(int ordinal) {
		return array[ordinal];
	}
}
