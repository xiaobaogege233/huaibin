package com.anbang.qipai.huaibinmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.ShanxiMajiangPanPlayerResultDbo;
import lombok.Data;

@Data
public class PanResultVO {

	private List<HuaibinMajiangPanPlayerResultVO> playerResultList;

	private boolean hu;

	private int panNo;

	private long finishTime;

	private int paiCount;

	private PanActionFrameVO panActionFrame;

	private double difen;


	public PanResultVO(PanResultDbo dbo, MajiangGameDbo majiangGameDbo) {
		List<ShanxiMajiangPanPlayerResultDbo> list = dbo.getPlayerResultList();
		if (list != null) {
			playerResultList = new ArrayList<>(list.size());
            list.forEach((panPlayerResult) -> playerResultList.add(new HuaibinMajiangPanPlayerResultVO(majiangGameDbo.findPlayer(panPlayerResult.getPlayerId()), dbo.getZhuangPlayerId(), dbo.isZimo(), dbo.getDianpaoPlayerId(), panPlayerResult)));
		}
		hu = dbo.isHu();
		difen = majiangGameDbo.getDifen();
		panNo = dbo.getPanNo();
		finishTime = dbo.getFinishTime();
		paiCount = dbo.getPanActionFrame().getPanAfterAction().getAvaliablePaiList().getPaiCount();
		panActionFrame = new PanActionFrameVO(dbo.getPanActionFrame());
	}

}
