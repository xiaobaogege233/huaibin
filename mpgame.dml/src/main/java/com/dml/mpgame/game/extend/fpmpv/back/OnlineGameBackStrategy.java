package com.dml.mpgame.game.extend.fpmpv.back;

import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.back.GameBackStrategy;
import com.dml.mpgame.game.player.GamePlayerOnlineState;

/**
 * 上线就设为在线
 *
 * @author lsc
 */
public class OnlineGameBackStrategy implements GameBackStrategy {

    @Override
    public void back(String playerId, Game game) throws Exception {
        game.getPlayerIdLeaveTimeMap().remove(playerId);
        game.updatePlayerOnlineState(playerId, GamePlayerOnlineState.online);
    }

}
