package com.anbang.qipai.huaibinmajiang.cqrs.q.dao;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;

public interface GameFinishVoteDboDao {

    void save(GameFinishVoteDbo gameFinishVoteDbo);

    void update(String gameId, GameFinishVoteValueObject gameFinishVoteValueObject);

    GameFinishVoteDbo findByGameId(String gameId);

    void removeGameFinishVoteDboByGameId(String gameId);

    void removeByTime(long endTime);
}
