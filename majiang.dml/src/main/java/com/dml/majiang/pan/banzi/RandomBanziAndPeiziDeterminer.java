package com.dml.majiang.pan.banzi;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;

import java.util.List;
import java.util.Random;

public class RandomBanziAndPeiziDeterminer implements BanziAndPeiziDeterminer {

    private long seed;

    private Boolean peiziWanfa;

    public RandomBanziAndPeiziDeterminer() {
    }

    public RandomBanziAndPeiziDeterminer(long seed) {
        this.seed = seed;
    }

    public RandomBanziAndPeiziDeterminer(long seed, Boolean banziWanfa) {
        this.seed = seed;
        this.peiziWanfa = banziWanfa;
    }

    @Override
    public void determineBanziAndPeizi(Ju ju) {
        Pan currentPan = ju.getCurrentPan();
        List<MajiangPai> paiTypeList = currentPan.getPaiTypeList();
        Random r = new Random(seed + currentPan.getNo());
        MajiangPai banziPai = paiTypeList.get(r.nextInt(paiTypeList.size()));
        MajiangPai[] allMajiangPaiArray = MajiangPai.values();
        MajiangPai peiziPai = null;
        if (peiziWanfa) {
            switch (banziPai.ordinal()) {
                case 8:
                    peiziPai = allMajiangPaiArray[0];
                    break;
                case 17:
                    peiziPai = allMajiangPaiArray[9];
                    break;
                case 26:
                    peiziPai = allMajiangPaiArray[18];
                    break;
                case 30:
                    peiziPai = allMajiangPaiArray[27];
                    break;
                case 33:
                    peiziPai = allMajiangPaiArray[31];
                    break;
                default:
                    peiziPai = allMajiangPaiArray[banziPai.ordinal() + 1];
                    break;
            }
        }
        currentPan.publicBanziPeiziAndRemoveBanziFromList(banziPai, peiziPai);
        for (MajiangPlayer majiangPlayer : currentPan.getMajiangPlayerIdMajiangPlayerMap().values()) {
            majiangPlayer.addBanziAndPeiziType(banziPai, peiziPai);
        }
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public Boolean getPeiziWanfa() {
        return peiziWanfa;
    }

    public void setPeiziWanfa(Boolean peiziWanfa) {
        this.peiziWanfa = peiziWanfa;
    }
}
