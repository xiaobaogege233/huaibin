package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.pan.frame.PanActionFrame;
import lombok.Data;

/**
 * 麻将动作结果
 */
@Data
public class MajiangActionResult {

	private MajiangGameValueObject majiangGame;
	private PanActionFrame panActionFrame;
	private HuaibinMajiangPanResult panResult; // 盘结果
	private HuaibinMajiangJuResult juResult; // 局结果


}
