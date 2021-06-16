package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.listener.HuaibinMajiangPengGangActionStatisticsListener;
import com.anbang.qipai.huaibinmajiang.utils.SpringUtil;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.guo.MajiangPlayerGuoActionUpdater;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.TuoguanMopai;
import com.dml.majiang.player.action.peng.MajiangPengAction;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HuaibinMajiangGuoActionUpdater implements MajiangPlayerGuoActionUpdater {

	@Override
	public void updateActions(MajiangGuoAction guoAction, Ju ju) {

		Pan currentPan = ju.getCurrentPan();
		currentPan.playerClearActionCandidates(guoAction.getActionPlayerId());
		HuaibinMajiangPanResultBuilder huaibinMajiangPanResultBuilder = (HuaibinMajiangPanResultBuilder) ju.getCurrentPanResultBuilder();
		OptionalPlay optionalPlay = huaibinMajiangPanResultBuilder.getOptionalPlay();

		MajiangPlayer player = currentPan.findPlayerById(guoAction.getActionPlayerId());


		// 首先看一下,我过的是什么? 是我摸牌之后的胡,杠? 还是别人打出牌之后我可以吃碰杠胡
		PanActionFrame latestPanActionFrame = currentPan.findNotGuoLatestActionFrame();
		MajiangPlayerAction action = latestPanActionFrame.getAction();
		if (action.getType().equals(MajiangPlayerActionType.mo)) {// 过的是我摸牌之后的胡,杠
		    // 那要我打牌
            if (player.getActionCandidates().isEmpty()) {
                player.generateDaGuipaiActions();
            }
		} else if (action.getType().equals(MajiangPlayerActionType.da)) {// 过的是别人打出牌之后我可以吃碰杠胡
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 如果所有玩家啥也干不了
				HuaibinMajiangPengGangActionStatisticsListener chiPengGangRecordListener = ju.getActionStatisticsListenerManager().findListener(HuaibinMajiangPengGangActionStatisticsListener.class);
				MajiangPlayerAction finallyDoneAction = chiPengGangRecordListener.findPlayerFinallyDoneAction();
				if (finallyDoneAction != null) {// 有其他吃碰杠动作，先执行吃碰杠
					MajiangPlayer actionPlayer = currentPan.findPlayerById(finallyDoneAction.getActionPlayerId());
					if (finallyDoneAction instanceof MajiangPengAction) {// 如果是碰
						MajiangPengAction doAction = (MajiangPengAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangPengAction(doAction.getActionPlayerId(), doAction.getDachupaiPlayerId(), doAction.getPai()));
					} else if (finallyDoneAction instanceof MajiangGangAction) {// 如果是杠
						MajiangGangAction doAction = (MajiangGangAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangGangAction(doAction.getActionPlayerId(), doAction.getDachupaiPlayerId(), doAction.getPai(), doAction.getGangType()));
					}
				} else {
					// 打牌那家的下家摸牌
					MajiangPlayer xiajiaPlayer = currentPan.findXiajia(currentPan.findPlayerById(action.getActionPlayerId()));
					xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
					Map<String, String> depositPlayerList = ju.getDepositPlayerList();
					String gameId = depositPlayerList.get(xiajiaPlayer.getId());
					if (depositPlayerList.containsKey(xiajiaPlayer.getId())&& !automatic.isPlayerOnLine(xiajiaPlayer.getId())) {
						executorService.submit(() -> {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							autoMopai(xiajiaPlayer, depositPlayerList);
						});
					}
				}
				chiPengGangRecordListener.updateForNextLun();// 清空动作缓存
			}
		} else if (action.getType().equals(MajiangPlayerActionType.gang)) {// 过的是别人杠牌之后我可以胡
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 如果所有玩家啥也干不了
				// 杠牌那家摸牌
				MajiangPlayer gangPlayer = currentPan.findPlayerById(action.getActionPlayerId());
				gangPlayer.addActionCandidate(new MajiangMoAction(gangPlayer.getId(), new LundaoMopai()));
			}
		} else if (action.getType().equals(MajiangPlayerActionType.peng)) {// 过的是我碰了之后的杠
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 如果所有玩家啥也干不了
				// 那要我打牌
				if (player.getActionCandidates().isEmpty()) {
					player.generateDaGuipaiActions();
				}
			}
		}
	}

	private final Automatic automatic = SpringUtil.getBean(Automatic.class);
	private final ExecutorService executorService = Executors.newCachedThreadPool();

	/**
	 * 自动摸牌
	 *
	 * @param xiajia            下家玩家
	 * @param depositPlayerList 托管玩家列表
	 */
	public void autoMopai(MajiangPlayer xiajia, Map<String, String> depositPlayerList) {
		xiajia.clearActionCandidates();
		xiajia.addActionCandidate(new MajiangMoAction(xiajia.getId(), new TuoguanMopai()));
		String gameId = depositPlayerList.get(xiajia.getId());
		automatic.automaticAction(xiajia.getId(), 1, gameId);
	}

}
