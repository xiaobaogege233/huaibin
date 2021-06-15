package com.dml.majiang.player.action.gang;

import com.dml.majiang.ju.Ju;

public interface MajiangPlayerGangActionProcessor {
	public void process(MajiangGangAction action, Ju ju) throws Exception;
}
