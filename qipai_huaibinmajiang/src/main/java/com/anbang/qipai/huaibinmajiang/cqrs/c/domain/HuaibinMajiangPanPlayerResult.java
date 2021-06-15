package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.pan.result.PanPlayerResult;
import lombok.Data;

/**
 * 盘玩家结果
 */
@Data
public class HuaibinMajiangPanPlayerResult extends PanPlayerResult {

    private HuaibinMajiangHushu hushu; // 胡数

    private HuaibinMajiangGang gang; // 杠

    private HuaibinMajiangLiangFeng liangFeng; // 亮风

    private ShanxiMajiangNiao niao; //

    private double totalScore; // 总分

    private double score; // 分数


}
