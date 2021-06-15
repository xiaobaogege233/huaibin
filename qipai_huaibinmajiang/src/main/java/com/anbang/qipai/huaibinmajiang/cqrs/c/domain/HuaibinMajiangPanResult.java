package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.pan.result.PanResult;
import lombok.Data;

/**
 * 盘结果
 */
@Data
public class HuaibinMajiangPanResult extends PanResult {

	private boolean hu; // 是否有胡

	private boolean zimo; // 是否自摸

	private String dianpaoPlayerId; // 点炮玩家ID

	private List<HuaibinMajiangPanPlayerResult> panPlayerResultList; // 盘玩家结果集

}
