package com.dml.majiang.pan.avaliablepai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;

public class NoHuapaiRandomAvaliablePaiFiller implements AvaliablePaiFiller {

	private long seed;

	public NoHuapaiRandomAvaliablePaiFiller() {
	}

	public NoHuapaiRandomAvaliablePaiFiller(long seed) {
		this.seed = seed;
	}

	@Override
	public void fillAvaliablePai(Ju ju) throws Exception {
		Set<MajiangPai> notPlaySet = new HashSet<>();
		notPlaySet.add(MajiangPai.chun);
		notPlaySet.add(MajiangPai.xia);
		notPlaySet.add(MajiangPai.qiu);
		notPlaySet.add(MajiangPai.dong);
		notPlaySet.add(MajiangPai.mei);
		notPlaySet.add(MajiangPai.lan);
		notPlaySet.add(MajiangPai.zhu);
		notPlaySet.add(MajiangPai.ju);
		MajiangPai[] allMajiangPaiArray = MajiangPai.values();
		List<MajiangPai> playPaiTypeList = new ArrayList<>();
		for (int i = 0; i < allMajiangPaiArray.length; i++) {
			MajiangPai pai = allMajiangPaiArray[i];
			if (!notPlaySet.contains(pai)) {
				playPaiTypeList.add(pai);
			}
		}

		List<MajiangPai> allPaiList = new ArrayList<>();
		playPaiTypeList.forEach((paiType) -> {
			for (int i = 0; i < 4; i++) {
				allPaiList.add(paiType);
			}
		});
		Random r = new Random(seed);
		r.nextInt(allPaiList.size());
		Collections.shuffle(allPaiList, r);
		ju.getCurrentPan().setAvaliablePaiList(allPaiList);
		ju.getCurrentPan().setPaiTypeList(playPaiTypeList);
		seed++;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

}
