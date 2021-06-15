package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.firstpan.StartFirstPanProcess;
import com.dml.majiang.player.MajiangPlayer;
import lombok.Data;

import java.util.List;

@Data
public class HuaibinMajiangStartFirstPanProcess implements StartFirstPanProcess {

    private OptionalPlay optionalPlay;

	@Override
	public void startFirstPan(Ju ju, List<String> allPlayerIds) throws Exception {

		// 开始定第一盘的门风 也就是位置
		ju.determinePlayersMenFengForFirstPan();

		// 开始定第一盘庄家
		ju.determineZhuangForFirstPan();

		// 开始填充可用的牌
		ju.fillAvaliablePai();

        // 开始发牌
        ju.faPai();

        // 庄家可以摸第一张牌
        ju.updateInitialAction();

		// 遍历每个玩家
		for (MajiangPlayer majiangPlayer : ju.getCurrentPan().getMajiangPlayerIdMajiangPlayerMap().values()) {
			// 通过计算缺门给每个玩家设置自己的缺门
			majiangPlayer.setSuggestQuemen(majiangPlayer.calculateQuemen());
		}
		// TODO 亮风
		ju.determineLiangFeng();

		// 天杠
		// ju.determineTianGang();

		// 庄家摸第一张牌,进入正式行牌流程
        ju.action(ju.getCurrentPan().getZhuangPlayerId(), 1, 0, System.currentTimeMillis());
	}

}
