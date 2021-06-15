package com.dml.majiang.player.shoupaisort;

import java.util.Comparator;

import com.dml.majiang.pai.MajiangPai;

/**
 * 百板当作了鬼牌的本牌来用，所以排序也要体现出来。（只适用于只有一种鬼牌）
 * 
 * @author Neo
 *
 */
public class BaibanDangGuipaiBenpaiShoupaiSortComparator implements Comparator<MajiangPai> {

	private MajiangPai guipai;

	public BaibanDangGuipaiBenpaiShoupaiSortComparator() {
	}

	public BaibanDangGuipaiBenpaiShoupaiSortComparator(MajiangPai guipai) {
		this.guipai = guipai;
	}

	@Override
	public int compare(MajiangPai pai1, MajiangPai pai2) {
		MajiangPai dangPai1 = pai1.equals(MajiangPai.baiban) ? guipai : pai1;
		MajiangPai dangPai2 = pai2.equals(MajiangPai.baiban) ? guipai : pai2;
		return dangPai1.compareTo(dangPai2);
	}

	public MajiangPai getGuipai() {
		return guipai;
	}

	public void setGuipai(MajiangPai guipai) {
		this.guipai = guipai;
	}

}
