package com.dml.mpgame.game.extend.fpmpv;

import com.dml.mpgame.game.*;
import com.dml.mpgame.game.extend.fpmpv.player.PlayerPanFinishedAndVoted;
import com.dml.mpgame.game.extend.fpmpv.player.PlayerPanFinishedAndVoting;
import com.dml.mpgame.game.extend.fpmpv.player.PlayerReadyToStartNextPanAndVoted;
import com.dml.mpgame.game.extend.fpmpv.player.PlayerReadyToStartNextPanAndVoting;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.multipan.player.PlayerPanFinished;
import com.dml.mpgame.game.extend.multipan.player.PlayerReadyToStartNextPan;
import com.dml.mpgame.game.extend.vote.*;
import com.dml.mpgame.game.extend.vote.player.PlayerPlayingAndVoted;
import com.dml.mpgame.game.extend.vote.player.PlayerPlayingAndVoting;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.PlayerFinished;
import com.dml.mpgame.game.player.PlayerPlaying;

import java.util.HashSet;
import java.util.Set;

/**
 * 游戏固定玩家人数，过程中要玩多盘（典型的如棋牌类游戏），并且以玩家投票的方式决定是否中止游戏。
 * 
 * @author Neo
 *
 */
public abstract class FixedPlayersMultipanAndVotetofinishGame extends Game {

	/**
	 * 第几盘，从1开始
	 */
	private int panNo = 1;

	private int fixedPlayerCount;

	private VotePlayersFilter votePlayersFilter;

	private GameFinishVote vote;

	private Set<String> readyToStartNextPanPlayerIdsSet = new HashSet<>();

	/**
	 * 结束当前盘，有可能导致整个过程结束
	 */
	public void checkAndFinishPan() throws Exception {
		if (checkToFinishCurrentPan()) {
			// 还要判断游戏是否结束
			if (checkToFinishGame()) {
				finish();
				state = new Finished();
				updateAllPlayersState(new PlayerFinished());
			} else {
				finishCurrentPan();
			}
		}
	}

	private void finishCurrentPan() {
		state = new WaitingNextPan();
		updateAllPlayersState(new PlayerPanFinished());
	}

	protected abstract boolean checkToFinishGame() throws Exception;

	protected abstract boolean checkToFinishCurrentPan() throws Exception;

	public void readyToNextPan(String playerId) throws Exception {
		if (state.name().equals(VoteNotPassWhenWaitingNextPan.name)) {
			state = new WaitingNextPan();
		}
		if (state.name().equals(WaitingNextPan.name)) {
			readyToStartNextPanPlayerIdsSet.add(playerId);
			updatePlayerState(playerId, new PlayerReadyToStartNextPan());
			// 还要判断是否都准备好了，开始游戏了
			if (allPlayersReadyToNextPan()) {
				startNextPan();
				panNo++;
				readyToStartNextPanPlayerIdsSet.clear();
			}
		} else {
			throw new IllegalOperationException();
		}
	}

	public void readyToNextPan(String playerId, Set<String> playerIds) throws Exception {
		if (state.name().equals(VoteNotPassWhenWaitingNextPan.name)) {
			state = new WaitingNextPan();
		}
		if (state.name().equals(WaitingNextPan.name)) {
			readyToStartNextPanPlayerIdsSet.add(playerId);
			if (playerIds != null) {
				readyToStartNextPanPlayerIdsSet.addAll(playerIds);
			}
			updatePlayerState(playerId, new PlayerReadyToStartNextPan());
			// 还要判断是否都准备好了，开始游戏了
			if (allPlayersReadyToNextPan()) {
				startNextPan();
				panNo++;
				readyToStartNextPanPlayerIdsSet.clear();
			}
		} else {
			throw new IllegalOperationException();
		}
	}

	private boolean allPlayersReadyToNextPan() {
		return readyToStartNextPanPlayerIdsSet.size() == fixedPlayerCount;
	}

	protected abstract void startNextPan() throws Exception;

	public void launchVoteToFinish(String playerId, VoteCalculator voteCalculator, long currentTime, long keepTime)
			throws Exception {

		if (ifVoting()) {
			throw new VoteAlreadyLaunchedException();
		}

		if (state.name().equals(Canceled.name) || state.name().equals(FinishedByVote.name)
				|| state.name().equals(Finished.name)) {
			throw new IllegalOperationException();
		}
		// Set<String> votePlayerIds = votePlayersFilter.filter(this);
		Set<String> votePlayerIds = new HashSet<>(allPlayerIds());
		vote = new GameFinishVote(playerId, voteCalculator, votePlayerIds, currentTime, keepTime);
		updateToVotingState();
	}

	public void joinToVote(String playerId) {
		vote.join(playerId);
	}

	private void updateToVotingState() {
		for (GamePlayer player : idPlayerMap.values()) {
			updatePlayerToVotingState(player);
		}
		if (state.name().equals(Playing.name) || state.name().equals(VoteNotPassWhenPlaying.name)) {
			state = new VotingWhenPlaying();
		} else if (state.name().equals(WaitingNextPan.name)
				|| state.name().equals(VoteNotPassWhenWaitingNextPan.name)) {
			state = new VotingWhenWaitingNextPan();
		} else {
			updateToExtendedVotingState();
		}
	}

