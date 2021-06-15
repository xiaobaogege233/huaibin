package com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.JuResultDbo;

public interface JuResultDboRepository extends MongoRepository<JuResultDbo, String> {

	JuResultDbo findOneByGameId(String gameId);

}
