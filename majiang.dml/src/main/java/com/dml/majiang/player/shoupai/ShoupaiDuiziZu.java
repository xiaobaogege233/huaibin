package com.dml.majiang.player.shoupai;

import com.dml.majiang.pai.MajiangPai;

/**
 * 手牌对子组
 *
 * @author Neo
 */
public class ShoupaiDuiziZu implements ShoupaiMajiangPaiFenZu {

    private MajiangPai duiziType;

    private ShoupaiJiesuanPai pai1;

    private ShoupaiJiesuanPai pai2;

    @Override
    public ShoupaiDuiziZu copy() {
        ShoupaiDuiziZu newShoupaiDuiziZu = new ShoupaiDuiziZu();
        newShoupaiDuiziZu.setDuiziType(duiziType);
        newShoupaiDuiziZu.setPai1(pai1.copy());
        newShoupaiDuiziZu.setPai2(pai2.copy());
        return newShoupaiDuiziZu;
    }

    @Override
    public void fillAllBlankPaiWithBenPai() {
        if (pai1 == null) {
            pai1 = new BenPai(duiziType);
        }
        if (pai2 == null) {
            pai2 = new BenPai(duiziType);
        }
    }

    @Override
    public void addShoupaiJiesuanPai(ShoupaiJiesuanPai shoupaiJiesuanPai) {
        if (pai1 == null) {
            pai1 = shoupaiJiesuanPai;
            return;
        }
        if (pai2 == null) {
            pai2 = shoupaiJiesuanPai;
            return;
        }
    }

    @Override
    public boolean containsLastActionPai() {
        return (pai1.isLastActionPai() || pai2.isLastActionPai());
    }

    @Override
    public boolean yuanPaiFenZu() {
        return (pai1.dangBenPai() && pai2.dangBenPai());
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
        return count;
    }

    public MajiangPai getDuiziType() {
        return duiziType;
    }

    public void setDuiziType(MajiangPai duiziType) {
        this.duiziType = duiziType;
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

}
