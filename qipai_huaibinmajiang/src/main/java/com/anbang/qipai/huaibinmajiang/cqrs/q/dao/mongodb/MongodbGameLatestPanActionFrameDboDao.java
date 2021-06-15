package com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dao.GameLatestPanActionFrameDboDao;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb.repository.GameLatestPanActionFrameDboRepository;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.dml.majiang.pan.frame.PanActionFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongodbGameLatestPanActionFrameDboDao implements GameLatestPanActionFrameDboDao {

    @Autowired
    private GameLatestPanActionFrameDboRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public GameLatestPanActionFrameDbo findById(String id) {
        return repository.findOne(id);
    }

    @Override
    public void save(String id, PanActionFrame panActionFrame) {
        GameLatestPanActionFrameDbo dbo = new GameLatestPanActionFrameDbo();
        dbo.setId(id);
        dbo.setPanActionFrame(panActionFrame);
        repository.save(dbo);
    }

    @Override
    public void removeByTime(long endTime) {
        mongoTemplate.remove(new Query(Criteria.where("panActionFrame.actionTime").lt(endTime)), GameLatestPanActionFrameDbo.class);
    }

}
