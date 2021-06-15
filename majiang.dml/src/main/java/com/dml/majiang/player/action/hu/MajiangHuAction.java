package com.dml.majiang.player.action.hu;

import java.nio.ByteBuffer;

import com.dml.majiang.player.Hu;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;

public class MajiangHuAction extends MajiangPlayerAction {

	private Hu hu;

	public MajiangHuAction() {
	}

	public MajiangHuAction(String actionPlayerId, Hu hu) {
		super(MajiangPlayerActionType.hu, actionPlayerId);
		this.hu = hu;
	}

	public Hu getHu() {
		return hu;
	}

	public void setHu(Hu hu) {
		this.hu = hu;
	}

}
