package com.dml.majiang.player.action.listener.gang;

import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.player.action.gang.MajiangGangAction;

/**
 * 统计玩家放杠数
 * 
 * @author lsc
 *
 */
public class FangGangCounter implements MajiangPlayerGangActionStatisticsListener {

	private Map<String, Integer> playerIdFangGangShuMap = new HashMap<>();

	@Override
	public void updateForNextPan() {
		playerIdFangGangShuMap = new HashMap<>();
	}

	@Override
	public void update(MajiangGangAction gangAction, Ju ju) {
		if (gangAction.getGangType().equals(GangType.gangdachu)) {
			String dachupaiPlayerId = gangAction.getDachupaiPlayerId();
			if (playerIdFangGangShuMap.containsKey(dachupaiPlayerId)) {
				Integer count = playerIdFangGangShuMap.get(dachupaiPlayerId) + 1;
				playerIdFangGangShuMap.put(dachupaiPlayerId, count);
			} else {
				playerIdFangGangShuMap.put(dachupaiPlayerId, 1);
			}
		}
	}

	public Map<String, Integer> getPlayerIdFangGangShuMap() {
		return playerIdFangGangShuMap;
	}

	public void setPlayerIdFangGangShuMap(Map<String, Integer> playerIdFangGangShuMap) {
		this.playerIdFangGangShuMap = playerIdFangGangShuMap;
	}

}
