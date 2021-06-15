package com.dml.mpgame.game.leave;

import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.GamePlayerOnlineState;

public class HostGameLeaveStrategy implements GameLeaveStrategy {

	private String hostPlayerId;

	public HostGameLeaveStrategy() {
	}

	public HostGameLeaveStrategy(String hostPlayerId) {
		this.hostPlayerId = hostPlayerId;
	}

	@Override
	public void leave(String playerId, Game game) throws Exception {
		GamePlayer player = game.findPlayer(playerId);
		if (player != null && !player.getOnlineState().equals(GamePlayerOnlineState.offline)) {
			if (playerId.equals(hostPlayerId)) {// 主机玩家正常离开
				game.updatePlayerOnlineState(playerId, GamePlayerOnlineState.offline);
			} else {// 副机玩家离开就是直接退出
				game.removePlayer(playerId);
			}
		}
	}

}
