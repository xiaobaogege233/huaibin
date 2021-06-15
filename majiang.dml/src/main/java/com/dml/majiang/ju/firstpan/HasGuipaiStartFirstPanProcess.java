package com.dml.majiang.ju.firstpan;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;

import java.util.List;

public class HasGuipaiStartFirstPanProcess implements StartFirstPanProcess {

    @Override
    public void startFirstPan(Ju ju, List<String> allPlayerIds) throws Exception {
        Pan firstPan = new Pan();
        firstPan.setNo(1);
        allPlayerIds.forEach(firstPan::addPlayer);
        ju.setCurrentPan(firstPan);

        // 开始定第一盘的门风
        ju.determinePlayersMenFengForFirstPan();

        // 开始定第一盘庄家
        ju.determineZhuangForFirstPan();

        // 开始填充可用的牌
        ju.fillAvaliablePai();

        // 开始定财神
		ju.determineGuipai();

        // 开始发牌
        ju.faPai();

        // 庄家可以摸第一张牌
        ju.updateInitialAction();

        // 庄家摸第一张牌,进入正式行牌流程
        ju.action(ju.getCurrentPan().getZhuangPlayerId(), 1, 0, System.currentTimeMillis());
    }

}
