package com.dml.majiang.pai;

public enum MajiangPai {
	yiwan, erwan, sanwan, siwan, wuwan, liuwan, qiwan, bawan, jiuwan,
    yitong, ertong, santong, sitong, wutong, liutong, qitong, batong, jiutong,
    yitiao, ertiao, santiao, sitiao, wutiao, liutiao, qitiao, batiao, jiutiao,
    dongfeng, nanfeng, xifeng, beifeng, hongzhong, facai, baiban,
    chun, xia, qiu, dong, mei, lan, zhu, ju;
	private static MajiangPai[] array = MajiangPai.values();

	public static MajiangPai valueOf(int ordinal) {
		return array[ordinal];
	}

	public static boolean isXushupai(MajiangPai pai) {
		int ordinal = pai.ordinal();
		return (ordinal >= 0 && ordinal <= 26);
	}

	public static boolean isZipai(MajiangPai pai) {
		int ordinal = pai.ordinal();
		return (ordinal >= 27 && ordinal <= 33);
	}

	public static boolean isFengpai(MajiangPai pai) {
		int ordinal = pai.ordinal();
		return (ordinal >= 27 && ordinal <= 30);
	}

	/**
	 * // 获取万 筒 条 东南西北 红中发财白板这些牌
	 * @return 牌集
	 */
	public static MajiangPai[] xushupaiAndZipaiArray() {
		MajiangPai[] xushupaiAndZipaiArray = new MajiangPai[34];
		System.arraycopy(array, 0, xushupaiAndZipaiArray, 0, 34);
		return xushupaiAndZipaiArray;
	}

	public static MajiangPai[] xushupaiArray() {
		MajiangPai[] xushupaiArray = new MajiangPai[27];
		System.arraycopy(array, 0, xushupaiArray, 0, 27);
		return xushupaiArray;
	}

	public static MajiangPai[] fengpaiArray() {
		MajiangPai[] fengpaiArray = new MajiangPai[4];
		System.arraycopy(array, 27, fengpaiArray, 0, 4);
		return fengpaiArray;
	}

}
