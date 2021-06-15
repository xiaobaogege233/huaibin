package com.dml.majiang.player.action.ting;

import com.dml.majiang.player.Hu;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;

public class MajiangTingAction extends MajiangPlayerAction {


    public MajiangTingAction() {
    }

    public MajiangTingAction(String actionPlayerId) {
        super(MajiangPlayerActionType.ting, actionPlayerId);
    }

}
