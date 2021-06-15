package com.dml.majiang.pan.guipai;

import java.util.List;
import java.util.Random;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;

public class RandomGuipaiDeterminer implements GuipaiDeterminer {

	private long seed;

	public RandomGuipaiDeterminer() {
	}

	public RandomGuipaiDeterminer(long seed) {
		this.seed = seed;
	}

	@Override
	public void determineGuipai(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		List<MajiangPai> paiTypeList = currentPan.getPaiTypeList();
		Random r = new Random(seed + currentPan.getNo());
		r.nextInt(paiTypeList.size());
		MajiangPai guipaiType = paiTypeList.get(r.nextInt(paiTypeList.size()));
		currentPan.publicGuipaiAndRemoveFromList(guipaiType);
		for (MajiangPlayer majiangPlayer : currentPan.getMajiangPlayerIdMajiangPlayerMap().values()) {
			majiangPlayer.addGuipaiType(guipaiType);
		}
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

}
