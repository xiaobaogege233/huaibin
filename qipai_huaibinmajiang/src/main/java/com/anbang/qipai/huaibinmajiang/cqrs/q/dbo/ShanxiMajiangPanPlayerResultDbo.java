package com.anbang.qipai.huaibinmajiang.cqrs.q.dbo;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangPanPlayerResult;
import com.dml.majiang.player.valueobj.MajiangPlayerValueObject;
import lombok.Data;

@Data
public class ShanxiMajiangPanPlayerResultDbo {

	private String playerId;
	private HuaibinMajiangPanPlayerResult playerResult;
	private MajiangPlayerValueObject player;


}
