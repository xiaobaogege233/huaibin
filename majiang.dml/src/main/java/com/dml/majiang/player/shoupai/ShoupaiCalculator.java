package com.dml.majiang.player.shoupai;

import java.util.*;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.XushupaiCategory;
import com.dml.majiang.pai.fenzu.Shunzi;
import com.dml.majiang.player.shoupai.gouxing.DuLiPaiZu;
import com.dml.majiang.player.shoupai.gouxing.DuLiPaiZuGouXing;
import com.dml.majiang.player.shoupai.gouxing.GouXing;
import com.dml.majiang.player.shoupai.gouxing.GouXingCalculatorHelper;
import com.dml.majiang.player.shoupai.gouxing.JutihuaLianXuPaiZu;
import com.dml.majiang.player.shoupai.gouxing.LianXuPaiZu;
import com.dml.majiang.player.shoupai.gouxing.LianXuPaiZuDuLiPaiZuZuHeGouXing;
import com.dml.majiang.player.shoupai.gouxing.LianXuPaiZuGouXing;
import com.dml.majiang.player.shoupai.gouxing.LianXuPaiZuZuHeGouXing;

/**
 * 手牌计算器
 * 
 * @author Neo
 *
 */
public class ShoupaiCalculator {

	/**
	 * 0-8万,9-17筒,18-26条,27东风,28南风,29西风,30北风,31红中,32发财,33白板
	 */
	private int[] paiQuantityArray = new int[42];

	public List<PaiXing> calculateAllPaiXingFromGouXing(GouXing gouXing) {
		List<JutihuaLianXuPaiZu> jutihuaLianXuPaiZuList = new ArrayList<>();
		List<Integer> duLiPaiIdxList = new ArrayList<>();
		parseXuShuPai(0, 8, jutihuaLianXuPaiZuList, duLiPaiIdxList);
		parseXuShuPai(9, 17, jutihuaLianXuPaiZuList, duLiPaiIdxList);
		parseXuShuPai(18, 26, jutihuaLianXuPaiZuList, duLiPaiIdxList);
		parseZiPai(duLiPaiIdxList);
		jutihuaLianXuPaiZuList.forEach((jutiZu) -> jutiZu.getLianXuPaiZu().calculateCode());
		Collections.sort(jutihuaLianXuPaiZuList);
		List<MajiangPai[]> jutiLianXuPaiTypesArrayList = new ArrayList<>();
		jutihuaLianXuPaiZuList.forEach((jutihuaLianXuPaiZu) -> jutiLianXuPaiTypesArrayList
				.add(jutihuaLianXuPaiZu.getJutiLianXuPaiTypesArray()));
		return gouXing.calculateAllPaiXing(paiQuantityArray, jutiLianXuPaiTypesArrayList, duLiPaiIdxList);
	}


	public List<GouXing> calculateAllGouXing() {
		List<GouXing> result = new ArrayList<>();
		List<JutihuaLianXuPaiZu> jutihuaLianXuPaiZuList = new ArrayList<>();
		List<Integer> duLiPaiIdxList = new ArrayList<>();
		parseXuShuPai(0, 8, jutihuaLianXuPaiZuList, duLiPaiIdxList);
		parseXuShuPai(9, 17, jutihuaLianXuPaiZuList, duLiPaiIdxList);
		parseXuShuPai(18, 26, jutihuaLianXuPaiZuList, duLiPaiIdxList);
		parseZiPai(duLiPaiIdxList);
		List<LianXuPaiZu> lianXuPaiZuList = new ArrayList<>();
		jutihuaLianXuPaiZuList.forEach((jutihuaLianXuPaiZu) -> {
			LianXuPaiZu lianXuPaiZu = jutihuaLianXuPaiZu.getLianXuPaiZu();
			lianXuPaiZu.calculateCode();
			lianXuPaiZuList.add(lianXuPaiZu);
		});
		Collections.sort(lianXuPaiZuList);

		// 开始分情况查询构型
		boolean hasLianXuPaiZu = !lianXuPaiZuList.isEmpty();
		boolean hasDuLiPaiZu = !duLiPaiIdxList.isEmpty();
		if (hasLianXuPaiZu && !hasDuLiPaiZu) {// 手牌全由连续牌组组成
			if (lianXuPaiZuList.size() == 1) {// 只有一个连续牌组
				int idx = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(0));
				LianXuPaiZuGouXing[] lianXuPaiZuGouXingArray = GouXingCalculatorHelper.gouXingCalculator.yiLianXuPaiZuGouXingsArray[idx];
				for (int i = 0; i < lianXuPaiZuGouXingArray.length; i++) {
					result.add(lianXuPaiZuGouXingArray[i]);
				}
			} else if (lianXuPaiZuList.size() == 2) {// 由两个连续牌组组成
				int idx1 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(0));

				int idx2 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(1));

