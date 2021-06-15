package com.anbang.qipai.huaibinmajiang.web.vo;

import java.util.List;
import java.util.Map;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.valueobj.MajiangPaiValueObject;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.chupaizu.ChichuPaiZu;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;
import com.dml.majiang.player.valueobj.MajiangPlayerValueObject;
import com.dml.majiang.position.MajiangPosition;
import lombok.Data;

@Data
public class MajiangPlayerValueObjectVO {

	private String id;
	/**
	 * 门风
	 */
	private MajiangPosition menFeng;

	private FangruShoupaiListVO fangruShoupaiList;
	/**
	 * 公开的牌，不能行牌
	 */
	private List<MajiangPai> publicPaiList;


	private List<MajiangPlayerAction> actionCandidates;

    private Map<MajiangPai,List<MajiangPai>> hupaiCandidates ;

    private List<MajiangPai> kehuCandidates ;

	/**
	 * 刚摸进待处理的手牌（未放入）
	 */
	private MajiangPaiValueObject gangmoShoupai;

	/**
	 * 打出的牌
	 */
	private List<MajiangPai> dachupaiList;

	private List<ChichuPaiZu> chichupaiZuList;
	private List<PengchuPaiZu> pengchupaiZuList;
	private List<GangchuPaiZu> gangchupaiZuList;


	private boolean watingForMe = false;


	public MajiangPlayerValueObjectVO(MajiangPlayerValueObject majiangPlayerValueObject) {
		id = majiangPlayerValueObject.getId();
		menFeng = majiangPlayerValueObject.getMenFeng();
		fangruShoupaiList = new FangruShoupaiListVO(majiangPlayerValueObject.getFangruShoupaiList(),
				majiangPlayerValueObject.getFangruGuipaiList(), majiangPlayerValueObject.getTotalShoupaiCount());
		publicPaiList = majiangPlayerValueObject.getPublicPaiList();
		actionCandidates = majiangPlayerValueObject.getActionCandidates();
		hupaiCandidates =majiangPlayerValueObject.getHupaiCandidates();
		kehuCandidates = majiangPlayerValueObject.getKehuCandidates();
		if (actionCandidates != null && !actionCandidates.isEmpty()) {
			watingForMe = true;
		}
		gangmoShoupai = majiangPlayerValueObject.getGangmoShoupai();
		dachupaiList = majiangPlayerValueObject.getDachupaiList();
		chichupaiZuList = majiangPlayerValueObject.getChichupaiZuList();
		pengchupaiZuList = majiangPlayerValueObject.getPengchupaiZuList();
		gangchupaiZuList = majiangPlayerValueObject.getGangchupaiZuList();

	}


}
