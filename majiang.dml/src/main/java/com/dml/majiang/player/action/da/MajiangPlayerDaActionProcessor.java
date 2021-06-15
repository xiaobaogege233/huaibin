package com.dml.majiang.player.action.da;

import com.dml.majiang.ju.Ju;

public interface MajiangPlayerDaActionProcessor {
	public void process(MajiangDaAction action, Ju ju) throws Exception;
}
