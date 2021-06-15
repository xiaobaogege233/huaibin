package com.dml.majiang.player.shoupai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.Danpai;
import com.dml.majiang.pai.fenzu.Duizi;
import com.dml.majiang.pai.fenzu.Gangzi;
import com.dml.majiang.pai.fenzu.Kezi;
import com.dml.majiang.pai.fenzu.MajiangPaiFenZu;
import com.dml.majiang.pai.fenzu.Shunzi;

public class PaiXing {

	private List<Danpai> danpaiList;
	private List<Duizi> duiziList;
	private List<Kezi> keziList;
	private List<Gangzi> gangziList;
	private List<Shunzi> shunziList;

	public static PaiXing combine(PaiXing[] paiXingArray) {
		PaiXing newPaiXing = new PaiXing();
		List<Danpai> newDanpaiList = new ArrayList<>();
		List<Duizi> newDuiziList = new ArrayList<>();
		List<Kezi> newKeziList = new ArrayList<>();
		List<Gangzi> newGangziList = new ArrayList<>();
		List<Shunzi> newShunziList = new ArrayList<>();
		newPaiXing.setDanpaiList(newDanpaiList);
		newPaiXing.setDuiziList(newDuiziList);
		newPaiXing.setGangziList(newGangziList);
		newPaiXing.setKeziList(newKeziList);
		newPaiXing.setShunziList(newShunziList);
		for (int i = 0; i < paiXingArray.length; i++) {
			PaiXing paiXing = paiXingArray[i];
			newDanpaiList.addAll(paiXing.danpaiList);
			newDuiziList.addAll(paiXing.duiziList);
			newKeziList.addAll(paiXing.keziList);
			newGangziList.addAll(paiXing.gangziList);
			newShunziList.addAll(paiXing.shunziList);
		}
		return newPaiXing;
	}

	public ShoupaiPaiXing generateAllBenPaiShoupaiPaiXing() {
		ShoupaiPaiXing shoupaiPaiXing = new ShoupaiPaiXing();
		List<ShoupaiDanpai> danpaiList = new ArrayList<>();
		List<ShoupaiDuiziZu> duiziList = new ArrayList<>();
		List<ShoupaiKeziZu> keziList = new ArrayList<>();
		List<ShoupaiGangziZu> gangziList = new ArrayList<>();
		List<ShoupaiShunziZu> shunziList = new ArrayList<>();
		shoupaiPaiXing.setDanpaiList(danpaiList);
		shoupaiPaiXing.setDuiziList(duiziList);
		shoupaiPaiXing.setGangziList(gangziList);
		shoupaiPaiXing.setKeziList(keziList);
		shoupaiPaiXing.setShunziList(shunziList);

		for (Danpai danpai : this.danpaiList) {
			ShoupaiDanpai shoupaiDanpai = danpai.generateShoupaiMajiangPaiFenZuSkeleton();
			shoupaiDanpai.fillAllBlankPaiWithBenPai();
			danpaiList.add(shoupaiDanpai);
		}
		for (Duizi duizi : this.duiziList) {
			ShoupaiDuiziZu shoupaiDuiziZu = duizi.generateShoupaiMajiangPaiFenZuSkeleton();
			shoupaiDuiziZu.fillAllBlankPaiWithBenPai();
			duiziList.add(shoupaiDuiziZu);
		}
		for (Kezi kezi : this.keziList) {
			ShoupaiKeziZu shoupaiKeziZu = kezi.generateShoupaiMajiangPaiFenZuSkeleton();
			shoupaiKeziZu.fillAllBlankPaiWithBenPai();
			keziList.add(shoupaiKeziZu);
		}
		for (Gangzi gangzi : this.gangziList) {
			ShoupaiGangziZu shoupaiGangziZu = gangzi.generateShoupaiMajiangPaiFenZuSkeleton();
			shoupaiGangziZu.fillAllBlankPaiWithBenPai();
			gangziList.add(shoupaiGangziZu);
		}
		for (Shunzi shunzi : this.shunziList) {
			ShoupaiShunziZu shoupaiShunziZu = shunzi.generateShoupaiMajiangPaiFenZuSkeleton();
			shoupaiShunziZu.fillAllBlankPaiWithBenPai();
			shunziList.add(shoupaiShunziZu);
		}
		return shoupaiPaiXing;
	}

