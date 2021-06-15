package com.dml.mpgame.game.extend.vote;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameFinishVote {

	private String sponsorId;

	private VoteCalculator calculator;

	private Set<String> votePlayerIds;

	private Map<String, VoteOption> playerIdVoteOptionMap = new HashMap<>();

	private VoteResult result;

	private long startTime;// 投票发起时间

	private long endTime;// 投票过期时间

	public GameFinishVote() {
	}

	public GameFinishVote(String sponsorId, VoteCalculator calculator, Set<String> votePlayerIds, long currentTime,
			long keepTime) {
		this.sponsorId = sponsorId;
		this.calculator = calculator;
		this.votePlayerIds = votePlayerIds;
		startTime = currentTime;
		endTime = startTime + keepTime;
	}

	public void vote(String playerId, VoteOption option) throws Exception {
		if (result != null) {
			throw new VoteIsFinishedException();
		}
		if (ifPlayerVoted(playerId)) {
			throw new PlayerAlreadyVoteException();
		}
		playerIdVoteOptionMap.put(playerId, option);
	}

	/**
	 * 投票时间结束，未投票的视为弃权
	 */
	public void voteByTimeOver(long currentTime) throws Exception {
		if (result != null) {
			throw new VoteIsFinishedException();
		}
		if (currentTime < endTime) {
			throw new VoteTimeIsNotExhaustedException();
		}
		for (String playerId : votePlayerIds) {
			if (!playerIdVoteOptionMap.containsKey(playerId)) {
				playerIdVoteOptionMap.put(playerId, VoteOption.waiver);
			}
		}
	}

	public void join(String playerId) {
		votePlayerIds.add(playerId);
	}

	public void calculateResult() {
		calculator.calculateResult(this);
	}

	public VoteOption findPlayerVoteOption(String playerId) {
		return playerIdVoteOptionMap.get(playerId);
	}

	public boolean ifPlayerVoted(String playerId) {
		return playerIdVoteOptionMap.containsKey(playerId);
	}

	public boolean ifPlayerJoinVote(String playerId) {
		return votePlayerIds.contains(playerId);
	}

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public VoteCalculator getCalculator() {
		return calculator;
	}

	public void setCalculator(VoteCalculator calculator) {
		this.calculator = calculator;
	}

	public Set<String> getVotePlayerIds() {
		return votePlayerIds;
	}

	public void setVotePlayerIds(Set<String> votePlayerIds) {
		this.votePlayerIds = votePlayerIds;
	}

	public Map<String, VoteOption> getPlayerIdVoteOptionMap() {
		return playerIdVoteOptionMap;
	}

	public void setPlayerIdVoteOptionMap(Map<String, VoteOption> playerIdVoteOptionMap) {
		this.playerIdVoteOptionMap = playerIdVoteOptionMap;
	}

	public VoteResult getResult() {
		return result;
	}

	public void setResult(VoteResult result) {
		this.result = result;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
