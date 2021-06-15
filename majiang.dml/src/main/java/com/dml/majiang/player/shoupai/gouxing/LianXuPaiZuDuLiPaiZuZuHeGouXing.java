package com.dml.majiang.player.shoupai.gouxing;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.shoupai.PaiXing;

/**
 * 连续牌组独立牌组组合构型。 包含有构型编码，和组合出这个构型的连续牌组构型对象和独立牌组构型编码
 * 
 * @author Neo
 *
 */
public class LianXuPaiZuDuLiPaiZuZuHeGouXing extends GouXing {

	private LianXuPaiZuGouXing[] lianXuPaiZuGouXingArray;

	private DuLiPaiZuGouXing duLiPaiZuGouXing;

	@Override
	public List<PaiXing> calculateAllPaiXing(int[] paiQuantityArray, List<MajiangPai[]> jutiLianXuPaiTypesArrayList,
			List<Integer> duLiPaiIdxList) {
		List<PaiXing> result = new ArrayList<>();

		List<PaiXing> duLiPaiZuPaiXingList = duLiPaiZuGouXing.doCalculateAllPaiXing(paiQuantityArray, duLiPaiIdxList);

		List<PaiXing>[] lianXuPaiZuPaiXingListArray = new List[lianXuPaiZuGouXingArray.length];
		for (int i = 0; i < lianXuPaiZuGouXingArray.length; i++) {
			lianXuPaiZuPaiXingListArray[i] = lianXuPaiZuGouXingArray[i]
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
		PaiXing[] paiXingZuheArray = new PaiXing[lianXuPaiZuGouXingArray.length + 1];// +1要算上独立的
		for (int code = 0; code < maxZuheCode; code++) {
			int temp = code;
			for (int i = 0; i < modArray.length; i++) {
				int shang = temp / modArray[i];
				paiXingZuheArray[i] = lianXuPaiZuPaiXingListArray[i].get(shang);
				temp = temp % modArray[i];
			}
			for (PaiXing duLiPaiZuPaiXing : duLiPaiZuPaiXingList) {
				paiXingZuheArray[paiXingZuheArray.length - 1] = duLiPaiZuPaiXing;
				PaiXing combinedPaiXing = PaiXing.combine(paiXingZuheArray);
				result.add(combinedPaiXing);
			}

		}
		return result;
	}

	public LianXuPaiZuGouXing[] getLianXuPaiZuGouXingArray() {
		return lianXuPaiZuGouXingArray;
	}

	public void setLianXuPaiZuGouXingArray(LianXuPaiZuGouXing[] lianXuPaiZuGouXingArray) {
		this.lianXuPaiZuGouXingArray = lianXuPaiZuGouXingArray;
	}

	public DuLiPaiZuGouXing getDuLiPaiZuGouXing() {
		return duLiPaiZuGouXing;
	}

	public void setDuLiPaiZuGouXing(DuLiPaiZuGouXing duLiPaiZuGouXing) {
		this.duLiPaiZuGouXing = duLiPaiZuGouXing;
	}

}
