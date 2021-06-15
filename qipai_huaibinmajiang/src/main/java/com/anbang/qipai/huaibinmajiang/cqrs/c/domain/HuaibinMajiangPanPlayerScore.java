package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import lombok.Data;

@Data
public class HuaibinMajiangPanPlayerScore {

    private HuaibinMajiangHushu hushu;

    /**
     * 有可能是负数
     */
    private int jiesuanHushu;

    /**
     * 有可能是负数
     */

    private int jiesuanScore;

    private int value;

    public void jiesuan() {
        jiesuanScore = jiesuanHushu  ;
    }

    public void jiesuanHushu(int delta) {
        jiesuanHushu += delta;
    }

    public void calculate() {
        value = hushu.getNormalValue();
    }


}
