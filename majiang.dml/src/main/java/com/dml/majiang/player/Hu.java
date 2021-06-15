package com.dml.majiang.player;

import com.dml.majiang.player.shoupai.ShoupaiPaiXing;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Hu {
    private ShoupaiPaiXing shoupaiPaiXing; // 手牌牌型
    private boolean zimo; // 是否为自摸
    private boolean dianpao; // 是否为点炮
    private String dianpaoPlayerId; // 点炮玩家ID
    private boolean qianggang; // 是否为抢杠

    public Hu(ShoupaiPaiXing shoupaiPaiXing) {
        this.shoupaiPaiXing = shoupaiPaiXing;
    }


}
