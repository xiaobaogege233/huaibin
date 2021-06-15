package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.nextpan.StartNextPanProcess;
import lombok.Data;

/**
 * 开始下一盘流程
 */
@Data
public class HuaibinMajiangStartNextPanProcess implements StartNextPanProcess {

    private OptionalPlay optionalPlay; // 可选玩法
	@Override
	public void startNextPan(Ju ju) throws Exception {

		// 开始定下一盘的门风
		ju.determinePlayersMenFengForNextPan();

		// 开始定下一盘庄家
		ju.determineZhuangForNextPan();

		// 开始填充可用的牌
		ju.fillAvaliablePai();

        // ju.determineGuipai();

        // 开始发牌
        ju.faPai();

        // TODO 决定亮风
        ju.determineLiangFeng();

        // 庄家可以摸第一张牌
        ju.updateInitialAction();

        // 庄家摸第一张牌,进入正式行牌流程
        ju.action(ju.getCurrentPan().getZhuangPlayerId(), 1, 0, System.currentTimeMillis());

	}

}
