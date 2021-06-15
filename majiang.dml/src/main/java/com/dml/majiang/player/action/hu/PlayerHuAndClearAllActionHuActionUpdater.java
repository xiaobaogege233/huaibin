package com.dml.majiang.player.action.hu;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.fenzu.Kezi;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;

/**
 * 玩家胡了之后清除自身所有动作
 * 
 * @author lsc
 *
 */
public class PlayerHuAndClearAllActionHuActionUpdater implements MajiangPlayerHuActionUpdater {

	@Override
	public void updateActions(MajiangHuAction huAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer huPlayer = currentPan.findPlayerById(huAction.getActionPlayerId());
		huPlayer.clearActionCandidates();

		// 抢杠胡，删除被抢的杠
		if (huAction.getHu().isQianggang()) {

			MajiangPlayer dianpaoPlayer = currentPan.findPlayerById(huAction.getHu().getDianpaoPlayerId());
			List<GangchuPaiZu> gangchupaiZuList = dianpaoPlayer.getGangchupaiZuList();
			if (gangchupaiZuList.size()>0){
			    GangchuPaiZu gangChuPaiZu = gangchupaiZuList.remove(gangchupaiZuList.size() - 1);
                PengchuPaiZu pengChuPaiZu = new PengchuPaiZu(new Kezi(gangChuPaiZu.getGangzi().getPaiType()),
                        gangChuPaiZu.getDachuPlayerId(), dianpaoPlayer.getId());
                dianpaoPlayer.getPengchupaiZuList().add(pengChuPaiZu);}
//			GangchuPaiZu gangChuPaiZu = gangchupaiZuList.remove(gangchupaiZuList.size() - 1);
//			PengchuPaiZu pengChuPaiZu = new PengchuPaiZu(new Kezi(gangChuPaiZu.getGangzi().getPaiType()),
//					gangChuPaiZu.getDachuPlayerId(), dianpaoPlayer.getId());
//			dianpaoPlayer.getPengchupaiZuList().add(pengChuPaiZu);
		}
	}

}
