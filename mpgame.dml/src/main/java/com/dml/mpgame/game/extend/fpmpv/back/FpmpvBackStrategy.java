package com.dml.mpgame.game.extend.fpmpv.back;

import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.back.GameBackStrategy;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGame;
import com.dml.mpgame.game.player.GamePlayerOnlineState;

/**
 * 回来的时候要加入投票
 * 
 * @author Neo
 *
 */
public class FpmpvBackStrategy implements GameBackStrategy {

	@Override
	public void back(String playerId, Game game) throws Exception {

		game.updatePlayerOnlineState(playerId, GamePlayerOnlineState.online);
		FixedPlayersMultipanAndVotetofinishGame fpmpvGame = (FixedPlayersMultipanAndVotetofinishGame) game;
		if (fpmpvGame.ifVoting() && !fpmpvGame.ifPlayerJoinVote(playerId)) {
			fpmpvGame.joinToVote(playerId);
		}

	}

}
