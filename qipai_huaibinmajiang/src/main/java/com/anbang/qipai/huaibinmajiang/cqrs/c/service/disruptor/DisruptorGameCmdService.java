package com.anbang.qipai.huaibinmajiang.cqrs.c.service.disruptor;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.OptionalPlay;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.huaibinmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.huaibinmajiang.cqrs.c.service.impl.GameCmdServiceImpl;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component(value = "gameCmdService")
public class DisruptorGameCmdService extends DisruptorCmdServiceBase implements GameCmdService {

    @Autowired
    private GameCmdServiceImpl gameCmdServiceImpl;

    @Override
    public MajiangGameValueObject newMajiangGame(String gameId, String lianmengId, String playerId,
                                                 Integer panshu, Integer renshu, Double difen, Integer powerLimit, OptionalPlay optionalPlay) {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "newMajiangGame", gameId,lianmengId, playerId,
                panshu, renshu, difen, powerLimit, optionalPlay);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                ()->{
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.newMajiangGame(
                            cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(),
                            cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public MajiangGameValueObject newMajiangGameLeaveAndQuit(String gameId, String playerId, Integer panshu,
//                                                             Integer renshu,Double difen,Integer powerLimit, OptionalPlay optionalPlay) {
//        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "newMajiangGameLeaveAndQuit", gameId,
//                playerId,  panshu, renshu,difen,powerLimit,optionalPlay);
//        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
//                () -> {
//                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.newMajiangGameLeaveAndQuit(
//                            cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(),cmd.getParameter(),cmd.getParameter(),
//                            cmd.getParameter());
//                    return majiangGameValueObject;
//                });
//        try {
//            return result.getResult();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public MajiangGameValueObject joinGame(String playerId, String gameId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "joinGame", playerId, gameId);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.joinGame(cmd.getParameter(),
                            cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject leaveGame(String playerId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "leaveGame", playerId);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.leaveGame(cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "readyForGame", playerId,
                currentTime);
        DeferredResult<ReadyForGameResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
            ReadyForGameResult readyForGameResult = gameCmdServiceImpl.readyForGame(cmd.getParameter(),
                    cmd.getParameter());
            return readyForGameResult;
        });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject backToGame(String playerId, String gameId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "backToGame", playerId, gameId);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.backToGame(cmd.getParameter(),
                            cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void bindPlayer(String playerId, String gameId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "bindPlayer", playerId, gameId);
        DeferredResult<Object> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
            gameCmdServiceImpl.bindPlayer(cmd.getParameter(), cmd.getParameter());
            return null;
        });
        try {
            result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject finish(String playerId, Long currentTime) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "finish", playerId, currentTime);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.finish(cmd.getParameter(),
                            cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject quit(String playerId, Long currentTime,String gameId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "quit", playerId, currentTime,gameId);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
            MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.quit(cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
            return majiangGameValueObject;
        });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject voteToFinish(String playerId, Boolean yes) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "voteToFinish", playerId, yes);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.voteToFinish(cmd.getParameter(),
                            cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject finishGameImmediately(String gameId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "finishGameImmediately", gameId);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl
                            .finishGameImmediately(cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject leaveGameByOffline(String playerId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "leaveGameByOffline", playerId);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl
                            .leaveGameByOffline(cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject leaveGameByHangup(String playerId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "leaveGameByHangup", playerId);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl
                            .leaveGameByHangup(cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject voteToFinishByTimeOver(String playerId, Long currentTime) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "voteToFinishByTimeOver", playerId,
                currentTime);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl
                            .voteToFinishByTimeOver(cmd.getParameter(), cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ReadyForGameResult cancelReadyForGame(String playerId, Long currentTime) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "cancelReadyForGame", playerId,
                currentTime);
        DeferredResult<ReadyForGameResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
            ReadyForGameResult readyForGameResult = gameCmdServiceImpl.cancelReadyForGame(cmd.getParameter(),
                    cmd.getParameter());
            return readyForGameResult;
        });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject joinWatch(String playerId, String nickName, String headimgurl, String gameId)
            throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "joinWatch", playerId, nickName,
                headimgurl, gameId);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.joinWatch(cmd.getParameter(),
                            cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public MajiangGameValueObject leaveWatch(String playerId, String gameId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "leaveWatch", playerId, gameId);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
                () -> {
                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.leaveWatch(cmd.getParameter(),
                            cmd.getParameter());
                    return majiangGameValueObject;
                });
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Map getwatch(String gameId) {
        return gameCmdServiceImpl.getwatch(gameId);
    }

    @Override
    public void recycleWatch(String gameId) {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "recycleWatch", gameId);
        DeferredResult<Object> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
            gameCmdServiceImpl.recycleWatch(cmd.getParameter());
            return null;
        });
        try {
            result.getResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public MajiangGameValueObject newMajiangGamePlayerLeaveAndQuit(String gameId, String playerId, Integer panshu, Integer renshu,
//                                                                   Double difen, Integer powerLimit, OptionalPlay optionalPlay, MajiangGameQueryService majiangGameQueryService, ShanxiMajiangGameMsgService gameMsgService, GamePlayWsNotifier wsNotifier) {
//        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "newMajiangGamePlayerLeaveAndQuit",
//                gameId, playerId, panshu, renshu,difen, powerLimit,optionalPlay,majiangGameQueryService,gameMsgService,wsNotifier);
//        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
//                () -> {
//                    MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.newMajiangGamePlayerLeaveAndQuit(
//                            cmd.getParameter(), cmd.getParameter(), cmd.getParameter(),
//                            cmd.getParameter(), cmd.getParameter(),cmd.getParameter(), cmd.getParameter(),cmd.getParameter(),cmd.getParameter(), cmd.getParameter());
//                    return majiangGameValueObject;
//                });
//        try {
//            return result.getResult();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public Set<String> listGameId() {
        return gameCmdServiceImpl.listGameId();
    }

    @Override
    public Map<String, String> playLeaveGameHosting(String playerId, String gameId, boolean isLeave) {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "playLeaveGameHosting", playerId, gameId, isLeave);
        DeferredResult<Map<String, String>> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () ->
                gameCmdServiceImpl.playLeaveGameHosting(cmd.getParameter(), cmd.getParameter(), cmd.getParameter()));
        try {
            return result.getResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MajiangGameValueObject automaticFinish(String gameId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "voteToFinish", gameId);
        DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () ->
                gameCmdServiceImpl.automaticFinish(cmd.getParameter()));
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<PanActionFrame> getPanActionFrame(String gameId) throws Exception {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "getPanActionFrame", gameId);
        DeferredResult<List<PanActionFrame>> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> gameCmdServiceImpl.getPanActionFrame(cmd.getParameter()));
        try {
            return result.getResult();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String getGameIdByPlayerId(String playerId) {
        CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "getGameIdByPlayerId", playerId);
        DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> gameCmdServiceImpl.getGameIdByPlayerId(cmd.getParameter()));
        try {
            return result.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
