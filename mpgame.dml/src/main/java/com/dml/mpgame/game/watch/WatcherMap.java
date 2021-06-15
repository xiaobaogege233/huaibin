package com.dml.mpgame.game.watch;

import com.dml.mpgame.game.CrowdLimitsException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yins
 * @Description: 观战者库
 */
public class WatcherMap {
    private Map<String, Map<String, Watcher>> gameMap = new HashMap<>();  //游戏-玩家

    public void join(String playerId, String nickName, String headimgurl, String gameId) throws CrowdLimitsException {
        if (gameMap.containsKey(gameId)) {  //已有游戏存在直接放入
            Map<String, Watcher> watchMap = gameMap.get(gameId);

            if (!watchMap.containsKey(playerId) && watchMap.size() >= 2) {
                throw new CrowdLimitsException();
            }

            Watcher watcher = new Watcher();
            watcher.setId(playerId);
            watcher.setNickName(nickName);
            watcher.setHeadimgurl(headimgurl);
            watcher.setJoinTime(System.currentTimeMillis());
            watchMap.put(playerId, watcher);
            return;
        }
        Map<String, Watcher> newWatcher = new HashMap<>();
        Watcher watcher = new Watcher();
        watcher.setId(playerId);
        watcher.setNickName(nickName);
        watcher.setHeadimgurl(headimgurl);
        watcher.setJoinTime(System.currentTimeMillis());
        newWatcher.put(playerId, watcher);
        gameMap.put(gameId, newWatcher);
    }

    public void leave(String playerId, String gameId) {
        if (gameMap.containsKey(gameId) && gameMap.get(gameId).containsKey(playerId)) {  //已有游戏且正在观战
            gameMap.get(gameId).remove(playerId);
            if (gameMap.get(gameId) == null || gameMap.get(gameId).isEmpty()) {  //移除无人观战的游戏
                gameMap.remove(gameId);
            }
        }
    }

    /**
     * 移除断开心跳的玩家
     * @return gameId
     */
    public String timeoutLeave(String playerId) {
        String gameId = "";
        if (gameMap != null) {
            Iterator<Map.Entry<String, Map<String, Watcher>>> iter = gameMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Map<String, Watcher>> entry = iter.next();
                if (entry.getValue().containsKey(playerId)) {
                    gameId = entry.getKey();
                    entry.getValue().remove(playerId);

                    //移除无人观战的游戏
                    if (entry.getValue() == null || entry.getValue().isEmpty()) {
                        iter.remove();
                    }
                    break;
                }
            }
        }
        return gameId;
    }

    /**
     * 回收观战map
     */
    public void recycleWatch(String gameId) {
        gameMap.remove(gameId);
    }

    public Map<String, Watcher> getWatch(String gameId) {
        if (gameMap.get(gameId) == null) {
            return new HashMap<>();
        }
        return new HashMap<>(gameMap.get(gameId));
    }
}
