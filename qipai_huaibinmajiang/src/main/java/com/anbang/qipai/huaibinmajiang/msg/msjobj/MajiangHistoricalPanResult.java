package com.anbang.qipai.huaibinmajiang.msg.msjobj;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.ShanxiMajiangPanPlayerResultDbo;
import lombok.Data;

@Data
public class MajiangHistoricalPanResult {
	private String gameId;
	private int no;// 盘数
	private long finishTime;// 完成时间
	private List<HuaibinMajiangPanPlayerResultMO> playerResultList;

	public MajiangHistoricalPanResult(PanResultDbo dbo, MajiangGameDbo majiangGameDbo) {
		gameId = majiangGameDbo.getId();
		List<ShanxiMajiangPanPlayerResultDbo> list = dbo.getPlayerResultList();
		if (list != null) {
			playerResultList = new ArrayList<>(list.size());
			list.forEach((panPlayerResult) -> playerResultList.add(new HuaibinMajiangPanPlayerResultMO(
					majiangGameDbo.findPlayer(panPlayerResult.getPlayerId()), panPlayerResult)));
		}
		no = dbo.getPanNo();
		finishTime = dbo.getFinishTime();
	}


}
