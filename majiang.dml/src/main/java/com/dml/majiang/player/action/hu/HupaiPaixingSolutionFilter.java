package com.dml.majiang.player.action.hu;



import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

import java.util.List;
import java.util.Map;


public interface HupaiPaixingSolutionFilter {

    Map<MajiangPai,List<MajiangPai>> hupaiFilter(MajiangPlayer majiangPlayer, GouXingPanHu gouXingPanHu);

    List<MajiangPai> kehuFilter(MajiangPlayer majiangPlayer, GouXingPanHu gouXingPanHu);

    Map<MajiangPai,List<MajiangPai>> pengHupaiFilter(MajiangPlayer majiangPlayer, GouXingPanHu gouXingPanHu);
}
