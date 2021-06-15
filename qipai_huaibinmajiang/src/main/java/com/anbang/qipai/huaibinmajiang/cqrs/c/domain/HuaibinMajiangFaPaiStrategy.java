package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.fapai.FaPaiStrategy;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.position.MajiangPosition;
import com.dml.majiang.position.MajiangPositionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 顺序发牌。
 *
 * @author Neo
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuaibinMajiangFaPaiStrategy implements FaPaiStrategy {

	private int faPaiCountsForOnePlayer; // 一个玩家需要发多少张牌的统计

	/**
	 * 发牌
	 * @param ju 当前局
	 * @throws Exception
	 */
	@Override
	public void faPai(Ju ju) throws Exception {
		// 获取当前盘
		Pan currentPan = ju.getCurrentPan();
		// 获取所有可用牌
		List<MajiangPai> avaliablePaiList = currentPan.getAvaliablePaiList();
		// 获取庄家位置
		MajiangPosition zhuangPlayerMenFeng = currentPan.findMenFengForZhuang();
		//
		for (int i = 0; i < faPaiCountsForOnePlayer; i++) {
			MajiangPosition playerMenFeng = zhuangPlayerMenFeng;
			// 遍历4个位置 玩家可能也不一定有四个人
			for (int j = 0; j < 4; j++) {
				// 获取当前位置的玩家
				MajiangPlayer player = currentPan.findPlayerByMenFeng(playerMenFeng);
				// 判断玩家是否存在
				if (player != null) { // 存在
					// 发牌
					faPai(avaliablePaiList, player);
				}
				// 不存在 逆时针找到下个玩家位置
				playerMenFeng = MajiangPositionUtil.nextPositionAntiClockwise(playerMenFeng);
			}


		}
	}

	private void faPai(List<MajiangPai> avaliablePaiList, MajiangPlayer player) {
		// 移除一张牌
		MajiangPai pai = avaliablePaiList.remove(0);
		//
        if (pai.ordinal()>=MajiangPai.chun.ordinal()) {
            player.addPublicPai(pai);
            faPai(avaliablePaiList, player);
        } else {
        	// 添加到该玩家手牌上去
            player.addShoupai(pai);
        }
	}


}
