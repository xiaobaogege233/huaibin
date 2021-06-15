package com.anbang.qipai.huaibinmajiang.web.vo;

import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.action.MajiangPlayerAction;
import lombok.Data;

@Data
public class PanActionFrameVO {
	private int no;
	private MajiangPlayerAction action;
	private PanValueObjectVO panAfterAction;
	private long actionTime;

	public PanActionFrameVO(PanActionFrame panActionFrame) {
		no = panActionFrame.getNo();
		action = panActionFrame.getAction();
		actionTime = panActionFrame.getActionTime();
		panAfterAction = new PanValueObjectVO(panActionFrame.getPanAfterAction());
	}


}
