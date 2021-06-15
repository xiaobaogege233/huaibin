package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.finish.CurrentPanFinishiDeterminer;

/**
 * 盘结束
 */
public class HuaibinMajiangPanFinishiDeterminer implements CurrentPanFinishiDeterminer {

    /**
     * 决定是否结束当前盘
     * @param ju 当前局
     * @return true 结束 false 不结束
     */
	@Override
	public boolean determineToFinishCurrentPan(Ju ju) {

        Pan currentPan = ju.getCurrentPan();

        boolean hu = currentPan.anyPlayerHu();
        // 有人胡
        if (hu && currentPan.allPlayerHasNoHuActionCandidates()) {
            return true;
        } else {
            // 定义一个没牌摸时的 剩下牌张数
            int liupai = 0;
            // 获取当前盘可用牌张数
            int avaliablePaiLeft = currentPan.countAvaliablePai();
            //
            if (avaliablePaiLeft <= liupai && currentPan.allPlayerHasNoHuActionCandidates()) {
                return true;
            } else {
                return false;
            }
        }
	}

}
