package com.dml.majiang.player.shoupai.gouxing;

/**
 * 构型没有单牌且只有一个对子就成胡。这种是最普遍的。
 * 
 * @author neo
 *
 */
public class NoDanpaiOneDuiziGouXingPanHu extends GouXingPanHu {

	@Override
	protected boolean panHu(int chichuShunziCount, int pengchuKeziCount, int gangchuGangziCount, int shoupaiDanpaiCount,
			int shoupaiDuiziCount, int shoupaiKeziCount, int shoupaiGangziCount, int shoupaiShunziCount) {
		return (shoupaiDanpaiCount == 0 && shoupaiDuiziCount == 1);
	}

}
