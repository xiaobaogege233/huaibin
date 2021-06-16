package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.listener.HuaibinMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.player.action.peng.MajiangPlayerPengActionUpdater;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;

import java.util.List;

public class HuaibinMajiangPengActionUpdater implements MajiangPlayerPengActionUpdater {


	/**
	 * 碰动作过后
	 * @param pengAction
	 * @param ju
	 * @throws Exception
	 */
	@Override
	public void updateActions(MajiangPengAction pengAction, Ju ju) throws Exception {

		// TODO 一碰砸到的情况可能在这里做

		HuaibinMajiangPengGangActionStatisticsListener juezhangStatisticsListener = ju.getActionStatisticsListenerManager().findListener(HuaibinMajiangPengGangActionStatisticsListener.class);
		Pan currentPan = ju.getCurrentPan();
		HuaibinMajiangPanResultBuilder huaibinMajiangPanResultBuilder = (HuaibinMajiangPanResultBuilder) ju.getCurrentPanResultBuilder();
		OptionalPlay optionalPlay = huaibinMajiangPanResultBuilder.getOptionalPlay();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());

		if (pengAction.isDisabledByHigherPriorityAction()) { // 如果碰动作被阻塞 说明别人可能有胡 也有可能牌是最后一张打的牌
			player.clearActionCandidates();// 玩家删除碰动作

			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 所有玩家行牌结束，并且没人胡
				MajiangPlayerAction finallyDoneAction = juezhangStatisticsListener.findPlayerFinallyDoneAction();// 找出最终应该执行的动作
				MajiangPlayer actionPlayer = currentPan.findPlayerById(finallyDoneAction.getActionPlayerId());
				if (finallyDoneAction instanceof MajiangPengAction) {// 如果是碰，也只能是碰
					MajiangPengAction action = (MajiangPengAction) finallyDoneAction;
					actionPlayer.addActionCandidate(new MajiangPengAction(action.getActionPlayerId(), action.getDachupaiPlayerId(), action.getPai()));
				}
				juezhangStatisticsListener.updateForNextLun();// 清空动作缓存
			}
		} else {
			currentPan.clearAllPlayersActionCandidates();
			juezhangStatisticsListener.updateForNextLun();// 清空动作缓存
			// 刻子杠手牌
			List<PengchuPaiZu> pengchupaiZuList = player.getPengchupaiZuList();
			PengchuPaiZu pengchuPaiZu = pengchupaiZuList.get(pengchupaiZuList.size() - 1);
			for (MajiangPai fangruShoupai : player.getFangruShoupaiList()) {
				if (pengchuPaiZu.getKezi().getPaiType().equals(fangruShoupai)) {
					player.addActionCandidate(new MajiangGangAction(pengAction.getActionPlayerId(),
							pengAction.getDachupaiPlayerId(), fangruShoupai, GangType.kezigangshoupai));
					break;
				}
			}
            if (!player.getActionCandidates().isEmpty()){
                player.addActionCandidate(new MajiangGuoAction(player.getId()));
            }
            if (player.getActionCandidates().isEmpty()) {
			    player.generateDaGuipaiActions();
			}
		}
	}


}
