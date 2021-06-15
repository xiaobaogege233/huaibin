package com.anbang.qipai.huaibinmajiang.cqrs.c.service;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.OptionalPlay;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.ReadyForGameResult;
import com.dml.majiang.pan.frame.PanActionFrame;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GameCmdService {

    MajiangGameValueObject newMajiangGame(String gameId, String lianmengId, String playerId, Integer panshu, Integer renshu, Double difen, Integer powerLimit, OptionalPlay optionalPlay);

//    MajiangGameValueObject newMajiangGameLeaveAndQuit(String gameId, String playerId,Integer panshu, Integer renshu,Double difen,Integer powerLimit, OptionalPlay optionalPlay);

//    MajiangGameValueObject newMajiangGamePlayerLeaveAndQuit(String gameId, String playerId, Integer panshu, Integer renshu, Double difen, Integer powerLimit, OptionalPlay optionalPlay,MajiangGameQueryService majiangGameQueryService, ShanxiMajiangGameMsgService gameMsgService, GamePlayWsNotifier wsNotifier);

    MajiangGameValueObject leaveGame(String playerId) throws Exception;

    ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception;

    ReadyForGameResult cancelReadyForGame(String playerId, Long currentTime) throws Exception;

    MajiangGameValueObject joinGame(String playerId, String gameId) throws Exception;

    MajiangGameValueObject backToGame(String playerId, String gameId) throws Exception;

    void bindPlayer(String playerId, String gameId) throws Exception;

    MajiangGameValueObject finish(String playerId, Long currentTime) throws Exception;

    MajiangGameValueObject quit(String playerId, Long currentTime, String gameId) throws Exception;

    MajiangGameValueObject voteToFinish(String playerId, Boolean yes) throws Exception;

    MajiangGameValueObject voteToFinishByTimeOver(String playerId, Long currentTime) throws Exception;

    MajiangGameValueObject finishGameImmediately(String gameId) throws Exception;

    MajiangGameValueObject leaveGameByOffline(String playerId) throws Exception;

    MajiangGameValueObject leaveGameByHangup(String playerId) throws Exception;

    MajiangGameValueObject joinWatch(String playerId, String nickName, String headimgurl, String gameId) throws Exception;

    MajiangGameValueObject leaveWatch(String playerId, String gameId) throws Exception;

    Map getwatch(String gameId);

    void recycleWatch(String gameId);

    Set<String> listGameId();

    Map<String, String> playLeaveGameHosting(String playerId, String gameId, boolean isLeave);

    MajiangGameValueObject automaticFinish(String gameId) throws Exception;

    List<PanActionFrame> getPanActionFrame(String gameId) throws Exception;

    String getGameIdByPlayerId(String playerId);
}
