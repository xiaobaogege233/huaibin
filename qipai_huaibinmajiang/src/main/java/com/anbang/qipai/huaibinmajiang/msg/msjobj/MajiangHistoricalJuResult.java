package com.anbang.qipai.huaibinmajiang.msg.msjobj;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangJuResult;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGameDbo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MajiangHistoricalJuResult {
	private String gameId;
	private String dayingjiaId;
	private String datuhaoId;
	private List<HuaibinMajiangJuPlayerResultMO> playerResultList;
	private int lastPanNo;
	private int panshu;
	private long finishTime;

	public MajiangHistoricalJuResult(JuResultDbo juResultDbo, MajiangGameDbo majiangGameDbo) {
		gameId = juResultDbo.getGameId();
		HuaibinMajiangJuResult huaibinMajiangJuResult = juResultDbo.getJuResult();
		dayingjiaId = huaibinMajiangJuResult.getDayingjiaId();
		datuhaoId = huaibinMajiangJuResult.getDatuhaoId();
		finishTime = juResultDbo.getFinishTime();
		this.panshu = majiangGameDbo.getPanshu();
		lastPanNo = huaibinMajiangJuResult.getFinishedPanCount();
		playerResultList = new ArrayList<>();
		if (huaibinMajiangJuResult.getPlayerResultList() != null) {
			huaibinMajiangJuResult.getPlayerResultList()
					.forEach((juPlayerResult) -> playerResultList.add(new HuaibinMajiangJuPlayerResultMO(juPlayerResult,
							majiangGameDbo.findPlayer(juPlayerResult.getPlayerId()))));
		} else {
			majiangGameDbo.getPlayers().forEach((majiangGamePlayerDbo) -> playerResultList
					.add(new HuaibinMajiangJuPlayerResultMO(majiangGamePlayerDbo)));
		}
	}

}
