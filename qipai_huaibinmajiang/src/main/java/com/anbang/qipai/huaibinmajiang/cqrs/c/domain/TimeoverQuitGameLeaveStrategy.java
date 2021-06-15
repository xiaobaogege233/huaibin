package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;


import com.anbang.qipai.huaibinmajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.huaibinmajiang.msg.service.HuaibinMajiangGameMsgService;
import com.anbang.qipai.huaibinmajiang.websocket.GamePlayWsNotifier;
import com.anbang.qipai.huaibinmajiang.websocket.QueryScope;
import com.dml.mpgame.game.Canceled;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.WaitingStart;
import com.dml.mpgame.game.extend.vote.FinishedByVote;
import com.dml.mpgame.game.leave.GameLeaveStrategy;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.dml.mpgame.game.player.PlayerJoined;
import com.dml.mpgame.game.player.PlayerReadyToStart;

import java.util.ArrayList;
import java.util.List;

/**
 * 离线一定时间退出游戏并通知其他人
 */
public class TimeoverQuitGameLeaveStrategy implements GameLeaveStrategy {
    private long overtime;
    private MajiangGameQueryService majiangGameQueryService;
    private HuaibinMajiangGameMsgService gameMsgService;
    private GamePlayWsNotifier wsNotifier;

    public TimeoverQuitGameLeaveStrategy() {

    }

    public TimeoverQuitGameLeaveStrategy(long overtime, MajiangGameQueryService majiangGameQueryService, HuaibinMajiangGameMsgService gameMsgService, GamePlayWsNotifier wsNotifier) {
        this.overtime = overtime;
        this.majiangGameQueryService = majiangGameQueryService;
        this.gameMsgService = gameMsgService;
        this.wsNotifier = wsNotifier;
    }

    @Override
    public void leave(String playerId, Game game) throws Exception {
        GamePlayer player = game.findPlayer(playerId);
        if (player != null && !player.getOnlineState().equals(GamePlayerOnlineState.offline)) {
            game.updatePlayerOnlineState(playerId, GamePlayerOnlineState.offline);
        }
        if (game.getState().name().equals(WaitingStart.name)) {// 游戏未开始
            game.getPlayerIdLeaveTimeMap().put(playerId, System.currentTimeMillis());
            if (game.playerState(playerId).name().equals(PlayerReadyToStart.name)) {
                game.updatePlayerState(playerId, new PlayerJoined());// 取消准备
            }

            new Thread(() -> {
                try {
                    Thread.sleep(overtime + 10000);
                    Long leaveTime = game.getPlayerIdLeaveTimeMap().get(playerId);
                    if (leaveTime != null && System.currentTimeMillis() - leaveTime >= overtime) {
                        game.removePlayer(playerId);
                        if (game.allPlayerIds().isEmpty()) {
                            // 直接解散房间
                            game.cancel();
                        }
                        MajiangGameValueObject majiangGameValueObject = game.toValueObject();
                        majiangGameQueryService.leaveGame(majiangGameValueObject);
                        String gameId = majiangGameValueObject.getId();
                        if (majiangGameValueObject.getState().name().equals(FinishedByVote.name)
                                || majiangGameValueObject.getState().name().equals(Canceled.name)) {
                            gameMsgService.gameFinished(gameId);
                        } else if (majiangGameValueObject.getState().name().equals(Finished.name)) {
                            gameMsgService.gameCanceled(gameId, playerId);
                        } else {
                            gameMsgService.gamePlayerLeave(majiangGameValueObject, playerId);
                        }
                        // 通知其他人
                        for (String otherPlayerId : game.allPlayerIds()) {
                            if (!otherPlayerId.equals(playerId)) {
                                List<QueryScope> scopes = new ArrayList<>();
                                scopes.add(QueryScope.gameInfo);
                                wsNotifier.notifyToQuery(otherPlayerId, scopes);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
