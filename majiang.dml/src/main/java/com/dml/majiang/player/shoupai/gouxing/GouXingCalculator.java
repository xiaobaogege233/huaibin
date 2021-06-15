package com.dml.majiang.player.shoupai.gouxing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构型计算器
 * 
 * @author Neo
 *
 */
public class GouXingCalculator {

	private int maxShouPai;
	private int maxGuiPai;

	/**
	 * 连续牌组构型索引,小空间连续牌组编码作为数组下标,值为 排好序的 List<LianXuPaiZu> 的下标
	 */
	public int[] lianXuPaiZuGouXingsIdxArray = new int[134217728];// 9连顺子*3位牌数=27,27个1=134217727

	/**
	 * 连续牌组构型索引,key为大空间连续牌组编码,值为 排好序的 List<LianXuPaiZu> 的下标
	 */
	public Map<Long, Integer> lianXuPaiZuGouXingsIdxMap = new HashMap<>();

	/**
	 * 一个连续牌组组成手牌的构型
	 */
	public LianXuPaiZuGouXing[][] yiLianXuPaiZuGouXingsArray;

	/**
	 * 二个连续牌组组成手牌的构型
	 */
	public LianXuPaiZuZuHeGouXing[][] erLianXuPaiZuGouXingsArray;
	public int erLianXuPaiZuGouXingsArrayIdx1Mod;

	/**
	 * 三个连续牌组组成手牌的构型
	 */
	public LianXuPaiZuZuHeGouXing[][] sanLianXuPaiZuGouXingsArray;
	public int sanLianXuPaiZuGouXingsArrayIdx1Mod;
	public int sanLianXuPaiZuGouXingsArrayIdx2Mod;

	/**
	 * 四个连续牌组组成手牌的构型
	 */
	public LianXuPaiZuZuHeGouXing[][] siLianXuPaiZuGouXingsArray;
	public int siLianXuPaiZuGouXingsArrayIdx1Mod;
	public int siLianXuPaiZuGouXingsArrayIdx2Mod;
	public int siLianXuPaiZuGouXingsArrayIdx3Mod;

	/**
	 * 五个连续牌组组成手牌的构型
	 */
	public LianXuPaiZuZuHeGouXing[][] wuLianXuPaiZuGouXingsArray;
	public int wuLianXuPaiZuGouXingsArrayIdx1Mod;
	public int wuLianXuPaiZuGouXingsArrayIdx2Mod;
	public int wuLianXuPaiZuGouXingsArrayIdx3Mod;
	public int wuLianXuPaiZuGouXingsArrayIdx4Mod;

	/**
	 * 独立牌组构型索引,独立牌组编码作为数组下标,值是duLiPaiZuGouXingsArray的下标
	 */
	public int[] duLiPaiZuGouXingsIdxArray = new int[16777216];

	/**
	 * 只有独立牌组组成手牌的构型,按牌少到牌多排序
	 */
	public DuLiPaiZuGouXing[][] duLiPaiZuGouXingsArray;

	/**
	 * 一个连续牌组和独立牌组组成手牌的构型
	 */
	public LianXuPaiZuDuLiPaiZuZuHeGouXing[][] yiLianXuPaiZuAndDuLiPaiZuGouXingsArray;
	public int yiLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod;

	/**
	 * 二个连续牌组和独立牌组组成手牌的构型
	 */
	public LianXuPaiZuDuLiPaiZuZuHeGouXing[][] erLianXuPaiZuAndDuLiPaiZuGouXingsArray;
	public int erLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod;
	public int erLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod;

	/**
	 * 三个连续牌组和独立牌组组成手牌的构型
	 */
	public LianXuPaiZuDuLiPaiZuZuHeGouXing[][] sanLianXuPaiZuAndDuLiPaiZuGouXingsArray;
	public int sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod;
	public int sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod;
	public int sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod;

	/**
	 * 四个连续牌组和独立牌组组成手牌的构型
	 */
	public LianXuPaiZuDuLiPaiZuZuHeGouXing[][] siLianXuPaiZuAndDuLiPaiZuGouXingsArray;
	public int siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod;
	public int siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod;
	public int siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod;
	public int siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx4Mod;

	/**
	 * 五个连续牌组和独立牌组组成手牌的构型
	 */
	public LianXuPaiZuDuLiPaiZuZuHeGouXing[][] wuLianXuPaiZuAndDuLiPaiZuGouXingsArray;
	public int wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod;
	public int wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod;
	public int wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod;
	public int wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx4Mod;
	public int wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx5Mod;

