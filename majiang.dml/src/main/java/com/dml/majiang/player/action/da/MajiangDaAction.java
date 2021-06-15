package com.dml.majiang.player.action.da;

import java.nio.ByteBuffer;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MajiangDaAction extends MajiangPlayerAction {

	private MajiangPai pai;

	public MajiangDaAction(String actionPlayerId, MajiangPai pai) {
		super(MajiangPlayerActionType.da, actionPlayerId);
		this.pai = pai;
	}

}
