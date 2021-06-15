package com.dml.majiang.player.action.listener.peng;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.listener.MajiangPlayerActionStatisticsListener;
import com.dml.majiang.player.action.peng.MajiangPengAction;

public interface MajiangPlayerPengActionStatisticsListener extends MajiangPlayerActionStatisticsListener {
	public void update(MajiangPengAction pengAction, Ju ju);
}
