package com.dml.majiang.pai;

public enum XushupaiCategory {
	wan, tong, tiao;

	public static XushupaiCategory getCategoryforXushupai(MajiangPai xushupai) {
		int paiOrdinal = xushupai.ordinal();
		if (paiOrdinal >= 0 && paiOrdinal <= 8) {
			return wan;
		} else if (paiOrdinal >= 9 && paiOrdinal <= 17) {
			return tong;
		} else if (paiOrdinal >= 18 && paiOrdinal <= 26) {
			return tiao;
		} else {
			return null;
		}
	}
}
