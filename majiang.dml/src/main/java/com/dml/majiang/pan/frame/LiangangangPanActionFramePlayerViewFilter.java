package com.dml.majiang.pan.frame;

import com.dml.majiang.pai.valueobj.MajiangPaiValueObject;

/**
 * 暗杠要亮出来
 * 
 * @author Neo
 *
 */
public class LiangangangPanActionFramePlayerViewFilter implements PanActionFramePlayerViewFilter {

	@Override
	public void filter(PanActionFrame frame, String playerId) {

		PanValueObject pan = frame.getPanAfterAction();
		pan.getAvaliablePaiList().setPaiList(null);
		pan.getPlayerList().forEach((player) -> {
			if (player.getId().equals(playerId)) {// 是自己
				// 什么都不过滤，全要看
			} else {// 是其他玩家
				player.setActionCandidates(null);
				MajiangPaiValueObject gangmoShoupai = player.getGangmoShoupai();
				if (gangmoShoupai != null) {
					gangmoShoupai.setPai(null);
				}
				player.setFangruShoupaiList(null);
				player.setFangruGuipaiList(null);
				player.setHupaiCandidates(null);
				player.setKehuCandidates(null);
			}
		});
	}

}
