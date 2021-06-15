package com.dml.majiang.player.action.chi;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;

/**
 * 吃的那个人要打牌
 * 
 * @author neo
 *
 */
public class ChiPlayerDaPaiChiActionUpdater implements MajiangPlayerChiActionUpdater {

	@Override
	public void updateActions(MajiangChiAction chiAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();

		MajiangPlayer player = currentPan.findPlayerById(chiAction.getActionPlayerId());

		// 吃的那个人要打出牌
		player.generateDaActions();

	}

}
