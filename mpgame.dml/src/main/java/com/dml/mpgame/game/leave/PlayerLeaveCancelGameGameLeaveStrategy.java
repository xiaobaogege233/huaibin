package com.dml.mpgame.game.leave;

import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.PlayerFinished;

/**
 * 玩家退出游戏就结束
 * 
 * @author lsc
 *
 */
public class PlayerLeaveCancelGameGameLeaveStrategy implements GameLeaveStrategy {

	@Override
	public void leave(String playerId, Game game) throws Exception {
		GamePlayer player = game.findPlayer(playerId);
		if (player != null) {
			// 游戏结束
			game.finish();
			game.setState(new Finished());
			game.updateAllPlayersState(new PlayerFinished());
		}
	}

}
