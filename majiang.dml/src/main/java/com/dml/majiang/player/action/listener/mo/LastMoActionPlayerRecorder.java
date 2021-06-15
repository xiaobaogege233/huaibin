package com.dml.majiang.player.action.listener.mo;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.mo.MajiangMoAction;

/**
 * 记录最后摸牌的玩家
 * 
 * @author lsc
 *
 */
public class LastMoActionPlayerRecorder implements MajiangPlayerMoActionStatisticsListener {

	private String lastMoActionPlayerId;

	@Override
	public void updateForNextPan() {
		lastMoActionPlayerId = null;
	}

	@Override
	public void update(MajiangMoAction moAction, Ju ju) {
		lastMoActionPlayerId = moAction.getActionPlayerId();
	}

	public String getLastMoActionPlayerId() {
		return lastMoActionPlayerId;
	}

	public void setLastMoActionPlayerId(String lastMoActionPlayerId) {
		this.lastMoActionPlayerId = lastMoActionPlayerId;
	}

}
