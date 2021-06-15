package com.dml.majiang.player.shoupai.gouxing;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.shoupai.PaiXing;

/**
 * 连续牌组构型: 包含有构型编码，和达到这个构型的所有"连续牌组牌型"对象
 * 
 * @author Neo
 *
 */
public class LianXuPaiZuGouXing extends GouXing {

	private LianXuPaiZuPaiXing[] paiXingArrayForGouXing;

	@Override
	public List<PaiXing> calculateAllPaiXing(int[] paiQuantityArray, List<MajiangPai[]> jutiLianXuPaiTypesArrayList,
			List<Integer> duLiPaiIdxList) {
		MajiangPai[] jutiLianXuPaiTypesArray = jutiLianXuPaiTypesArrayList.get(0);
		return doCalculateAllPaiXing(jutiLianXuPaiTypesArray);
	}

	public List<PaiXing> doCalculateAllPaiXing(MajiangPai[] jutiLianXuPaiTypesArray) {
		List<PaiXing> result = new ArrayList<PaiXing>();
		for (int i = 0; i < paiXingArrayForGouXing.length; i++) {
			LianXuPaiZuPaiXing lianXuPaiZuPaiXing = paiXingArrayForGouXing[i];
			PaiXing jutiPaiXing = lianXuPaiZuPaiXing.calculateJutiPaiXing(jutiLianXuPaiTypesArray);
			result.add(jutiPaiXing);
		}
		return result;
	}

	public LianXuPaiZuPaiXing[] getPaiXingArrayForGouXing() {
		return paiXingArrayForGouXing;
	}

	public void setPaiXingArrayForGouXing(LianXuPaiZuPaiXing[] paiXingArrayForGouXing) {
		this.paiXingArrayForGouXing = paiXingArrayForGouXing;
	}

}
