package com.dml.majiang.player.action.mo;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.GangType;

/**
 * 杠后补牌
 *
 * @author Neo
 */
public class GanghouBupai implements MopaiReason {

    public static final String name = "bugang";

    private MajiangPai gangPai;

    private GangType gangType;

    public GanghouBupai() {
    }

    public GanghouBupai(MajiangPai gangPai, GangType gangType) {
        this.gangPai = gangPai;
        this.gangType = gangType;
    }

    @Override
    public String getName() {
        return name;
    }

    public MajiangPai getGangPai() {
        return gangPai;
    }

    public void setGangPai(MajiangPai gangPai) {
        this.gangPai = gangPai;
    }

    public GangType getGangType() {
        return gangType;
    }

    public void setGangType(GangType gangType) {
        this.gangType = gangType;
    }

}
