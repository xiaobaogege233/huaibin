package com.dml.majiang.pai.fenzu;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.shoupai.ShoupaiMajiangPaiFenZu;

/**
 * 麻将规则的牌分组，包括单牌、对子、刻子、杠子、顺子
 * 
 * @author Neo
 *
 */
public interface MajiangPaiFenZu {
	public int countPai(MajiangPai paiType);

	public MajiangPai[] toPaiArray();

	public <T extends ShoupaiMajiangPaiFenZu> T generateShoupaiMajiangPaiFenZuSkeleton();
}
