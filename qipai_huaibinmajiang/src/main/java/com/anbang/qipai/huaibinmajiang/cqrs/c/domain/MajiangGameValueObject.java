package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.MajiangPlayerXiapiaoState;
import com.dml.majiang.ju.result.JuResult;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGameValueObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MajiangGameValueObject extends FixedPlayersMultipanAndVotetofinishGameValueObject {
    private OptionalPlay optionalPlay;
	private int panshu;
	private int renshu;
	private double difen;
	private Map<String, Double> playerTotalScoreMap = new HashMap<>();
    private Map<String, MajiangPlayerXiapiaoState> playerXiapiaoStateMap;
    private Map<String,Integer> playerpiaofenMap;
	private JuResult juResult;
    private int powerLimit;
    private String lianmengId;

	public MajiangGameValueObject(MajiangGame majiangGame) {
		super(majiangGame);
        difen = majiangGame.getDifen();
		optionalPlay= majiangGame.getOptionalPlay();
		panshu = majiangGame.getPanshu();
		renshu = majiangGame.getRenshu();
		powerLimit = majiangGame.getPowerLimit();
        playerpiaofenMap=majiangGame.getPlayerpiaofenMap();
        playerXiapiaoStateMap = majiangGame.getPlayerXiapiaoStateMap();
		playerTotalScoreMap.putAll(majiangGame.getPlayerTotalScoreMap());
		if (majiangGame.getJu() != null) {
			juResult = majiangGame.getJu().getJuResult();
		}
		lianmengId=majiangGame.getLianmengId();
	}

}
