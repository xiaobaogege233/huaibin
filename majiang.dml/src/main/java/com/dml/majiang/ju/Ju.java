package com.dml.majiang.ju;

import java.util.*;

import com.dml.majiang.ju.finish.JuFinishiDeterminer;
import com.dml.majiang.ju.firstpan.StartFirstPanProcess;
import com.dml.majiang.ju.nextpan.StartNextPanProcess;
import com.dml.majiang.ju.result.JuResult;
import com.dml.majiang.ju.result.JuResultBuilder;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.avaliablepai.AvaliablePaiFiller;
import com.dml.majiang.pan.banzi.BanziAndPeiziDeterminer;
import com.dml.majiang.pan.fapai.FaPaiStrategy;
import com.dml.majiang.pan.finish.CurrentPanFinishiDeterminer;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.pan.guipai.GuipaiDeterminer;
import com.dml.majiang.pan.liangfeng.LiangFengStrategy;
import com.dml.majiang.pan.publicwaitingplayer.CurrentPanPublicWaitingPlayerDeterminer;
import com.dml.majiang.pan.result.CurrentPanResultBuilder;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.ActionHasDoneException;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionNotFoundException;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.WrongActionNoException;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.chi.MajiangPlayerChiActionProcessor;
import com.dml.majiang.player.action.chi.MajiangPlayerChiActionUpdater;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.da.MajiangPlayerDaActionProcessor;
import com.dml.majiang.player.action.da.MajiangPlayerDaActionUpdater;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.gang.MajiangPlayerGangActionProcessor;
import com.dml.majiang.player.action.gang.MajiangPlayerGangActionUpdater;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.guo.MajiangPlayerGuoActionProcessor;
import com.dml.majiang.player.action.guo.MajiangPlayerGuoActionUpdater;
import com.dml.majiang.player.action.hu.HupaiPaixingSolutionFilter;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.hu.MajiangPlayerHuActionProcessor;
import com.dml.majiang.player.action.hu.MajiangPlayerHuActionUpdater;
import com.dml.majiang.player.action.initial.MajiangPlayerInitialActionUpdater;
import com.dml.majiang.player.action.listener.ActionStatisticsListenerManager;
import com.dml.majiang.player.action.listener.MajiangPlayerActionStatisticsListener;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionProcessor;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionUpdater;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.player.action.peng.MajiangPlayerPengActionProcessor;
import com.dml.majiang.player.action.peng.MajiangPlayerPengActionUpdater;
import com.dml.majiang.player.action.ting.MajiangPlayerTingActionProcessor;
import com.dml.majiang.player.action.ting.MajiangPlayerTingActionUpdater;
import com.dml.majiang.player.action.ting.MajiangTingAction;
import com.dml.majiang.player.menfeng.PlayersMenFengDeterminer;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;
import com.dml.majiang.player.zhuang.ZhuangDeterminer;
import lombok.Data;

/**
 * 一局麻将
 *
 * @author Neo
 */
@Data
public class Ju {

    private Pan currentPan;

    private List<PanResult> finishedPanResultList = new ArrayList<>();
    private Map<String, String> depositPlayerList = new HashMap<>();
    private JuResult juResult;
    private Set<String> gaungtouPlayers = new HashSet<>();

