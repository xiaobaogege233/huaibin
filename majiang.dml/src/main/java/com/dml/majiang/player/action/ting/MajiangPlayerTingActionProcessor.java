package com.dml.majiang.player.action.ting;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.hu.MajiangHuAction;

public interface MajiangPlayerTingActionProcessor {
	public void process(MajiangTingAction action, Ju ju) throws Exception;
}
