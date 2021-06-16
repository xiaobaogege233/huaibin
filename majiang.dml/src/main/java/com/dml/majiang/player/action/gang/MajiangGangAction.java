package com.dml.majiang.player.action.gang;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import lombok.Data;

@Data
public class MajiangGangAction extends MajiangPlayerAction {

    private String dachupaiPlayerId;

    private MajiangPai pai;

    private GangType gangType;

    public MajiangGangAction() {
    }

    public MajiangGangAction(String actionPlayerId, String dachupaiPlayerId, MajiangPai pai, GangType gangType) {
        super(MajiangPlayerActionType.gang, actionPlayerId);
        this.dachupaiPlayerId = dachupaiPlayerId;
        this.pai = pai;
        this.gangType = gangType;
    }

}
