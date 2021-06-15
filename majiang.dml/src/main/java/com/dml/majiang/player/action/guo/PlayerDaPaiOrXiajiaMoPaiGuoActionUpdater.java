package com.dml.majiang.player.action.guo;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;

/**
 * 我打牌或者下家摸牌。最常见的'过'后的逻辑
 * 
 * @author neo
 *
 */
public class PlayerDaPaiOrXiajiaMoPaiGuoActionUpdater implements MajiangPlayerGuoActionUpdater {

	@Override
	public void updateActions(MajiangGuoAction guoAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		currentPan.playerClearActionCandidates(guoAction.getActionPlayerId());
		MajiangPlayer player = currentPan.findPlayerById(guoAction.getActionPlayerId());

		// 首先看一下,我过的是什么? 是我摸牌之后的胡,杠? 还是别人打出牌之后我可以吃碰杠胡
		PanActionFrame latestPanActionFrame = currentPan.findLatestActionFrame();
		MajiangPlayerAction action = latestPanActionFrame.getAction();
		if (action.getType().equals(MajiangPlayerActionType.mo)) {// 过的是我摸牌之后的胡,杠
			// 那要我打牌
			player.generateDaActions();
		} else if (action.getType().equals(MajiangPlayerActionType.da)) {// 过的是别人打出牌之后我可以吃碰杠胡
			if (currentPan.allPlayerHasNoActionCandidates()) {// 如果所有玩家啥也干不了
				// 打牌那家的下家摸牌 TODO 还没处理牌摸完
				// 打牌那家的下家摸牌
				MajiangPlayer xiajiaPlayer = currentPan
						.findXiajia(currentPan.findPlayerById(action.getActionPlayerId()));
				xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
			}
		} else {
		}
	}

}
