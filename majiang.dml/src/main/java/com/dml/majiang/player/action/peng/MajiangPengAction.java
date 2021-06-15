package com.dml.majiang.player.action.peng;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;

public class MajiangPengAction extends MajiangPlayerAction {

    private String dachupaiPlayerId;

    private MajiangPai pai;

    public MajiangPengAction() {
    }

    public MajiangPengAction(String actionPlayerId, String dachupaiPlayerId, MajiangPai pai) {
        super(MajiangPlayerActionType.peng, actionPlayerId);
        this.dachupaiPlayerId = dachupaiPlayerId;
        this.pai = pai;
    }

    public String getDachupaiPlayerId() {
        return dachupaiPlayerId;
    }

    public void setDachupaiPlayerId(String dachupaiPlayerId) {
        this.dachupaiPlayerId = dachupaiPlayerId;
    }

    public MajiangPai getPai() {
        return pai;
    }

    public void setPai(MajiangPai pai) {
        this.pai = pai;
    }

}
