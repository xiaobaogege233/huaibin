package com.anbang.qipai.huaibinmajiang.cqrs.q.dbo;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangJuResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndexes({ @CompoundIndex(name = "gameId_1", def = "{'gameId': 1}") })
@Data
@NoArgsConstructor
public class JuResultDbo {

    private String id;
    @Indexed(unique = false)
    private String gameId;
    private PanResultDbo lastPanResult;
    private HuaibinMajiangJuResult juResult;
    private long finishTime;

    public JuResultDbo(String gameId, PanResultDbo lastPanResult, HuaibinMajiangJuResult juResult) {
        this.gameId = gameId;
        this.lastPanResult = lastPanResult;
        this.juResult = juResult;
        finishTime = System.currentTimeMillis();
    }

}
