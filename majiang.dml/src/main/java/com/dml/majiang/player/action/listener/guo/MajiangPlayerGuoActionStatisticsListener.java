package com.dml.majiang.player.action.listener.guo;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.listener.MajiangPlayerActionStatisticsListener;

public interface MajiangPlayerGuoActionStatisticsListener extends MajiangPlayerActionStatisticsListener {
	public void update(MajiangGuoAction guoAction, Ju ju);
}
