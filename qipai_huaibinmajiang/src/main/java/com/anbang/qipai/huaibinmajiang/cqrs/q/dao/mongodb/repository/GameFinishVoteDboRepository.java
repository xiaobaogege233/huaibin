package com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.GameFinishVoteDbo;

public interface GameFinishVoteDboRepository extends MongoRepository<GameFinishVoteDbo, String> {

	GameFinishVoteDbo findOneByGameId(String gameId);

}
