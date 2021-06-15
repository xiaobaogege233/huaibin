package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.avaliablepai.AvaliablePaiFiller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuaibinMajiangRandomAvaliablePaiFiller implements AvaliablePaiFiller {

    private long seed;
    private boolean daifeng; // 是否带东南西北风
    private boolean hongzhonglaizi; // 是否带红中发财白板

    /**
     * 填充牌
     * @param ju 当前局
     * @throws Exception
     */
    @Override
    public void fillAvaliablePai(Ju ju) throws Exception {
        // 获取万 筒 条 东南西北 红中发财白板这些牌
        MajiangPai[] allMajiangPaiArray = MajiangPai.xushupaiAndZipaiArray();
        // 创建集合装需要玩的牌类型
        List<MajiangPai> playPaiTypeList = new ArrayList<>();
        // 遍历牌类型
        for (MajiangPai pai : allMajiangPaiArray) {

            // 添加牌
            playPaiTypeList.add(pai);
        }

        // 创建整局游戏需要的牌集合
		List<MajiangPai> allPaiList = new ArrayList<>();
        // 遍历上面需要玩的牌类型
		playPaiTypeList.forEach((paiType) ->{
		    // 每张牌添4张
            for (int i = 0; i < 4; i++) {
                allPaiList.add(paiType);
            }
        });
        // 洗牌
		Collections.shuffle(allPaiList, new Random(seed + ju.countFinishedPan()));
		// 所有牌添加到当前盘去
		ju.getCurrentPan().setAvaliablePaiList(allPaiList);
		// 需要玩的牌类型添加到当前盘去
		ju.getCurrentPan().setPaiTypeList(playPaiTypeList);
	}

}
