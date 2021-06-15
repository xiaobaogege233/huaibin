package com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dao.mongodb.repository.MajiangGameDboRepository;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongodbMajiangGameDboDao implements MajiangGameDboDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MajiangGameDboRepository repository;

    @Override
    public MajiangGameDbo findById(String id) {
        return repository.findOne(id);
    }

    @Override
    public void save(MajiangGameDbo majiangGameDbo) {
        repository.save(majiangGameDbo);
    }

    @Override
    public void updatePlayerOnlineState(String id, String playerId, GamePlayerOnlineState onlineState) {
        MajiangGameDbo majiangGameDbo = repository.findOne(id);
        majiangGameDbo.getPlayers().forEach((player) -> {
            if (player.getPlayerId().equals(playerId)) {
                player.setOnlineState(onlineState);
            }
        });
        repository.save(majiangGameDbo);
    }

    @Override
    public void removeByTime(long endTime) {
        mongoTemplate.remove(new Query(Criteria.where("createTime").lt(endTime)), MajiangGameDbo.class);
    }

}
