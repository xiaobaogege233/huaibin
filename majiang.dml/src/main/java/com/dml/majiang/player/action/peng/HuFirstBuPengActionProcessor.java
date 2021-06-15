package com.dml.majiang.player.action.peng;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;

/**
 * 胡优先的碰，有胡的情况下不能碰，但要记录碰操作
 * 
 * @author lsc
 *
 */
public class HuFirstBuPengActionProcessor implements MajiangPlayerPengActionProcessor {

	@Override
	public void process(MajiangPengAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();

		if (!action.isDisabledByHigherPriorityAction()) {// 没有被阻塞
			currentPan.playerPengPai(action.getActionPlayerId(), action.getDachupaiPlayerId(), action.getPai());
		}
	}

}
