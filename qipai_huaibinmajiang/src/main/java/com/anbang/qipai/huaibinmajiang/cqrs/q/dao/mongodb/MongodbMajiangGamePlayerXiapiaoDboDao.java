package com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb;


import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.MajiangPlayerXiapiaoState;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dao.MajiangGamePlayerXiapiaoDboDao;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGamePlayerXiapiaoDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MongodbMajiangGamePlayerXiapiaoDboDao implements MajiangGamePlayerXiapiaoDboDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addMajiangGamePlayerXiapiaoDbo(MajiangGamePlayerXiapiaoDbo dbo) {
        mongoTemplate.insert(dbo);
    }

    @Override
    public MajiangGamePlayerXiapiaoDbo findLastByGameId(String gameId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gameId").is(gameId));
        Sort sort = new Sort(new Order(Direction.DESC, "panNo"));
        query.with(sort);
        return mongoTemplate.findOne(query, MajiangGamePlayerXiapiaoDbo.class);
    }

    @Override
    public void updateMajiangGamePlayerXiapiaoDbo(String gameId, int panNo,
                                                Map<String, MajiangPlayerXiapiaoState> playerXiapiaoStateMap
    ,Map<String ,Integer> playerpiaofenMap) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gameId").is(gameId));
        query.addCriteria(Criteria.where("panNo").is(panNo));
        Update update = new Update();
        update.set("playerXiapiaoStateMap", playerXiapiaoStateMap);
        update.set("playerpiaofenMap",playerpiaofenMap);
        mongoTemplate.updateFirst(query, update, MajiangGamePlayerXiapiaoDbo.class);
    }

    @Override
    public MajiangGamePlayerXiapiaoDbo findByGameIdAndPanNo(String gameId, int panNo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gameId").is(gameId));
        query.addCriteria(Criteria.where("panNo").is(panNo));
        return mongoTemplate.findOne(query, MajiangGamePlayerXiapiaoDbo.class);
    }

    @Override
    public void removeByTime(long endTime) {
        mongoTemplate.remove(new Query(Criteria.where("createTime").lt(endTime)), MajiangGamePlayerXiapiaoDbo.class);
    }

}
