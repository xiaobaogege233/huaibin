package com.dml.majiang.player.shoupai;

import com.dml.majiang.pai.MajiangPai;

/**
 * 手牌分组出来的单牌
 *
 * @author Neo
 */
public class ShoupaiDanpai implements ShoupaiMajiangPaiFenZu {
    private MajiangPai danpaiType;
    private ShoupaiJiesuanPai pai;

    @Override
    public void fillAllBlankPaiWithBenPai() {
        if (pai == null) {
            pai = new BenPai(danpaiType);
        }
    }

    @Override
    public void addShoupaiJiesuanPai(ShoupaiJiesuanPai shoupaiJiesuanPai) {
        if (pai == null) {
            pai = shoupaiJiesuanPai;
        }
    }

    @Override
    public ShoupaiDanpai copy() {
        ShoupaiDanpai newShoupaiDanpai = new ShoupaiDanpai();
        newShoupaiDanpai.setDanpaiType(danpaiType);
        newShoupaiDanpai.setPai(pai.copy());
        return newShoupaiDanpai;
    }

    @Override
    public boolean yuanPaiFenZu() {
        return pai.dangBenPai();
    }

    @Override
    public boolean containsLastActionPai() {
        return pai.isLastActionPai();
    }

    @Override
    public int countDangPai(String dangType) {
        int count = 0;
        if (pai.dangType().equals(dangType)) {
            count++;
        }
        return count;
    }

    public MajiangPai getDanpaiType() {
        return danpaiType;
    }

    public void setDanpaiType(MajiangPai danpaiType) {
        this.danpaiType = danpaiType;
    }

    public ShoupaiJiesuanPai getPai() {
        return pai;
    }

    public void setPai(ShoupaiJiesuanPai pai) {
        this.pai = pai;
    }

}
