package com.dml.majiang.player.shoupai.gouxing;

import com.dml.majiang.pai.MajiangPai;

/**
 * 具体化连续牌组
 * 
 * @author Neo
 *
 */
public class JutihuaLianXuPaiZu implements Comparable<JutihuaLianXuPaiZu> {

	private LianXuPaiZu lianXuPaiZu;

	/**
	 * 具体是哪几种连续牌
	 */
	private MajiangPai[] jutiLianXuPaiTypesArray;

	public JutihuaLianXuPaiZu(LianXuPaiZu lianXuPaiZu, MajiangPai[] jutiLianXuPaiTypesArray) {
		this.lianXuPaiZu = lianXuPaiZu;
		this.jutiLianXuPaiTypesArray = jutiLianXuPaiTypesArray;
	}

	public LianXuPaiZu getLianXuPaiZu() {
		return lianXuPaiZu;
	}

	public void setLianXuPaiZu(LianXuPaiZu lianXuPaiZu) {
		this.lianXuPaiZu = lianXuPaiZu;
	}

	public MajiangPai[] getJutiLianXuPaiTypesArray() {
		return jutiLianXuPaiTypesArray;
	}

	public void setJutiLianXuPaiTypesArray(MajiangPai[] jutiLianXuPaiTypesArray) {
		this.jutiLianXuPaiTypesArray = jutiLianXuPaiTypesArray;
	}

	@Override
	public int compareTo(JutihuaLianXuPaiZu o) {
		return lianXuPaiZu.compareTo(o.getLianXuPaiZu());
	}

}
