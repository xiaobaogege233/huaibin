package com.anbang.qipai.huaibinmajiang.cqrs.q.dbo;

import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class GameFinishVoteDbo {

    private String id;
    @Indexed(unique = false)
    private String gameId;
    private GameFinishVoteValueObject vote;
    private long createTime;

}