    private StartFirstPanProcess startFirstPanProcess;
    private StartNextPanProcess startNextPanProcess;
    private ActionStatisticsListenerManager actionStatisticsListenerManager = new ActionStatisticsListenerManager();
    private PlayersMenFengDeterminer playersMenFengDeterminerForFirstPan;
    private PlayersMenFengDeterminer playersMenFengDeterminerForNextPan;
    private ZhuangDeterminer zhuangDeterminerForFirstPan;
    private ZhuangDeterminer zhuangDeterminerForNextPan;
    private AvaliablePaiFiller avaliablePaiFiller;
    private GuipaiDeterminer guipaiDeterminer;
    private BanziAndPeiziDeterminer banziAndPeiziDeterminer;
    private FaPaiStrategy faPaiStrategy;
    private CurrentPanFinishiDeterminer currentPanFinishiDeterminer;
    private GouXingPanHu GouXingPanHu;
    private CurrentPanPublicWaitingPlayerDeterminer currentPanPublicWaitingPlayerDeterminer;
    private CurrentPanResultBuilder currentPanResultBuilder;
    private JuFinishiDeterminer juFinishiDeterminer;
    private JuResultBuilder juResultBuilder;
    private MajiangPlayerInitialActionUpdater initialActionUpdater;
    private MajiangPlayerMoActionProcessor moActionProcessor;
    private MajiangPlayerMoActionUpdater moActionUpdater;
    private MajiangPlayerDaActionProcessor daActionProcessor;
    private MajiangPlayerDaActionUpdater daActionUpdater;
    private MajiangPlayerChiActionProcessor chiActionProcessor;
    private MajiangPlayerChiActionUpdater chiActionUpdater;
    private MajiangPlayerPengActionProcessor pengActionProcessor;
    private MajiangPlayerPengActionUpdater pengActionUpdater;
    private MajiangPlayerGangActionProcessor gangActionProcessor;
    private MajiangPlayerGangActionUpdater gangActionUpdater;
    private MajiangPlayerGuoActionProcessor guoActionProcessor;
    private MajiangPlayerGuoActionUpdater guoActionUpdater;
    private MajiangPlayerHuActionProcessor huActionProcessor;
    private MajiangPlayerHuActionUpdater huActionUpdater;
    private MajiangPlayerTingActionProcessor tingActionProcessor;
    private MajiangPlayerTingActionUpdater tingActionUpdater;
    private HupaiPaixingSolutionFilter hupaiPaixingSolutionFilter;

    private LiangFengStrategy liangFengStrategy;

    /**
     * 荒庄数量
     */
    private int huangzhuangCount = 0;
    /**
     * 是否荒庄
     */
    private boolean huangzhuang;

    /**
     * 决定第一局的位置
     * @throws Exception
     */
    public void determinePlayersMenFengForFirstPan() throws Exception {
        playersMenFengDeterminerForFirstPan.determinePlayersMenFeng(this);
        // TODO 或许每个determiner运行过之后都需要统计一些个性化的信息，比如目前谁连庄了几次
    }

    /**
     * 决定下一把位置
     * @throws Exception
     */
    public void determinePlayersMenFengForNextPan() throws Exception {
        playersMenFengDeterminerForNextPan.determinePlayersMenFeng(this);
    }

    /**
     * 决定第一盘庄家
     * @throws Exception
     */
    public void determineZhuangForFirstPan() throws Exception {
        zhuangDeterminerForFirstPan.determineZhuang(this);
    }

    /**
     * 决定下一把庄家
     * @throws Exception
     */
    public void determineZhuangForNextPan() throws Exception {
        zhuangDeterminerForNextPan.determineZhuang(this);
    }

    /**
     * 填充牌
     * @throws Exception
     */
    public void fillAvaliablePai() throws Exception {
        avaliablePaiFiller.fillAvaliablePai(this);
    }

    /**
     * 决定鬼牌
     * @throws Exception
     */
    public void determineGuipai() throws Exception {
        guipaiDeterminer.determineGuipai(this);
    }

    /**
     *
     * @throws Exception
     */
    public void determineBanziAndPeizi() throws Exception {
        banziAndPeiziDeterminer.determineBanziAndPeizi(this);
    }

    /**
     * 发牌
     * @throws Exception
     */
    public void faPai() throws Exception {
        faPaiStrategy.faPai(this);
    }

    /**
     * 初始化动作 庄家摸牌
     * @return
     * @throws Exception
     */
    public PanActionFrame updateInitialAction() throws Exception {
        // 更新动作
        initialActionUpdater.updateActions(this);
        // 记录动作
        return currentPan.recordPanActionFrame(null, 0);
    }

