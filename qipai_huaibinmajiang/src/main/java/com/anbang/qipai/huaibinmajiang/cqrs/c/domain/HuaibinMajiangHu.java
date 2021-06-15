package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.player.Hu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HuaibinMajiangHu extends Hu {

    private HuaibinMajiangHushu hushu; // 胡分
    private boolean huxingHu;// 三财神十三幺不是胡形的胡

    public HuaibinMajiangHu(ShoupaiPaiXing shoupaiPaiXing, HuaibinMajiangHushu hushu) {
        super(shoupaiPaiXing);
        this.hushu = hushu;
        this.huxingHu = true;
    }

    public HuaibinMajiangHu(HuaibinMajiangHushu hushu) {
        this.hushu = hushu;
        this.huxingHu = false;
    }


}
