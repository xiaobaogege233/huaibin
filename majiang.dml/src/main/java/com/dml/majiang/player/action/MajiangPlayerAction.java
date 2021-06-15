package com.dml.majiang.player.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 吃碰杠胡摸打过
 *
 * @author Neo
 */
@Data
@NoArgsConstructor
public abstract class MajiangPlayerAction {

    private int id;

    private MajiangPlayerActionType type;

    private String actionPlayerId;

    private boolean disabledByHigherPriorityAction; // 是否被更高优先权的动作禁用

    public MajiangPlayerAction(MajiangPlayerActionType type, String actionPlayerId) {
        this.type = type;
        this.actionPlayerId = actionPlayerId;
    }

}
