package com.dml.mpgame.game.extend.vote;

import java.util.Set;

import com.dml.mpgame.game.Game;

public interface VotePlayersFilter {
	public Set<String> filter(Game game);
}
