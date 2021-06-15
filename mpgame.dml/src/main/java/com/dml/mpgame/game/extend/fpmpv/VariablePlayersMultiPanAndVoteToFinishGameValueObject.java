package com.dml.mpgame.game.extend.fpmpv;

import com.dml.mpgame.game.GameValueObject;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;

import java.util.HashSet;
import java.util.Set;

public abstract class VariablePlayersMultiPanAndVoteToFinishGameValueObject extends GameValueObject {

	private int panNo;

	private int fixedPlayerCount;

	private GameFinishVoteValueObject vote;

	private Set<String> readyToStartNextPanPlayerIdsSet;

	public VariablePlayersMultiPanAndVoteToFinishGameValueObject(VariablePlayersMultiPanAndVoteToFinishGame game) {
		super(game);
		panNo = game.getPanNo();
		fixedPlayerCount = game.getFixedPlayerCount();
		if (game.getVote() != null) {
			vote = new GameFinishVoteValueObject(game.getVote());
		}
		readyToStartNextPanPlayerIdsSet = new HashSet<>(game.getReadyToStartNextPanPlayerIdsSet());
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public int getFixedPlayerCount() {
		return fixedPlayerCount;
	}

	public void setFixedPlayerCount(int fixedPlayerCount) {
		this.fixedPlayerCount = fixedPlayerCount;
	}

	public GameFinishVoteValueObject getVote() {
		return vote;
	}

	public void setVote(GameFinishVoteValueObject vote) {
		this.vote = vote;
	}

	public Set<String> getReadyToStartNextPanPlayerIdsSet() {
		return readyToStartNextPanPlayerIdsSet;
	}

	public void setReadyToStartNextPanPlayerIdsSet(Set<String> readyToStartNextPanPlayerIdsSet) {
		this.readyToStartNextPanPlayerIdsSet = readyToStartNextPanPlayerIdsSet;
	}

}
