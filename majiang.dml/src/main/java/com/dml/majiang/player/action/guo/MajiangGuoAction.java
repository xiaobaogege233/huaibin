package com.dml.majiang.player.action.guo;

import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;

public class MajiangGuoAction extends MajiangPlayerAction {

    public MajiangGuoAction() {

    }

    public MajiangGuoAction(String actionPlayerId) {
        super(MajiangPlayerActionType.guo, actionPlayerId);
    }
}
