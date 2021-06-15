package com.dml.majiang.pai.valueobj;

import com.dml.majiang.pai.MajiangPai;

import java.util.ArrayList;
import java.util.List;

public class PaiListValueObject {

    private List<MajiangPai> paiList;
    private int paiCount;

    public PaiListValueObject() {
    }

    public PaiListValueObject(List<MajiangPai> shoupaiList) {
        paiList = new ArrayList<>(shoupaiList);
        paiCount = shoupaiList.size();
    }

    public List<MajiangPai> getPaiList() {
        return paiList;
    }

    public void setPaiList(List<MajiangPai> paiList) {
        this.paiList = paiList;
    }

    public int getPaiCount() {
        return paiCount;
    }

    public void setPaiCount(int paiCount) {
        this.paiCount = paiCount;
    }

}