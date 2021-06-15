package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.guipai.GuipaiDeterminer;
import com.dml.majiang.player.MajiangPlayer;

public class HuaibinMajiangRandomGuipaiDeterminer implements GuipaiDeterminer {

	private long seed;
	private boolean hongzhonglaizi;

	public HuaibinMajiangRandomGuipaiDeterminer() {
	}

	public HuaibinMajiangRandomGuipaiDeterminer(long seed , boolean hongzhonglaizi) {
		this.seed = seed;
		this.hongzhonglaizi=hongzhonglaizi;
	}

	@Override
	public void determineGuipai(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		if (hongzhonglaizi){
            MajiangPai guipaiType = MajiangPai.hongzhong;
            currentPan.publicGuipaiAndNotRemoveFromList(guipaiType);
            for (MajiangPlayer majiangPlayer : currentPan.getMajiangPlayerIdMajiangPlayerMap().values()) {
                majiangPlayer.addGuipaiType(guipaiType);
            }
        }

	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

}
