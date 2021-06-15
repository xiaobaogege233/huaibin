package com.dml.majiang.player.shoupai.gouxing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 连续牌组
 * 
 * @author Neo
 *
 */
public class LianXuPaiZu implements Comparable<LianXuPaiZu> {

	private int[] paiQuantityArray;

	private int lian;

	private int totalPai;

	private int atleastGuiPai;

	private int smallCode;// 每3位

	private long bigCode;// 每4位

	private boolean bigCodeMode;

	private LianXuPaiZuGouXing[] gouXingArray;

	public LianXuPaiZu(int[] paiQuantityArray, int totalPai, int atleastGuiPai, boolean bigCodeMode) {
		this.lian = paiQuantityArray.length;
		this.paiQuantityArray = new int[lian];
		System.arraycopy(paiQuantityArray, 0, this.paiQuantityArray, 0, lian);
		this.totalPai = totalPai;
		this.atleastGuiPai = atleastGuiPai;
		this.bigCodeMode = bigCodeMode;
	}

	public int[] generateGouXingCodeArray() {
		int[] array = new int[gouXingArray.length];
		for (int i = 0; i < gouXingArray.length; i++) {
			array[i] = gouXingArray[i].getGouXingCode();
		}
		return array;
	}

	public void calculateGouXing() {
		Map<Integer, Set<LianXuPaiZuPaiXing>> gouXingCodeToPaiXingSetMap = new HashMap<>();
		List<Integer> yiquShunziXuhaoList = new ArrayList<>();
		List<Integer> yiquGangziXuhaoList = new ArrayList<>();
		List<Integer> yiquKeziXuhaoList = new ArrayList<>();
		List<Integer> yiquDuiziXuhaoList = new ArrayList<>();
		doCalculateGouXing(yiquShunziXuhaoList, yiquGangziXuhaoList, yiquKeziXuhaoList, yiquDuiziXuhaoList,
				gouXingCodeToPaiXingSetMap);
		gouXingArray = new LianXuPaiZuGouXing[gouXingCodeToPaiXingSetMap.size()];
		int i = 0;
		for (int gouXingCode : gouXingCodeToPaiXingSetMap.keySet()) {
			Set<LianXuPaiZuPaiXing> paiXingSet = gouXingCodeToPaiXingSetMap.get(gouXingCode);
			LianXuPaiZuGouXing gouXing = new LianXuPaiZuGouXing();
			gouXing.setGouXingCode(gouXingCode);
			LianXuPaiZuPaiXing[] paiXingArrayForGouXing = new LianXuPaiZuPaiXing[paiXingSet.size()];
			int j = 0;
			for (LianXuPaiZuPaiXing paiXing : paiXingSet) {
				paiXingArrayForGouXing[j++] = paiXing;
			}
			gouXing.setPaiXingArrayForGouXing(paiXingArrayForGouXing);
			gouXingArray[i++] = gouXing;
		}
	}

