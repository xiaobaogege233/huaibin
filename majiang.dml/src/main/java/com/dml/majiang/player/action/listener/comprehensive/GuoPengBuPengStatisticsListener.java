package com.dml.majiang.player.action.listener.comprehensive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.listener.guo.MajiangPlayerGuoActionStatisticsListener;
import com.dml.majiang.player.action.listener.mo.MajiangPlayerMoActionStatisticsListener;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import lombok.Data;

/**
 * 过碰不碰
 * 
 * @author lsc
 *
 */
@Data
public class GuoPengBuPengStatisticsListener
		implements MajiangPlayerGuoActionStatisticsListener, MajiangPlayerMoActionStatisticsListener {

	private Map<String, List<MajiangPai>> canNotPengPlayersPaiMap = new HashMap<>();

	@Override
	public void updateForNextPan() {
		canNotPengPlayersPaiMap = new HashMap<>();
	}

	@Override
	public void update(MajiangMoAction moAction, Ju ju) {
		if (canNotPengPlayersPaiMap.containsKey(moAction.getActionPlayerId())) {
			canNotPengPlayersPaiMap.remove(moAction.getActionPlayerId());
		}
	}

	@Override
	public void update(MajiangGuoAction guoAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();

		// 首先看一下,我过的是什么? 是我摸牌之后的胡,杠? 还是别人打出牌之后我可以吃碰杠胡
		PanActionFrame latestPanActionFrame = currentPan.findNotGuoLatestActionFrame();
		MajiangPlayerAction lastAction = latestPanActionFrame.getAction();
		if (lastAction.getType().equals(MajiangPlayerActionType.da)) {// 过的是别人打出牌之后我可以吃碰杠胡
			MajiangPai guoPai = ((MajiangDaAction) lastAction).getPai();
			MajiangPlayer player = currentPan.findPlayerById(guoAction.getActionPlayerId());
			Map<Integer, MajiangPlayerAction> actionCandidates = player.getActionCandidates();
			for (int i = 1; i <= actionCandidates.size(); i++) {
				MajiangPlayerAction action = actionCandidates.get(i);
				if (action.getType().equals(MajiangPlayerActionType.peng)) {// 如果能碰
					List<MajiangPai> paiList = canNotPengPlayersPaiMap.get(guoAction.getActionPlayerId());
					if (paiList == null) {
						paiList = new ArrayList<>();
					}
					paiList.add(guoPai);
					canNotPengPlayersPaiMap.put(guoAction.getActionPlayerId(), paiList);
				}
			}
		}
	}

}
