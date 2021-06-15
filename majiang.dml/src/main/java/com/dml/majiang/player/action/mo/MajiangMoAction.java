package com.dml.majiang.player.action.mo;

import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;

public class MajiangMoAction extends MajiangPlayerAction {

    private MopaiReason reason;

    public MajiangMoAction() {

    }

    public MajiangMoAction(String actionPlayerId, MopaiReason reason) {
        super(MajiangPlayerActionType.mo, actionPlayerId);
        this.reason = reason;
    }

    public MopaiReason getReason() {
        return reason;
    }

    public void setReason(MopaiReason reason) {
        this.reason = reason;
    }

}
