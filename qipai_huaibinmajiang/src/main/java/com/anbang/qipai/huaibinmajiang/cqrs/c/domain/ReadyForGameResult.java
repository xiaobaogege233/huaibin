package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.pan.frame.PanActionFrame;
import lombok.Data;

@Data
public class ReadyForGameResult {
	private MajiangGameValueObject majiangGame;
	private PanActionFrame firstActionFrame;

}
