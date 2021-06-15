package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.MajiangPlayer;

/**
 * 手牌型无关结算参数
 *
 * @author Neo
 */
public class ShoupaixingWuguanJiesuancanshu {

    private boolean qingyise;

    public ShoupaixingWuguanJiesuancanshu(MajiangPlayer player,MajiangPai hupai) {
        qingyise=player.allXushupaiWithoutGuipaiInSameCategory(hupai);
    }


    public boolean isQingyise() {
        return qingyise;
    }

    public void setQingyise(boolean qingyise) {
        this.qingyise = qingyise;
    }
}
