package com.dml.majiang.pan.result;

import java.util.List;

import com.dml.majiang.pan.frame.PanValueObject;
import com.dml.majiang.player.valueobj.MajiangPlayerValueObject;
import com.dml.majiang.position.MajiangPosition;

public abstract class PanResult {
	private long panFinishTime;
	private PanValueObject pan;

	public List<String> allPlayerIds() {
		return pan.allPlayerIds();
	}

	public String findZhuangPlayerId() {
		return pan.getZhuangPlayerId();
	}

	public boolean ifPlayerHu(String playerId) {
		return pan.ifPlayerHu(playerId);
	}

	public boolean hasHu() {
		return pan.hasHu();
	}

	public boolean ifPlayerGang(String playerId) {
		return pan.ifPlayerGang(playerId);
	}

	public boolean hasGang() {
		return pan.hasGang();
	}

	public MajiangPosition playerMenFeng(String playerId) {
		return pan.playerMenFeng(playerId);
	}

	public String findXiajiaPlayerId(String playerId) {
		return pan.findXiajiaPlayerId(playerId);
	}

	public int playerGuipaiCount(String playerId) {
		return pan.playerGuipaiCount(playerId);
	}

	public MajiangPlayerValueObject findPlayer(String playerId) {
		return pan.findPlayer(playerId);
	}

	public long getPanFinishTime() {
		return panFinishTime;
	}

	public void setPanFinishTime(long panFinishTime) {
		this.panFinishTime = panFinishTime;
	}

	public PanValueObject getPan() {
		return pan;
	}

	public void setPan(PanValueObject pan) {
		this.pan = pan;
	}

}
