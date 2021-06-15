package com.dml.majiang.pai.valueobj;

import com.dml.majiang.pai.MajiangPai;

public class MajiangPaiValueObject {

    private MajiangPai pai;

    public MajiangPaiValueObject() {
    }

    public MajiangPaiValueObject(MajiangPai pai) {
        this.pai = pai;
    }

    public MajiangPai getPai() {
        return pai;
    }

    public void setPai(MajiangPai pai) {
        this.pai = pai;
    }

}
