package com.anbang.qipai.huaibinmajiang.web.vo;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangHushu;
import lombok.Data;

@Data
public class HuaibinMajiangHushuVO {
    private boolean zhuang;
    private boolean hu;                         //胡
    /**
     * 小嘴
     */
    private boolean jia; // 是否为夹
    private boolean kou; // 是否为扣
    private boolean pao; // 是否为跑  默认为true
    private boolean zimoHu; // 是否为自摸胡

    /**
     * 公嘴
     */
    private boolean shiyizhang; // 是否为十一张
    private boolean menqianqing; // 是否为门前清
    private boolean yaojiuduwu; // 是否为幺九独五
    private boolean sanpeng; // 是否为三碰
    private boolean yipengzadao; // 是否为一碰就倒
    private boolean juezhang; // 是否为绝张
    private boolean sasali; // 是否为卅卅哩
    private boolean siguiyi; // 是否为四归一


    private int value;
    private double score;
    public HuaibinMajiangHushuVO() {
    }

    public HuaibinMajiangHushuVO(HuaibinMajiangHushu hushu) {
        hu = hushu.isHu();
        zhuang = hushu.isZhuang();

        jia = hushu.isJia();
        kou = hushu.isKou();
        pao = hushu.isPao();
        zimoHu = hushu.isZimoHu();

        shiyizhang = hushu.isShiyizhang();
        menqianqing = hushu.isMenqianqing();
        yaojiuduwu = hushu.isYaojiuduwu();
        sanpeng = hushu.isSanpeng();
        yipengzadao = hushu.isYipengzadao();
        juezhang = hushu.isJuezhang();
        sasali = hushu.isSasali();
        siguiyi = hushu.isSiguiyi();

        value = hushu.getNormalValue();
        score = hushu.getScore();
    }

}
