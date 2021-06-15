package com.anbang.qipai.huaibinmajiang.msg.msjobj;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangJuPlayerResult;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HuaibinMajiangJuPlayerResultMO {
	private String playerId;
	private String nickname;
	private String headimgurl;
    private int huCount;
    private int caishenCount;
    private double totalScore;
    private int zimoCount;
    private int fangPaoCount;

	public HuaibinMajiangJuPlayerResultMO(HuaibinMajiangJuPlayerResult juPlayerResult,
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

	public HuaibinMajiangJuPlayerResultMO(MajiangGamePlayerDbo majiangGamePlayerDbo) {
		playerId = majiangGamePlayerDbo.getPlayerId();
		nickname = majiangGamePlayerDbo.getNickname();
		headimgurl = majiangGamePlayerDbo.getHeadimgurl();
        huCount = 0;
        caishenCount = 0;
        zimoCount = 0;
        fangPaoCount = 0;
        totalScore = 0;
	}

}
