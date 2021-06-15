package com.dml.majiang.player.action.listener.comprehensive;

import java.util.Arrays;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.listener.da.MajiangPlayerDaActionStatisticsListener;
import com.dml.majiang.player.action.listener.peng.MajiangPlayerPengActionStatisticsListener;
import com.dml.majiang.player.action.peng.MajiangPengAction;

/**
 * 某种牌是否绝张了（明了3张牌）
 * 
 * @author Neo
 *
 */
public class JuezhangStatisticsListener
		implements MajiangPlayerPengActionStatisticsListener, MajiangPlayerDaActionStatisticsListener {

	private int[] mingpaiCountArray = new int[MajiangPai.values().length];

	@Override
	public void update(MajiangDaAction daAction, Ju ju) {
		mingpaiCountArray[daAction.getPai().ordinal()]++;
	}

	@Override
	public void update(MajiangPengAction pengAction, Ju ju) {
		mingpaiCountArray[pengAction.getPai().ordinal()] += 2;
	}

	@Override
	public void updateForNextPan() {
		Arrays.fill(mingpaiCountArray, 0);
	}

	public boolean ifJuezhang(MajiangPai pai) {
		return mingpaiCountArray[pai.ordinal()] == 3;
	}

	public boolean ifMingPai(MajiangPai pai) {
		return mingpaiCountArray[pai.ordinal()] > 0;
	}
}