    /**
     * 打牌流程
     * @param playerId 玩家ID
     * @param actionId 动作ID
     * @param actionNo 动作序号
     * @param actionTime 打牌的时间
     * @return 盘动作记录
     * @throws Exception
     */
    public PanActionFrame action(String playerId, int actionId, int actionNo, long actionTime) throws Exception {
        // 判断该序号的是否是当前盘最新一个动作
        if (!currentPan.isLastestActionNo(actionNo)) { // 不是
            // 找到最后一个动作
            PanActionFrame panActionFrame = currentPan.findLatestActionFrame();
            // 得到玩家动作
            MajiangPlayerAction action = panActionFrame.getAction();
            //
            if (panActionFrame.getNo() == actionNo && action.getId() == actionId
                    && action.getActionPlayerId().equals(playerId)) {
                throw new ActionHasDoneException();
            }
            throw new WrongActionNoException();
        }

        // 这里则表示这是最新的一个动作
        // 获取到该玩家的动作
        MajiangPlayerAction action = currentPan.findPlayerActionCandidate(playerId, actionId);
        if (action == null) {
            throw new MajiangPlayerActionNotFoundException();
        }
        // 即时结算杠分
        currentPan.gangScoreClear();
        // 执行动作
        doAction(action);



        currentPanPublicWaitingPlayerDeterminer.determinePublicWaitingPlayer(this);
        PanActionFrame panActionFrame = currentPan.recordPanActionFrame(action, actionTime);
        // action之后要试探一盘是否结束
        if (currentPanFinishiDeterminer.determineToFinishCurrentPan(this)) {
            int PlayerCount = currentPan.getMajiangPlayerIdMajiangPlayerMap().size();
            finishCurrentPan(actionTime);
            // 试探一局是否结束
            if (juFinishiDeterminer.determineToFinishJu(this) || guangtouOver(PlayerCount)) {
                finish();
            }
        }



        return panActionFrame;
    }

    public PanActionFrame automaticAction(String playerId, int actionId, long actionTime) throws Exception {
        MajiangPlayerAction action = currentPan.findPlayerActionCandidate(playerId, actionId);
        if (action == null) {
            throw new MajiangPlayerActionNotFoundException();
        }

        if (action instanceof MajiangDaAction) {    //托管玩家打刚摸入的手牌
            MajiangDaAction daAction = (MajiangDaAction) action;
            MajiangPlayer player = currentPan.findPlayerById(playerId);
            if (player.getGangmoShoupai() != null && !player.isTingpai()) {
                if (!currentPan.getPublicGuipaiSet().contains(player.getGangmoShoupai())) {
                    daAction.setPai(player.getGangmoShoupai());
                }
            }
        }


        currentPan.gangScoreClear();
        doAction(action);

        if (action instanceof MajiangDaAction) {    //托管玩家打牌后清空动作防止重复出牌
            MajiangPlayer player = currentPan.findPlayerById(playerId);
            player.clearActionCandidates();
        }

        if (action.getType().equals(MajiangPlayerActionType.mo) || action.getType().equals(MajiangPlayerActionType.guo) || action.getType().equals(MajiangPlayerActionType.da)) {
            currentPan.moGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
        } else if (action.getType().equals(MajiangPlayerActionType.chi) || action.getType().equals(MajiangPlayerActionType.peng)) {
            currentPan.chiOrPengGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
        }
        currentPanPublicWaitingPlayerDeterminer.determinePublicWaitingPlayer(this);
        PanActionFrame panActionFrame = currentPan.recordPanActionFrame(action, actionTime);
        // action之后要试探一盘是否结束
        if (currentPanFinishiDeterminer.determineToFinishCurrentPan(this)) {
            int PlayerCount = currentPan.getMajiangPlayerIdMajiangPlayerMap().size();
            finishCurrentPan(actionTime);
            // 试探一局是否结束
            if (juFinishiDeterminer.determineToFinishJu(this) || guangtouOver(PlayerCount)) {
                finish();
            }
        }

        return panActionFrame;
    }

    public void finish() {
        juResult = juResultBuilder.buildJuResult(this);
    }

    public void finishCurrentPan(long finishTime) {
        PanResult currentPanResult = currentPanResultBuilder.buildCurrentPanResult(this, finishTime);
        addFinishedPanResult(currentPanResult);
        setCurrentPan(null);
    }

    public void addFinishedPanResult(PanResult panResult) {
        finishedPanResultList.add(panResult);
    }