	public GouXingCalculator(int maxShouPai, int maxGuiPai) {
		long startTime = System.currentTimeMillis();

		this.maxShouPai = maxShouPai;
		this.maxGuiPai = maxGuiPai;

		// 花色: 什么牌，一万，三条，发财等
		// 牌型: 牌型是一个划分好的杠子、刻子、顺子、对子、单牌的集合,它能完全描述一个牌的集合
		// 构型: 面向和牌关心的几个点，单牌个数，对子个数，刻子个数，杠子个数，顺子个数
		// 构型编码: 构形用一个int来编码:低到高--5位单牌个数，4位对子个数，3位刻子个数，3位杠子个数，3位顺子个数
		// 同构: 具有不同牌型的两个牌的集合，他们具有相同的构型集合（可能可以看成多种构型，所以是构型集合），那么他们同构
		// 连续牌组: 完全连的一组牌,至少3连,每种牌张数不限（抽象概念，不关心花色，不关心起点）
		// 连续牌组编码: 从小序号到大序号,从低位到高位,每n位的值代表该序号牌的数量
		// 连续牌组牌型: 把连续牌左移到顶之后计算出来的牌型。比如三万、四万、四万、五万，左移到顶之后就是一万、二万、二万、三万，再计算牌型
		// 连续牌组构型对象: 包含有构型编码，和达到这个构型的所有"连续牌组牌型"对象
		// 连续牌组牌型对象数据结构:
		// 一个short表示单牌: 0-9位的0/1值表示该位置有没有一个单牌
		// 一个int表示对子: 每3位表示该位置对子个数，9次
		// 一个int表示刻子: 每2位表示该位置刻子个数，9次
		// 一个int表示杠子: 每2位表示该位置杠子个数，9次
		// 一个short表示顺子: 每2位表示该位置对顺子个数，7次
		// 连续牌组组合构型对象: 包含有构型编码，和组合出这个构型的连续牌组构型对象
		// 独立牌组:
		// 没有顺子，只关心有几个单牌，几个对子，几个刻子，几个杠子，几个5同牌，几个6同牌，几个7同牌，几个8同牌，几个9同牌，几个10同牌的一组牌（抽象概念，不关心花色）
		// 独立牌组编码:低到高--5位单牌个数，4位对子个数，3位刻子个数，3位杠子个数，2位5同牌个数，2位6同牌个数，2位7同牌个数，1位8同牌个数，1位9同牌个数，1位10同牌个数
		// 独立牌组构型对象: 包含有构型编码
		// 连续牌组独立牌组组合构型对象: 包含有构型编码，和组合出这个构型的连续牌组构型对象和独立牌组构型编码

		int maxShouPaiLianXuPaiZu = maxShouPai / 3;// 手牌最多可能几个连续牌组

		// 连续牌组是抽象的，其目的是为了计算抽象的构型，显然，不同花色但是相同连续牌组的两组牌一定同构

		// 我们希望直接用连续牌组编码作为数组下标来查询其构型list的索引（这是关键效率优化）。虽然连续牌组编码空间很大，但是很稀疏。
		// 所以其构型list的索引空间其实很小。那么这就有利于实现直接数组下标查询多个连续牌组组合好后的结果。
		// 刚才提到连续牌组编码空间会很大，然而不幸的是在双鬼牌的情况下一种花色的牌有可能多达10张，这样编码空间就会过大。
		// 应对这种情况，我们在游戏过程中的手牌的结构中先按照约定的较小的比如3位一种牌编码，一旦摸牌太多出现3位不够存储的情况就整体改为4位一种牌编码。
		// 有两种结构的构型list索引，一种是数组，直接用连续牌组编码作为数组下标来查询，用于连续牌组编码空间大小还能接受的情况
		// 另一种是以hashmap作为存储结构，用连续牌组编码作为key来查询，用于连续牌组编码空间太大的情况

		// 对于给定的所有牌张数和最多鬼牌数，计算其所有可能的连续牌组，目的是前期过滤，缩小计算规模，计算结果是一个已经排好序的list，按牌少到牌多排序
		List<LianXuPaiZu> lianXuPaiZuList = calculateLianXuPaiZu(maxShouPai, maxGuiPai);

		// 为连续牌组计算构型
		calculateGouXingForLianXuPaiZu(lianXuPaiZuList);

		// 考虑手牌由多个连续牌组组成。这个时候可以在游戏中每次要判断和的时候都去把各个连续牌组的构型查出来，再做组合计算。这样效率太浪费。
		// 我们希望在这里把多个连续牌组组合好后的所有构型计算好，到时候提供直接查询结果。

		// 之前的lianXuPaiZuList是按照牌少到牌多排好序的，我们将利用这一点来节省多个连续牌组组合好后的结果的地址空间。
		// 首先我们分组合的连续牌组个数讨论，也就是分1个连续牌组，2个连续牌组，3个连续牌组，。。。，maxShouPaiLianXuPaiZu个连续牌组讨论。
		// 考虑2个连续牌组，取第一个连续牌组的时候考虑到还要取第二个连续牌组，所以牌不能取满，至少要给第二个组留3张牌，这样一来，必然在lianXuPaiZuList中取到前面的某一段。
		// 所以2个连续牌组的查询空间必然不会是lianXuPaiZuList.size()*lianXuPaiZuList.size()，而是要小一些。

		// 计算连续牌组组合构型。算法减少了计算规模，所以查询的时候索引顺序应该是从牌少的组到牌多的组
		calculateLianXuPaiZuZuHeGouXing(lianXuPaiZuList, maxShouPai, maxGuiPai, maxShouPaiLianXuPaiZu);

		// 如果手牌中没有任何的顺子，那就是一些独立牌形成一个独立牌组。

		// 对于给定的所有牌张数和最多鬼牌数，计算其所有可能的独立牌组，目的是前期过滤，缩小计算规模，计算结果是一个已经排好序的list，按牌少到牌多排序
		List<DuLiPaiZu> duLiPaiZuList = calculateDuLiPaiZu(maxShouPai, maxGuiPai);

		// 为独立牌组计算构型
		calculateGouXingForDuLiPaiZu(duLiPaiZuList);

		// 最常见的情况是手中既有连续牌组又有独立牌组

		// 为连续牌组和独立牌组组合的计算构型
		calculateGouXingForLianXuPaiZuAndDuLiPaiZu(lianXuPaiZuList, duLiPaiZuList, maxShouPai, maxGuiPai,
				maxShouPaiLianXuPaiZu);

		long finishTime = System.currentTimeMillis();

		System.out.println("耗时(毫秒):" + (finishTime - startTime));

		System.gc();

	}

	private void calculateGouXingForLianXuPaiZuAndDuLiPaiZu(List<LianXuPaiZu> lianXuPaiZuList,
			List<DuLiPaiZu> duLiPaiZuList, int maxShouPai, int maxGuiPai, int maxShouPaiLianXuPaiZu) {
		if (maxShouPaiLianXuPaiZu >= 1) {
			// 先计算数组规模
			int maxi = 0;
			int maxj = 0;
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = 0; j < duLiPaiZuList.size(); j++) {
					DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(j);
					int totalPai = totalPai1 + duLiPaiZu.getTotalPai();
					int atleastGuiPai = atleastGuiPai1 + duLiPaiZu.getAtleastGuiPai();
					if (totalPai <= maxShouPai) {
						if (atleastGuiPai <= maxGuiPai) {
							maxi = (i > maxi) ? i : maxi;
							maxj = (j > maxj) ? j : maxj;
						}
					} else {
						break;
					}
				}
			}
			yiLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod = (maxj + 1);

