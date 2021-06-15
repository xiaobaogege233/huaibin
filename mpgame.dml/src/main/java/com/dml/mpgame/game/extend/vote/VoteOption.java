package com.dml.mpgame.game.extend.vote;

public enum VoteOption {
	yes, no, waiver;
	private static VoteOption[] array = VoteOption.values();

	public static VoteOption valueOf(int ordinal) {
		return array[ordinal];
	}
}
