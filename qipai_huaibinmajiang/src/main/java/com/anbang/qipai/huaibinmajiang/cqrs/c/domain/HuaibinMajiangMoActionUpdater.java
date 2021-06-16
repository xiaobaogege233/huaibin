package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.anbang.qipai.huaibinmajiang.utils.SpringUtil;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionUpdater;
import com.dml.majiang.player.action.mo.QishouMopai;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 摸完后动作
 */
public class HuaibinMajiangMoActionUpdater implements MajiangPlayerMoActionUpdater {

    /**
     * 摸完后动作 可能产生的杠 胡 过等动作 如果都没有啥都不干  正常继续打牌
     * @param moAction 摸动作
     * @param ju 当前局
     * @throws Exception
     */
    @Override
    public void updateActions(MajiangMoAction moAction, Ju ju) throws Exception {



        HuaibinMajiangPanResultBuilder huaibinMajiangPanResultBuilder = (HuaibinMajiangPanResultBuilder) ju.getCurrentPanResultBuilder();
        OptionalPlay optionalPlay = huaibinMajiangPanResultBuilder.getOptionalPlay();
        Pan currentPan = ju.getCurrentPan();
        MajiangPlayer player = currentPan.findPlayerById(moAction.getActionPlayerId());
        currentPan.clearAllPlayersActionCandidates();



        // TODO 可能亮风也会在这里做一些处理

        MajiangPai gangmoShoupai = player.getGangmoShoupai();

        // 有手牌或刻子可以杠这个摸来的牌
        player.tryShoupaigangmoAndGenerateCandidateAction();
        player.tryKezigangmoAndGenerateCandidateAction();

        // TODO 存在天杠需要在这里判断是否为第一次摸排 第一次打牌之前的杠四个手牌即为天杠 打了第一张牌之后即为暗杠
        if (moAction.getReason().getName().equals(QishouMopai.name)){
            // 天杠
            player.tryTianGangAndGenerateCandidateAction();
        }else{
            // 杠四个手牌 暗杠
            player.tryGangsigeshoupaiAndGenerateCandidateAction();
        }
        // TODO 可能亮风也会在这里做一些处理

        // 胡
        GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
        // 天胡
        HuaibinMajiangHu bestHu = HuaibinMajiangJiesuanCalculator.calculateBestZimoHu(gouXingPanHu, player, optionalPlay,currentPan);
        if (bestHu != null) {
            bestHu.setZimo(true);
            player.addActionCandidate(new MajiangHuAction(player.getId(), bestHu));
        }


        // 啥也不能干
        Map<String, String> depositPlayerList = ju.getDepositPlayerList();
        if (depositPlayerList.containsKey(player.getId())) {    //如果玩家离线自动出牌
            executorService.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                autoDapai(player, depositPlayerList);
            });
        }

    }

    private final Automatic automatic = SpringUtil.getBean(Automatic.class);
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void autoDapai(MajiangPlayer player, Map<String, String> playerGameHostingList) {
        Map<Integer, MajiangPlayerAction> actionCandidates = player.getActionCandidates();
        if (actionCandidates.size() > 0) {
            MajiangPlayerAction action = actionCandidates.get(1);
            if (!(action instanceof MajiangDaAction) && !(action instanceof MajiangHuAction)) {
                player.clearActionCandidates();
                player.generateDaActions();
            }
            String gameId = playerGameHostingList.get(player.getId());
            automatic.automaticAction(player.getId(), 1, gameId);
        }

    }

    public void autoBuhua(MajiangPlayer xiajia, Map<String, String> depositPlayerList) {
        String gameId = depositPlayerList.get(xiajia.getId());
        automatic.automaticAction(xiajia.getId(), 1, gameId);
    }

}
