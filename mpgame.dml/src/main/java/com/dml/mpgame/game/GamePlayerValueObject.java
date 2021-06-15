package com.dml.mpgame.game;

import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.dml.mpgame.game.player.GamePlayerState;

public class GamePlayerValueObject {
	private String id;
	private GamePlayerState state;
	private GamePlayerOnlineState onlineState;

	public GamePlayerValueObject() {
	}

	public GamePlayerValueObject(GamePlayer gamePlayer) {
		id = gamePlayer.getId();
		state = gamePlayer.getState();
		onlineState = gamePlayer.getOnlineState();
	}

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
