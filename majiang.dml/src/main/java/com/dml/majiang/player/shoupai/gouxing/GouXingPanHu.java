package com.dml.majiang.player.shoupai.gouxing;

/**
 * 判断是否是成胡的构型
 * 
 * @author neo
 *
 */
public abstract class GouXingPanHu {
	/**
	 * @param shoupaiGouXingCode
	 *            用一个int来编码:低到高--5位单牌个数，4位对子个数，3位刻子个数，3位杠子个数，3位顺子个数
	 * @param chichuShunziCount
	 * @param pengchuKeziCount
	 * @param gangchuGangziCount
	 * @return
	 */
	public boolean panHu(int shoupaiGouXingCode, int chichuShunziCount, int pengchuKeziCount, int gangchuGangziCount) {
		// 先解析shoupaiGouXingCode
		int shoupaiDanpaiCount = (shoupaiGouXingCode & 31);
		int shoupaiDuiziCount = ((shoupaiGouXingCode >>> 5) & 15);
		int shoupaiKeziCount = ((shoupaiGouXingCode >>> 9) & 7);
		int shoupaiGangziCount = ((shoupaiGouXingCode >>> 12) & 7);
		int shoupaiShunziCount = ((shoupaiGouXingCode >>> 15) & 7);
		return panHu(chichuShunziCount, pengchuKeziCount, gangchuGangziCount, shoupaiDanpaiCount, shoupaiDuiziCount,
				shoupaiKeziCount, shoupaiGangziCount, shoupaiShunziCount);
	}

	protected abstract boolean panHu(int chichuShunziCount, int pengchuKeziCount, int gangchuGangziCount,
			int shoupaiDanpaiCount, int shoupaiDuiziCount, int shoupaiKeziCount, int shoupaiGangziCount,
			int shoupaiShunziCount);
}
