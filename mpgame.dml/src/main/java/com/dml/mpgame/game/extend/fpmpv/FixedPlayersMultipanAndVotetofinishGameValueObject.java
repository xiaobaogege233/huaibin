package com.dml.mpgame.game.extend.fpmpv;

import com.dml.mpgame.game.GameValueObject;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public abstract class FixedPlayersMultipanAndVotetofinishGameValueObject extends GameValueObject {

	private int panNo;

	private int fixedPlayerCount;

	private GameFinishVoteValueObject vote;

	private Set<String> readyToStartNextPanPlayerIdsSet;



	public FixedPlayersMultipanAndVotetofinishGameValueObject(FixedPlayersMultipanAndVotetofinishGame game) {
		super(game);
		panNo = game.getPanNo();
		fixedPlayerCount = game.getFixedPlayerCount();
		if (game.getVote() != null) {
			vote = new GameFinishVoteValueObject(game.getVote());
		}
		readyToStartNextPanPlayerIdsSet = new HashSet<>(game.getReadyToStartNextPanPlayerIdsSet());
	}



}
