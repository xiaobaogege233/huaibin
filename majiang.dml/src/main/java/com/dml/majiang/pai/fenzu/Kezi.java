package com.dml.majiang.pai.fenzu;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.shoupai.ShoupaiKeziZu;

public class Kezi implements MajiangPaiFenZu {

    private MajiangPai paiType;

    public Kezi() {
    }

    public Kezi(MajiangPai paiType) {
        this.paiType = paiType;
    }

    @Override
    public MajiangPai[] toPaiArray() {
        return new MajiangPai[]{paiType, paiType, paiType};
    }

    @Override
    public ShoupaiKeziZu generateShoupaiMajiangPaiFenZuSkeleton() {
        ShoupaiKeziZu shoupaiKeziZu = new ShoupaiKeziZu();
        shoupaiKeziZu.setKezi(this);
        return shoupaiKeziZu;
    }

    @Override
    public int countPai(MajiangPai paiType) {
        if (paiType.equals(this.paiType)) {
            return 3;
        } else {
            return 0;
        }
    }

    public MajiangPai getPaiType() {
        return paiType;
    }

    public void setPaiType(MajiangPai paiType) {
        this.paiType = paiType;
    }

}
