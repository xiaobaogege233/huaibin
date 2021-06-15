package com.anbang.qipai.huaibinmajiang.cqrs.c.domain.listener;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.listener.gang.MajiangPlayerGangActionStatisticsListener;
import com.dml.majiang.player.action.listener.peng.MajiangPlayerPengActionStatisticsListener;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 淮滨麻将统计器，包括绝张统计、吃碰杠同时出现时记录动作、总杠数统计
 * 
 * @author lsc
 *
 */
@Data
public class HuaibinMajiangPengGangActionStatisticsListener
		implements  MajiangPlayerPengActionStatisticsListener,
        MajiangPlayerGangActionStatisticsListener {

	private Map<String, MajiangPlayerAction> playerActionMap = new HashMap<>();


    private Map<String, Integer> playerIdFangGangShuMap = new HashMap<>(); // 玩家放杠数集合  《玩家ID ：放杠次数》


    @Override
	public void updateForNextPan() {
		playerActionMap = new HashMap<>();
        playerIdFangGangShuMap = new HashMap<>();
	}

	// 清空当前轮动作
	public void updateForNextLun() {
		playerActionMap.clear();
	}

	@Override
	public void update(MajiangGangAction gangAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(gangAction.getActionPlayerId());
		if (gangAction.isDisabledByHigherPriorityAction()) {// 如果被阻塞
			playerActionMap.put(player.getId(), gangAction);// 记录下被阻塞的动作
		} else {
            if (gangAction.getGangType().equals(GangType.gangdachu)) { //杠打出
                String dachupaiPlayerId = gangAction.getDachupaiPlayerId();
                if (playerIdFangGangShuMap.containsKey(dachupaiPlayerId)) {
                    Integer count = playerIdFangGangShuMap.get(dachupaiPlayerId) + 1;
                    playerIdFangGangShuMap.put(dachupaiPlayerId, count);
                } else {
                    playerIdFangGangShuMap.put(dachupaiPlayerId, 1);
                }
            }
//            else if (gangAction.getGangType().equals(GangType.kezigangshoupai) || gangAction.getGangType().equals(GangType.kezigangmo)) { //刻子杠手牌
//                String dachupaiPlayerId = gangAction.getDachupaiPlayerId();
//                if (dachupaiPlayerId != null) {
//                    if (playerIdFangGangShuMap.containsKey(dachupaiPlayerId)) {
//                        Integer count = playerIdFangGangShuMap.get(dachupaiPlayerId) + 1;
//                        playerIdFangGangShuMap.put(dachupaiPlayerId, count);
//                    } else {
//                        playerIdFangGangShuMap.put(dachupaiPlayerId, 1);
//                    }
//                }
//            }
		}
	}

	@Override
	public void update(MajiangPengAction pengAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());
		if (pengAction.isDisabledByHigherPriorityAction()) {// 如果被阻塞
			playerActionMap.put(player.getId(), pengAction);// 记录下被阻塞的动作
		}
	}


	public MajiangPlayerAction findPlayerFinallyDoneAction() {
		if (playerActionMap.isEmpty()) {
			return null;
		}
		for (MajiangPlayerAction action : playerActionMap.values()) {
			if (action.getType().equals(MajiangPlayerActionType.gang)) {
				return action;
			}
		}
		for (MajiangPlayerAction action : playerActionMap.values()) {
			if (action.getType().equals(MajiangPlayerActionType.peng)) {
				return action;
			}
		}
		for (MajiangPlayerAction action : playerActionMap.values()) {
			if (action.getType().equals(MajiangPlayerActionType.chi)) {
				return action;
			}
		}
		return null;
	}

}
