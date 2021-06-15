package com.dml.mpgame.game.back;

import com.dml.mpgame.game.Game;

public interface GameBackStrategy {
	public void back(String playerId, Game game) throws Exception;
}
