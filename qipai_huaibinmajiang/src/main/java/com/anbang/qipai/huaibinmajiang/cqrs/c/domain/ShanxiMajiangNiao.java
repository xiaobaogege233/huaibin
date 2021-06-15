package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.pai.MajiangPai;

import java.util.ArrayList;
import java.util.List;

public class ShanxiMajiangNiao {
    private List<MajiangPai> zhuaPai;
    private List<MajiangPai> niaoPai;
    private int totalScore;
    private int value;

    public ShanxiMajiangNiao() {

    }

    public ShanxiMajiangNiao(List<MajiangPai> availablePaiList, int niaoshu) {
        zhuaPai = new ArrayList<>();
        if (niaoshu > 0) {
            for (int i = 0; i < niaoshu; i++) {
                if (availablePaiList.isEmpty()) {
                    break;
                }
                MajiangPai remove = availablePaiList.remove(0);
                zhuaPai.add(remove);
            }
        }
    }

    public void calculate() {
            int niao = 0;
            niaoPai = new ArrayList<>();
            niaoPai.add(MajiangPai.yitiao);
            niaoPai.add(MajiangPai.yiwan);
            niaoPai.add(MajiangPai.yitong);
            niaoPai.add(MajiangPai.wutiao);
            niaoPai.add(MajiangPai.wuwan);
            niaoPai.add(MajiangPai.wutong);
            niaoPai.add(MajiangPai.jiutiao);
            niaoPai.add(MajiangPai.jiuwan);
            niaoPai.add(MajiangPai.jiutong);
            niaoPai.add(MajiangPai.hongzhong);
            for (MajiangPai pai : zhuaPai) {
                if (niaoPai.contains(pai)) {
                    niao++;
                }
            }
            value = niao;
    }

    public int jiesuan(int delta) {
        return totalScore += delta;
    }

    public List<MajiangPai> getZhuaPai() {
        return zhuaPai;
    }

    public void setZhuaPai(List<MajiangPai> zhuaPai) {
        this.zhuaPai = zhuaPai;
    }

    public List<MajiangPai> getNiaoPai() {
        return niaoPai;
    }

    public void setNiaoPai(List<MajiangPai> niaoPai) {
        this.niaoPai = niaoPai;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