	private void doCalculateGouXing(List<Integer> yiquShunziXuhaoList, List<Integer> yiquGangziXuhaoList,
			List<Integer> yiquKeziXuhaoList, List<Integer> yiquDuiziXuhaoList,
			Map<Integer, Set<LianXuPaiZuPaiXing>> gouXingCodeToPaiXingSetMap) {

		int yiquShunziGeshu = yiquShunziXuhaoList.size();
		int yiquGangziGeshu = yiquGangziXuhaoList.size();
		int yiquKeziGeshu = yiquKeziXuhaoList.size();
		int yiquDuiziGeshu = yiquDuiziXuhaoList.size();

		// 计算所有顺子取法
		List<Integer> shunziStartIdxList = new ArrayList(lian - 2);
		int lianxuCount = 0;
		for (int i = 0; i < paiQuantityArray.length; i++) {
			if (paiQuantityArray[i] > 0) {
				lianxuCount++;
				if (lianxuCount == 3) {
					shunziStartIdxList.add(i - 2);
					lianxuCount = 0;
					i -= 2;
				}
			} else {
				lianxuCount = 0;
			}
		}

		// 找出第一个可能的杠子取法
		int gangziIdx = -1;
		for (int i = 0; i < paiQuantityArray.length; i++) {
			if (paiQuantityArray[i] >= 4) {
				gangziIdx = i;
				break;
			}
		}

		// 找出第一个可能的刻子取法
		int keziIdx = -1;
		for (int i = 0; i < paiQuantityArray.length; i++) {
			if (paiQuantityArray[i] >= 3) {
				keziIdx = i;
				break;
			}
		}

		// 找出第一个可能的对子取法
		int duiziIdx = -1;
		for (int i = 0; i < paiQuantityArray.length; i++) {
			if (paiQuantityArray[i] >= 2) {
				duiziIdx = i;
				break;
			}
		}

		// 如果什么都不能取，说明可以记录一个结果
		if (shunziStartIdxList.isEmpty() && gangziIdx == -1 && keziIdx == -1 && duiziIdx == -1) {
			// 构形用一个int来编码:低到高--5位单牌个数，4位对子个数，3位刻子个数，3位杠子个数，3位顺子个数
			List<Integer> danpaiXuhaoList = new ArrayList<>();
			int danpaiGeshu = 0;
			for (int i = 0; i < paiQuantityArray.length; i++) {
				int paiQuantity = paiQuantityArray[i];
				if (paiQuantity == 1) {
					danpaiGeshu++;
					danpaiXuhaoList.add(i);
				}
			}
			int couXingCode = danpaiGeshu;
			couXingCode = couXingCode | (yiquDuiziGeshu << 5);
			couXingCode = couXingCode | (yiquKeziGeshu << 9);
			couXingCode = couXingCode | (yiquGangziGeshu << 12);
			couXingCode = couXingCode | (yiquShunziGeshu << 15);

			Set<LianXuPaiZuPaiXing> paiXingSet = gouXingCodeToPaiXingSetMap.get(couXingCode);
			if (paiXingSet == null) {
				paiXingSet = new HashSet<>();
				gouXingCodeToPaiXingSetMap.put(couXingCode, paiXingSet);
			}
			paiXingSet.add(new LianXuPaiZuPaiXing(danpaiXuhaoList, yiquDuiziXuhaoList, yiquKeziXuhaoList,
					yiquGangziXuhaoList, yiquShunziXuhaoList));
			return;
		}

		shunziStartIdxList.forEach((shunziStartIdx) -> {
			paiQuantityArray[shunziStartIdx]--;
			paiQuantityArray[shunziStartIdx + 1]--;
			paiQuantityArray[shunziStartIdx + 2]--;
			yiquShunziXuhaoList.add(shunziStartIdx);
			doCalculateGouXing(yiquShunziXuhaoList, yiquGangziXuhaoList, yiquKeziXuhaoList, yiquDuiziXuhaoList,
					gouXingCodeToPaiXingSetMap);
			yiquShunziXuhaoList.remove(yiquShunziXuhaoList.size() - 1);
			paiQuantityArray[shunziStartIdx]++;
			paiQuantityArray[shunziStartIdx + 1]++;
			paiQuantityArray[shunziStartIdx + 2]++;
		});
		if (gangziIdx != -1) {
			paiQuantityArray[gangziIdx] -= 4;
			yiquGangziXuhaoList.add(gangziIdx);
			doCalculateGouXing(yiquShunziXuhaoList, yiquGangziXuhaoList, yiquKeziXuhaoList, yiquDuiziXuhaoList,
					gouXingCodeToPaiXingSetMap);
			yiquGangziXuhaoList.remove(yiquGangziXuhaoList.size() - 1);
			paiQuantityArray[gangziIdx] += 4;
		}

		if (keziIdx != -1) {
			paiQuantityArray[keziIdx] -= 3;
			yiquKeziXuhaoList.add(keziIdx);
			doCalculateGouXing(yiquShunziXuhaoList, yiquGangziXuhaoList, yiquKeziXuhaoList, yiquDuiziXuhaoList,
					gouXingCodeToPaiXingSetMap);
			yiquKeziXuhaoList.remove(yiquKeziXuhaoList.size() - 1);
			paiQuantityArray[keziIdx] += 3;
		}

		if (duiziIdx != -1) {
			paiQuantityArray[duiziIdx] -= 2;
			yiquDuiziXuhaoList.add(duiziIdx);
			doCalculateGouXing(yiquShunziXuhaoList, yiquGangziXuhaoList, yiquKeziXuhaoList, yiquDuiziXuhaoList,
					gouXingCodeToPaiXingSetMap);
			yiquDuiziXuhaoList.remove(yiquDuiziXuhaoList.size() - 1);
			paiQuantityArray[duiziIdx] += 2;
		}

	}

	public void calculateCode() {
		for (int i = 0; i < paiQuantityArray.length; i++) {
			bigCode = (bigCode | (paiQuantityArray[i] << (i * 4)));
		}
		if (!bigCodeMode) {
			for (int i = 0; i < paiQuantityArray.length; i++) {
				smallCode = (smallCode | (paiQuantityArray[i] << (i * 3)));
			}
		}
	}

	@Override
	public int compareTo(LianXuPaiZu another) {
		if (totalPai < another.totalPai) {
			return -1;
		} else if (totalPai > another.totalPai) {
			return 1;
		} else {
			if (bigCode < another.bigCode) {
				return -1;
			} else if (bigCode > another.bigCode) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public int getLian() {
		return lian;
	}

	public int getTotalPai() {
		return totalPai;
	}

	public int getAtleastGuiPai() {
		return atleastGuiPai;
	}

	public int getSmallCode() {
		return smallCode;
	}

	public long getBigCode() {
		return bigCode;
	}

	public boolean isBigCodeMode() {
		return bigCodeMode;
	}

	public LianXuPaiZuGouXing[] getGouXingArray() {
		return gouXingArray;
	}

}
