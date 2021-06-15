package com.dml.majiang.player.shoupai.gouxing;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.Danpai;
import com.dml.majiang.pai.fenzu.Duizi;
import com.dml.majiang.pai.fenzu.Gangzi;
import com.dml.majiang.pai.fenzu.Kezi;
import com.dml.majiang.pai.fenzu.Shunzi;
import com.dml.majiang.player.shoupai.PaiXing;

/**
 * 独立牌组构型
 * 
 * @author Neo
 *
 */
public class DuLiPaiZuGouXing extends GouXing {

	@Override
	public List<PaiXing> calculateAllPaiXing(int[] paiQuantityArray, List<MajiangPai[]> jutiLianXuPaiTypesArrayList,
			List<Integer> duLiPaiIdxList) {
		return doCalculateAllPaiXing(paiQuantityArray, duLiPaiIdxList);
	}

	public List<PaiXing> doCalculateAllPaiXing(int[] paiQuantityArray, List<Integer> duLiPaiIdxList) {
		List<PaiXing> result = new ArrayList<>();
		// 尝试每种组合出来的构型编码是否是匹配的构型编码，是的话就收集该种组合
		int[][] tongpaiGouXingArrayArray = new int[duLiPaiIdxList.size()][];
		for (int i = 0; i < duLiPaiIdxList.size(); i++) {
			int duLiPaiIdx = duLiPaiIdxList.get(i);
			tongpaiGouXingArrayArray[i] = DuLiPaiZu.tongpaiGouXingArray[paiQuantityArray[duLiPaiIdx] - 1];
		}

		int maxZuheCode = 1;
		int[] tongpaiGouXingArrayCountArray = new int[tongpaiGouXingArrayArray.length];
		for (int i = 0; i < tongpaiGouXingArrayArray.length; i++) {
			int count = tongpaiGouXingArrayArray[i].length;
			tongpaiGouXingArrayCountArray[i] = count;
			maxZuheCode *= count;
		}
		int[] modArray = new int[tongpaiGouXingArrayCountArray.length];
		int mod = 1;
		for (int i = 0; i < tongpaiGouXingArrayCountArray.length; i++) {
			modArray[tongpaiGouXingArrayCountArray.length - 1 - i] = mod;
			mod *= tongpaiGouXingArrayCountArray[tongpaiGouXingArrayCountArray.length - 1 - i];
		}
		int[] gouXingZuheArray = new int[duLiPaiIdxList.size()];
		int combinedGouXing;
		for (int code = 0; code < maxZuheCode; code++) {
			combinedGouXing = 0;
			int temp = code;
			for (int i = 0; i < modArray.length; i++) {
				int shang = temp / modArray[i];
				int gouXing = tongpaiGouXingArrayArray[i][shang];
				gouXingZuheArray[i] = gouXing;
				combinedGouXing += gouXing;
				temp = temp % modArray[i];
			}
			if (combinedGouXing == gouXingCode) {// 该种构型组合匹配
				// 生成一个牌型
				PaiXing paiXing = new PaiXing();
				List<Danpai> danpaiList = new ArrayList<>();
				List<Duizi> duiziList = new ArrayList<>();
				List<Kezi> keziList = new ArrayList<>();
				List<Gangzi> gangziList = new ArrayList<>();
				List<Shunzi> shunziList = new ArrayList<>();
				paiXing.setDanpaiList(danpaiList);
				paiXing.setDuiziList(duiziList);
				paiXing.setGangziList(gangziList);
				paiXing.setKeziList(keziList);
				paiXing.setShunziList(shunziList);

				for (int i = 0; i < gouXingZuheArray.length; i++) {
					int gouXing = gouXingZuheArray[i];

					int danpaiCount = (gouXing & 31);
					int duiziCount = ((gouXing >>> 5) & 15);
					int keziCount = ((gouXing >>> 9) & 7);
					int gangziCount = ((gouXing >>> 12) & 7);

					MajiangPai paiType = MajiangPai.valueOf(duLiPaiIdxList.get(i));

					for (int j = 0; j < danpaiCount; j++) {
						danpaiList.add(new Danpai(paiType));
					}
					for (int j = 0; j < duiziCount; j++) {
						duiziList.add(new Duizi(paiType));
					}
					for (int j = 0; j < keziCount; j++) {
						keziList.add(new Kezi(paiType));
					}
					for (int j = 0; j < gangziCount; j++) {
						gangziList.add(new Gangzi(paiType));
					}

				}

				result.add(paiXing);

			}
		}
		return result;
	}

}
