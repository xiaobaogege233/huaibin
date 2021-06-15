package com.dml.majiang.player.action.peng;

import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.HuFirstException;
import com.dml.majiang.player.action.MajiangPlayerActionType;

/**
 * 胡优先的碰，最普遍的碰。在其他玩家有胡的机会的情况下不能碰。
 * 
 * @author Neo
 *
 */
public class HuFirstPengActionProcessor implements MajiangPlayerPengActionProcessor {

	@Override
	public void process(MajiangPengAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(action.getActionPlayerId());
		MajiangPlayer xiajia = currentPan.findXiajia(player);
		while (true) {
			if (!xiajia.getId().equals(player.getId())) {
				Set<MajiangPlayerActionType> actionTypesSet = xiajia.collectActionCandidatesType();
				if (actionTypesSet.contains(MajiangPlayerActionType.hu)) {
					throw new HuFirstException();
				}
			} else {
				break;
			}
			xiajia = currentPan.findXiajia(xiajia);
		}

		currentPan.playerPengPai(action.getActionPlayerId(), action.getDachupaiPlayerId(), action.getPai());
	}

}
