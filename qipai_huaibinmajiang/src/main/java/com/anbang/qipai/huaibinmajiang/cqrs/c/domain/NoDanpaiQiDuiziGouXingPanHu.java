package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

/**
 * 构型没有单牌且有七个对子就成胡。
 * 
 * @author alexis
 *
 */
public class NoDanpaiQiDuiziGouXingPanHu extends GouXingPanHu {

	@Override
	protected boolean panHu(int chichuShunziCount, int pengchuKeziCount, int gangchuGangziCount, int shoupaiDanpaiCount,
			int shoupaiDuiziCount, int shoupaiKeziCount, int shoupaiGangziCount, int shoupaiShunziCount) {
		return (shoupaiDanpaiCount == 0 && shoupaiDuiziCount == 1) || (shoupaiDuiziCount == 7);
	}

}
