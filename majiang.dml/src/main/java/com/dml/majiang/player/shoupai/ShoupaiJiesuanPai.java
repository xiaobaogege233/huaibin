package com.dml.majiang.player.shoupai;

import com.dml.majiang.pai.MajiangPai;

/**
 * 手牌结算牌。手牌结算的时候分杠子、刻子、顺子、对子、单牌时当的牌。一个牌参与分组会有不同当法，比如就是本牌当本牌或者一万的鬼牌当作4条或者白板当一万。另外要标注是否是最后搞来的一张牌
 *
 * @author Neo
 */
public abstract class ShoupaiJiesuanPai {

    private boolean lastActionPai;

    public abstract String dangType();

    public abstract MajiangPai getYuanPaiType();

    public abstract MajiangPai getZuoyongPaiType();

    public abstract ShoupaiJiesuanPai copy();

    public abstract boolean dangBenPai();

    public boolean isLastActionPai() {
        return lastActionPai;
    }

    public void setLastActionPai(boolean lastActionPai) {
        this.lastActionPai = lastActionPai;
    }

}