				LianXuPaiZuZuHeGouXing[] lianXuPaiZuZuHeGouXingArray = GouXingCalculatorHelper.gouXingCalculator.erLianXuPaiZuGouXingsArray[idx1
						* GouXingCalculatorHelper.gouXingCalculator.erLianXuPaiZuGouXingsArrayIdx1Mod + idx2];

				for (int i = 0; i < lianXuPaiZuZuHeGouXingArray.length; i++) {
					result.add(lianXuPaiZuZuHeGouXingArray[i]);
				}
			} else if (lianXuPaiZuList.size() == 3) {// 由三个连续牌组组成
				int idx1 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(0));

				int idx2 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(1));

				int idx3 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(2));

				LianXuPaiZuZuHeGouXing[] lianXuPaiZuZuHeGouXingArray = GouXingCalculatorHelper.gouXingCalculator.sanLianXuPaiZuGouXingsArray[idx1
						* GouXingCalculatorHelper.gouXingCalculator.sanLianXuPaiZuGouXingsArrayIdx1Mod
						+ idx2 * GouXingCalculatorHelper.gouXingCalculator.sanLianXuPaiZuGouXingsArrayIdx2Mod + idx3];

				for (int i = 0; i < lianXuPaiZuZuHeGouXingArray.length; i++) {
					result.add(lianXuPaiZuZuHeGouXingArray[i]);
				}
			} else if (lianXuPaiZuList.size() == 4) {// 由四个连续牌组组成
				int idx1 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(0));

				int idx2 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(1));

				int idx3 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(2));

				int idx4 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(3));

				LianXuPaiZuZuHeGouXing[] lianXuPaiZuZuHeGouXingArray = GouXingCalculatorHelper.gouXingCalculator.siLianXuPaiZuGouXingsArray[idx1
						* GouXingCalculatorHelper.gouXingCalculator.siLianXuPaiZuGouXingsArrayIdx1Mod
						+ idx2 * GouXingCalculatorHelper.gouXingCalculator.siLianXuPaiZuGouXingsArrayIdx2Mod
						+ idx3 * GouXingCalculatorHelper.gouXingCalculator.siLianXuPaiZuGouXingsArrayIdx3Mod + idx4];

				for (int i = 0; i < lianXuPaiZuZuHeGouXingArray.length; i++) {
					result.add(lianXuPaiZuZuHeGouXingArray[i]);
				}
			} else if (lianXuPaiZuList.size() == 5) {// 由五个连续牌组组成

				int idx1 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(0));

				int idx2 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(1));

				int idx3 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(2));

				int idx4 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(3));

				int idx5 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(4));

				LianXuPaiZuZuHeGouXing[] lianXuPaiZuZuHeGouXingArray = GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuGouXingsArray[idx1
						* GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuGouXingsArrayIdx1Mod
						+ idx2 * GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuGouXingsArrayIdx2Mod
						+ idx3 * GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuGouXingsArrayIdx3Mod
						+ idx4 * GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuGouXingsArrayIdx4Mod + idx5];

				for (int i = 0; i < lianXuPaiZuZuHeGouXingArray.length; i++) {
					result.add(lianXuPaiZuZuHeGouXingArray[i]);
				}
			}
		} else if (!hasLianXuPaiZu && hasDuLiPaiZu) {// 手牌全由独立牌组成
			int idx = calculateDuLiPaiZuGouXingIdx(duLiPaiIdxList);
			DuLiPaiZuGouXing[] duLiPaiZuGouXingArray = GouXingCalculatorHelper.gouXingCalculator.duLiPaiZuGouXingsArray[idx];
			for (int i = 0; i < duLiPaiZuGouXingArray.length; i++) {
				result.add(duLiPaiZuGouXingArray[i]);
			}
		} else {// 手牌由连续牌组和独立牌组组成
			if (lianXuPaiZuList.size() == 1) {// 有一个连续牌组
				int lianXuIdx = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(0));

				int duLiIdx = calculateDuLiPaiZuGouXingIdx(duLiPaiIdxList);

				LianXuPaiZuDuLiPaiZuZuHeGouXing[] lianXuPaiZuDuLiPaiZuZuHeGouXingArray = GouXingCalculatorHelper.gouXingCalculator.yiLianXuPaiZuAndDuLiPaiZuGouXingsArray[lianXuIdx
						* GouXingCalculatorHelper.gouXingCalculator.yiLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod
						+ duLiIdx];
				for (int i = 0; i < lianXuPaiZuDuLiPaiZuZuHeGouXingArray.length; i++) {
					result.add(lianXuPaiZuDuLiPaiZuZuHeGouXingArray[i]);
				}
			} else if (lianXuPaiZuList.size() == 2) {// 有两个连续牌组
				int lianXuIdx1 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(0));

				int lianXuIdx2 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(1));

				int duLiIdx = calculateDuLiPaiZuGouXingIdx(duLiPaiIdxList);

				LianXuPaiZuDuLiPaiZuZuHeGouXing[] lianXuPaiZuDuLiPaiZuZuHeGouXingArray = GouXingCalculatorHelper.gouXingCalculator.erLianXuPaiZuAndDuLiPaiZuGouXingsArray[lianXuIdx1
						* GouXingCalculatorHelper.gouXingCalculator.erLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod
						+ lianXuIdx2
								* GouXingCalculatorHelper.gouXingCalculator.erLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod
						+ duLiIdx];
				for (int i = 0; i < lianXuPaiZuDuLiPaiZuZuHeGouXingArray.length; i++) {
					result.add(lianXuPaiZuDuLiPaiZuZuHeGouXingArray[i]);
				}
			} else if (lianXuPaiZuList.size() == 3) {// 有三个连续牌组
				int lianXuIdx1 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(0));

				int lianXuIdx2 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(1));

				int lianXuIdx3 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(2));

				int duLiIdx = calculateDuLiPaiZuGouXingIdx(duLiPaiIdxList);

				LianXuPaiZuDuLiPaiZuZuHeGouXing[] lianXuPaiZuDuLiPaiZuZuHeGouXingArray = GouXingCalculatorHelper.gouXingCalculator.sanLianXuPaiZuAndDuLiPaiZuGouXingsArray[lianXuIdx1
						* GouXingCalculatorHelper.gouXingCalculator.sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod
						+ lianXuIdx2
								* GouXingCalculatorHelper.gouXingCalculator.sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod
						+ lianXuIdx3
								* GouXingCalculatorHelper.gouXingCalculator.sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod
						+ duLiIdx];
				for (int i = 0; i < lianXuPaiZuDuLiPaiZuZuHeGouXingArray.length; i++) {
					result.add(lianXuPaiZuDuLiPaiZuZuHeGouXingArray[i]);
				}
			} else if (lianXuPaiZuList.size() == 4) {// 有四个连续牌组
				int lianXuIdx1 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(0));

				int lianXuIdx2 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(1));

				int lianXuIdx3 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(2));

				int lianXuIdx4 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(3));

				int duLiIdx = calculateDuLiPaiZuGouXingIdx(duLiPaiIdxList);

				LianXuPaiZuDuLiPaiZuZuHeGouXing[] lianXuPaiZuDuLiPaiZuZuHeGouXingArray = GouXingCalculatorHelper.gouXingCalculator.siLianXuPaiZuAndDuLiPaiZuGouXingsArray[lianXuIdx1
						* GouXingCalculatorHelper.gouXingCalculator.siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod
						+ lianXuIdx2
								* GouXingCalculatorHelper.gouXingCalculator.siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod
						+ lianXuIdx3
								* GouXingCalculatorHelper.gouXingCalculator.siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod
						+ lianXuIdx4
								* GouXingCalculatorHelper.gouXingCalculator.siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx4Mod
						+ duLiIdx];
				for (int i = 0; i < lianXuPaiZuDuLiPaiZuZuHeGouXingArray.length; i++) {
					result.add(lianXuPaiZuDuLiPaiZuZuHeGouXingArray[i]);
				}
			} else if (lianXuPaiZuList.size() == 5) {// 有五个连续牌组
				int lianXuIdx1 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(0));

				int lianXuIdx2 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(1));

				int lianXuIdx3 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(2));

				int lianXuIdx4 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(3));

				int lianXuIdx5 = calculateLianXuPaiZuGouXingIdx(lianXuPaiZuList.get(4));

				int duLiIdx = calculateDuLiPaiZuGouXingIdx(duLiPaiIdxList);

				LianXuPaiZuDuLiPaiZuZuHeGouXing[] lianXuPaiZuDuLiPaiZuZuHeGouXingArray = GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuAndDuLiPaiZuGouXingsArray[lianXuIdx1
						* GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod
						+ lianXuIdx2
								* GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod
						+ lianXuIdx3
								* GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod
						+ lianXuIdx4
								* GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx4Mod
						+ lianXuIdx5
								* GouXingCalculatorHelper.gouXingCalculator.wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx5Mod
						+ duLiIdx];
				for (int i = 0; i < lianXuPaiZuDuLiPaiZuZuHeGouXingArray.length; i++) {
					result.add(lianXuPaiZuDuLiPaiZuZuHeGouXingArray[i]);
				}
			}
		}
		return result;
	}

	private int calculateLianXuPaiZuGouXingIdx(LianXuPaiZu lianXuPaiZu) {
		if (!lianXuPaiZu.isBigCodeMode()) {
			return GouXingCalculatorHelper.gouXingCalculator.lianXuPaiZuGouXingsIdxArray[lianXuPaiZu.getSmallCode()];
		} else {
			return GouXingCalculatorHelper.gouXingCalculator.lianXuPaiZuGouXingsIdxMap.get(lianXuPaiZu.getBigCode());
		}
	}

	private int calculateDuLiPaiZuGouXingIdx(List<Integer> duLiPaiIdxList) {
		int[] duLiPaiCountArray = new int[10];
		int totalPai = 0;
		for (int duLiPaiIdx : duLiPaiIdxList) {
			int duLiPaiQuantity = paiQuantityArray[duLiPaiIdx];
			totalPai += duLiPaiQuantity;
			duLiPaiCountArray[duLiPaiQuantity - 1]++;
		}
		DuLiPaiZu duLiPaiZu = new DuLiPaiZu(0, duLiPaiCountArray, totalPai, 0);
		duLiPaiZu.calculateCode();
		int idx = GouXingCalculatorHelper.gouXingCalculator.duLiPaiZuGouXingsIdxArray[duLiPaiZu.getCode()];
		return idx;
	}

	private void parseZiPai(List<Integer> duLiPaiIdxList) {
		for (int i = 27; i <= 33; i++) {
			if (paiQuantityArray[i] > 0) {
				duLiPaiIdxList.add(i);
			}
		}
	}

	private void parseXuShuPai(int startIdx, int endIdx, List<JutihuaLianXuPaiZu> jutihuaLianXuPaiZuList,
			List<Integer> duLiPaiIdxList) {
		MajiangPai[] allPaiTypeArray = MajiangPai.values();
		int lian = 0;
		int totalPai = 0;
		boolean bigCodeMode = false;
		for (int i = startIdx; i <= endIdx; i++) {
			int paiQuantity = paiQuantityArray[i];
			if (paiQuantity > 0) {
				lian++;
				totalPai += paiQuantity;
				if (paiQuantity > 7) {
					bigCodeMode = true;
				}
			} else {
				if (lian >= 3) {
					int[] lianXuPaiZuPaiQuantityArray = new int[lian];
					System.arraycopy(paiQuantityArray, i - lian, lianXuPaiZuPaiQuantityArray, 0, lian);

					MajiangPai[] jutiLianXuPaiTypesArray = new MajiangPai[lian];
					System.arraycopy(allPaiTypeArray, i - lian, jutiLianXuPaiTypesArray, 0, lian);

					jutihuaLianXuPaiZuList.add(new JutihuaLianXuPaiZu(
							new LianXuPaiZu(lianXuPaiZuPaiQuantityArray, totalPai, 0, bigCodeMode),
							jutiLianXuPaiTypesArray));
				} else {
					for (int j = lian; j > 0; j--) {
						duLiPaiIdxList.add(i - j);
					}
				}
				lian = 0;
				totalPai = 0;
			}
		}
		// 收尾
		if (lian >= 3) {
			int[] lianXuPaiZuPaiQuantityArray = new int[lian];
			System.arraycopy(paiQuantityArray, (endIdx + 1) - lian, lianXuPaiZuPaiQuantityArray, 0, lian);

			MajiangPai[] jutiLianXuPaiTypesArray = new MajiangPai[lian];
			System.arraycopy(allPaiTypeArray, (endIdx + 1) - lian, jutiLianXuPaiTypesArray, 0, lian);

			jutihuaLianXuPaiZuList.add(new JutihuaLianXuPaiZu(
					new LianXuPaiZu(lianXuPaiZuPaiQuantityArray, totalPai, 0, bigCodeMode), jutiLianXuPaiTypesArray));
		} else {
			for (int j = lian; j > 0; j--) {
				duLiPaiIdxList.add((endIdx + 1) - j);
			}
		}
	}

	public void addPai(MajiangPai pai) {
		int paiOrdinal = pai.ordinal();
		paiQuantityArray[paiOrdinal]++;
	}

	public void removePai(MajiangPai pai) {
		int paiOrdinal = pai.ordinal();
		paiQuantityArray[paiOrdinal]--;
	}

	public void removePai(MajiangPai pai, int zhangShu) {
		int paiOrdinal = pai.ordinal();
		paiQuantityArray[paiOrdinal] -= zhangShu;
	}

	public Shunzi tryAndMakeShunziWithPai1(MajiangPai pai1) {
		int paiOrdinal = pai1.ordinal();
		if (paiOrdinal >= 0 && paiOrdinal <= 8) {// 万
			if (paiOrdinal <= 6) {
				if (paiQuantityArray[paiOrdinal + 1] > 0 && paiQuantityArray[paiOrdinal + 2] > 0) {
					Shunzi shunzi = new Shunzi(pai1, MajiangPai.valueOf(paiOrdinal + 1),
							MajiangPai.valueOf(paiOrdinal + 2));
					return shunzi;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else if (paiOrdinal >= 9 && paiOrdinal <= 17) {// 筒
			if (paiOrdinal <= 15) {
				if (paiQuantityArray[paiOrdinal + 1] > 0 && paiQuantityArray[paiOrdinal + 2] > 0) {
					Shunzi shunzi = new Shunzi(pai1, MajiangPai.valueOf(paiOrdinal + 1),
							MajiangPai.valueOf(paiOrdinal + 2));
					return shunzi;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else if (paiOrdinal >= 18 && paiOrdinal <= 26) {// 条
			if (paiOrdinal <= 24) {
				if (paiQuantityArray[paiOrdinal + 1] > 0 && paiQuantityArray[paiOrdinal + 2] > 0) {
					Shunzi shunzi = new Shunzi(pai1, MajiangPai.valueOf(paiOrdinal + 1),
							MajiangPai.valueOf(paiOrdinal + 2));
					return shunzi;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public Shunzi tryAndMakeShunziWithPai2(MajiangPai pai2) {
		int paiOrdinal = pai2.ordinal();
		if (paiOrdinal >= 0 && paiOrdinal <= 8) {// 万
			if (paiOrdinal >= 1 && paiOrdinal <= 7) {
				if (paiQuantityArray[paiOrdinal - 1] > 0 && paiQuantityArray[paiOrdinal + 1] > 0) {
					Shunzi shunzi = new Shunzi(MajiangPai.valueOf(paiOrdinal - 1), pai2,
							MajiangPai.valueOf(paiOrdinal + 1));
					return shunzi;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else if (paiOrdinal >= 9 && paiOrdinal <= 17) {// 筒
			if (paiOrdinal >= 10 && paiOrdinal <= 16) {
				if (paiQuantityArray[paiOrdinal - 1] > 0 && paiQuantityArray[paiOrdinal + 1] > 0) {
					Shunzi shunzi = new Shunzi(MajiangPai.valueOf(paiOrdinal - 1), pai2,
							MajiangPai.valueOf(paiOrdinal + 1));
					return shunzi;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else if (paiOrdinal >= 18 && paiOrdinal <= 26) {// 条
			if (paiOrdinal >= 19 && paiOrdinal <= 25) {
				if (paiQuantityArray[paiOrdinal - 1] > 0 && paiQuantityArray[paiOrdinal + 1] > 0) {
					Shunzi shunzi = new Shunzi(MajiangPai.valueOf(paiOrdinal - 1), pai2,
							MajiangPai.valueOf(paiOrdinal + 1));
					return shunzi;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public Shunzi tryAndMakeShunziWithPai3(MajiangPai pai3) {
		int paiOrdinal = pai3.ordinal();
		if (paiOrdinal >= 0 && paiOrdinal <= 8) {// 万
			if (paiOrdinal >= 2) {
				if (paiQuantityArray[paiOrdinal - 1] > 0 && paiQuantityArray[paiOrdinal - 2] > 0) {
					Shunzi shunzi = new Shunzi(MajiangPai.valueOf(paiOrdinal - 2), MajiangPai.valueOf(paiOrdinal - 1),
							pai3);
					return shunzi;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else if (paiOrdinal >= 9 && paiOrdinal <= 17) {// 筒
			if (paiOrdinal >= 11) {
				if (paiQuantityArray[paiOrdinal - 1] > 0 && paiQuantityArray[paiOrdinal - 2] > 0) {
					Shunzi shunzi = new Shunzi(MajiangPai.valueOf(paiOrdinal - 2), MajiangPai.valueOf(paiOrdinal - 1),
							pai3);
					return shunzi;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else if (paiOrdinal >= 18 && paiOrdinal <= 26) {// 条
			if (paiOrdinal >= 20) {
				if (paiQuantityArray[paiOrdinal - 1] > 0 && paiQuantityArray[paiOrdinal - 2] > 0) {
					Shunzi shunzi = new Shunzi(MajiangPai.valueOf(paiOrdinal - 2), MajiangPai.valueOf(paiOrdinal - 1),
							pai3);
					return shunzi;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public List<MajiangPai> findAllPaiQuantityIsFour() {
		List<MajiangPai> allGangzi = new ArrayList<>();
		for (int i = 0; i < paiQuantityArray.length; i++) {
			if (paiQuantityArray[i] == 4) {
				allGangzi.add(MajiangPai.valueOf(i));
			}
		}
		return allGangzi;
	}

	public int count(MajiangPai pai) {
		int paiOrdinal = pai.ordinal();
		return paiQuantityArray[paiOrdinal];
	}

	public int[] getPaiQuantityArray() {
		return paiQuantityArray;
	}

	public void setPaiQuantityArray(int[] paiQuantityArray) {
		this.paiQuantityArray = paiQuantityArray;
	}

	/**
	 * 获取所有花色的张数
	 */
	public Map<XushupaiCategory,Integer> getAllPaiCategoryCount() {
		Map<XushupaiCategory,Integer> huaseCountMap = new HashMap<>();
		int wanCount = 0;
		int tongCount = 0;
		int tiaoCount = 0;
		for (int i = 0; i < 9; i++){
			wanCount += paiQuantityArray[i];
		}
		for (int i = 9; i < 18; i++){
			tongCount += paiQuantityArray[i];
		}
		for (int i = 18; i < 27; i++){
			tiaoCount += paiQuantityArray[i];
		}
		huaseCountMap.put(XushupaiCategory.wan,wanCount);
		huaseCountMap.put(XushupaiCategory.tong,tongCount);
		huaseCountMap.put(XushupaiCategory.tiao,tiaoCount);
		return huaseCountMap;
	}
}
