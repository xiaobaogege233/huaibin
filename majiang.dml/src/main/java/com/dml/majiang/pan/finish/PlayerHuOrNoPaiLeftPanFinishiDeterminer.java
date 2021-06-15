package com.dml.majiang.pan.finish;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;

/**
 * 最常见的，有人胡了就结束,或者牌打完就结束
 * 
 * @author Neo
 *
 */
public class PlayerHuOrNoPaiLeftPanFinishiDeterminer implements CurrentPanFinishiDeterminer {

	@Override
	public boolean determineToFinishCurrentPan(Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		return currentPan.anyPlayerHu();
		// TODO 还没处理牌打完的情况, 要判断牌没有了，且没人还有候选动作（最后一个动作）
	}

}
