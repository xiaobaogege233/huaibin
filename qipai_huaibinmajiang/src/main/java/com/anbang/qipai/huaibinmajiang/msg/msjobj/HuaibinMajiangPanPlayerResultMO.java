package com.anbang.qipai.huaibinmajiang.msg.msjobj;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.ShanxiMajiangPanPlayerResultDbo;
import lombok.Data;

@Data
public class HuaibinMajiangPanPlayerResultMO {
	private String playerId;// 玩家id
	private String nickname;// 玩家昵称
	private double score;// 一盘总分
	private boolean hu;

	public HuaibinMajiangPanPlayerResultMO(MajiangGamePlayerDbo gamePlayerDbo,
										   ShanxiMajiangPanPlayerResultDbo panPlayerResult) {
		playerId = gamePlayerDbo.getPlayerId();
		nickname = gamePlayerDbo.getNickname();
		score = panPlayerResult.getPlayerResult().getScore();
		hu = panPlayerResult.getPlayer().getHu() != null;
	}
}
