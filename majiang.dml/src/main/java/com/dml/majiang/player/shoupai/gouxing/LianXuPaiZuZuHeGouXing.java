package com.dml.majiang.player.shoupai.gouxing;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.shoupai.PaiXing;

/**
 * 连续牌组组合构型。 包含有构型编码，和组合出这个构型的连续牌组构型对象
 * 
 * @author Neo
 *
 */
public class LianXuPaiZuZuHeGouXing extends GouXing {

	private LianXuPaiZuGouXing[] zuHePaiZuGouXingArray;

	@Override
	public List<PaiXing> calculateAllPaiXing(int[] paiQuantityArray, List<MajiangPai[]> jutiLianXuPaiTypesArrayList,
			List<Integer> duLiPaiIdxList) {
		List<PaiXing> result = new ArrayList<>();
		List<PaiXing>[] lianXuPaiZuPaiXingListArray = new List[zuHePaiZuGouXingArray.length];
		for (int i = 0; i < zuHePaiZuGouXingArray.length; i++) {
			lianXuPaiZuPaiXingListArray[i] = zuHePaiZuGouXingArray[i]
					.doCalculateAllPaiXing(jutiLianXuPaiTypesArrayList.get(i));
		}

		// 所有连续牌组的牌型组合起来
		int maxZuheCode = 1;
		int[] lianXuPaiZuPaiXingCountArray = new int[lianXuPaiZuPaiXingListArray.length];
		for (int i = 0; i < lianXuPaiZuPaiXingListArray.length; i++) {
			int count = lianXuPaiZuPaiXingListArray[i].size();
			lianXuPaiZuPaiXingCountArray[i] = count;
			maxZuheCode *= count;
		}
		int[] modArray = new int[lianXuPaiZuPaiXingCountArray.length];
		int mod = 1;
		for (int i = 0; i < lianXuPaiZuPaiXingCountArray.length; i++) {
			modArray[lianXuPaiZuPaiXingCountArray.length - 1 - i] = mod;
			mod *= lianXuPaiZuPaiXingCountArray[lianXuPaiZuPaiXingCountArray.length - 1 - i];
		}
		PaiXing[] paiXingZuheArray = new PaiXing[zuHePaiZuGouXingArray.length];
		for (int code = 0; code < maxZuheCode; code++) {
			int temp = code;
			for (int i = 0; i < modArray.length; i++) {
				int shang = temp / modArray[i];
				paiXingZuheArray[i] = lianXuPaiZuPaiXingListArray[i].get(shang);
				temp = temp % modArray[i];
			}
			PaiXing combinedPaiXing = PaiXing.combine(paiXingZuheArray);
			result.add(combinedPaiXing);
		}
		return result;
	}

	public LianXuPaiZuGouXing[] getZuHePaiZuGouXingArray() {
		return zuHePaiZuGouXingArray;
	}

	public void setZuHePaiZuGouXingArray(LianXuPaiZuGouXing[] zuHePaiZuGouXingArray) {
		this.zuHePaiZuGouXingArray = zuHePaiZuGouXingArray;
	}

}
