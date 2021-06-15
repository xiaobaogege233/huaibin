package com.dml.mpgame.game.ready;

import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.IllegalOperationException;
import com.dml.mpgame.game.WaitingStart;
import com.dml.mpgame.game.player.PlayerJoined;
import com.dml.mpgame.game.player.PlayerReadyToStart;

public class FixedNumberOfPlayersGameReadyStrategy implements GameReadyStrategy {

	private int fixedNumberOfPlayers;

	public FixedNumberOfPlayersGameReadyStrategy() {
	}

	public FixedNumberOfPlayersGameReadyStrategy(int fixedNumberOfPlayers) {
		this.fixedNumberOfPlayers = fixedNumberOfPlayers;
	}

	@Override
	public void ready(String playerId, Game game, long currentTime) throws Exception {

		if (!game.getState().name().equals(WaitingStart.name)) {
			throw new IllegalOperationException();
		}
		if (!game.playerState(playerId).name().equals(PlayerJoined.name)) {
			throw new IllegalOperationException();
		}
		game.updatePlayerState(playerId, new PlayerReadyToStart());

		int playerCounts = game.playerCounts();
		if (playerCounts == fixedNumberOfPlayers) {// 达到游戏规定人数
			if (game.allPlayersReady()) {// 并且所有玩家都准备好了，那就开始
				game.start(currentTime);
			}
		}
	}

	@Override
	public void cancelReady(String playerId, Game game, long currentTime) throws Exception {
		if (!game.getState().name().equals(WaitingStart.name)) {
			throw new IllegalOperationException();
		}
		if (!game.playerState(playerId).name().equals(PlayerReadyToStart.name)) {
			throw new IllegalOperationException();
		}
		game.updatePlayerState(playerId, new PlayerJoined());
	}

}
