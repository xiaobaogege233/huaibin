package com.anbang.qipai.huaibinmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangJuResult;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGameDbo;
import lombok.Data;

@Data
public class JuResultVO {

	private String gameId;
	private String dayingjiaId;
	private String datuhaoId;
	private int panshu;
	private int finishedPanCount;
	private List<HuaibinMajiangJuPlayerResultVO> playerResultList;

	private PanResultVO lastPanResult;
	private long finishTime;

	public JuResultVO(JuResultDbo juResultDbo, MajiangGameDbo majiangGameDbo) {
		gameId = juResultDbo.getGameId();
		HuaibinMajiangJuResult huaibinMajiangJuResult = juResultDbo.getJuResult();
		dayingjiaId = huaibinMajiangJuResult.getDayingjiaId();
		datuhaoId = huaibinMajiangJuResult.getDatuhaoId();
		if (juResultDbo.getLastPanResult() != null) {
			lastPanResult = new PanResultVO(juResultDbo.getLastPanResult(), majiangGameDbo);
		}
		finishTime = juResultDbo.getFinishTime();
		this.panshu = majiangGameDbo.getPanshu();
		finishedPanCount = huaibinMajiangJuResult.getFinishedPanCount();
		playerResultList = new ArrayList<>();
		if (huaibinMajiangJuResult.getPlayerResultList() != null
				&& !huaibinMajiangJuResult.getPlayerResultList().isEmpty()) {
			huaibinMajiangJuResult.getPlayerResultList()
					.forEach((juPlayerResult) -> playerResultList.add(new HuaibinMajiangJuPlayerResultVO(juPlayerResult,
							majiangGameDbo.findPlayer(juPlayerResult.getPlayerId()))));
		} else {
			majiangGameDbo.getPlayers().forEach((majiangGamePlayerDbo) -> playerResultList
					.add(new HuaibinMajiangJuPlayerResultVO(majiangGamePlayerDbo)));
		}
	}


}
