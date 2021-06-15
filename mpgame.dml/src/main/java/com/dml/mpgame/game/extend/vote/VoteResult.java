package com.dml.mpgame.game.extend.vote;

public enum VoteResult {
	yes, no;
	private static VoteResult[] array = VoteResult.values();

	public static VoteResult valueOf(int ordinal) {
		return array[ordinal];
	}
}
