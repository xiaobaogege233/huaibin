package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionProcessor;

/**
 * 摸动作流程
 */
public class HuaibinMajiangMoActionProcessor implements MajiangPlayerMoActionProcessor {

	@Override
	public void process(MajiangMoAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
        MajiangPlayer player = currentPan.findPlayerById(action.getActionPlayerId());

        if (action.getReason().getName().equals(HuaibinMajiangBupai.name)) {
            player.fangruPublicPai();
        }
		currentPan.playerMoPai(action.getActionPlayerId());

	}

}