			// 再计算组合
			yiLianXuPaiZuAndDuLiPaiZuGouXingsArray = new LianXuPaiZuDuLiPaiZuZuHeGouXing[(maxi + 1)
					* yiLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod][];
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				LianXuPaiZuGouXing[] gouXingArray1 = zu1.getGouXingArray();
				for (int j = 0; j < duLiPaiZuList.size(); j++) {
					DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(j);
					int totalPai = totalPai1 + duLiPaiZu.getTotalPai();
					int atleastGuiPai = atleastGuiPai1 + duLiPaiZu.getAtleastGuiPai();
					if (totalPai <= maxShouPai) {
						if (atleastGuiPai <= maxGuiPai) {
							LianXuPaiZuDuLiPaiZuZuHeGouXing[] joindGouXingArray = joinLianXuPaiZuDuLiPaiZuGouXingArray(
									gouXingArray1, duLiPaiZu.getGouXingArray());
							yiLianXuPaiZuAndDuLiPaiZuGouXingsArray[i * yiLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod
									+ j] = joindGouXingArray;
						}
					} else {
						break;
					}
				}
			}
		}

		if (maxShouPaiLianXuPaiZu >= 2) {
			// 先计算数组规模
			int maxi = 0;
			int maxj = 0;
			int maxk = 0;
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = 0; k < duLiPaiZuList.size(); k++) {
								DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(k);
								int totalPai = totalPai2 + duLiPaiZu.getTotalPai();
								int atleastGuiPai = atleastGuiPai2 + duLiPaiZu.getAtleastGuiPai();
								if (totalPai <= maxShouPai) {
									if (atleastGuiPai <= maxGuiPai) {
										maxi = (i > maxi) ? i : maxi;
										maxj = (j > maxj) ? j : maxj;
										maxk = (k > maxk) ? k : maxk;
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}
			}
			erLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod = (maxk + 1);
			erLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod = (maxj + 1) * erLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod;

			// 再计算组合
			erLianXuPaiZuAndDuLiPaiZuGouXingsArray = new LianXuPaiZuDuLiPaiZuZuHeGouXing[(maxi + 1)
					* erLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod][];
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							LianXuPaiZuZuHeGouXing[] joindGouXingArray = erLianXuPaiZuGouXingsArray[i
									* erLianXuPaiZuGouXingsArrayIdx1Mod + j];
							for (int k = 0; k < duLiPaiZuList.size(); k++) {
								DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(k);
								int totalPai = totalPai2 + duLiPaiZu.getTotalPai();
								int atleastGuiPai = atleastGuiPai2 + duLiPaiZu.getAtleastGuiPai();
								if (totalPai <= maxShouPai) {
									if (atleastGuiPai <= maxGuiPai) {
										erLianXuPaiZuAndDuLiPaiZuGouXingsArray[i
												* erLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod
												+ j * erLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod
												+ k] = joinLianXuPaiZuDuLiPaiZuGouXingArray(joindGouXingArray,
														duLiPaiZu.getGouXingArray());
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}

			}

		}

		if (maxShouPaiLianXuPaiZu >= 3) {
			// 先计算数组规模
			int maxi = 0;
			int maxj = 0;
			int maxk = 0;
			int maxl = 0;
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										for (int l = 0; l < duLiPaiZuList.size(); l++) {
											DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(l);
											int totalPai = totalPai3 + duLiPaiZu.getTotalPai();
											int atleastGuiPai = atleastGuiPai3 + duLiPaiZu.getAtleastGuiPai();
											if (totalPai <= maxShouPai) {
												if (atleastGuiPai <= maxGuiPai) {
													maxi = (i > maxi) ? i : maxi;
													maxj = (j > maxj) ? j : maxj;
													maxk = (k > maxk) ? k : maxk;
													maxl = (l > maxl) ? l : maxl;
												}
											} else {
												break;
											}
										}
									}
								} else {
									break;
								}
							}

						}
					} else {
						break;
					}
				}
			}
			sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod = (maxl + 1);
			sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod = (maxk + 1)
					* sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod;
			sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod = (maxj + 1)
					* sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod;

			// 再计算组合
			sanLianXuPaiZuAndDuLiPaiZuGouXingsArray = new LianXuPaiZuDuLiPaiZuZuHeGouXing[(maxi + 1)
					* sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod][];
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {

							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										LianXuPaiZuZuHeGouXing[] joindGouXingArray = sanLianXuPaiZuGouXingsArray[i
												* sanLianXuPaiZuGouXingsArrayIdx1Mod
												+ j * sanLianXuPaiZuGouXingsArrayIdx2Mod + k];
										for (int l = 0; l < duLiPaiZuList.size(); l++) {
											DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(l);
											int totalPai = totalPai3 + duLiPaiZu.getTotalPai();
											int atleastGuiPai = atleastGuiPai3 + duLiPaiZu.getAtleastGuiPai();
											if (totalPai <= maxShouPai) {
												if (atleastGuiPai <= maxGuiPai) {
													sanLianXuPaiZuAndDuLiPaiZuGouXingsArray[i
															* sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod
															+ j * sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod
															+ k * sanLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod
															+ l] = joinLianXuPaiZuDuLiPaiZuGouXingArray(
																	joindGouXingArray, duLiPaiZu.getGouXingArray());
												}
											} else {
												break;
											}
										}
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}

			}

		}

		if (maxShouPaiLianXuPaiZu >= 4) {
			// 先计算数组规模
			int maxi = 0;
			int maxj = 0;
			int maxk = 0;
			int maxl = 0;
			int maxm = 0;
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										for (int l = k; l < lianXuPaiZuList.size(); l++) {
											LianXuPaiZu zu4 = lianXuPaiZuList.get(l);
											int totalPai4 = totalPai3 + zu4.getTotalPai();
											int atleastGuiPai4 = atleastGuiPai3 + zu4.getAtleastGuiPai();
											if (totalPai4 <= maxShouPai) {
												if (atleastGuiPai4 <= maxGuiPai) {
													for (int m = 0; m < duLiPaiZuList.size(); m++) {
														DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(m);
														int totalPai = totalPai4 + duLiPaiZu.getTotalPai();
														int atleastGuiPai = atleastGuiPai4
																+ duLiPaiZu.getAtleastGuiPai();
														if (totalPai <= maxShouPai) {
															if (atleastGuiPai <= maxGuiPai) {
																maxi = (i > maxi) ? i : maxi;
																maxj = (j > maxj) ? j : maxj;
																maxk = (k > maxk) ? k : maxk;
																maxl = (l > maxl) ? l : maxl;
																maxm = (m > maxm) ? m : maxm;
															}
														} else {
															break;
														}
													}
												}
											} else {
												break;
											}
										}

									}
								} else {
									break;
								}
							}

						}
					} else {
						break;
					}
				}
			}
			siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx4Mod = (maxm + 1);
			siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod = (maxl + 1) * siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx4Mod;
			siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod = (maxk + 1) * siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod;
			siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod = (maxj + 1) * siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod;

			// 再计算组合
			siLianXuPaiZuAndDuLiPaiZuGouXingsArray = new LianXuPaiZuDuLiPaiZuZuHeGouXing[(maxi + 1)
					* siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod][];
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										for (int l = k; l < lianXuPaiZuList.size(); l++) {
											LianXuPaiZu zu4 = lianXuPaiZuList.get(l);
											int totalPai4 = totalPai3 + zu4.getTotalPai();
											int atleastGuiPai4 = atleastGuiPai3 + zu4.getAtleastGuiPai();
											if (totalPai4 <= maxShouPai) {
												if (atleastGuiPai4 <= maxGuiPai) {
													LianXuPaiZuZuHeGouXing[] joindGouXingArray = siLianXuPaiZuGouXingsArray[i
															* siLianXuPaiZuGouXingsArrayIdx1Mod
															+ j * siLianXuPaiZuGouXingsArrayIdx2Mod
															+ k * siLianXuPaiZuGouXingsArrayIdx3Mod + l];
													for (int m = 0; m < duLiPaiZuList.size(); m++) {
														DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(m);
														int totalPai = totalPai4 + duLiPaiZu.getTotalPai();
														int atleastGuiPai = atleastGuiPai4
																+ duLiPaiZu.getAtleastGuiPai();
														if (totalPai <= maxShouPai) {
															if (atleastGuiPai <= maxGuiPai) {
																siLianXuPaiZuAndDuLiPaiZuGouXingsArray[i
																		* siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod
																		+ j * siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod
																		+ k * siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod
																		+ l * siLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx4Mod
																		+ m] = joinLianXuPaiZuDuLiPaiZuGouXingArray(
																				joindGouXingArray,
																				duLiPaiZu.getGouXingArray());
															}
														} else {
															break;
														}
													}
												}
											} else {
												break;
											}

										}
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}

			}

		}

		if (maxShouPaiLianXuPaiZu >= 5) {
			// 先计算数组规模
			int maxi = 0;
			int maxj = 0;
			int maxk = 0;
			int maxl = 0;
			int maxm = 0;
			int maxn = 0;
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										for (int l = k; l < lianXuPaiZuList.size(); l++) {
											LianXuPaiZu zu4 = lianXuPaiZuList.get(l);
											int totalPai4 = totalPai3 + zu4.getTotalPai();
											int atleastGuiPai4 = atleastGuiPai3 + zu4.getAtleastGuiPai();
											if (totalPai4 <= maxShouPai) {
												if (atleastGuiPai4 <= maxGuiPai) {

													for (int m = l; m < lianXuPaiZuList.size(); m++) {
														LianXuPaiZu zu5 = lianXuPaiZuList.get(m);
														int totalPai5 = totalPai4 + zu5.getTotalPai();
														int atleastGuiPai5 = atleastGuiPai4 + zu5.getAtleastGuiPai();
														if (totalPai5 <= maxShouPai) {
															if (atleastGuiPai5 <= maxGuiPai) {
																for (int n = 0; n < duLiPaiZuList.size(); n++) {
																	DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(n);
																	int totalPai = totalPai5 + duLiPaiZu.getTotalPai();
																	int atleastGuiPai = atleastGuiPai5
																			+ duLiPaiZu.getAtleastGuiPai();
																	if (totalPai <= maxShouPai) {
																		if (atleastGuiPai <= maxGuiPai) {
																			maxi = (i > maxi) ? i : maxi;
																			maxj = (j > maxj) ? j : maxj;
																			maxk = (k > maxk) ? k : maxk;
																			maxl = (l > maxl) ? l : maxl;
																			maxm = (m > maxm) ? m : maxm;
																			maxn = (n > maxn) ? n : maxn;
																		}
																	} else {
																		break;
																	}
																}
															}
														} else {
															break;
														}
													}
												}
											} else {
												break;
											}
										}

									}
								} else {
									break;
								}
							}

						}
					} else {
						break;
					}
				}
			}
			wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx5Mod = (maxn + 1);
			wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx4Mod = (maxm + 1) * wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx5Mod;
			wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod = (maxl + 1) * wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx4Mod;
			wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod = (maxk + 1) * wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod;
			wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod = (maxj + 1) * wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod;

			// 再计算组合
			wuLianXuPaiZuAndDuLiPaiZuGouXingsArray = new LianXuPaiZuDuLiPaiZuZuHeGouXing[(maxi + 1)
					* wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod][];
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										for (int l = k; l < lianXuPaiZuList.size(); l++) {
											LianXuPaiZu zu4 = lianXuPaiZuList.get(l);
											int totalPai4 = totalPai3 + zu4.getTotalPai();
											int atleastGuiPai4 = atleastGuiPai3 + zu4.getAtleastGuiPai();
											if (totalPai4 <= maxShouPai) {
												if (atleastGuiPai4 <= maxGuiPai) {
													for (int m = l; m < lianXuPaiZuList.size(); m++) {
														LianXuPaiZu zu5 = lianXuPaiZuList.get(m);
														int totalPai5 = totalPai4 + zu5.getTotalPai();
														int atleastGuiPai5 = atleastGuiPai4 + zu5.getAtleastGuiPai();
														if (totalPai5 <= maxShouPai) {
															if (atleastGuiPai5 <= maxGuiPai) {
																LianXuPaiZuZuHeGouXing[] joindGouXingArray = wuLianXuPaiZuGouXingsArray[i
																		* wuLianXuPaiZuGouXingsArrayIdx1Mod
																		+ j * wuLianXuPaiZuGouXingsArrayIdx2Mod
																		+ k * wuLianXuPaiZuGouXingsArrayIdx3Mod
																		+ l * wuLianXuPaiZuGouXingsArrayIdx4Mod + m];
																for (int n = 0; n < duLiPaiZuList.size(); n++) {
																	DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(n);
																	int totalPai = totalPai5 + duLiPaiZu.getTotalPai();
																	int atleastGuiPai = atleastGuiPai5
																			+ duLiPaiZu.getAtleastGuiPai();
																	if (totalPai <= maxShouPai) {
																		if (atleastGuiPai <= maxGuiPai) {
																			wuLianXuPaiZuAndDuLiPaiZuGouXingsArray[i
																					* wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx1Mod
																					+ j * wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx2Mod
																					+ k * wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx3Mod
																					+ l * wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx4Mod
																					+ m * wuLianXuPaiZuAndDuLiPaiZuGouXingsArrayIdx5Mod
																					+ n] = joinLianXuPaiZuDuLiPaiZuGouXingArray(
																							joindGouXingArray, duLiPaiZu
																									.getGouXingArray());
																		}
																	} else {
																		break;
																	}
																}
															}
														} else {
															break;
														}
													}
												}
											} else {
												break;
											}

										}
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}

			}

		}

	}

	private void calculateGouXingForDuLiPaiZu(List<DuLiPaiZu> duLiPaiZuList) {
		duLiPaiZuList.forEach((duLiPaiZu) -> duLiPaiZu.calculateGouXing());
		duLiPaiZuGouXingsArray = new DuLiPaiZuGouXing[duLiPaiZuList.size()][];
		for (int i = 0; i < duLiPaiZuList.size(); i++) {
			DuLiPaiZu duLiPaiZu = duLiPaiZuList.get(i);
			duLiPaiZuGouXingsArray[i] = duLiPaiZu.getGouXingArray();
			duLiPaiZuGouXingsIdxArray[duLiPaiZu.getCode()] = i;
		}
	}

	private List<DuLiPaiZu> calculateDuLiPaiZu(int maxShouPai, int maxGuiPai) {
		List<DuLiPaiZu> result = new ArrayList<>();
		// 低到高--5位单牌个数，4位对子个数，3位刻子个数，3位杠子个数，2位5同牌个数，2位6同牌个数，2位7同牌个数，1位8同牌个数，1位9同牌个数，1位10同牌个数
		for (int code = 1; code < 16777216; code++) {
			int danpai = (code & 31);
			int duizi = ((code >>> 5) & 15);
			int kezi = ((code >>> 9) & 7);
			int gangzi = ((code >>> 12) & 7);
			int wuGe = ((code >>> 15) & 3);
			int liuGe = ((code >>> 17) & 3);
			int qiGe = ((code >>> 19) & 3);
			int baGe = ((code >>> 21) & 1);
			int jiuGe = ((code >>> 22) & 1);
			int shiGe = ((code >>> 23) & 1);

			int totalPai = danpai + duizi * 2 + kezi * 3 + gangzi * 4 + wuGe * 5 + liuGe * 6 + qiGe * 7 + baGe * 8
					+ jiuGe * 9 + shiGe * 10;
			int atleastGuiPai = wuGe * 1 + liuGe * 2 + qiGe * 3 + baGe * 4 + jiuGe * 5 + shiGe * 6;

			if (totalPai <= maxShouPai && atleastGuiPai <= maxGuiPai) {
				result.add(new DuLiPaiZu(code,
						new int[] { danpai, duizi, kezi, gangzi, wuGe, liuGe, qiGe, baGe, jiuGe, shiGe }, totalPai,
						atleastGuiPai));
			}
		}
		Collections.sort(result);
		return result;
	}

	private void calculateLianXuPaiZuZuHeGouXing(List<LianXuPaiZu> lianXuPaiZuList, int maxShouPai, int maxGuiPai,
			int maxShouPaiLianXuPaiZu) {
		if (maxShouPaiLianXuPaiZu >= 1) {
			yiLianXuPaiZuGouXingsArray = new LianXuPaiZuGouXing[lianXuPaiZuList.size()][];
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu lianXuPaiZu = lianXuPaiZuList.get(i);
				yiLianXuPaiZuGouXingsArray[i] = lianXuPaiZu.getGouXingArray();
				if (!lianXuPaiZu.isBigCodeMode()) {
					lianXuPaiZuGouXingsIdxArray[lianXuPaiZu.getSmallCode()] = i;
				} else {
					lianXuPaiZuGouXingsIdxMap.put(lianXuPaiZu.getBigCode(), i);
				}
			}
		}

		if (maxShouPaiLianXuPaiZu >= 2) {
			// 先计算数组规模
			int maxi = 0;
			int maxj = 0;
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai = totalPai1 + zu2.getTotalPai();
					if (totalPai <= maxShouPai) {
						int atleastGuiPai = atleastGuiPai1 + zu2.getAtleastGuiPai();
						if (atleastGuiPai <= maxGuiPai) {
							maxi = (i > maxi) ? i : maxi;
							maxj = (j > maxj) ? j : maxj;
						}
					} else {
						break;
					}
				}
			}
			erLianXuPaiZuGouXingsArrayIdx1Mod = (maxj + 1);

			// 再计算组合
			erLianXuPaiZuGouXingsArray = new LianXuPaiZuZuHeGouXing[(maxi + 1) * erLianXuPaiZuGouXingsArrayIdx1Mod][];
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				LianXuPaiZuGouXing[] gouXingArray1 = zu1.getGouXingArray();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai = totalPai1 + zu2.getTotalPai();
					if (totalPai <= maxShouPai) {
						int atleastGuiPai = atleastGuiPai1 + zu2.getAtleastGuiPai();
						if (atleastGuiPai <= maxGuiPai) {
							LianXuPaiZuZuHeGouXing[] joindGouXingArray = joinLianXuPaiZuGouXingArray(gouXingArray1,
									zu2.getGouXingArray());
							erLianXuPaiZuGouXingsArray[i * erLianXuPaiZuGouXingsArrayIdx1Mod + j] = joindGouXingArray;
						}
					} else {
						break;
					}
				}
			}
		}

		if (maxShouPaiLianXuPaiZu >= 3) {
			// 先计算数组规模
			int maxi = 0;
			int maxj = 0;
			int maxk = 0;
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										maxi = (i > maxi) ? i : maxi;
										maxj = (j > maxj) ? j : maxj;
										maxk = (k > maxk) ? k : maxk;
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}
			}
			sanLianXuPaiZuGouXingsArrayIdx2Mod = (maxk + 1);
			sanLianXuPaiZuGouXingsArrayIdx1Mod = (maxj + 1) * sanLianXuPaiZuGouXingsArrayIdx2Mod;

			// 再计算组合
			sanLianXuPaiZuGouXingsArray = new LianXuPaiZuZuHeGouXing[(maxi + 1) * sanLianXuPaiZuGouXingsArrayIdx1Mod][];
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							LianXuPaiZuZuHeGouXing[] joindGouXingArray = erLianXuPaiZuGouXingsArray[i
									* erLianXuPaiZuGouXingsArrayIdx1Mod + j];
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										sanLianXuPaiZuGouXingsArray[i * sanLianXuPaiZuGouXingsArrayIdx1Mod
												+ j * sanLianXuPaiZuGouXingsArrayIdx2Mod
												+ k] = joinLianXuPaiZuGouXingArray(joindGouXingArray,
														zu3.getGouXingArray());
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}
			}

		}

		if (maxShouPaiLianXuPaiZu >= 4) {
			// 先计算数组规模
			int maxi = 0;
			int maxj = 0;
			int maxk = 0;
			int maxl = 0;
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										for (int l = k; l < lianXuPaiZuList.size(); l++) {
											LianXuPaiZu zu4 = lianXuPaiZuList.get(l);
											int totalPai4 = totalPai3 + zu4.getTotalPai();
											int atleastGuiPai4 = atleastGuiPai3 + zu4.getAtleastGuiPai();
											if (totalPai4 <= maxShouPai) {
												if (atleastGuiPai4 <= maxGuiPai) {
													maxi = (i > maxi) ? i : maxi;
													maxj = (j > maxj) ? j : maxj;
													maxk = (k > maxk) ? k : maxk;
													maxl = (l > maxl) ? l : maxl;
												}
											} else {
												break;
											}
										}
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}
			}
			siLianXuPaiZuGouXingsArrayIdx3Mod = (maxl + 1);
			siLianXuPaiZuGouXingsArrayIdx2Mod = (maxk + 1) * siLianXuPaiZuGouXingsArrayIdx3Mod;
			siLianXuPaiZuGouXingsArrayIdx1Mod = (maxj + 1) * siLianXuPaiZuGouXingsArrayIdx2Mod;

			// 再计算组合
			siLianXuPaiZuGouXingsArray = new LianXuPaiZuZuHeGouXing[(maxi + 1) * siLianXuPaiZuGouXingsArrayIdx1Mod][];
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										LianXuPaiZuZuHeGouXing[] joindGouXingArray = sanLianXuPaiZuGouXingsArray[i
												* sanLianXuPaiZuGouXingsArrayIdx1Mod
												+ j * sanLianXuPaiZuGouXingsArrayIdx2Mod + k];
										for (int l = k; l < lianXuPaiZuList.size(); l++) {
											LianXuPaiZu zu4 = lianXuPaiZuList.get(l);
											int totalPai4 = totalPai3 + zu4.getTotalPai();
											int atleastGuiPai4 = atleastGuiPai3 + zu4.getAtleastGuiPai();
											if (totalPai4 <= maxShouPai) {
												if (atleastGuiPai4 <= maxGuiPai) {
													siLianXuPaiZuGouXingsArray[i * siLianXuPaiZuGouXingsArrayIdx1Mod
															+ j * siLianXuPaiZuGouXingsArrayIdx2Mod
															+ k * siLianXuPaiZuGouXingsArrayIdx3Mod
															+ l] = joinLianXuPaiZuGouXingArray(joindGouXingArray,
																	zu4.getGouXingArray());
												}
											} else {
												break;
											}
										}
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}
			}

		}

		if (maxShouPaiLianXuPaiZu >= 5) {
			// 先计算数组规模
			int maxi = 0;
			int maxj = 0;
			int maxk = 0;
			int maxl = 0;
			int maxm = 0;
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										for (int l = k; l < lianXuPaiZuList.size(); l++) {
											LianXuPaiZu zu4 = lianXuPaiZuList.get(l);
											int totalPai4 = totalPai3 + zu4.getTotalPai();
											int atleastGuiPai4 = atleastGuiPai3 + zu4.getAtleastGuiPai();
											if (totalPai4 <= maxShouPai) {
												if (atleastGuiPai4 <= maxGuiPai) {
													for (int m = l; m < lianXuPaiZuList.size(); m++) {
														LianXuPaiZu zu5 = lianXuPaiZuList.get(m);
														int totalPai5 = totalPai4 + zu5.getTotalPai();
														int atleastGuiPai5 = atleastGuiPai4 + zu5.getAtleastGuiPai();
														if (totalPai5 <= maxShouPai) {
															if (atleastGuiPai5 <= maxGuiPai) {
																maxi = (i > maxi) ? i : maxi;
																maxj = (j > maxj) ? j : maxj;
																maxk = (k > maxk) ? k : maxk;
																maxl = (l > maxl) ? l : maxl;
																maxm = (m > maxm) ? m : maxm;
															}
														} else {
															break;
														}
													}
												}
											} else {
												break;
											}
										}
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}
			}
			wuLianXuPaiZuGouXingsArrayIdx4Mod = (maxm + 1);
			wuLianXuPaiZuGouXingsArrayIdx3Mod = (maxl + 1) * wuLianXuPaiZuGouXingsArrayIdx4Mod;
			wuLianXuPaiZuGouXingsArrayIdx2Mod = (maxk + 1) * wuLianXuPaiZuGouXingsArrayIdx3Mod;
			wuLianXuPaiZuGouXingsArrayIdx1Mod = (maxj + 1) * wuLianXuPaiZuGouXingsArrayIdx2Mod;

			// 再计算组合
			wuLianXuPaiZuGouXingsArray = new LianXuPaiZuZuHeGouXing[(maxi + 1) * wuLianXuPaiZuGouXingsArrayIdx1Mod][];
			for (int i = 0; i < lianXuPaiZuList.size(); i++) {
				LianXuPaiZu zu1 = lianXuPaiZuList.get(i);
				int totalPai1 = zu1.getTotalPai();
				int atleastGuiPai1 = zu1.getAtleastGuiPai();
				for (int j = i; j < lianXuPaiZuList.size(); j++) {
					LianXuPaiZu zu2 = lianXuPaiZuList.get(j);
					int totalPai2 = totalPai1 + zu2.getTotalPai();
					int atleastGuiPai2 = atleastGuiPai1 + zu2.getAtleastGuiPai();
					if (totalPai2 <= maxShouPai) {
						if (atleastGuiPai2 <= maxGuiPai) {
							for (int k = j; k < lianXuPaiZuList.size(); k++) {
								LianXuPaiZu zu3 = lianXuPaiZuList.get(k);
								int totalPai3 = totalPai2 + zu3.getTotalPai();
								int atleastGuiPai3 = atleastGuiPai2 + zu3.getAtleastGuiPai();
								if (totalPai3 <= maxShouPai) {
									if (atleastGuiPai3 <= maxGuiPai) {
										for (int l = k; l < lianXuPaiZuList.size(); l++) {
											LianXuPaiZu zu4 = lianXuPaiZuList.get(l);
											int totalPai4 = totalPai3 + zu4.getTotalPai();
											int atleastGuiPai4 = atleastGuiPai3 + zu4.getAtleastGuiPai();
											if (totalPai4 <= maxShouPai) {
												if (atleastGuiPai4 <= maxGuiPai) {
													LianXuPaiZuZuHeGouXing[] joindGouXingArray = siLianXuPaiZuGouXingsArray[i
															* siLianXuPaiZuGouXingsArrayIdx1Mod
															+ j * siLianXuPaiZuGouXingsArrayIdx2Mod
															+ k * siLianXuPaiZuGouXingsArrayIdx3Mod + l];

													for (int m = l; m < lianXuPaiZuList.size(); m++) {
														LianXuPaiZu zu5 = lianXuPaiZuList.get(m);
														int totalPai5 = totalPai4 + zu5.getTotalPai();
														int atleastGuiPai5 = atleastGuiPai4 + zu5.getAtleastGuiPai();
														if (totalPai5 <= maxShouPai) {
															if (atleastGuiPai5 <= maxGuiPai) {
																wuLianXuPaiZuGouXingsArray[i
																		* wuLianXuPaiZuGouXingsArrayIdx1Mod
																		+ j * wuLianXuPaiZuGouXingsArrayIdx2Mod
																		+ k * wuLianXuPaiZuGouXingsArrayIdx3Mod
																		+ l * wuLianXuPaiZuGouXingsArrayIdx4Mod
																		+ m] = joinLianXuPaiZuGouXingArray(
																				joindGouXingArray,
																				zu5.getGouXingArray());
															}
														} else {
															break;
														}
													}
												}
											} else {
												break;
											}
										}
									}
								} else {
									break;
								}
							}
						}
					} else {
						break;
					}
				}
			}
		}

	}

	private static LianXuPaiZuZuHeGouXing[] joinLianXuPaiZuGouXingArray(LianXuPaiZuZuHeGouXing[] zuHeGouXingArray,
			LianXuPaiZuGouXing[] gouXingArray) {
		LianXuPaiZuZuHeGouXing[] result = new LianXuPaiZuZuHeGouXing[zuHeGouXingArray.length * gouXingArray.length];
		int ri = 0;
		for (int i = 0; i < zuHeGouXingArray.length; i++) {
			LianXuPaiZuZuHeGouXing zuHeGouXing = zuHeGouXingArray[i];
			LianXuPaiZuGouXing[] gouXingArrayFromZuHeGouXing = zuHeGouXing.getZuHePaiZuGouXingArray();
			for (int j = 0; j < gouXingArray.length; j++) {
				LianXuPaiZuGouXing gouXing = gouXingArray[j];
				LianXuPaiZuZuHeGouXing lianXuPaiZuZuHeGouXing = new LianXuPaiZuZuHeGouXing();
				lianXuPaiZuZuHeGouXing.setGouXingCode(zuHeGouXing.getGouXingCode() + gouXing.getGouXingCode());
				LianXuPaiZuGouXing[] zuHePaiZuGouXingArray = new LianXuPaiZuGouXing[gouXingArrayFromZuHeGouXing.length
						+ 1];
				System.arraycopy(gouXingArrayFromZuHeGouXing, 0, zuHePaiZuGouXingArray, 0,
						gouXingArrayFromZuHeGouXing.length);
				zuHePaiZuGouXingArray[zuHePaiZuGouXingArray.length - 1] = gouXing;
				lianXuPaiZuZuHeGouXing.setZuHePaiZuGouXingArray(zuHePaiZuGouXingArray);
				result[ri++] = lianXuPaiZuZuHeGouXing;
			}
		}
		return result;
	}

	private static LianXuPaiZuZuHeGouXing[] joinLianXuPaiZuGouXingArray(LianXuPaiZuGouXing[] gouXingArray1,
			LianXuPaiZuGouXing[] gouXingArray2) {
		LianXuPaiZuZuHeGouXing[] result = new LianXuPaiZuZuHeGouXing[gouXingArray1.length * gouXingArray2.length];
		int ri = 0;
		for (int i = 0; i < gouXingArray1.length; i++) {
			LianXuPaiZuGouXing gouXing1 = gouXingArray1[i];
			for (int j = 0; j < gouXingArray2.length; j++) {
				LianXuPaiZuGouXing gouXing2 = gouXingArray2[j];
				LianXuPaiZuZuHeGouXing lianXuPaiZuZuHeGouXing = new LianXuPaiZuZuHeGouXing();
				lianXuPaiZuZuHeGouXing.setGouXingCode(gouXing1.getGouXingCode() + gouXing2.getGouXingCode());
				LianXuPaiZuGouXing[] zuHePaiZuGouXingArray = new LianXuPaiZuGouXing[] { gouXing1, gouXing2 };
				lianXuPaiZuZuHeGouXing.setZuHePaiZuGouXingArray(zuHePaiZuGouXingArray);
				result[ri++] = lianXuPaiZuZuHeGouXing;
			}
		}
		return result;
	}

	private static LianXuPaiZuDuLiPaiZuZuHeGouXing[] joinLianXuPaiZuDuLiPaiZuGouXingArray(
			LianXuPaiZuGouXing[] lianXuPaiZuGouXingArray, DuLiPaiZuGouXing[] duLiPaiZuGouXingArray) {
		LianXuPaiZuDuLiPaiZuZuHeGouXing[] result = new LianXuPaiZuDuLiPaiZuZuHeGouXing[lianXuPaiZuGouXingArray.length
				* duLiPaiZuGouXingArray.length];
		int ri = 0;
		for (int i = 0; i < lianXuPaiZuGouXingArray.length; i++) {
			LianXuPaiZuGouXing lianXuPaiZuGouXing = lianXuPaiZuGouXingArray[i];
			for (int j = 0; j < duLiPaiZuGouXingArray.length; j++) {
				DuLiPaiZuGouXing duLiPaiZuGouXing = duLiPaiZuGouXingArray[j];
				LianXuPaiZuDuLiPaiZuZuHeGouXing zuHeGouXing = new LianXuPaiZuDuLiPaiZuZuHeGouXing();
				zuHeGouXing.setGouXingCode(lianXuPaiZuGouXing.getGouXingCode() + duLiPaiZuGouXing.getGouXingCode());
				zuHeGouXing.setLianXuPaiZuGouXingArray(new LianXuPaiZuGouXing[] { lianXuPaiZuGouXing });
				zuHeGouXing.setDuLiPaiZuGouXing(duLiPaiZuGouXing);
				result[ri++] = zuHeGouXing;
			}
		}
		return result;
	}

	private static LianXuPaiZuDuLiPaiZuZuHeGouXing[] joinLianXuPaiZuDuLiPaiZuGouXingArray(
			LianXuPaiZuZuHeGouXing[] lianXuPaiZuZuHeGouXingArray, DuLiPaiZuGouXing[] duLiPaiZuGouXingArray) {
		LianXuPaiZuDuLiPaiZuZuHeGouXing[] result = new LianXuPaiZuDuLiPaiZuZuHeGouXing[lianXuPaiZuZuHeGouXingArray.length
				* duLiPaiZuGouXingArray.length];
		int ri = 0;
		for (int i = 0; i < lianXuPaiZuZuHeGouXingArray.length; i++) {
			LianXuPaiZuZuHeGouXing lianXuPaiZuZuHeGouXing = lianXuPaiZuZuHeGouXingArray[i];
			for (int j = 0; j < duLiPaiZuGouXingArray.length; j++) {
				DuLiPaiZuGouXing duLiPaiZuGouXing = duLiPaiZuGouXingArray[j];
				LianXuPaiZuDuLiPaiZuZuHeGouXing zuHeGouXing = new LianXuPaiZuDuLiPaiZuZuHeGouXing();
				zuHeGouXing.setGouXingCode(lianXuPaiZuZuHeGouXing.getGouXingCode() + duLiPaiZuGouXing.getGouXingCode());
				zuHeGouXing.setLianXuPaiZuGouXingArray(lianXuPaiZuZuHeGouXing.getZuHePaiZuGouXingArray());
				zuHeGouXing.setDuLiPaiZuGouXing(duLiPaiZuGouXing);
				result[ri++] = zuHeGouXing;
			}
		}
		return result;
	}

	private static void calculateGouXingForLianXuPaiZu(List<LianXuPaiZu> lianXuPaiZuList) {
		int count = 0;
		int total = 0;
		for (LianXuPaiZu lianXuPaiZu : lianXuPaiZuList) {
			System.out.println("计算第" + (++count) + "个连续牌组的构型");
			lianXuPaiZu.calculateGouXing();
			total += lianXuPaiZu.getGouXingArray().length;
		}
		System.out.println("所有连续牌组总共" + total + "个构型");
	}

	private static List<LianXuPaiZu> calculateLianXuPaiZu(int maxShouPai, int maxGuiPai) {
		List<LianXuPaiZu> list = new ArrayList<>();
		for (int shouPai = 3; shouPai <= maxShouPai; shouPai++) {
			calculateLianXuPaiZuForShouPai(shouPai, maxGuiPai, list);
		}
		list.forEach((lianXuPaiZu) -> lianXuPaiZu.calculateCode());
		Collections.sort(list);
		return list;
	}

	private static void calculateLianXuPaiZuForShouPai(int shouPai, int maxGuiPai, List<LianXuPaiZu> list) {
		int maxLian = (shouPai < 9) ? shouPai : 9;
		for (int lian = 3; lian <= maxLian; lian++) {
			calculateLianXuPaiZuForShouPaiAndLian(shouPai, maxGuiPai, lian, list);
		}
	}

	private static void calculateLianXuPaiZuForShouPaiAndLian(int shouPai, int maxGuiPai, int lian,
			List<LianXuPaiZu> list) {
		int maxPaiForXuHao = 4 + maxGuiPai;
		int mod = maxPaiForXuHao + 1;
		int codeSpace = (int) Math.pow(mod, lian);// 3连，一个花色最多7张牌的编码空间是 8的3次方（此编码是用于计算的压缩编码，尽可能小空间）
		int[] paiQuantityArray = new int[lian];
		for (int code = 0; code < codeSpace; code++) {
			int num = code;
			int totalPai = 0;
			int atleastGuiPai = 0;
			boolean bigCodeMode = false;
			boolean gotOne = true;
			for (int i = 0; i < lian; i++) {
				int shang = num / mod;
				if (shang == 0 && i < (lian - 1)) {
					gotOne = false;
					break;
				}
				int yu = num % mod;
				if (yu == 0) {
					gotOne = false;
					break;
				}
				totalPai += yu;
				if (totalPai > shouPai) {
					gotOne = false;
					break;
				}
				atleastGuiPai += ((yu > 4) ? (yu - 4) : 0);
				if (atleastGuiPai > maxGuiPai) {
					gotOne = false;
					break;
				}
				paiQuantityArray[i] = yu;
				if (yu > 7) {
					bigCodeMode = true;
				}
				num = shang;
			}
			if (!gotOne) {
				continue;
			} else {
				if (totalPai != shouPai) {
					continue;
				}
				LianXuPaiZu lianXuPaiZu = new LianXuPaiZu(paiQuantityArray, totalPai, atleastGuiPai, bigCodeMode);
				list.add(lianXuPaiZu);
			}
		}
		System.out.println(shouPai + "张牌" + lian + "连连续牌组计算完毕！已有" + list.size() + "个结果");
	}

	public int getMaxShouPai() {
		return maxShouPai;
	}

	public int getMaxGuiPai() {
		return maxGuiPai;
	}

}
