package com.dml.majiang.player.action.listener.mo;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import lombok.Data;

/**
 * 统计摸了几张鬼牌
 * 
 * @author Neo
 *
 */
@Data
public class MoGuipaiCounter implements MajiangPlayerMoActionStatisticsListener {

	private int count = 0;

	@Override
	public void update(MajiangMoAction moAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(moAction.getActionPlayerId());
		if (player.gangmoGuipai()) {
			count++;
		}
	}

	@Override
	public void updateForNextPan() {
		count = 0;
	}


}
