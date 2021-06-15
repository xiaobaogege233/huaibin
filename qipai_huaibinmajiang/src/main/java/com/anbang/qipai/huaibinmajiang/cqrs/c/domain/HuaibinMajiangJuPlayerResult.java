package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import lombok.Data;

@Data
public class HuaibinMajiangJuPlayerResult {

    private String playerId;
    private int huCount;
    private int caishenCount;
    private int zimoCount;
    private int fangPaoCount;
    private Double totalScore;

    public void increaseHuCount() {
        huCount++;
    }

    public void increaseCaishenCount(int amount) {
        caishenCount += amount;
    }

    public void increaseZiMoCount() {
        zimoCount++;
    }

    public void increaseFangPaoCount() {
        fangPaoCount++;
    }

}
