package com.dml.majiang.player.action.listener.gang;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.listener.MajiangPlayerActionStatisticsListener;

public interface MajiangPlayerGangActionStatisticsListener extends MajiangPlayerActionStatisticsListener {
	public void update(MajiangGangAction gangAction, Ju ju);
}
