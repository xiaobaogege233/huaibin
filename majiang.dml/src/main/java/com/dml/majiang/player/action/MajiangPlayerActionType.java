package com.dml.majiang.player.action;

public enum MajiangPlayerActionType {
    chi, peng, gang, hu, mo, da, guo, ting, dingque;;
    private static MajiangPlayerActionType[] array = MajiangPlayerActionType.values();

    public static MajiangPlayerActionType valueOf(int ordinal) {
        return array[ordinal];
    }
}
