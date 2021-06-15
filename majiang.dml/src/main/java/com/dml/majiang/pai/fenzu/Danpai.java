package com.dml.majiang.pai.fenzu;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.shoupai.ShoupaiDanpai;

public class Danpai implements MajiangPaiFenZu {

	private MajiangPai pai;

	public Danpai() {
	}

	public Danpai(MajiangPai pai) {
		this.pai = pai;
	}

	@Override
	public int countPai(MajiangPai paiType) {
		if (paiType.equals(pai)) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public ShoupaiDanpai generateShoupaiMajiangPaiFenZuSkeleton() {
		ShoupaiDanpai shoupaiDanpai = new ShoupaiDanpai();
		shoupaiDanpai.setDanpaiType(pai);
		return shoupaiDanpai;
	}

	@Override
	public MajiangPai[] toPaiArray() {
		return new MajiangPai[] { pai };
	}

	public MajiangPai getPai() {
		return pai;
	}

	public void setPai(MajiangPai pai) {
		this.pai = pai;
	}

}
