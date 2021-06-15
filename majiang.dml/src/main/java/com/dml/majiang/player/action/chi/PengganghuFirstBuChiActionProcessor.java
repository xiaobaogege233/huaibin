package com.dml.majiang.player.action.chi;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;

/**
 * 碰杠胡优先的吃，有碰杠胡的情况下不能吃，但要记录吃操作
 * 
 * @author lsc
 *
 */
public class PengganghuFirstBuChiActionProcessor implements MajiangPlayerChiActionProcessor {

	@Override
	public void process(MajiangChiAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();

		if (!action.isDisabledByHigherPriorityAction()) {// 没有被阻塞
			currentPan.playerChiPai(action.getActionPlayerId(), action.getDachupaiPlayerId(), action.getChijinPai(),
					action.getShunzi());
		}
	}

}
