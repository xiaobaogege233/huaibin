package com.dml.mpgame.game.player;

/**
 * 游戏玩家
 * 
 * @author Neo
 *
 */
public class GamePlayer {
	private String id;
	private GamePlayerState state;
	private GamePlayerOnlineState onlineState;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GamePlayerState getState() {
		return state;
	}

	public void setState(GamePlayerState state) {
		this.state = state;
	}

	public GamePlayerOnlineState getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(GamePlayerOnlineState onlineState) {
		this.onlineState = onlineState;
	}

}
