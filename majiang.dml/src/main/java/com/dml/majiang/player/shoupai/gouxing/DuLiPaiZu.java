package com.dml.majiang.player.shoupai.gouxing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 独立牌组
 * 
 * @author Neo
 *
 */
public class DuLiPaiZu implements Comparable<DuLiPaiZu> {

	public static int[][] tongpaiGouXingArray;

	static {
		tongpaiGouXingArray = new int[10][];
		for (int n = 1; n <= 10; n++) {
			tongpaiGouXingArray[n - 1] = calculateGouXingsForNTongPai(n);
		}
	}

	private static int[] calculateGouXingsForNTongPai(int n) {
		Set<Integer> gouXingSet = new HashSet<>();
		doCalculateGouXingsForNTongPai(n, 0, 0, 0, 0, gouXingSet);
		int[] gouXings = new int[gouXingSet.size()];
		int i = 0;
		for (int gouXing : gouXingSet) {
			gouXings[i++] = gouXing;
		}
		return gouXings;
	}

	private static void doCalculateGouXingsForNTongPai(int n, int yiquDanpai, int yiquDuizi, int yiquKezi,
			int yiquGangzi, Set<Integer> gouXingSet) {

		// 如果只有一张牌或者没牌了，记录一个结果并返回
		if (n <= 1) {
			int gouXing = yiquDanpai + n;
			gouXing = gouXing | (yiquDuizi << 5);
			gouXing = gouXing | (yiquKezi << 9);
			gouXing = gouXing | (yiquGangzi << 12);
			gouXingSet.add(gouXing);
			return;
		}

		// 取一个对子
		if (n >= 2) {
			doCalculateGouXingsForNTongPai(n - 2, yiquDanpai, yiquDuizi + 1, yiquKezi, yiquGangzi, gouXingSet);
		}

		// 取一个刻子
		if (n >= 3) {
			doCalculateGouXingsForNTongPai(n - 3, yiquDanpai, yiquDuizi, yiquKezi + 1, yiquGangzi, gouXingSet);
		}

		// 取一个杠子
		if (n >= 4) {
			doCalculateGouXingsForNTongPai(n - 4, yiquDanpai, yiquDuizi, yiquKezi, yiquGangzi + 1, gouXingSet);
		}

	}

	private static int[] joinGouXingArray(int[] gouXingArray1, int[] gouXingArray2) {
		int[] result = new int[gouXingArray1.length * gouXingArray2.length];
		int ri = 0;
		for (int i = 0; i < gouXingArray1.length; i++) {
			int gouXing1 = gouXingArray1[i];
			for (int j = 0; j < gouXingArray2.length; j++) {
				result[ri++] = gouXing1 + gouXingArray2[j];
			}
		}
		return result;
	}

	public DuLiPaiZu(int code, int[] paiCountArray, int totalPai, int atleastGuiPai) {
		this.code = code;
		this.paiCountArray = paiCountArray;
		this.totalPai = totalPai;
		this.atleastGuiPai = atleastGuiPai;
	}

	private int[] paiCountArray = new int[10];

	private int totalPai;

	private int atleastGuiPai;

	private DuLiPaiZuGouXing[] gouXingArray;

	/**
	 * 低到高--5位单牌个数，4位对子个数，3位刻子个数，3位杠子个数，2位5同牌个数，2位6同牌个数，2位7同牌个数，1位8同牌个数，1位9同牌个数，1位10同牌个数
	 */
	private int code;

	public void calculateCode() {
		code = paiCountArray[0];
		code = code | (paiCountArray[1] << 5);
		code = code | (paiCountArray[2] << 9);
		code = code | (paiCountArray[3] << 12);
		code = code | (paiCountArray[4] << 15);
		code = code | (paiCountArray[5] << 17);
		code = code | (paiCountArray[6] << 19);
		code = code | (paiCountArray[7] << 21);
		code = code | (paiCountArray[8] << 22);
		code = code | (paiCountArray[9] << 23);
	}

	@Override
	public int compareTo(DuLiPaiZu another) {
		if (totalPai < another.totalPai) {
			return -1;
		} else if (totalPai > another.totalPai) {
			return 1;
		} else {
			if (code < another.code) {
				return -1;
			} else if (code > another.code) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public void calculateGouXing() {
		Set<Integer> gouXingSet = new HashSet<>();
		List<int[]> tongpaiGouXingsList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			int nCount = paiCountArray[i];
			for (int j = 0; j < nCount; j++) {
				tongpaiGouXingsList.add(tongpaiGouXingArray[i]);
			}
		}

		int[] tempGouXingArray = new int[] { 0 };
		for (int[] gouXings : tongpaiGouXingsList) {
			tempGouXingArray = joinGouXingArray(tempGouXingArray, gouXings);
		}
		for (int i = 0; i < tempGouXingArray.length; i++) {
			gouXingSet.add(tempGouXingArray[i]);
		}
		gouXingArray = new DuLiPaiZuGouXing[gouXingSet.size()];
		int i = 0;
		for (int gouXing : gouXingSet) {
			DuLiPaiZuGouXing duLiPaiZuGouXing = new DuLiPaiZuGouXing();
			duLiPaiZuGouXing.setGouXingCode(gouXing);
			gouXingArray[i++] = duLiPaiZuGouXing;
		}

	}

	public int getCode() {
		return code;
	}

	public DuLiPaiZuGouXing[] getGouXingArray() {
		return gouXingArray;
	}

	public int getTotalPai() {
		return totalPai;
	}

	public int getAtleastGuiPai() {
		return atleastGuiPai;
	}

}
