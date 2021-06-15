package com.dml.majiang.player.action.dingque;

import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;

public class MajiangDingqueAction extends MajiangPlayerAction {
    public MajiangDingqueAction() {

    }

    public MajiangDingqueAction(String actionPlayerId) {
        super(MajiangPlayerActionType.dingque, actionPlayerId);
    }
}
