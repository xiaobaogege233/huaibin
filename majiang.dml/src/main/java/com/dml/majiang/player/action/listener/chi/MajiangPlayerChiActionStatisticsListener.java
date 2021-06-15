package com.dml.majiang.player.action.listener.chi;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.listener.MajiangPlayerActionStatisticsListener;

public interface MajiangPlayerChiActionStatisticsListener extends MajiangPlayerActionStatisticsListener {
	public void update(MajiangChiAction chiAction, Ju ju);
}
