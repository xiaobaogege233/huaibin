package com.dml.majiang.player.action.chi;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.Shunzi;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;

public class MajiangChiAction extends MajiangPlayerAction {
    private String dachupaiPlayerId;
    private MajiangPai chijinPai;
    private Shunzi shunzi;

    public MajiangChiAction() {

    }

    public MajiangChiAction(String actionPlayerId, String dachupaiPlayerId, MajiangPai chijinPai, Shunzi shunzi) {
        super(MajiangPlayerActionType.chi, actionPlayerId);
        this.dachupaiPlayerId = dachupaiPlayerId;
        this.chijinPai = chijinPai;
        this.shunzi = shunzi;
    }

    public String getDachupaiPlayerId() {
        return dachupaiPlayerId;
    }

    public void setDachupaiPlayerId(String dachupaiPlayerId) {
        this.dachupaiPlayerId = dachupaiPlayerId;
    }

    public MajiangPai getChijinPai() {
        return chijinPai;
    }

    public void setChijinPai(MajiangPai chijinPai) {
        this.chijinPai = chijinPai;
    }

    public Shunzi getShunzi() {
        return shunzi;
    }

    public void setShunzi(Shunzi shunzi) {
        this.shunzi = shunzi;
    }

}
