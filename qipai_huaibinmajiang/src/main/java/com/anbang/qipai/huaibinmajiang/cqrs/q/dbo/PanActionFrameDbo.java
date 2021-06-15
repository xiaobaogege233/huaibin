package com.anbang.qipai.huaibinmajiang.cqrs.q.dbo;

import com.dml.majiang.pan.frame.PanActionFrame;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndexes({@CompoundIndex(name = "gameId_1_panNo_1", def = "{'gameId': 1, 'panNo': 1}")})
@Data
@NoArgsConstructor
public class PanActionFrameDbo {
    private String id;
    private String gameId;
    private int panNo;
    private int actionNo;
    private PanActionFrame panActionFrame;

    public PanActionFrameDbo(String gameId, int panNo, int actionNo) {
        this.gameId = gameId;
        this.panNo = panNo;
        this.actionNo = actionNo;
    }


}
