package com.anbang.qipai.huaibinmajiang.cqrs.q.dbo;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.MajiangPlayerXiapiaoState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
public class MajiangGamePlayerXiapiaoDbo {
    private String id;
    private String gameId;
    private int panNo;
    private Map<String, MajiangPlayerXiapiaoState> playerXiapiaoStateMap;
    private Map<String ,Integer> playerpiaofenMap;
    private long createTime;

    public MajiangGamePlayerXiapiaoDbo(MajiangGameValueObject majiangGame) {
        this.gameId = majiangGame.getId();
        this.panNo = majiangGame.getPanNo();
        this.playerXiapiaoStateMap = majiangGame.getPlayerXiapiaoStateMap();
        this.playerpiaofenMap=majiangGame.getPlayerpiaofenMap();
        createTime = System.currentTimeMillis();
    }

}
