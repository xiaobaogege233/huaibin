package com.dml.majiang.player.shoupai;

import java.util.List;

import com.dml.majiang.player.shoupai.gouxing.GouXing;

/**
 * 鬼牌按照一定的当牌安排下的手牌计算出来的构型组
 * 
 * @author Neo
 *
 */
public class ShoupaiWithGuipaiDangGouXingZu {

	/**
	 * 鬼牌当牌的安排
	 */
	private GuipaiDangPai[] guipaiDangPaiArray;

	private List<GouXing> gouXingList;

	public GuipaiDangPai[] getGuipaiDangPaiArray() {
		return guipaiDangPaiArray;
	}

	public void setGuipaiDangPaiArray(GuipaiDangPai[] guipaiDangPaiArray) {
		this.guipaiDangPaiArray = guipaiDangPaiArray;
	}

	public List<GouXing> getGouXingList() {
		return gouXingList;
	}

	public void setGouXingList(List<GouXing> gouXingList) {
		this.gouXingList = gouXingList;
	}

}
