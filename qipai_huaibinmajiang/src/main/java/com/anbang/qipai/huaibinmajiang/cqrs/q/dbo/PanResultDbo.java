package com.anbang.qipai.huaibinmajiang.cqrs.q.dbo;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangPanPlayerResult;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangPanResult;
import com.dml.majiang.pan.frame.PanActionFrame;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@CompoundIndexes({@CompoundIndex(name = "gameId_panNo_index", def = "{'gameId': 1, 'panNo': 1}")})
@Data
@NoArgsConstructor
public class PanResultDbo {
    private String id;
    private String gameId;
    private int panNo;
    private String zhuangPlayerId;
    private boolean hu;
    private boolean zimo;
    private String dianpaoPlayerId;
    private List<ShanxiMajiangPanPlayerResultDbo> playerResultList;
    private long finishTime;
    private PanActionFrame panActionFrame;

    public PanResultDbo(String gameId, HuaibinMajiangPanResult huaibinMajiangPanResult) {
        this.gameId = gameId;
        panNo = huaibinMajiangPanResult.getPan().getNo();
        zhuangPlayerId = huaibinMajiangPanResult.findZhuangPlayerId();
        hu = huaibinMajiangPanResult.isHu();
        zimo = huaibinMajiangPanResult.isZimo();
        dianpaoPlayerId = huaibinMajiangPanResult.getDianpaoPlayerId();
        playerResultList = new ArrayList<>();
        for (HuaibinMajiangPanPlayerResult playerResult : huaibinMajiangPanResult.getPanPlayerResultList()) {
            ShanxiMajiangPanPlayerResultDbo dbo = new ShanxiMajiangPanPlayerResultDbo();
            dbo.setPlayerId(playerResult.getPlayerId());
            dbo.setPlayerResult(playerResult);
            dbo.setPlayer(huaibinMajiangPanResult.findPlayer(playerResult.getPlayerId()));
            playerResultList.add(dbo);
        }

        finishTime = huaibinMajiangPanResult.getPanFinishTime();
    }


}
