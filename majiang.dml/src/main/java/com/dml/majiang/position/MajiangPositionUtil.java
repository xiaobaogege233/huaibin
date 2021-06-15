package com.dml.majiang.position;

import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.pai.MajiangPai;

public class MajiangPositionUtil {
	private static Map<MajiangPosition, MajiangPosition> nextPositionAntiClockwiseMap;
	private static Map<MajiangPosition, MajiangPosition> nextPositionClockwiseMap;
	private static Map<MajiangPosition, MajiangPai> positionFengpaiMap;

	static {
		nextPositionAntiClockwiseMap = new HashMap<>();
		nextPositionAntiClockwiseMap.put(MajiangPosition.dong, MajiangPosition.nan);
		nextPositionAntiClockwiseMap.put(MajiangPosition.nan, MajiangPosition.xi);
		nextPositionAntiClockwiseMap.put(MajiangPosition.xi, MajiangPosition.bei);
		nextPositionAntiClockwiseMap.put(MajiangPosition.bei, MajiangPosition.dong);

		nextPositionClockwiseMap = new HashMap<>();
		nextPositionClockwiseMap.put(MajiangPosition.dong, MajiangPosition.bei);
		nextPositionClockwiseMap.put(MajiangPosition.bei, MajiangPosition.xi);
		nextPositionClockwiseMap.put(MajiangPosition.xi, MajiangPosition.nan);
		nextPositionClockwiseMap.put(MajiangPosition.nan, MajiangPosition.dong);

		positionFengpaiMap = new HashMap<>();
		positionFengpaiMap.put(MajiangPosition.dong, MajiangPai.dongfeng);
		positionFengpaiMap.put(MajiangPosition.nan, MajiangPai.nanfeng);
		positionFengpaiMap.put(MajiangPosition.xi, MajiangPai.xifeng);
		positionFengpaiMap.put(MajiangPosition.bei, MajiangPai.beifeng);

	}

	public static MajiangPosition nextPositionClockwise(MajiangPosition currentPosition) {
		return nextPositionClockwiseMap.get(currentPosition);
	}

	public static MajiangPosition nextPositionAntiClockwise(MajiangPosition currentPosition) {
		return nextPositionAntiClockwiseMap.get(currentPosition);
	}

	public static MajiangPai getFengpaiByPosition(MajiangPosition position) {
		return positionFengpaiMap.get(position);
	}

}
