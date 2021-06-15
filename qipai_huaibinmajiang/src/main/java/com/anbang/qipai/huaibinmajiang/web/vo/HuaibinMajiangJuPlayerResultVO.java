package com.anbang.qipai.huaibinmajiang.web.vo;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangJuPlayerResult;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import lombok.Data;

@Data
public class HuaibinMajiangJuPlayerResultVO {

	private String playerId;
	private String nickname;
	private String headimgurl;
    private int huCount;
    private int caishenCount;
    private int zimoCount;
    private int fangPaoCount;
    private Double totalScore;

	public HuaibinMajiangJuPlayerResultVO(HuaibinMajiangJuPlayerResult juPlayerResult,
                                          MajiangGamePlayerDbo majiangGamePlayerDbo) {
		playerId = majiangGamePlayerDbo.getPlayerId();
		nickname = majiangGamePlayerDbo.getNickname();
		headimgurl = majiangGamePlayerDbo.getHeadimgurl();
		huCount = juPlayerResult.getHuCount();
        caishenCount = juPlayerResult.getCaishenCount();
        zimoCount = juPlayerResult.getZimoCount();
        fangPaoCount = juPlayerResult.getFangPaoCount();
		totalScore = juPlayerResult.getTotalScore();
	}

	public HuaibinMajiangJuPlayerResultVO(MajiangGamePlayerDbo majiangGamePlayerDbo) {
		playerId = majiangGamePlayerDbo.getPlayerId();
		nickname = majiangGamePlayerDbo.getNickname();
		headimgurl = majiangGamePlayerDbo.getHeadimgurl();
        huCount = 0;
        caishenCount = 0;
        zimoCount = 0;
        fangPaoCount = 0;
        totalScore = 0d;
	}

}
