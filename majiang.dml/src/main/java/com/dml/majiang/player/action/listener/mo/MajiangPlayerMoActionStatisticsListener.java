package com.dml.majiang.player.action.listener.mo;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.listener.MajiangPlayerActionStatisticsListener;
import com.dml.majiang.player.action.mo.MajiangMoAction;

public interface MajiangPlayerMoActionStatisticsListener extends MajiangPlayerActionStatisticsListener {
	public void update(MajiangMoAction moAction, Ju ju);
}
