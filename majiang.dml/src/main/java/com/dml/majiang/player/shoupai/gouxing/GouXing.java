package com.dml.majiang.player.shoupai.gouxing;

import java.util.List;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.shoupai.PaiXing;

/**
 * 构型
 * 
 * @author Neo
 *
 */
public abstract class GouXing {

	/**
	 * 用一个int来编码:低到高--5位单牌个数，4位对子个数，3位刻子个数，3位杠子个数，3位顺子个数
	 */
	protected int gouXingCode;

	/**
	 * @param paiQuantityArray
	 * @param jutiLianXuPaiTypesArrayList
	 * @param duLiPaiIdxList
	 *            是关于paiQuantityArray的idx的list
	 * @return
	 */
	public abstract List<PaiXing> calculateAllPaiXing(int[] paiQuantityArray,
			List<MajiangPai[]> jutiLianXuPaiTypesArrayList, List<Integer> duLiPaiIdxList);

	public int getGouXingCode() {
		return gouXingCode;
	}

	public void setGouXingCode(int gouXingCode) {
		this.gouXingCode = gouXingCode;
	}

}
