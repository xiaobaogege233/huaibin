package com.dml.majiang.position;

public enum MajiangPosition {
	dong, nan, xi, bei;

	private static MajiangPosition[] array = MajiangPosition.values();

	public static MajiangPosition valueOf(int ordinal) {
		return array[ordinal];
	}
}
