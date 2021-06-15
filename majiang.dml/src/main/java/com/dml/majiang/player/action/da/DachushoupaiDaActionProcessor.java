package com.dml.majiang.player.action.da;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;

/**
 * 最常见的打出手牌
 * 
 * @author Neo
 *
 */
public class DachushoupaiDaActionProcessor implements MajiangPlayerDaActionProcessor {

	@Override
	public void process(MajiangDaAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.playerDaChuPai(action.getActionPlayerId(), action.getPai());
	}

}
