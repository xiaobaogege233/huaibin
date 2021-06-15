package com.anbang.qipai.huaibinmajiang.cqrs.q.dbo;

import com.dml.mpgame.game.player.GamePlayerOnlineState;
import lombok.Data;

@Data
public class MajiangGamePlayerDbo {
    private String playerId;
    private String nickname;
    private String gender;// 会员性别:男:male,女:female
    private String headimgurl;
    private String state;// 原来是 joined, readyToStart, playing, panFinished, finished
    private GamePlayerOnlineState onlineState;
    private double totalScore;

}
