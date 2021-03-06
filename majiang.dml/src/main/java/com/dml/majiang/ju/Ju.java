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
 * ????????????
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
     * ????????????
     */
    private int huangzhuangCount = 0;
    /**
     * ????????????
     */
    private boolean huangzhuang;

    /**
     * ????????????????????????
     * @throws Exception
     */
    public void determinePlayersMenFengForFirstPan() throws Exception {
        playersMenFengDeterminerForFirstPan.determinePlayersMenFeng(this);
        // TODO ????????????determiner???????????????????????????????????????????????????????????????????????????????????????
    }

    /**
     * ?????????????????????
     * @throws Exception
     */
    public void determinePlayersMenFengForNextPan() throws Exception {
        playersMenFengDeterminerForNextPan.determinePlayersMenFeng(this);
    }

    /**
     * ?????????????????????
     * @throws Exception
     */
    public void determineZhuangForFirstPan() throws Exception {
        zhuangDeterminerForFirstPan.determineZhuang(this);
    }

    /**
     * ?????????????????????
     * @throws Exception
     */
    public void determineZhuangForNextPan() throws Exception {
        zhuangDeterminerForNextPan.determineZhuang(this);
    }

    /**
     * ?????????
     * @throws Exception
     */
    public void fillAvaliablePai() throws Exception {
        avaliablePaiFiller.fillAvaliablePai(this);
    }

    /**
     * ????????????
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
     * ??????
     * @throws Exception
     */
    public void faPai() throws Exception {
        faPaiStrategy.faPai(this);
    }

    /**
     * ??????????????? ????????????
     * @return
     * @throws Exception
     */
    public PanActionFrame updateInitialAction() throws Exception {
        // ????????????
        initialActionUpdater.updateActions(this);
        // ????????????
        return currentPan.recordPanActionFrame(null, 0);
    }

    /**
     * ????????????
     * @param playerId ??????ID
     * @param actionId ??????ID
     * @param actionNo ????????????
     * @param actionTime ???????????????
     * @return ???????????????
     * @throws Exception
     */
    public PanActionFrame action(String playerId, int actionId, int actionNo, long actionTime) throws Exception {
        // ??????????????????????????????????????????????????????
        if (!currentPan.isLastestActionNo(actionNo)) { // ??????
            // ????????????????????????
            PanActionFrame panActionFrame = currentPan.findLatestActionFrame();
            // ??????????????????
            MajiangPlayerAction action = panActionFrame.getAction();
            //
            if (panActionFrame.getNo() == actionNo && action.getId() == actionId
                    && action.getActionPlayerId().equals(playerId)) {
                throw new ActionHasDoneException();
            }
            throw new WrongActionNoException();
        }

        // ??????????????????????????????????????????
        // ???????????????????????????
        MajiangPlayerAction action = currentPan.findPlayerActionCandidate(playerId, actionId);
        if (action == null) {
            throw new MajiangPlayerActionNotFoundException();
        }
        // ??????????????????
        currentPan.gangScoreClear();
        // ????????????
        doAction(action);



        currentPanPublicWaitingPlayerDeterminer.determinePublicWaitingPlayer(this);
        PanActionFrame panActionFrame = currentPan.recordPanActionFrame(action, actionTime);
        // action?????????????????????????????????
        if (currentPanFinishiDeterminer.determineToFinishCurrentPan(this)) {
            int PlayerCount = currentPan.getMajiangPlayerIdMajiangPlayerMap().size();
            finishCurrentPan(actionTime);
            // ????????????????????????
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

        if (action instanceof MajiangDaAction) {    //?????????????????????????????????
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

        if (action instanceof MajiangDaAction) {    //???????????????????????????????????????????????????
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
        // action?????????????????????????????????
        if (currentPanFinishiDeterminer.determineToFinishCurrentPan(this)) {
            int PlayerCount = currentPan.getMajiangPlayerIdMajiangPlayerMap().size();
            finishCurrentPan(actionTime);
            // ????????????????????????
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
     * ????????????
     * @param action ??????
     * @throws Exception
     */
    private void doAction(MajiangPlayerAction action) throws Exception {
        // ??????????????????
        MajiangPlayerActionType actionType = action.getType();
        // ????????????
        if (actionType.equals(MajiangPlayerActionType.mo)) {

            moActionProcessor.process((MajiangMoAction) action, this);
            actionStatisticsListenerManager.updateMoActionListener((MajiangMoAction) action, this);
            currentPan.moGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
            moActionUpdater.updateActions((MajiangMoAction) action, this);
        }
        // ????????????
        else if (actionType.equals(MajiangPlayerActionType.da)) {
            // ?????????????????????
            daActionProcessor.process((MajiangDaAction) action, this);
            actionStatisticsListenerManager.updateDaActionListener((MajiangDaAction) action, this);
            currentPan.moGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
            daActionUpdater.updateActions((MajiangDaAction) action, this);
        }
        // ????????????
        else if (actionType.equals(MajiangPlayerActionType.chi)) {
            chiActionProcessor.process((MajiangChiAction) action, this);
            actionStatisticsListenerManager.updateChiActionListener((MajiangChiAction) action, this);
            currentPan.chiOrPengGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
            chiActionUpdater.updateActions((MajiangChiAction) action, this);
        }
        // ????????????
        else if (actionType.equals(MajiangPlayerActionType.peng)) {
            pengActionProcessor.process((MajiangPengAction) action, this);
            actionStatisticsListenerManager.updatePengActionListener((MajiangPengAction) action, this);
            currentPan.chiOrPengGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
            pengActionUpdater.updateActions((MajiangPengAction) action, this);
        }
        // ????????????
        else if (actionType.equals(MajiangPlayerActionType.gang)) {
            gangActionProcessor.process((MajiangGangAction) action, this);
            actionStatisticsListenerManager.updateGangActionListener((MajiangGangAction) action, this);
            gangActionUpdater.updateActions((MajiangGangAction) action, this);
        }
        // ????????????
        else if (actionType.equals(MajiangPlayerActionType.guo)) {
            guoActionProcessor.process((MajiangGuoAction) action, this);
            actionStatisticsListenerManager.updateGuoActionListener((MajiangGuoAction) action, this);
            currentPan.moGenerateHupaiSolutionForTips(hupaiPaixingSolutionFilter, GouXingPanHu);
            guoActionUpdater.updateActions((MajiangGuoAction) action, this);
        }
        // ????????????
        else if (actionType.equals(MajiangPlayerActionType.hu)) {
            huActionProcessor.process((MajiangHuAction) action, this);
            // TODO listener?
            huActionUpdater.updateActions((MajiangHuAction) action, this);
        }
        // ????????????
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
     * ??????
     */
    public void determineLiangFeng() throws Exception {
        liangFengStrategy.liangfeng(this);
    }
}
