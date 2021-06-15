package com.dml.mpgame.game.extend.vote.leave;

import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGame;
import com.dml.mpgame.game.extend.vote.VoteOption;
import com.dml.mpgame.game.leave.GameLeaveStrategy;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.GamePlayerOnlineState;

/**
 * 离开的时候要投弃权票
 * 
 * @author Neo
 *
 */
public class VoteWaiverLeaveStrategy implements GameLeaveStrategy {

	@Override
	public void leave(String playerId, Game game) throws Exception {
		GamePlayer player = game.findPlayer(playerId);
		if (player != null && !player.getOnlineState().equals(GamePlayerOnlineState.offline)) {
			// 开玩之后,离开的时候要投弃权票(如果在投票的话)
			FixedPlayersMultipanAndVotetofinishGame fpmpvGame = (FixedPlayersMultipanAndVotetofinishGame) game;
			if (fpmpvGame.ifVoting() && !fpmpvGame.ifPlayerVoted(playerId)) {
				fpmpvGame.voteToFinish(playerId, VoteOption.waiver);
			}
			game.updatePlayerOnlineState(playerId, GamePlayerOnlineState.offline);

		}
	}

}
