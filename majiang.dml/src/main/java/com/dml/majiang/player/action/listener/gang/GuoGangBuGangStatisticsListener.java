package com.dml.majiang.player.action.listener.gang;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.listener.guo.MajiangPlayerGuoActionStatisticsListener;

public class GuoGangBuGangStatisticsListener implements MajiangPlayerGuoActionStatisticsListener {

	private Set<String> canNotGangPlayers = new HashSet<>();

	@Override
	public void updateForNextPan() {
		canNotGangPlayers = new HashSet<>();

	}

	@Override
	public void update(MajiangGuoAction guoAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();

		// 首先看一下,我过的是什么? 是我摸牌之后的胡,杠? 还是别人打出牌之后我可以吃碰杠胡
		PanActionFrame latestPanActionFrame = currentPan.findNotGuoLatestActionFrame();
		MajiangPlayerAction lastAction = latestPanActionFrame.getAction();
		if (lastAction.getType().equals(MajiangPlayerActionType.da)) {// 过的是别人打出牌之后我可以吃碰杠胡
			MajiangPlayer player = currentPan.findPlayerById(guoAction.getActionPlayerId());
			Map<Integer, MajiangPlayerAction> actionCandidates = player.getActionCandidates();
			for (int i = 1; i <= actionCandidates.size(); i++) {
				MajiangPlayerAction action = actionCandidates.get(i);
				if (action.getType().equals(MajiangPlayerActionType.gang)) {// 如果能杠
					canNotGangPlayers.add(guoAction.getActionPlayerId());
				}
			}
		}
	}

	public Set<String> getCanNotGangPlayers() {
		return canNotGangPlayers;
	}

	public void setCanNotGangPlayers(Set<String> canNotGangPlayers) {
		this.canNotGangPlayers = canNotGangPlayers;
	}

}
