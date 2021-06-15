package com.anbang.qipai.huaibinmajiang.websocket;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 */
public enum WatchQueryScope {
    gameInfo, panForWatch, watchEnd, panResult, clearTable;

    public static List<WatchQueryScope> getQueryList(String flag){
        List<WatchQueryScope> scopes = new ArrayList<>();

        //绑定玩家时清牌桌
        if ("bindPlayer".equals(flag)) {
            scopes.add(WatchQueryScope.clearTable);
        }

        //默认查询 query
        scopes.add(WatchQueryScope.gameInfo);
        scopes.add(WatchQueryScope.panForWatch);

        if ("panResult".equals(flag)) {
            scopes.add(WatchQueryScope.panResult);
            return scopes;
        }
        if ("watchEnd".equals(flag)) {
            scopes.add(WatchQueryScope.watchEnd);
            return scopes;
        }

        return scopes;
    }

}
