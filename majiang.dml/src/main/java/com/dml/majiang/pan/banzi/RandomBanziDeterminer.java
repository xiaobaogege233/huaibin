package com.dml.majiang.pan.banzi;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBanziDeterminer implements BanziAndPeiziDeterminer {

    private long seed;

    private Boolean youpeizi;
    private Boolean shuangpeizi;

    public RandomBanziDeterminer() {
    }

    public RandomBanziDeterminer(long seed, Boolean youpeizi, Boolean shuangpeizi) {
        this.seed = seed;
        this.youpeizi = youpeizi;
        this.shuangpeizi = shuangpeizi;
    }

    @Override
    public void determineBanziAndPeizi(Ju ju) {
        Pan currentPan = ju.getCurrentPan();
        List<MajiangPai> paiTypeList = new ArrayList<>();
        paiTypeList.add(MajiangPai.dongfeng);
        paiTypeList.add(MajiangPai.nanfeng);
        paiTypeList.add(MajiangPai.xifeng);
        paiTypeList.add(MajiangPai.beifeng);
        Random r = new Random(seed + currentPan.getNo());
        MajiangPai peiziPai1 = paiTypeList.get(r.nextInt(paiTypeList.size()));
        MajiangPai[] allMajiangPaiArray = MajiangPai.values();
        MajiangPai peiziPai2 = null;
        if (shuangpeizi) {
            switch (peiziPai1.ordinal()) {
                case 27:
                    peiziPai2 = allMajiangPaiArray[28];
                    break;
                case 28:
                    peiziPai2 = allMajiangPaiArray[29];
                    break;
                case 29:
                    peiziPai2 = allMajiangPaiArray[30];
                    break;
                case 30:
                    peiziPai2 = allMajiangPaiArray[27];
                    break;
            }
        }

        if (shuangpeizi) {
            currentPan.publicGuipaiAndNotRemoveFromList(peiziPai1, peiziPai2);
            for (MajiangPlayer majiangPlayer : currentPan.getMajiangPlayerIdMajiangPlayerMap().values()) {
                majiangPlayer.addGuipaiType(peiziPai1, peiziPai2);
            }
        } else {
            currentPan.publicGuipaiAndNotRemoveFromList(peiziPai1);
            for (MajiangPlayer majiangPlayer : currentPan.getMajiangPlayerIdMajiangPlayerMap().values()) {
                majiangPlayer.addGuipaiType(peiziPai1);
            }
        }


    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public Boolean getYoupeizi() {
        return youpeizi;
    }

    public void setYoupeizi(Boolean youpeizi) {
        this.youpeizi = youpeizi;
    }

    public Boolean getShuangpeizi() {
        return shuangpeizi;
    }

    public void setShuangpeizi(Boolean shuangpeizi) {
        this.shuangpeizi = shuangpeizi;
    }
}
