package com.dml.majiang.player.shoupai;

import com.dml.majiang.pai.fenzu.Shunzi;

public class ShoupaiShunziZu implements ShoupaiMajiangPaiFenZu {
    private Shunzi shunzi;
    private ShoupaiJiesuanPai pai1;
    private ShoupaiJiesuanPai pai2;
    private ShoupaiJiesuanPai pai3;

    @Override
    public ShoupaiShunziZu copy() {
        ShoupaiShunziZu newShoupaiShunziZu = new ShoupaiShunziZu();
        newShoupaiShunziZu.setShunzi(shunzi);
        newShoupaiShunziZu.setPai1(pai1.copy());
        newShoupaiShunziZu.setPai2(pai2.copy());
        newShoupaiShunziZu.setPai3(pai3.copy());
        return newShoupaiShunziZu;
    }

    @Override
    public void fillAllBlankPaiWithBenPai() {
        if (pai1 == null) {
            pai1 = new BenPai(shunzi.getPai1());
        }
        if (pai2 == null) {
            pai2 = new BenPai(shunzi.getPai2());
        }
        if (pai3 == null) {
            pai3 = new BenPai(shunzi.getPai3());
        }
    }

    @Override
    public void addShoupaiJiesuanPai(ShoupaiJiesuanPai shoupaiJiesuanPai) {
        if (shoupaiJiesuanPai.getZuoyongPaiType().equals(shunzi.getPai1())) {
            pai1 = shoupaiJiesuanPai;
            return;
        }
        if (shoupaiJiesuanPai.getZuoyongPaiType().equals(shunzi.getPai2())) {
            pai2 = shoupaiJiesuanPai;
            return;
        }
        if (shoupaiJiesuanPai.getZuoyongPaiType().equals(shunzi.getPai3())) {
            pai3 = shoupaiJiesuanPai;
            return;
        }
    }

    @Override
    public boolean containsLastActionPai() {
        return (pai1.isLastActionPai() || pai2.isLastActionPai() || pai3.isLastActionPai());
    }

    public boolean containsLastActionPaiAndIsJia() {
        return (pai1.isLastActionPai() || pai2.isLastActionPai() || pai3.isLastActionPai()) && pai2.isLastActionPai();
    }

    @Override
    public boolean yuanPaiFenZu() {
        return (pai1.dangBenPai() && pai2.dangBenPai() && pai3.dangBenPai());
    }

    @Override
    public int countDangPai(String dangType) {
        int count = 0;
        if (pai1.dangType().equals(dangType)) {
            count++;
        }
        if (pai2.dangType().equals(dangType)) {
            count++;
        }
        if (pai3.dangType().equals(dangType)) {
            count++;
        }
        return count;
    }

    public Shunzi getShunzi() {
        return shunzi;
    }

    public void setShunzi(Shunzi shunzi) {
        this.shunzi = shunzi;
    }

    public ShoupaiJiesuanPai getPai1() {
        return pai1;
    }

    public void setPai1(ShoupaiJiesuanPai pai1) {
        this.pai1 = pai1;
    }

    public ShoupaiJiesuanPai getPai2() {
        return pai2;
    }

    public void setPai2(ShoupaiJiesuanPai pai2) {
        this.pai2 = pai2;
    }

    public ShoupaiJiesuanPai getPai3() {
        return pai3;
    }

    public void setPai3(ShoupaiJiesuanPai pai3) {
        this.pai3 = pai3;
    }

}
