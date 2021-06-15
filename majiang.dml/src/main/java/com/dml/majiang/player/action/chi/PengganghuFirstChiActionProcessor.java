package com.dml.majiang.player.action.chi;

import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerActionType;

/**
 * 碰杠胡优先的吃，最普遍的吃。在其他玩家有碰杠胡的机会的情况下不能吃。
 * 
 * @author Neo
 *
 */
public class PengganghuFirstChiActionProcessor implements MajiangPlayerChiActionProcessor {

	@Override
	public void process(MajiangChiAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(action.getActionPlayerId());
		MajiangPlayer xiajia = currentPan.findXiajia(player);
		while (true) {
			if (!xiajia.getId().equals(player.getId())) {
				Set<MajiangPlayerActionType> actionTypesSet = xiajia.collectActionCandidatesType();
				if (actionTypesSet.contains(MajiangPlayerActionType.peng)
						|| actionTypesSet.contains(MajiangPlayerActionType.gang)
						|| actionTypesSet.contains(MajiangPlayerActionType.hu)) {
					throw new PengganghuFirstException();
				}
			} else {
				break;
			}
			xiajia = currentPan.findXiajia(xiajia);
		}

		if (!action.isDisabledByHigherPriorityAction()) {// 没有被阻塞
			currentPan.playerChiPai(action.getActionPlayerId(), action.getDachupaiPlayerId(), action.getChijinPai(), action.getShunzi());
		}
	}

}
