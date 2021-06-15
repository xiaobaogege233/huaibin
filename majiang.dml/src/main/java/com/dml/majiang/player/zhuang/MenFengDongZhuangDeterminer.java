package com.dml.majiang.player.zhuang;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.position.MajiangPosition;

/**
 * 门风位置为东的玩家直接为庄
 */
public class MenFengDongZhuangDeterminer implements ZhuangDeterminer {

	@Override
	public void determineZhuang(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		String dongPlayerId = currentPan.playerIdForMenFeng(MajiangPosition.dong);
		currentPan.setZhuangPlayerId(dongPlayerId);
	}
}
