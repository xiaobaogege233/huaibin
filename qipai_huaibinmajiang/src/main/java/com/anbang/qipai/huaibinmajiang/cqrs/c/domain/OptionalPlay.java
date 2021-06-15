package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import lombok.Data;

/**
 * @Description: 麻将可选玩法（不包括局数人数）
 */
@Data
public class OptionalPlay {

    private boolean laojiuzui; // 是否勾选老九嘴
    private boolean paoshifen; // 是否勾选跑10分
    private boolean pingtui; // 是否勾选平推
    private boolean liangfeng; // 是否勾选亮风
    private boolean rezhangBuhu; // 是否热张不胡
    private boolean gps; // 是否勾选gps
    private boolean ip; // 是否勾选ip


    private int paofen;
    private int tuoguan;
    private boolean lixianchengfa;
    private int lixianshichang;
    private int lixianchengfaScore;
    private boolean tuoguanjiesan;
    private int buzhunbeituichushichang;


    private boolean jiangErwubaJiafan;
    private boolean huErwubaJiafan;
    private boolean hongzhongLaizi;
    private boolean qingyiseJiafan;
    private boolean kehuQiduiJiafan;
    private boolean erwubaYingjiang;
    private boolean banJiesan;


}
