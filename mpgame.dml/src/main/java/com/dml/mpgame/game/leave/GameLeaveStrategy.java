package com.dml.mpgame.game.leave;

import com.dml.mpgame.game.Game;

public interface GameLeaveStrategy {
	public void leave(String playerId, Game game) throws Exception;
}
