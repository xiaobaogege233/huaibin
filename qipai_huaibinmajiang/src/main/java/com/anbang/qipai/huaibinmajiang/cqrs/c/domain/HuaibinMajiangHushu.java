package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import lombok.Data;

/**
 * 胡分对象
 */
@Data
public class HuaibinMajiangHushu {

    private OptionalPlay optionalPlay; // 可选玩法

    private boolean zhuang; // 是否为庄
    private boolean hu; // 是否胡

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

    /**
     * 牌分
     */
    private int paifen; //牌分

    private int score; // 胡分

    private int normalValue;


    /**
     * 计算胡分
     */
    public void calculate () {
        if(optionalPlay.isLaojiuzui()){
            int xiaozuiCount = 0;
            int gongzuiCount = 0;
            if (jia){
                xiaozuiCount++;
            }
            if(kou){
                xiaozuiCount++;
            }
            if(pao){
                xiaozuiCount++;
            }
            if(shiyizhang){
                gongzuiCount++;
            }
            if(menqianqing){
                gongzuiCount++;
            }
            if(yaojiuduwu){
                gongzuiCount++;
            }
            if(sanpeng){
                gongzuiCount++;
            }
            if(yipengzadao){
                gongzuiCount++;
            }
            if(sasali){
                gongzuiCount += 3;
            }
            if(juezhang){
                gongzuiCount++;
            }
            if(siguiyi){
                gongzuiCount++;
            }
            if (zimoHu){
                xiaozuiCount++;
                if(optionalPlay.isPaoshifen()){
                    score = ((xiaozuiCount * 3) + (gongzuiCount * 6) + 10 + paifen) * 2 ;
                }
                score = ((xiaozuiCount * 3) + (gongzuiCount * 6) + paifen) * 2 ;
            }else{
                if(optionalPlay.isPaoshifen()){
                    score = ((xiaozuiCount * 3) + (gongzuiCount * 6) + 10 + paifen) * 2 ;
                }
                score = ((xiaozuiCount * 3) + (gongzuiCount * 6) + paifen);
            }

        }

        if(optionalPlay.isPingtui()){
            if(isZimoHu()){
                score = paifen * 2;
            }else{
                score = paifen;
            }
        }
    }

    /**
     * 结算分
     * @param delta 对冲分的数值
     * @return
     */
    public int jiesuan(int delta) {
        return score += delta;
    }

}
