package com.dml.majiang.pan.result;

public abstract class PanPlayerResult {

	private String playerId;

	public PanPlayerResult() {
	}

	public PanPlayerResult(String playerId) {
		this.playerId = playerId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

}
