package com.dml.majiang.player.action.gang;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.pan.Pan;

/**
 * 胡优先的杠，有胡的情况下不能杠，但要记录杠操作
 *
 * @author lsc
 *
 */
public class HuFirstBuGangActionProcessor implements MajiangPlayerGangActionProcessor {

	@Override
	public void process(MajiangGangAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();

		if (!action.isDisabledByHigherPriorityAction()) {// 没有被阻塞
			GangType gangType = action.getGangType();
			if (gangType.equals(GangType.gangdachu)) {
				currentPan.playerGangDachupai(action.getActionPlayerId(), action.getDachupaiPlayerId(), action.getPai());
			} else if (gangType.equals(GangType.shoupaigangmo)) {
				currentPan.playerShoupaiGangMo(action.getActionPlayerId(), action.getPai());
			} else if (gangType.equals(GangType.gangsigeshoupai)) {
				currentPan.playerGangSigeshoupai(action.getActionPlayerId(), action.getPai());
			} else if (gangType.equals(GangType.kezigangmo)) {
				currentPan.playerKeziGangMo(action.getActionPlayerId(), action.getPai());
			} else if (gangType.equals(GangType.kezigangshoupai)) {
				currentPan.playerKeziGangShoupai(action.getActionPlayerId(), action.getPai());
			} else if (gangType.equals(GangType.tiangang)) {
				currentPan.playerGangSigeshoupai(action.getActionPlayerId(), action.getPai());
			}else {
			}
		}
	}

}
