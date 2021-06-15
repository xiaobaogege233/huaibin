package com.anbang.qipai.huaibinmajiang.web.vo;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.shoupai.GuipaiDangPai;
import com.dml.majiang.player.shoupai.ShoupaiJiesuanPai;
import lombok.Data;

@Data
public class ResultShoupaiVO {

	private MajiangPai pai;
	private boolean caishen;
	private boolean hupai;

	public ResultShoupaiVO(ShoupaiJiesuanPai shoupaiJiesuanPai) {
		pai = shoupaiJiesuanPai.getYuanPaiType();
		hupai = shoupaiJiesuanPai.isLastActionPai();
		caishen = shoupaiJiesuanPai.dangType().equals(GuipaiDangPai.dangType);
	}

	public ResultShoupaiVO(MajiangPai pai) {
		this.pai = pai;
		caishen = false;
		hupai = false;
	}


}
