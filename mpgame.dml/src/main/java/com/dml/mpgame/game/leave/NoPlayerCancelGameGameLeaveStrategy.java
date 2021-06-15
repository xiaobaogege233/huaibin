package com.dml.mpgame.game.leave;

import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.player.GamePlayer;

public class NoPlayerCancelGameGameLeaveStrategy implements GameLeaveStrategy {

	@Override
	public void leave(String playerId, Game game) throws Exception {
		GamePlayer player = game.findPlayer(playerId);
		if (player != null) {
			if (game.allPlayerIds().isEmpty()) {
				// 直接解散房间
				game.cancel();
			} else {
				// 玩家离开就是直接退出
				game.removePlayer(playerId);
			}
		}
	}

}
