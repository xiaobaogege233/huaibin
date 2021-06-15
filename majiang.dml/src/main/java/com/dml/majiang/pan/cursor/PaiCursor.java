package com.dml.majiang.pan.cursor;

/**
 * 牌游标，用于保存定位牌需要的信息。由于牌没有id,所以定位牌需要通过一些信息的计算来实现。
 *
 * @author Neo
 */
public abstract class PaiCursor {

    private PaiCursorType type;

    public PaiCursor(PaiCursorType type) {
        this.type = type;
    }

    public PaiCursorType getType() {
        return type;
    }

    public void setType(PaiCursorType type) {
        this.type = type;
    }

}