    /**
     * 执行动作
     * @param action 动作
     * @throws Exception
     */
    private void doAction(MajiangPlayerAction action) throws Exception {
        // 获取动作类型
        MajiangPlayerActionType actionType = action.getType();
        // 动作为摸
        if (actionType.equals(MajiangPlayerActionType.mo)) {

            moActionProcessor.process((MajiangMoAction) action, this);
            actionStatisticsListenerManager.updateMoActionListener((MajiangMoAction) action, this);
            currentPan.moGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
            moActionUpdater.updateActions((MajiangMoAction) action, this);
        }
        // 动作为打
        else if (actionType.equals(MajiangPlayerActionType.da)) {
            // 执行打动作流程
            daActionProcessor.process((MajiangDaAction) action, this);
            actionStatisticsListenerManager.updateDaActionListener((MajiangDaAction) action, this);
            currentPan.moGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
            daActionUpdater.updateActions((MajiangDaAction) action, this);
        }
        // 动作为吃
        else if (actionType.equals(MajiangPlayerActionType.chi)) {
            chiActionProcessor.process((MajiangChiAction) action, this);
            actionStatisticsListenerManager.updateChiActionListener((MajiangChiAction) action, this);
            currentPan.chiOrPengGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
            chiActionUpdater.updateActions((MajiangChiAction) action, this);
        }
        // 动作为碰
        else if (actionType.equals(MajiangPlayerActionType.peng)) {
            pengActionProcessor.process((MajiangPengAction) action, this);
            actionStatisticsListenerManager.updatePengActionListener((MajiangPengAction) action, this);
            currentPan.chiOrPengGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
            pengActionUpdater.updateActions((MajiangPengAction) action, this);
        }
        // 动作为杠
        else if (actionType.equals(MajiangPlayerActionType.gang)) {
            gangActionProcessor.process((MajiangGangAction) action, this);
            actionStatisticsListenerManager.updateGangActionListener((MajiangGangAction) action, this);
            gangActionUpdater.updateActions((MajiangGangAction) action, this);
        }
        // 动作为过
        else if (actionType.equals(MajiangPlayerActionType.guo)) {
            guoActionProcessor.process((MajiangGuoAction) action, this);
            actionStatisticsListenerManager.updateGuoActionListener((MajiangGuoAction) action, this);
            currentPan.moGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
            guoActionUpdater.updateActions((MajiangGuoAction) action, this);
        }
        // 动作为胡
        else if (actionType.equals(MajiangPlayerActionType.hu)) {
            huActionProcessor.process((MajiangHuAction) action, this);
            // TODO listener?
            huActionUpdater.updateActions((MajiangHuAction) action, this);
        }
        // 动作为听
        else if (actionType.equals(MajiangPlayerActionType.ting)) {
            tingActionProcessor.process((MajiangTingAction) action, this);
            tingActionUpdater.updateActions((MajiangTingAction) action, this);
        }

        else {
        }

    }

    public boolean guangtouOver(int PlayerCount) {
        int guangtouPlayerCount = gaungtouPlayers.size();
        if (PlayerCount == 2 && guangtouPlayerCount >= 1) {
            return true;
        } else if (guangtouPlayerCount >= 2) {
            return true;
        } else {
            return false;
        }
    }

    public void addActionStatisticsListener(MajiangPlayerActionStatisticsListener listener) {
        actionStatisticsListenerManager.addListener(listener);
    }

    public PanResult findLatestFinishedPanResult() {
        if (!finishedPanResultList.isEmpty()) {
            return finishedPanResultList.get(finishedPanResultList.size() - 1);
        } else {
            return null;
        }
    }

    public int countFinishedPan() {
        return finishedPanResultList.size();
    }

    public void startFirstPan(List<String> allPlayerIds) throws Exception {
        startFirstPanProcess.startFirstPan(this, allPlayerIds);
    }

    public void startNextPan() throws Exception {
        actionStatisticsListenerManager.updateListenersForNextPan();
        startNextPanProcess.startNextPan(this);
    }

    public void updateShoupaiListSortComparatorForAllPlayersInCurrentPan(Comparator<MajiangPai> comparator) {
        currentPan.updateShoupaiListSortComparatorForAllPlayers(comparator);
    }

    /**
     * 亮风
     */
    public void determineLiangFeng() throws Exception {
        liangFengStrategy.liangfeng(this);
    }
}
