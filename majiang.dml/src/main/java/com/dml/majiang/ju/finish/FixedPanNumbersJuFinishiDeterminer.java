package com.dml.majiang.ju.finish;

import com.dml.majiang.ju.Ju;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 一局打固定的几盘
 * 
 * @author Neo
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixedPanNumbersJuFinishiDeterminer implements JuFinishiDeterminer {

	private int fixedPanNumbers; // 固定盘数

	@Override
	public boolean determineToFinishJu(Ju ju) {
		return (ju.countFinishedPan() >= fixedPanNumbers);
	}


}
