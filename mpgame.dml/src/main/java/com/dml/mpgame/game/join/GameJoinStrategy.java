package com.dml.mpgame.game.join;

import com.dml.mpgame.game.Game;

public interface GameJoinStrategy {
	public void join(String playerId, Game game) throws Exception;
}
