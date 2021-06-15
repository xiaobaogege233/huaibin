package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.anbang.qipai.huaibinmajiang.utils.SpringUtil;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.da.MajiangPlayerDaActionUpdater;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.listener.comprehensive.GuoHuBuHuStatisticsListener;
import com.dml.majiang.player.action.listener.comprehensive.GuoPengBuPengStatisticsListener;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class HuaibinMajiangDaActionUpdater implements MajiangPlayerDaActionUpdater {

    private boolean dianpao; // 是否点炮

    /**
     * 打完之后动作 其他玩家可能会有吃 碰 胡 动作具体看规则  都不做下家直接摸牌
     * @param daAction
     * @param ju
     */
    @Override
    public void updateActions(MajiangDaAction daAction, Ju ju) {

        Pan currentPan = ju.getCurrentPan();
        MajiangPlayer daPlayer = currentPan.findPlayerById(daAction.getActionPlayerId());
        // 是否是地胡
        daPlayer.clearActionCandidates();
        HuaibinMajiangPanResultBuilder huaibinMajiangPanResultBuilder = (HuaibinMajiangPanResultBuilder) ju.getCurrentPanResultBuilder();
        OptionalPlay optionalPlay = huaibinMajiangPanResultBuilder.getOptionalPlay();
        // 获取过碰不碰玩家集合
        GuoPengBuPengStatisticsListener guoPengBuPengStatisticsListener = ju.getActionStatisticsListenerManager().findListener(GuoPengBuPengStatisticsListener.class);
        Map<String, List<MajiangPai>> canNotPengPlayersPaiMap = guoPengBuPengStatisticsListener.getCanNotPengPlayersPaiMap();
        // 获取过胡不胡玩家集合
        GuoHuBuHuStatisticsListener guoHuBuHuStatisticsListener = ju.getActionStatisticsListenerManager().findListener(GuoHuBuHuStatisticsListener.class);
        Set<String> canNotHuPlayers = guoHuBuHuStatisticsListener.getCanNotHuPlayers();

        MajiangPlayer xiajiaPlayer = currentPan.findXiajia(daPlayer);
        xiajiaPlayer.clearActionCandidates();
        // 遍历其他所有玩家 不包括打动作的玩家
        while (true) {
            if (!xiajiaPlayer.getId().equals(daAction.getActionPlayerId())) {
                List<MajiangPai> fangruShoupaiList = xiajiaPlayer.getFangruShoupaiList();
                // 其他玩家可以碰杠胡 先判断是否能碰
                if (fangruShoupaiList.size() != 1) {
                    boolean canPeng = true;// 可以碰
                    // 判断玩法是否有热张不胡
                    if (optionalPlay.isRezhangBuhu()) {
                        // 判断 不能碰的玩家里是否包含该玩家
                        if (canNotPengPlayersPaiMap.containsKey(xiajiaPlayer.getId())) {
                            // 包含的话获取该玩家不能碰的牌集
                            List<MajiangPai> canNotPengPaiList = canNotPengPlayersPaiMap.get(xiajiaPlayer.getId());
                            if (canNotPengPaiList != null && !canNotPengPaiList.isEmpty()) {
                                // 循环遍历不能碰的牌集并且与打玩家打的牌相等的话 说明这张牌属于该玩家的过了碰的牌 不能再碰
                                for (MajiangPai pai : canNotPengPaiList) {
                                    if (pai.equals(daAction.getPai())) {
                                        canPeng = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    // 经过上面判断 如果还是能碰的话 执行碰动作
                    if (canPeng) {
                        xiajiaPlayer.tryPengAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
                    }
                }

                // 其他玩家尝试杠 杠就不需要做上述判断了
                xiajiaPlayer.tryGangdachuAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());

                // 判断其他玩家是否能胡 仍需判断这张牌是否存在其他玩家的热张不胡牌集里
                if (!canNotHuPlayers.contains(xiajiaPlayer.getId()) && dianpao) { //点炮胡
                    GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
                    // 先把这张牌放入计算器
                    HuaibinMajiangHu bestHu = HuaibinMajiangJiesuanCalculator.calculateBestDianpaoHu(gouXingPanHu, xiajiaPlayer, daAction.getPai(), optionalPlay,currentPan);
                    // 再把这张牌拿出计算器
                    if (bestHu != null) {
                        bestHu.setDianpao(true);
                        bestHu.setDianpaoPlayerId(daPlayer.getId());
                        xiajiaPlayer.addActionCandidate(new MajiangHuAction(xiajiaPlayer.getId(), bestHu));
                    }
                }
                xiajiaPlayer.checkAndGenerateGuoCandidateAction();
            } else {
                break;
            }
            xiajiaPlayer = currentPan.findXiajia(xiajiaPlayer);
            xiajiaPlayer.clearActionCandidates();
        }

        currentPan.disablePlayerActionsByHuPengGangChiPriority();// 吃碰杠胡优先级判断
        // 如果所有玩家啥也做不了,那就下家摸牌
        if (currentPan.allPlayerHasNoActionCandidates()) {
            xiajiaPlayer = currentPan.findXiajia(daPlayer);
            xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
        }

        // 托管玩家集合中有碰 胡 吃 自动过牌
        Map<String, String> depositPlayerList = ju.getDepositPlayerList();
        Set<String> tuoguanPlayerId = depositPlayerList.keySet();
        for (String playerId : tuoguanPlayerId) {
            if (playerId.equals(daAction.getActionPlayerId())) {
                continue;
            }
            MajiangPlayer player = currentPan.findPlayerById(playerId);
            Collection<MajiangPlayerAction> values = player.getActionCandidates().values();
            for (MajiangPlayerAction action : values) {
                if (action instanceof MajiangGuoAction) {
                    executorService.submit(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        autoGuopai(player, depositPlayerList);
                    });
                }
            }
        }

        // 托管玩家集合中包含下家 自动摸牌
        MajiangPlayer xiajia = currentPan.findXiajia(daPlayer);
        boolean xiajiaHasMoAction = false;
        if (depositPlayerList.containsKey(xiajia.getId())) {
            for (MajiangPlayerAction action : xiajia.getActionCandidates().values()) {
                if (action instanceof MajiangMoAction) {
                    xiajiaHasMoAction = true;
                    break;
                }
            }
            String gameId = depositPlayerList.get(xiajia.getId());
            if (xiajiaHasMoAction && !automatic.isPlayerOnLine(xiajia.getId())) {
                executorService.submit(() -> {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    autoMopai(xiajia, depositPlayerList);
                });
            }
        }

    }

    private final Automatic automatic = SpringUtil.getBean(Automatic.class);
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 自动摸牌
     *
     * @param xiajia            下家玩家
     * @param depositPlayerList 托管玩家列表
     */
    public void autoMopai(MajiangPlayer xiajia, Map<String, String> depositPlayerList) {
        xiajia.clearActionCandidates();
        xiajia.addActionCandidate(new MajiangMoAction(xiajia.getId(), new LundaoMopai()));
        String gameId = depositPlayerList.get(xiajia.getId());
        automatic.automaticAction(xiajia.getId(), 1, gameId);
    }

    /**
     * 自动过牌
     *
     * @param xiajia            下家玩家
     * @param depositPlayerList 托管玩家列表
     */
    public void autoGuopai(MajiangPlayer xiajia, Map<String, String> depositPlayerList) {
        xiajia.clearActionCandidates();
        xiajia.addActionCandidate(new MajiangGuoAction(xiajia.getId()));
        String gameId = depositPlayerList.get(xiajia.getId());
        automatic.automaticAction(xiajia.getId(), 1, gameId);
    }

}
