package com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dao.PanResultDboDao;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb.repository.PanResultDboRepository;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.PanResultDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongodbPanResultDboDao implements PanResultDboDao {

    @Autowired
    private PanResultDboRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(PanResultDbo panResultDbo) {
        repository.save(panResultDbo);
    }

    @Override
    public PanResultDbo findByGameIdAndPanNo(String gameId, int panNo) {
        return repository.findOneByGameIdAndPanNo(gameId, panNo);
    }

    @Override
    public void removeByTime(long endTime) {
        mongoTemplate.remove(new Query(Criteria.where("finishTime").lt(endTime)), PanResultDbo.class);
    }

}
