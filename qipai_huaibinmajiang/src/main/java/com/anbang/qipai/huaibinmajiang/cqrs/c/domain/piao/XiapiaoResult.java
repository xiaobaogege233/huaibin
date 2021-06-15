package com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao;


import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.dml.majiang.pan.frame.PanActionFrame;
import lombok.Data;

@Data
public class XiapiaoResult {
	private MajiangGameValueObject majiangGame;
	private PanActionFrame firstActionFrame;


}
