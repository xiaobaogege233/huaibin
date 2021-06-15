package com.anbang.qipai.huaibinmajiang.cqrs.q.dbo;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.OptionalPlay;
import com.anbang.qipai.huaibinmajiang.plan.bean.PlayerInfo;
import com.dml.mpgame.game.GamePlayerValueObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class MajiangGameDbo {
    private String id;
    private String lianmengId;
    private OptionalPlay optionalPlay;
    private int panshu;
    private int renshu;
    private int panNo;
    private double difen;
    private String state;// 原来是 waitingStart, playing, waitingNextPan, finished
    private List<MajiangGamePlayerDbo> players;
    private long createTime;
    private int powerLimit;

    public MajiangGameDbo(MajiangGameValueObject majiangGame, Map<String, PlayerInfo> playerInfoMap) {
        id = majiangGame.getId();
        difen=majiangGame.getDifen();
        lianmengId=majiangGame.getLianmengId();
        optionalPlay = majiangGame.getOptionalPlay();
        panshu = majiangGame.getPanshu();
        renshu = majiangGame.getRenshu();
        panNo = majiangGame.getPanNo();
        powerLimit= majiangGame.getPowerLimit();
        state = majiangGame.getState().name();
        players = new ArrayList<>();
        Map<String, Double> playeTotalScoreMap = majiangGame.getPlayerTotalScoreMap();
        for (GamePlayerValueObject playerValueObject : majiangGame.getPlayers()) {
            String playerId = playerValueObject.getId();
            PlayerInfo playerInfo = playerInfoMap.get(playerId);
            MajiangGamePlayerDbo playerDbo = new MajiangGamePlayerDbo();
            playerDbo.setHeadimgurl(playerInfo.getHeadimgurl());
            playerDbo.setNickname(playerInfo.getNickname());
            playerDbo.setGender(playerInfo.getGender());
            playerDbo.setOnlineState(playerValueObject.getOnlineState());
            playerDbo.setPlayerId(playerId);
            playerDbo.setState(playerValueObject.getState().name());
            if (playeTotalScoreMap.get(playerId) != null) {
                playerDbo.setTotalScore(playeTotalScoreMap.get(playerId));
            }
            players.add(playerDbo);
        }
        createTime = System.currentTimeMillis();
    }

    public MajiangGamePlayerDbo findPlayer(String playerId) {
        for (MajiangGamePlayerDbo player : players) {
            if (player.getPlayerId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }
}