	private void updatePlayerToVotingState(GamePlayer player) {
		String stateName = player.getState().name();
		if (stateName.equals(PlayerPlaying.name)) {
			player.setState(new PlayerPlayingAndVoting());
		} else if (stateName.equals(PlayerPanFinished.name)) {
			player.setState(new PlayerPanFinishedAndVoting());
		} else if (stateName.equals(PlayerReadyToStartNextPan.name)) {
			player.setState(new PlayerReadyToStartNextPanAndVoting());
		} else {
			updatePlayerToExtendedVotingState(player);
		}
	}

	protected abstract void updatePlayerToExtendedVotingState(GamePlayer player);

	protected abstract void updateToExtendedVotingState();

	public boolean ifVoting() {
		return (vote != null && vote.getResult() == null);
	}

	public boolean ifPlayerVoted(String playerId) {
		return vote != null && vote.ifPlayerVoted(playerId);
	}

	public boolean ifPlayerJoinVote(String playerId) {
		return vote != null && vote.ifPlayerJoinVote(playerId);
	}

	public void voteToFinish(String playerId, VoteOption option) throws Exception {
		vote.vote(playerId, option);
		GamePlayer player = idPlayerMap.get(playerId);
		updatePlayerToVotedState(player);
		vote.calculateResult();
		VoteResult voteResult = vote.getResult();
		if (voteResult != null) {// 出结果了
			if (voteResult.equals(VoteResult.yes)) {// 通过
				finish();
				state = new FinishedByVote();
				updateAllPlayersState(new PlayerFinished());
			} else {
				updateToVoteNotPassState();
			}
		}
	}

	public void automaticToFinish() throws Exception {
		finish();
		state = new FinishedByTuoguan();
		updateAllPlayersState(new PlayerFinished());
	}

	/**
	 * 投票时间到，未投票的视为弃权
	 */
	public void voteToFinishByTimeOver(long currentTime) throws Exception {
		if (!ifVoting()) {
			throw new VoteNotFoundException();
		}
		vote.voteByTimeOver(currentTime);
		vote.calculateResult();
		VoteResult voteResult = vote.getResult();
		if (voteResult != null) {// 出结果了
			if (voteResult.equals(VoteResult.yes)) {// 通过
				finish();
				state = new FinishedByVote();
				updateAllPlayersState(new PlayerFinished());
			} else {
				updateToVoteNotPassState();
			}
		}
	}

	private void updatePlayerToVotedState(GamePlayer player) {
		String stateName = player.getState().name();
		if (stateName.equals(PlayerPlayingAndVoting.name)) {
			player.setState(new PlayerPlayingAndVoted());
		} else if (stateName.equals(PlayerPanFinishedAndVoting.name)) {
			player.setState(new PlayerPanFinishedAndVoted());
		} else if (stateName.equals(PlayerReadyToStartNextPanAndVoting.name)) {
			player.setState(new PlayerReadyToStartNextPanAndVoted());
		} else {
			updatePlayerToExtendedVotedState(player);
		}
	}

	protected abstract void updatePlayerToExtendedVotedState(GamePlayer player);

	private void updateToVoteNotPassState() throws Exception {
		if (state.name().equals(VotingWhenPlaying.name)) {
			state = new VoteNotPassWhenPlaying();
			updateAllPlayersState(new PlayerPlaying());
		} else if (state.name().equals(VotingWhenWaitingNextPan.name)) {
			state = new VoteNotPassWhenWaitingNextPan();
			for (String playerId : idPlayerMap.keySet()) {
				if (readyToStartNextPanPlayerIdsSet.contains(playerId)) {
					updatePlayerState(playerId, new PlayerReadyToStartNextPan());
				} else {
					updatePlayerState(playerId, new PlayerPanFinished());
				}
			}
		} else {
			updateToVoteNotPassStateFromExtendedVoting();
			recoveryPlayersStateFromExtendedVoting();
		}
	}

	protected abstract void recoveryPlayersStateFromExtendedVoting() throws Exception;

	protected abstract void updateToVoteNotPassStateFromExtendedVoting() throws Exception;

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

	public VotePlayersFilter getVotePlayersFilter() {
		return votePlayersFilter;
	}

	public void setVotePlayersFilter(VotePlayersFilter votePlayersFilter) {
		this.votePlayersFilter = votePlayersFilter;
	}

	public GameFinishVote getVote() {
		return vote;
	}

	public void setVote(GameFinishVote vote) {
		this.vote = vote;
	}

	public Set<String> getReadyToStartNextPanPlayerIdsSet() {
		return readyToStartNextPanPlayerIdsSet;
	}

	public void setReadyToStartNextPanPlayerIdsSet(Set<String> readyToStartNextPanPlayerIdsSet) {
		this.readyToStartNextPanPlayerIdsSet = readyToStartNextPanPlayerIdsSet;
	}

}
