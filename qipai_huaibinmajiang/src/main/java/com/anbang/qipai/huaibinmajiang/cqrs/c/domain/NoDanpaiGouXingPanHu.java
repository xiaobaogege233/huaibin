package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

/**
 * 最普通胡。
 *
 * @author alexis
 *
 */
public class NoDanpaiGouXingPanHu extends GouXingPanHu {
    @Override
    protected boolean panHu(int chichuShunziCount, int pengchuKeziCount, int gangchuGangziCount, int shoupaiDanpaiCount,
                            int shoupaiDuiziCount, int shoupaiKeziCount, int shoupaiGangziCount, int shoupaiShunziCount) {
        return (shoupaiDanpaiCount == 0 && shoupaiDuiziCount == 1) ;
    }
}
