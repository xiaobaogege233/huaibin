package com.dml.mpgame.game.extend.vote;

import java.util.Set;

import com.dml.mpgame.game.Game;

public class OnlineVotePlayersFilter implements VotePlayersFilter {

	@Override
	public Set<String> filter(Game game) {
		return game.findOnlinePlayerIds();
	}

}
