package com.dml.majiang.player.shoupaisort;

import java.util.Comparator;

import com.dml.majiang.pai.MajiangPai;

/**
 * 麻将牌自然序的手牌排序比较器
 * 
 * @author Neo
 *
 */
public class MajiangPaiOrderShoupaiSortComparator implements Comparator<MajiangPai> {

	@Override
	public int compare(MajiangPai pai1, MajiangPai pai2) {
		return pai1.compareTo(pai2);
	}

}
