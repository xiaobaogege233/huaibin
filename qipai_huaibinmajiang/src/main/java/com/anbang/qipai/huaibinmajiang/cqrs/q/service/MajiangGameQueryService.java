package com.anbang.qipai.huaibinmajiang.cqrs.q.service;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangJuResult;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dao.*;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.huaibinmajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.huaibinmajiang.plan.dao.PlayerInfoDao;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;
import com.dml.mpgame.game.watch.WatchRecord;
import com.dml.mpgame.game.watch.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MajiangGameQueryService {

    @Autowired
    private PlayerInfoDao playerInfoDao;

    @Autowired
    private MajiangGameDboDao majiangGameDboDao;

    @Autowired
    private GameFinishVoteDboDao gameFinishVoteDboDao;

    @Autowired
    private JuResultDboDao juResultDboDao;

    @Autowired
    private WatchRecordDao watchRecordDao;

    @Autowired
    private GameLatestPanActionFrameDboDao gameLatestPanActionFrameDboDao;

    @Autowired
    private PanActionFrameDboDao panActionFrameDboDao;

    @Autowired
    private PanResultDboDao panResultDboDao;

    @Autowired
    private MajiangGamePlayerXiapiaoDboDao majiangGamePlayerXiapiaoDboDao;

    public void removeGameData(long endTime) {
        new Thread() {
            @Override
            public void run() {
                gameFinishVoteDboDao.removeByTime(endTime);
                gameLatestPanActionFrameDboDao.removeByTime(endTime);
                juResultDboDao.removeByTime(endTime);
                majiangGameDboDao.removeByTime(endTime);
                panActionFrameDboDao.removeByTime(endTime);
                panResultDboDao.removeByTime(endTime);
                watchRecordDao.removeByTime(endTime);
                majiangGamePlayerXiapiaoDboDao.removeByTime(endTime);
            }
        }.start();
    }

    public MajiangGameDbo findMajiangGameDboById(String gameId) {
        return majiangGameDboDao.findById(gameId);
    }

    public MajiangGameDbo findMajiangGameDboByIdForPlayBack(String gameId) {
        return majiangGameDboDao.findById(gameId);
    }

    public void newMajiangGame(MajiangGameValueObject majiangGame) {

        Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
        majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
        MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
        majiangGameDboDao.save(majiangGameDbo);
    }

    public void backToGame(String playerId, MajiangGameValueObject majiangGameValueObject) {
        majiangGameDboDao.updatePlayerOnlineState(majiangGameValueObject.getId(), playerId,
                majiangGameValueObject.findPlayerOnlineState(playerId));
        GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
        gameFinishVoteDboDao.update(majiangGameValueObject.getId(), gameFinishVoteValueObject);
    }

    public void joinGame(MajiangGameValueObject majiangGame) {
        Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
        majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
        MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
        majiangGameDboDao.save(majiangGameDbo);
    }

    public void leaveGame(MajiangGameValueObject majiangGame) {
        Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
        majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
        MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
        majiangGameDboDao.save(majiangGameDbo);

        GameFinishVoteValueObject gameFinishVoteValueObject = majiangGame.getVote();
        if (gameFinishVoteValueObject != null) {
            gameFinishVoteDboDao.removeGameFinishVoteDboByGameId(majiangGame.getId());
            GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
            gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
            gameFinishVoteDbo.setGameId(majiangGame.getId());
            gameFinishVoteDbo.setCreateTime(System.currentTimeMillis());
            gameFinishVoteDboDao.save(gameFinishVoteDbo);
        }
        if (majiangGame.getJuResult() != null) {
            HuaibinMajiangJuResult huaibinMajiangJuResult = (HuaibinMajiangJuResult) majiangGame.getJuResult();
            JuResultDbo juResultDbo = new JuResultDbo(majiangGame.getId(), null, huaibinMajiangJuResult);
            juResultDboDao.save(juResultDbo);
        }
    }

    public void finishGameImmediately(MajiangGameValueObject majiangGameValueObject, PanResultDbo panResultDbo) {
        Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
        majiangGameValueObject.allPlayerIds()
                .forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
        MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
        majiangGameDboDao.save(majiangGameDbo);

        if (majiangGameValueObject.getJuResult() != null) {
            HuaibinMajiangJuResult huaibinMajiangJuResult = (HuaibinMajiangJuResult) majiangGameValueObject
                    .getJuResult();
            JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), panResultDbo, huaibinMajiangJuResult);
            juResultDboDao.save(juResultDbo);
        }
    }

    public void finish(MajiangGameValueObject majiangGameValueObject) {
        GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
        if (gameFinishVoteValueObject != null) {
            gameFinishVoteDboDao.removeGameFinishVoteDboByGameId(majiangGameValueObject.getId());
            GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
            gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
            gameFinishVoteDbo.setGameId(majiangGameValueObject.getId());
            gameFinishVoteDbo.setCreateTime(System.currentTimeMillis());
            gameFinishVoteDboDao.save(gameFinishVoteDbo);
        }
        Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
        majiangGameValueObject.allPlayerIds()
                .forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
        MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
        majiangGameDboDao.save(majiangGameDbo);

        if (majiangGameValueObject.getJuResult() != null) {
            HuaibinMajiangJuResult huaibinMajiangJuResult = (HuaibinMajiangJuResult) majiangGameValueObject
                    .getJuResult();
            JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), null, huaibinMajiangJuResult);
            juResultDboDao.save(juResultDbo);
        }
    }

    public void quit(MajiangGameValueObject majiangGameValueObject) {
        Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
        majiangGameValueObject.allPlayerIds()
                .forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
        MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
        majiangGameDboDao.save(majiangGameDbo);
    }

    public void voteToFinish(MajiangGameValueObject majiangGameValueObject) {
        GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
        if (gameFinishVoteValueObject != null) {
            gameFinishVoteDboDao.update(majiangGameValueObject.getId(), gameFinishVoteValueObject);
        }
        Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
        majiangGameValueObject.allPlayerIds()
                .forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
        MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
        majiangGameDboDao.save(majiangGameDbo);

        if (majiangGameValueObject.getJuResult() != null) {
            HuaibinMajiangJuResult huaibinMajiangJuResult = (HuaibinMajiangJuResult) majiangGameValueObject
                    .getJuResult();
            JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), null, huaibinMajiangJuResult);
            juResultDboDao.save(juResultDbo);
        }
    }

    public GameFinishVoteDbo findGameFinishVoteDbo(String gameId) {
        return gameFinishVoteDboDao.findByGameId(gameId);
    }

    public WatchRecord saveWatchRecord(String gameId, Watcher watcher) {
        WatchRecord watchRecord = watchRecordDao.findByGameId(gameId);
        if (watchRecord == null) {
            WatchRecord record = new WatchRecord();
            List<Watcher> watchers = new ArrayList<>();
            watchers.add(watcher);

            record.setGameId(gameId);
            record.setWatchers(watchers);
            record.setCreateTime(System.currentTimeMillis());
            watchRecordDao.save(record);
            return record;
        }

        for (Watcher list : watchRecord.getWatchers()) {
            if (list.getId().equals(watcher.getId())) {
                list.setState(watcher.getState());
                watchRecord.setCreateTime(System.currentTimeMillis());
                watchRecordDao.save(watchRecord);
                return watchRecord;
            }
        }

        watchRecord.getWatchers().add(watcher);
        watchRecord.setCreateTime(System.currentTimeMillis());
        watchRecordDao.save(watchRecord);
        return watchRecord;
    }

    /**
     * 查询观战中的玩家
     */
    public boolean findByPlayerId(String gameId, String playerId) {
        if (watchRecordDao.findByPlayerId(gameId, playerId, "join") != null) {
            return true;
        }
        return false;
    }

}
