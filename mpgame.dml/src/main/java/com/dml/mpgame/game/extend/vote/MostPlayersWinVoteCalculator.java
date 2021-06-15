package com.dml.mpgame.game.extend.vote;

import java.util.Set;

/**
 * 少数服从多数.平手的话算不通过
 * 
 * @author Neo
 *
 */
public class MostPlayersWinVoteCalculator implements VoteCalculator {

	@Override
	public void calculateResult(GameFinishVote vote) {
		Set<String> votePlayerIds = vote.getVotePlayerIds();
		int yesCount = 0;
		int noCount = 0;
		int notDecidedCount = 0;
		for (String playerId : votePlayerIds) {
			VoteOption playerOption = vote.findPlayerVoteOption(playerId);
			if (playerOption != null) {
				if (playerOption.equals(VoteOption.yes)) {
					yesCount++;
				} else if (playerOption.equals(VoteOption.no)) {
					noCount++;
				} else if (playerOption.equals(VoteOption.waiver)) {
					// 弃权就是不计票
				} else {
				}
			} else {
				notDecidedCount++;
			}
		}

		if (noCount>=1){
            vote.setResult(VoteResult.no);
		}else {
		    if (notDecidedCount<=0){
                vote.setResult(VoteResult.yes);
            }
        }

	}

}
