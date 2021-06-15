package com.dml.majiang.player.shoupai.gouxing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.Danpai;
import com.dml.majiang.pai.fenzu.Duizi;
import com.dml.majiang.pai.fenzu.Gangzi;
import com.dml.majiang.pai.fenzu.Kezi;
import com.dml.majiang.pai.fenzu.Shunzi;
import com.dml.majiang.player.shoupai.PaiXing;

/**
 * 连续牌组牌型。把连续牌左移到顶之后计算出来的牌型。比如三万、四万、四万、五万，左移到顶之后就是一万、二万、二万、三万，再计算牌型
 *
 * @author neo
 */
public class LianXuPaiZuPaiXing {

    public LianXuPaiZuPaiXing(List<Integer> danpaiXuhaoList, List<Integer> duiziXuhaoList, List<Integer> keziXuhaoList,
                              List<Integer> gangziXuhaoList, List<Integer> shunziXuhaoList) {

        for (int xuhao : danpaiXuhaoList) {
            danpaiCode = (short) (danpaiCode | (1 << xuhao));
        }

        int[] tempQuantityArray = new int[9];
        for (int xuhao : duiziXuhaoList) {
            tempQuantityArray[xuhao]++;
        }
        for (int i = 0; i < tempQuantityArray.length; i++) {
            duiziCode = (duiziCode | (tempQuantityArray[i] << (i * 3)));
        }

        Arrays.fill(tempQuantityArray, 0);
        for (int xuhao : keziXuhaoList) {
            tempQuantityArray[xuhao]++;
        }
        for (int i = 0; i < tempQuantityArray.length; i++) {
            keziCode = (keziCode | (tempQuantityArray[i] << (i * 2)));
        }

        Arrays.fill(tempQuantityArray, 0);
        for (int xuhao : gangziXuhaoList) {
            tempQuantityArray[xuhao]++;
        }
        for (int i = 0; i < tempQuantityArray.length; i++) {
            gangziCode = (gangziCode | (tempQuantityArray[i] << (i * 2)));
        }

        Arrays.fill(tempQuantityArray, 0);
        for (int xuhao : shunziXuhaoList) {
            tempQuantityArray[xuhao]++;
        }
        for (int i = 0; i < 7; i++) {
            shunziCode = (short) (shunziCode | (tempQuantityArray[i] << (i * 2)));
        }
    }

    // 一个short表示单牌: 0-9位的0/1值表示该位置有没有一个单牌
    private short danpaiCode;

    // 一个int表示对子: 每3位表示该位置对子个数，9次
    private int duiziCode;

    // 一个int表示刻子: 每2位表示该位置刻子个数，9次
    private int keziCode;

    // 一个int表示杠子: 每2位表示该位置杠子个数，9次
    private int gangziCode;

    // 一个short表示顺子: 每2位表示该位置顺子个数，7次
    private short shunziCode;

    public PaiXing calculateJutiPaiXing(MajiangPai[] jutiLianXuPaiTypesArray) {
        List<Danpai> danpaiList = new ArrayList<>();
        List<Duizi> duiziList = new ArrayList<>();
        List<Kezi> keziList = new ArrayList<>();
        List<Gangzi> gangziList = new ArrayList<>();
        List<Shunzi> shunziList = new ArrayList<>();
        PaiXing paiXing = new PaiXing();
        paiXing.setDanpaiList(danpaiList);
        paiXing.setDuiziList(duiziList);
        paiXing.setGangziList(gangziList);
        paiXing.setKeziList(keziList);
        paiXing.setShunziList(shunziList);

        for (int i = 0; i < 9; i++) {

            if (((danpaiCode >>> i) & 1) == 1) {
                danpaiList.add(new Danpai(jutiLianXuPaiTypesArray[i]));
            }

            int duiziCount = ((duiziCode >>> (i * 3)) & 7);
            for (int j = 0; j < duiziCount; j++) {
                duiziList.add(new Duizi(jutiLianXuPaiTypesArray[i]));
            }

            int keziCount = ((keziCode >>> (i * 2)) & 3);
            for (int j = 0; j < keziCount; j++) {
                keziList.add(new Kezi(jutiLianXuPaiTypesArray[i]));
            }

            int gangziCount = ((gangziCode >>> (i * 2)) & 3);
            for (int j = 0; j < gangziCount; j++) {
                gangziList.add(new Gangzi(jutiLianXuPaiTypesArray[i]));
            }

            int shunziCount = ((shunziCode >>> (i * 2)) & 3);
            for (int j = 0; j < shunziCount; j++) {
                if (jutiLianXuPaiTypesArray.length > i + 2) {
                    shunziList.add(new Shunzi(jutiLianXuPaiTypesArray[i], jutiLianXuPaiTypesArray[i + 1], jutiLianXuPaiTypesArray[i + 2]));
                }
            }

        }
        return paiXing;

    }

    public boolean equals(Object o) {
        LianXuPaiZuPaiXing px = (LianXuPaiZuPaiXing) o;
        return (danpaiCode == px.danpaiCode && duiziCode == px.duiziCode && keziCode == px.keziCode
                && gangziCode == px.gangziCode && shunziCode == px.shunziCode);
    }

    public int hashCode() {
        return danpaiCode + duiziCode * 10 + keziCode * 100 + gangziCode * 1000 + shunziCode * 10000;
    }

    public short getDanpaiCode() {
        return danpaiCode;
    }

    public void setDanpaiCode(short danpaiCode) {
        this.danpaiCode = danpaiCode;
    }

    public int getDuiziCode() {
        return duiziCode;
    }

    public void setDuiziCode(int duiziCode) {
        this.duiziCode = duiziCode;
    }

    public int getKeziCode() {
        return keziCode;
    }

    public void setKeziCode(int keziCode) {
        this.keziCode = keziCode;
    }

    public int getGangziCode() {
        return gangziCode;
    }

    public void setGangziCode(int gangziCode) {
        this.gangziCode = gangziCode;
    }

    public short getShunziCode() {
        return shunziCode;
    }

    public void setShunziCode(short shunziCode) {
        this.shunziCode = shunziCode;
    }

}
