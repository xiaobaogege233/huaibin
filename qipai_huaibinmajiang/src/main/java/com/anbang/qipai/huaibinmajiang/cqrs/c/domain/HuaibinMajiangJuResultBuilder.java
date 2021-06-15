package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.result.JuResult;
import com.dml.majiang.ju.result.JuResultBuilder;
import com.dml.majiang.pan.result.PanResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 局结果构建
 */
public class HuaibinMajiangJuResultBuilder implements JuResultBuilder {

	@Override
	public JuResult buildJuResult(Ju ju) {
		HuaibinMajiangJuResult huaibinMajiangJuResult = new HuaibinMajiangJuResult();
		huaibinMajiangJuResult.setFinishedPanCount(ju.countFinishedPan());
		// 判断是否为第一把
		if (ju.countFinishedPan() > 0) { // 不是
			// 获取玩家局结果集
			Map<String, HuaibinMajiangJuPlayerResult> juPlayerResultMap = new HashMap<>();
			// 遍历每一把
			for (PanResult panResult : ju.getFinishedPanResultList()) {
				HuaibinMajiangPanResult huaibinMajiangPanResult = (HuaibinMajiangPanResult) panResult;
				// 遍历盘玩家结果集
				for (HuaibinMajiangPanPlayerResult panPlayerResult : huaibinMajiangPanResult.getPanPlayerResultList()) {
					// 通过ID获取局玩家结果
					HuaibinMajiangJuPlayerResult juPlayerResult = juPlayerResultMap.get(panPlayerResult.getPlayerId());
					// 判断是否为空
					if (juPlayerResult == null) { // 是
						// 新建对象
						juPlayerResult = new HuaibinMajiangJuPlayerResult();
						juPlayerResult.setPlayerId(panPlayerResult.getPlayerId());
						juPlayerResultMap.put(panPlayerResult.getPlayerId(), juPlayerResult);
					}
					// 判断该玩家是否胡
                    if (huaibinMajiangPanResult.ifPlayerHu(panPlayerResult.getPlayerId())) {
                    	// 是 则累计胡次数
                        juPlayerResult.increaseHuCount();
                    }
                    // 累计财神次数
                    juPlayerResult.increaseCaishenCount(huaibinMajiangPanResult.playerGuipaiCount(panPlayerResult.getPlayerId()));
                    // 判断是否自摸
                    if (huaibinMajiangPanResult.ifPlayerHu(panPlayerResult.getPlayerId()) && huaibinMajiangPanResult.isZimo()) {
                    	// 是 则累计自摸次数
                        juPlayerResult.increaseZiMoCount();
                    }
                    // 获取点炮玩家ID
                    String dianPaoPlayerId = huaibinMajiangPanResult.getDianpaoPlayerId();
                    // 判断是否点炮
                    if (dianPaoPlayerId != null && dianPaoPlayerId.equals(panPlayerResult.getPlayerId())) {
                    	// 是 则累计放炮次数
                        juPlayerResult.increaseFangPaoCount();
                    }
                    // 设置总分
                    juPlayerResult.setTotalScore(panPlayerResult.getTotalScore());
				}
			}


			HuaibinMajiangJuPlayerResult dayingjia = null;
			HuaibinMajiangJuPlayerResult datuhao = null;
			// 遍历所有局玩家结果
			for (HuaibinMajiangJuPlayerResult juPlayerResult : juPlayerResultMap.values()) {
				// 设置大赢家和大土豪
				if (dayingjia == null) {
					dayingjia = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() > dayingjia.getTotalScore()) {
						dayingjia = juPlayerResult;
					}
				}

				if (datuhao == null) {
					datuhao = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() < datuhao.getTotalScore()) {
						datuhao = juPlayerResult;
					}
				}
			}
			huaibinMajiangJuResult.setDatuhaoId(datuhao.getPlayerId());
			huaibinMajiangJuResult.setDayingjiaId(dayingjia.getPlayerId());
			huaibinMajiangJuResult.setPlayerResultList(new ArrayList<>(juPlayerResultMap.values()));
		}
		return huaibinMajiangJuResult;
	}

}
