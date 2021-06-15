package com.dml.majiang.pan.frame;

import com.dml.majiang.player.action.MajiangPlayerAction;

public class PanActionFrame {

    private int no;
    private MajiangPlayerAction action;
    private PanValueObject panAfterAction;
    private long actionTime;

    public PanActionFrame() {
    }

    public PanActionFrame(MajiangPlayerAction action, PanValueObject panAfterAction, long actionTime) {
        this.action = action;
        this.panAfterAction = panAfterAction;
        this.actionTime = actionTime;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public MajiangPlayerAction getAction() {
        return action;
    }

    public void setAction(MajiangPlayerAction action) {
        this.action = action;
    }

    public PanValueObject getPanAfterAction() {
        return panAfterAction;
    }

    public void setPanAfterAction(PanValueObject panAfterAction) {
        this.panAfterAction = panAfterAction;
    }

    public long getActionTime() {
        return actionTime;
    }

    public void setActionTime(long actionTime) {
        this.actionTime = actionTime;
    }

}
