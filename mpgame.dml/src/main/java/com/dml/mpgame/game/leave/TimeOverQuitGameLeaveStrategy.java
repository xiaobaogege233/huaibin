package com.dml.mpgame.game.leave;

import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.WaitingStart;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.dml.mpgame.game.player.PlayerJoined;
import com.dml.mpgame.game.player.PlayerReadyToStart;

/**
 * 离线超过一定时间退出游戏
 */
public class TimeOverQuitGameLeaveStrategy implements GameLeaveStrategy {
    private long overtime;

    public TimeOverQuitGameLeaveStrategy() {

    }

    public TimeOverQuitGameLeaveStrategy(long overtime) {
        this.overtime = overtime;
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
            //离线10分钟退出游戏
            new Thread(() -> {
                try {
                    Thread.sleep(overtime);
                    Long leaveTime = game.getPlayerIdLeaveTimeMap().get(playerId);
                    if (leaveTime != null && System.currentTimeMillis() - leaveTime >= overtime) {
                        game.removePlayer(playerId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
