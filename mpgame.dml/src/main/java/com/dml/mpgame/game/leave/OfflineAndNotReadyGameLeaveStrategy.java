package com.dml.mpgame.game.leave;

import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.WaitingStart;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.dml.mpgame.game.player.PlayerJoined;
import com.dml.mpgame.game.player.PlayerReadyToStart;

/**
 * 如果游戏未开始，离开就是离线并且取消准备。游戏开始后，离开就是离线
 * 
 * @author lsc
 *
 */
public class OfflineAndNotReadyGameLeaveStrategy implements GameLeaveStrategy {

	@Override
	public void leave(String playerId, Game game) throws Exception {
		GamePlayer player = game.findPlayer(playerId);
		if (player != null && !player.getOnlineState().equals(GamePlayerOnlineState.offline)) {
			game.updatePlayerOnlineState(playerId, GamePlayerOnlineState.offline);
		}
		if (game.getState().name().equals(WaitingStart.name)) {// 游戏未开始
			if (game.playerState(playerId).name().equals(PlayerReadyToStart.name)) {
				game.updatePlayerState(playerId, new PlayerJoined());// 取消准备
			}
		}
	}

}
