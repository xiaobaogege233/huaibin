package com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb.repository.JuResultDboRepository;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.JuResultDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongodbJuResultDboDao implements JuResultDboDao {

    @Autowired
    private JuResultDboRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(JuResultDbo juResultDbo) {
        repository.save(juResultDbo);
    }

    @Override
    public JuResultDbo findByGameId(String gameId) {
        return repository.findOneByGameId(gameId);
    }

    @Override
    public void removeByTime(long endTime) {
        mongoTemplate.remove(new Query(Criteria.where("finishTime").lt(endTime)), JuResultDbo.class);
    }

}
