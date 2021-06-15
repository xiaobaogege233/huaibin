package com.dml.majiang.pai.fenzu;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.shoupai.ShoupaiShunziZu;

public class Shunzi implements MajiangPaiFenZu {

    private MajiangPai pai1;
    private MajiangPai pai2;
    private MajiangPai pai3;

    public Shunzi() {
    }

    public Shunzi(MajiangPai pai1, MajiangPai pai2, MajiangPai pai3) {
        this.pai1 = pai1;
        this.pai2 = pai2;
        this.pai3 = pai3;
    }

    @Override
    public ShoupaiShunziZu generateShoupaiMajiangPaiFenZuSkeleton() {
        ShoupaiShunziZu shoupaiShunziZu = new ShoupaiShunziZu();
        shoupaiShunziZu.setShunzi(this);
        return shoupaiShunziZu;
    }

    @Override
    public int countPai(MajiangPai paiType) {
        if (paiType.equals(pai1) || paiType.equals(pai2) || paiType.equals(pai3)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public MajiangPai[] toPaiArray() {
        return new MajiangPai[]{pai1, pai2, pai3};
    }

    public MajiangPai getPai1() {
        return pai1;
    }

    public void setPai1(MajiangPai pai1) {
        this.pai1 = pai1;
    }

    public MajiangPai getPai2() {
        return pai2;
    }

    public void setPai2(MajiangPai pai2) {
        this.pai2 = pai2;
    }

    public MajiangPai getPai3() {
        return pai3;
    }

    public void setPai3(MajiangPai pai3) {
        this.pai3 = pai3;
    }

    public boolean equals(Object o) {
        Shunzi s = (Shunzi) o;
        return (pai1.equals(s.pai1) && pai2.equals(s.pai2) && pai3.equals(s.pai3));
    }

    public int hashCode() {
        return pai1.hashCode() * 100 + pai2.hashCode() * 10 + pai3.hashCode();
    }

}
