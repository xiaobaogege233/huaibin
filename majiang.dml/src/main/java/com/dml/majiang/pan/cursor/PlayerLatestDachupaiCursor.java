package com.dml.majiang.pan.cursor;

/**
 * 定位玩家最后打出的牌需要的信息
 *
 * @author Neo
 */
public class PlayerLatestDachupaiCursor extends PaiCursor {

    private String playerId;

    public PlayerLatestDachupaiCursor() {
        super(PaiCursorType.playerLatestDachupai);
    }

    public PlayerLatestDachupaiCursor(String playerId) {
        super(PaiCursorType.playerLatestDachupai);
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}
