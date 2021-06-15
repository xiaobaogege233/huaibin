package com.anbang.qipai.huaibinmajiang.web.vo;

import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;
import com.dml.mpgame.game.extend.vote.VoteOption;
import com.dml.mpgame.game.extend.vote.VoteResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;
@Data
@NoArgsConstructor
public class GameFinishVoteVO {
    private String sponsorId;

    private Set<String> votePlayerIds;

    private Map<String, VoteOption> playerIdVoteOptionMap;

    private VoteResult result;

    private long startTime;

    private long remainTime;

    public GameFinishVoteVO(GameFinishVoteValueObject vote) {
        sponsorId = vote.getSponsorId();
        votePlayerIds = vote.getVotePlayerIds();
        playerIdVoteOptionMap = vote.getPlayerIdVoteOptionMap();
        result = vote.getResult();
        startTime = vote.getStartTime();
        long endTime = vote.getEndTime();
        long currentTime = System.currentTimeMillis();
        remainTime = currentTime > endTime ? 0 : endTime - currentTime;
    }


}
