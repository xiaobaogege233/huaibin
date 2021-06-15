package com.dml.majiang.player.action.listener.da;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.listener.MajiangPlayerActionStatisticsListener;

public interface MajiangPlayerDaActionStatisticsListener extends MajiangPlayerActionStatisticsListener {
	public void update(MajiangDaAction daAction, Ju ju);
}
