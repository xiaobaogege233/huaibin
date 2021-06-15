package com.anbang.qipai.huaibinmajiang.web.vo;

import java.util.List;

import com.dml.majiang.pai.MajiangPai;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FangruShoupaiListVO {

	private List<MajiangPai> putongShoupaiList;
	private List<MajiangPai> guipaiShoupaiList;
	private int totalShoupaiCount;


	public FangruShoupaiListVO(List<MajiangPai> fangruShoupaiList, List<MajiangPai> fangruGuipaiList,
			int totalShoupaiCount) {
		putongShoupaiList = fangruShoupaiList;
		guipaiShoupaiList = fangruGuipaiList;
		this.totalShoupaiCount = totalShoupaiCount;
	}


}
