package com.anbang.qipai.huaibinmajiang.cqrs.c.domain.test;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.Kezi;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.fapai.FaPaiStrategy;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;
import com.dml.majiang.position.MajiangPosition;
import com.dml.majiang.position.MajiangPositionUtil;

import java.util.List;

/**
 * 顺序发牌。
 *
 * @author Neo
 *
 */
public class TestLongyanMajiangFaPaiStrategy implements FaPaiStrategy {

    private int faPaiCountsForOnePlayer;

    public TestLongyanMajiangFaPaiStrategy() {
    }

    public TestLongyanMajiangFaPaiStrategy(int faPaiCountsForOnePlayer) {
        this.faPaiCountsForOnePlayer = faPaiCountsForOnePlayer;
    }

    @Override
    public void faPai(Ju ju) throws Exception {
        Pan currentPan = ju.getCurrentPan();
        MajiangPosition zhuangPlayerMenFeng = currentPan.findMenFengForZhuang();
        MajiangPosition playerMenFeng = zhuangPlayerMenFeng;
        for (int j = 0; j < 4; j++) {
            MajiangPlayer player = currentPan.findPlayerByMenFeng(playerMenFeng);
            if (player != null) {
                if (playerMenFeng.equals(MajiangPosition.dong)){
                    player.addShoupai(MajiangPai.yiwan);
                    player.addShoupai(MajiangPai.yiwan);
                    player.addShoupai(MajiangPai.sanwan);
                    player.addShoupai(MajiangPai.siwan);
//                    player.addShoupai(MajiangPai.qiwan);
//                    player.addShoupai(MajiangPai.santiao);
//                    player.addShoupai(MajiangPai.sitiao);
//                    player.addShoupai(MajiangPai.wutiao);
//                    player.addShoupai(MajiangPai.qitiao);
//                    player.addShoupai(MajiangPai.hongzhong);
                }else if (playerMenFeng.equals(MajiangPosition.xi)){
                    player.addShoupai(MajiangPai.bawan);
                    player.addShoupai(MajiangPai.bawan);
                    player.addShoupai(MajiangPai.bawan);
                    player.addShoupai(MajiangPai.jiuwan);
                    player.getPengchupaiZuList().add(new PengchuPaiZu(new Kezi(MajiangPai.erwan),currentPan.findPlayerByMenFeng(zhuangPlayerMenFeng).getId(),player.getId()));
//                    player.addShoupai(MajiangPai.yitiao);
//                    player.addShoupai(MajiangPai.yitiao);
//                    player.addShoupai(MajiangPai.sitiao);
//                    player.addShoupai(MajiangPai.sitiao);
//                    player.addShoupai(MajiangPai.liutiao);
//                    player.addShoupai(MajiangPai.hongzhong);
                }else if(playerMenFeng.equals(MajiangPosition.nan)){
                    player.addShoupai(MajiangPai.liutong);
                    player.addShoupai(MajiangPai.liutong);
                    player.addShoupai(MajiangPai.liutong);
                    player.addShoupai(MajiangPai.qitong);
                    player.addShoupai(MajiangPai.qitong);
                    player.addShoupai(MajiangPai.jiutong);
                    player.addShoupai(MajiangPai.jiutong);
                    player.addShoupai(MajiangPai.ertiao);
                    player.addShoupai(MajiangPai.ertiao);
                    player.addShoupai(MajiangPai.hongzhong);
                }else if (playerMenFeng.equals(MajiangPosition.bei)){
                    player.addShoupai(MajiangPai.yiwan);
                    player.addShoupai(MajiangPai.yiwan);
                    player.addShoupai(MajiangPai.erwan);
                    player.addShoupai(MajiangPai.wuwan);
                    player.addShoupai(MajiangPai.wuwan);
                    player.addShoupai(MajiangPai.qiwan);
                    player.addShoupai(MajiangPai.bawan);
                    player.addShoupai(MajiangPai.jiuwan);
                    player.addShoupai(MajiangPai.jiuwan);
                    player.addShoupai(MajiangPai.liutong);
                    player.addShoupai(MajiangPai.qitong);
                    player.addShoupai(MajiangPai.jiutong);
                    player.addShoupai(MajiangPai.santiao);
                }
            }
            playerMenFeng = MajiangPositionUtil.nextPositionAntiClockwise(playerMenFeng);
        }
    }

    private void faPai(List<MajiangPai> avaliablePaiList, MajiangPlayer player) {
        MajiangPai pai = avaliablePaiList.remove(0);
        if (pai.ordinal()>= MajiangPai.chun.ordinal()) {
            player.addPublicPai(pai);
            faPai(avaliablePaiList, player);
        } else {
            player.addShoupai(pai);
        }
    }


    public int getFaPaiCountsForOnePlayer() {
        return faPaiCountsForOnePlayer;
    }

    public void setFaPaiCountsForOnePlayer(int faPaiCountsForOnePlayer) {
        this.faPaiCountsForOnePlayer = faPaiCountsForOnePlayer;
    }

}
