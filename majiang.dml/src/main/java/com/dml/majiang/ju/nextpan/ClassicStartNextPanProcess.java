package com.dml.majiang.ju.nextpan;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.result.PanResult;

public class ClassicStartNextPanProcess implements StartNextPanProcess {

	@Override
	public void startNextPan(Ju ju) throws Exception {
		Pan nextPan = new Pan();
		nextPan.setNo(ju.countFinishedPan() + 1);
		PanResult latestFinishedPanResult = ju.findLatestFinishedPanResult();
		List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
		allPlayerIds.forEach(nextPan::addPlayer);
		ju.setCurrentPan(nextPan);

		// 开始定下一盘的门风
		ju.determinePlayersMenFengForNextPan();

		// 开始定下一盘庄家
		ju.determineZhuangForNextPan();

		// 开始填充可用的牌
		ju.fillAvaliablePai();

		// 开始定财神
//		ju.determineGuipai();

		// 开始定搬子和配子
		ju.determineBanziAndPeizi();

		// 开始发牌
		ju.faPai();

		// 庄家可以摸第一张牌
		ju.updateInitialAction();

		// 庄家摸第一张牌,进入正式行牌流程
		ju.action(ju.getCurrentPan().getZhuangPlayerId(), 1, 0, System.currentTimeMillis());
	}

}