	/**
	 * 输入为各种当，比如鬼牌当、白板当等等<br/>
	 * 返回list是因为在确定当牌方案的情况下,对于某个鬼牌当的花色有多张牌,又分属不同组的情况（比如两个一万，一个属于一万对子，一个属于一二三万顺子）,产生多个ShoupaiPaiXing
	 * 
	 * @param dangPaiArray
	 * @return
	 */
	public List<ShoupaiPaiXing> generateShoupaiPaiXingByDangPai(ShoupaiJiesuanPai[] dangPaiArray) {
		List<ShoupaiPaiXing> shoupaiPaiXingList = new ArrayList<>();
		int allFenZuCount = danpaiList.size() + duiziList.size() + keziList.size() + gangziList.size()
				+ shunziList.size();
		int dangPaiCount = dangPaiArray.length;
		MajiangPaiFenZu[] allFenZuArray = new MajiangPaiFenZu[allFenZuCount];
		int i = 0;
		for (MajiangPaiFenZu fenZu : danpaiList) {
			allFenZuArray[i++] = fenZu;
		}
		for (MajiangPaiFenZu fenZu : duiziList) {
			allFenZuArray[i++] = fenZu;
		}
		for (MajiangPaiFenZu fenZu : keziList) {
			allFenZuArray[i++] = fenZu;
		}
		for (MajiangPaiFenZu fenZu : gangziList) {
			allFenZuArray[i++] = fenZu;
		}
		for (MajiangPaiFenZu fenZu : shunziList) {
			allFenZuArray[i++] = fenZu;
		}

		List<Integer>[] dangPaiDangFenZuIdxListArray = new List[dangPaiCount];
		for (int j = 0; j < dangPaiCount; j++) {
			List<Integer> dangPaiDangFenZuIdxList = new ArrayList<>();
			dangPaiDangFenZuIdxListArray[j] = dangPaiDangFenZuIdxList;
			ShoupaiJiesuanPai dangPai = dangPaiArray[j];
			for (int k = 0; k < allFenZuCount; k++) {
				MajiangPaiFenZu fenZu = allFenZuArray[k];
				if (fenZu.countPai(dangPai.getZuoyongPaiType()) > 0) {
					dangPaiDangFenZuIdxList.add(k);
				}
			}
		}

		int maxZuheCode = 1;
		int[] dangPaiDangFenZuIdxListCountArray = new int[dangPaiCount];
		for (int j = 0; j < dangPaiCount; j++) {
			int count = dangPaiDangFenZuIdxListArray[j].size();
			dangPaiDangFenZuIdxListCountArray[j] = count;
			maxZuheCode *= count;
		}

		int[] modArray = new int[dangPaiCount];
		int mod = 1;
		for (int j = 0; j < dangPaiDangFenZuIdxListCountArray.length; j++) {
			modArray[dangPaiCount - 1 - j] = mod;
			mod *= dangPaiDangFenZuIdxListCountArray[dangPaiDangFenZuIdxListCountArray.length - 1 - j];
		}

		int[] dangPaiDangFenZuIdxZuheArray = new int[dangPaiCount];
		for (int code = 0; code < maxZuheCode; code++) {
			int temp = code;
			for (int j = 0; j < modArray.length; j++) {
				int shang = temp / modArray[j];
				dangPaiDangFenZuIdxZuheArray[j] = dangPaiDangFenZuIdxListArray[j].get(shang);
				temp = temp % modArray[j];
			}
			// dangPaiDangFenZuIdxZuheArray是一种组合结果，需要先对其验证是否超当
			// 对哪个分组的什么牌当了几次，这个次数是否合法是需要验证的
			Map<Integer, Map<MajiangPai, Integer>> map = new HashMap<>();
			for (int k = 0; k < dangPaiCount; k++) {
				ShoupaiJiesuanPai dangPai = dangPaiArray[k];
				int fenZuIdx = dangPaiDangFenZuIdxZuheArray[k];
				Map<MajiangPai, Integer> mapT = map.get(fenZuIdx);
				if (mapT == null) {
					mapT = new HashMap<>();
					map.put(fenZuIdx, mapT);
				}
				Integer dangCount = mapT.get(dangPai.getZuoyongPaiType());
				if (dangCount == null) {
					dangCount = 0;
				}
				mapT.put(dangPai.getZuoyongPaiType(), dangCount + 1);
			}
			boolean allSuccess = true;
			for (Entry<Integer, Map<MajiangPai, Integer>> entry : map.entrySet()) {
				MajiangPaiFenZu fenZu = allFenZuArray[entry.getKey()];
				Map<MajiangPai, Integer> mapT = entry.getValue();
				boolean success = true;
				for (Entry<MajiangPai, Integer> entryT : mapT.entrySet()) {
					if (fenZu.countPai(entryT.getKey()) < entryT.getValue()) {// 不合法
						success = false;
						break;
					}
				}
				if (!success) {
					allSuccess = false;
					break;
				}
			}

			if (allSuccess) {// 验证通过
				// 对该种组合生成结果
				ShoupaiPaiXing shoupaiPaiXing = new ShoupaiPaiXing();
				List<ShoupaiDanpai> danpaiList = new ArrayList<>();
				List<ShoupaiDuiziZu> duiziList = new ArrayList<>();
				List<ShoupaiKeziZu> keziList = new ArrayList<>();
				List<ShoupaiGangziZu> gangziList = new ArrayList<>();
				List<ShoupaiShunziZu> shunziList = new ArrayList<>();
				shoupaiPaiXing.setDanpaiList(danpaiList);
				shoupaiPaiXing.setDuiziList(duiziList);
				shoupaiPaiXing.setGangziList(gangziList);
				shoupaiPaiXing.setKeziList(keziList);
				shoupaiPaiXing.setShunziList(shunziList);

				for (int j = 0; j < allFenZuCount; j++) {
					MajiangPaiFenZu fenZu = allFenZuArray[j];
					ShoupaiMajiangPaiFenZu shoupaiFenZu = fenZu.generateShoupaiMajiangPaiFenZuSkeleton();
					for (int k = 0; k < dangPaiCount; k++) {
						int guipaiDangFenZuIdx = dangPaiDangFenZuIdxZuheArray[k];
						if (guipaiDangFenZuIdx == j) {
							ShoupaiJiesuanPai dangPai = dangPaiArray[k];
							shoupaiFenZu.addShoupaiJiesuanPai(dangPai);
						}
					}
					shoupaiFenZu.fillAllBlankPaiWithBenPai();
					if (shoupaiFenZu instanceof ShoupaiDanpai) {
						danpaiList.add((ShoupaiDanpai) shoupaiFenZu);
					} else if (shoupaiFenZu instanceof ShoupaiDuiziZu) {
						duiziList.add((ShoupaiDuiziZu) shoupaiFenZu);
					} else if (shoupaiFenZu instanceof ShoupaiKeziZu) {
						keziList.add((ShoupaiKeziZu) shoupaiFenZu);
					} else if (shoupaiFenZu instanceof ShoupaiGangziZu) {
						gangziList.add((ShoupaiGangziZu) shoupaiFenZu);
					} else if (shoupaiFenZu instanceof ShoupaiShunziZu) {
						shunziList.add((ShoupaiShunziZu) shoupaiFenZu);
					}

				}
				shoupaiPaiXingList.add(shoupaiPaiXing);
			}

		}
		return shoupaiPaiXingList;
	}

	public List<Danpai> getDanpaiList() {
		return danpaiList;
	}

	public void setDanpaiList(List<Danpai> danpaiList) {
		this.danpaiList = danpaiList;
	}

	public List<Duizi> getDuiziList() {
		return duiziList;
	}

	public void setDuiziList(List<Duizi> duiziList) {
		this.duiziList = duiziList;
	}

	public List<Kezi> getKeziList() {
		return keziList;
	}

	public void setKeziList(List<Kezi> keziList) {
		this.keziList = keziList;
	}

	public List<Gangzi> getGangziList() {
		return gangziList;
	}

	public void setGangziList(List<Gangzi> gangziList) {
		this.gangziList = gangziList;
	}

	public List<Shunzi> getShunziList() {
		return shunziList;
	}

	public void setShunziList(List<Shunzi> shunziList) {
		this.shunziList = shunziList;
	}

}
