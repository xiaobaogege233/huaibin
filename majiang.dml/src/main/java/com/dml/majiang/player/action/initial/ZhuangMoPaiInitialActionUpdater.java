package com.dml.majiang.player.action.initial;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;

/**
 * 庄摸牌初始化动作
 */
public class ZhuangMoPaiInitialActionUpdater implements MajiangPlayerInitialActionUpdater {

	@Override
	public void updateActions(Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		currentPan.addPlayerActionCandidate(new MajiangMoAction(currentPan.getZhuangPlayerId(), new LundaoMopai()));
		currentPan.updatePublicWaitingPlayerIdToZhuang();
	}

}
