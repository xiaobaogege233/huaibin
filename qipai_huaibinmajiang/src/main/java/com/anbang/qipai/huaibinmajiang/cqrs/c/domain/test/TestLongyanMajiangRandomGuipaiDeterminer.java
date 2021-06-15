package com.anbang.qipai.huaibinmajiang.cqrs.c.domain.test;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.guipai.GuipaiDeterminer;
import com.dml.majiang.player.MajiangPlayer;

public class TestLongyanMajiangRandomGuipaiDeterminer implements GuipaiDeterminer {

    private long seed;

    public TestLongyanMajiangRandomGuipaiDeterminer() {
    }

    public TestLongyanMajiangRandomGuipaiDeterminer(long seed ) {
        this.seed = seed;
    }

    @Override
    public void determineGuipai(Ju ju) throws Exception {
        Pan currentPan = ju.getCurrentPan();
//        List<MajiangPai> paiTypeList = currentPan.getPaiTypeList();
//        Random r = new Random(seed + currentPan.getNo());
//        r.nextInt(paiTypeList.size());
        MajiangPai guipaiType = MajiangPai.hongzhong;
//        while (guipaiType.ordinal()>= MajiangPai.chun.ordinal()){
//            guipaiType = paiTypeList.get(r.nextInt(paiTypeList.size()));
//        }
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
