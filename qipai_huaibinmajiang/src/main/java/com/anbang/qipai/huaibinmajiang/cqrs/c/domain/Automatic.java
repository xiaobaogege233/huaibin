package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.MajiangPlayerXiapiaoState;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.XiapiaoResult;
import com.anbang.qipai.huaibinmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.huaibinmajiang.cqrs.c.service.MajiangPlayCmdService;
import com.anbang.qipai.huaibinmajiang.cqrs.c.service.impl.CmdServiceBase;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.*;
import com.anbang.qipai.huaibinmajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.huaibinmajiang.cqrs.q.service.MajiangPlayQueryService;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.MajiangHistoricalJuResult;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.MajiangHistoricalPanResult;
import com.anbang.qipai.huaibinmajiang.msg.service.HuaibinMajiangGameMsgService;
import com.anbang.qipai.huaibinmajiang.msg.service.HuaibinMajiangResultMsgService;
import com.anbang.qipai.huaibinmajiang.web.vo.CommonVO;
import com.anbang.qipai.huaibinmajiang.websocket.GamePlayWsNotifier;
import com.anbang.qipai.huaibinmajiang.websocket.QueryScope;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.player.valueobj.MajiangPlayerValueObject;
import com.dml.mpgame.game.Canceled;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.WaitingStart;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.vote.FinishedByTuoguan;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.extend.vote.VotingWhenPlaying;
import com.google.gson.Gson;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ??????
 */
@Component
public class Automatic extends CmdServiceBase {

    @Autowired
    private MajiangPlayQueryService majiangPlayQueryService;

    @Autowired
    private MajiangGameQueryService majiangGameQueryService;

    @Autowired
    private HuaibinMajiangResultMsgService huaibinMajiangResultMsgService;

    @Autowired
    private HuaibinMajiangGameMsgService gameMsgService;

    @Autowired
    private MajiangPlayCmdService majiangPlayCmdService;

    @Autowired
    private GameCmdService gameCmdService;

    @Autowired
    private GamePlayWsNotifier wsNotifier;

//    @Autowired
//    private MemberPowerBalanceService memberPowerBalanceService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Set<String> tuoguanPlayerIdSet = new HashSet<>();

    @Autowired
    private HttpClient httpClient;

    private final Gson gson = new Gson();

    /**
     * ????????????
     *
     * @param playerId ??????ID
     * @param id       ??????ID
     * @param gameId   ??????ID
     */
    public void automaticAction(String playerId, Integer id, String gameId) {
        List<QueryScope> queryScopes = new ArrayList<>();
        MajiangActionResult majiangActionResult;
        try {
            majiangActionResult = majiangPlayCmdService.automaticAction(playerId, id, System.currentTimeMillis(), gameId);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        majiangPlayQueryService.action(majiangActionResult);
        if (majiangActionResult.getPanResult() == null) {// ????????????
            queryScopes.add(QueryScope.panForMe);
            queryScopes.add(QueryScope.gameInfo);
        } else {// ????????????
            gameId = majiangActionResult.getMajiangGame().getId();
            MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
            if (majiangActionResult.getJuResult() != null) {// ???????????????
                JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
                MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
                huaibinMajiangResultMsgService.recordJuResult(juResult);
                gameMsgService.gameFinished(gameId);
                queryScopes.add(QueryScope.juResult);
            } else {
                int tuoguanCount = 0;
                Map<String, String> playerIdGameIdMap = gameCmdService.playLeaveGameHosting(playerId, gameId, true);
                if (playerIdGameIdMap != null) {
                    Set<String> playerIds = playerIdGameIdMap.keySet();//??????????????????
                    tuoguanCount = playerIds.size();
                }
                if (majiangGameDbo.getOptionalPlay().isTuoguanjiesan() && tuoguanCount != 0) {
                    try {
                        PanResultDbo panResultDbo = majiangPlayQueryService.findPanResultDbo(gameId, majiangActionResult.getPanResult().getPan().getNo());
                        MajiangHistoricalPanResult panResult = new MajiangHistoricalPanResult(panResultDbo, majiangGameDbo);
                        huaibinMajiangResultMsgService.recordPanResult(panResult);
                        gameMsgService.panFinished(majiangActionResult.getMajiangGame(), majiangActionResult.getPanActionFrame().getPanAfterAction());
                        MajiangGameValueObject gameValueObject = gameCmdService.finishGameImmediately(gameId);
                        majiangGameQueryService.finishGameImmediately(gameValueObject, panResultDbo);
                        JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
                        MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
                        huaibinMajiangResultMsgService.recordJuResult(juResult);
                        gameMsgService.gameFinished(gameId);
                        queryScopes.add(QueryScope.juResult);
                        for (String playerIds : gameValueObject.allPlayerIds()) {
                            wsNotifier.notifyToQuery(playerIds, QueryScope.scopesForState(gameValueObject.getState().name(), gameValueObject.findPlayerState(playerIds).name()));
                        }
                        return;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        return;
                    }
                } else {

//                    ReadyToNextPanResult readyToNextPanResult = null;
//                    Map<String, String> tuoguanPlayerIds = gameCmdService.playLeaveGameHosting(playerId, gameId, true);
//                    try {
//                        readyToNextPanResult = majiangPlayCmdService.autoReadyToNextPan(playerId, tuoguanPlayerIds.keySet(), gameId);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    List<QueryScope> scopes = new ArrayList<>();
//                    if (readyToNextPanResult != null) {
//                        majiangPlayQueryService.readyToNextPan(readyToNextPanResult);
//                        if (readyToNextPanResult.getMajiangGame().getState().name().equals(Playing.name)) {
//                            scopes.add(QueryScope.panForMe);
//                        }
//                    }
//                    scopes.add(QueryScope.gameInfo);
//                    List<MajiangPlayerValueObject> paodekuaiPlayerList = readyToNextPanResult.getFirstActionFrame().getPanAfterAction().getPlayerList();
//                    for (MajiangPlayerValueObject paodekuaiPlayerValueObject : paodekuaiPlayerList) {
//                        wsNotifier.notifyToQuery(paodekuaiPlayerValueObject.getId(), scopes);
//                    }
//                    logger.info("??????????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);

                    queryScopes.add(QueryScope.gameInfo);
                    queryScopes.add(QueryScope.panResult);
                }
            }
            PanResultDbo panResultDbo = majiangPlayQueryService.findPanResultDbo(gameId, majiangActionResult.getPanResult().getPan().getNo());
            MajiangHistoricalPanResult panResult = new MajiangHistoricalPanResult(panResultDbo, majiangGameDbo);
            huaibinMajiangResultMsgService.recordPanResult(panResult);
            gameMsgService.panFinished(majiangActionResult.getMajiangGame(), majiangActionResult.getPanActionFrame().getPanAfterAction());
        }
        List<MajiangPlayerValueObject> playerList = majiangActionResult.getPanActionFrame().getPanAfterAction().getPlayerList();
        List<String> playerIds = new ArrayList<>();
        for (MajiangPlayerValueObject valueObject : playerList) {
            playerIds.add(valueObject.getId());
        }

        wsNotifier.notifyAllOnLineToQuery(playerIds, queryScopes);

    }



    /**
     * ????????????
     *
     * @param gameId   ??????ID
     * @param playerId ??????ID
     */
    public void offlineHosting(String gameId, String playerId) {
        logger.info("????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
        MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
        OptionalPlay optionalPlay = majiangGameDbo.getOptionalPlay();
        if (optionalPlay.getTuoguan() == 0 && !optionalPlay.isLixianchengfa()) {
            logger.info("???????????????????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
            return;//?????????????????????????????????????????????
        }
        if (majiangGameDbo.getState().equals(WaitingStart.name)) {
            logger.info("??????????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
            return;//??????????????????????????????
        }
        PanActionFrame panActionFrame = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId);
        long actionTime = 0;//?????????????????????
        if (panActionFrame != null) {
            if (panActionFrame.getPanAfterAction().getPublicWaitingPlayerId().equals(playerId)) {
                actionTime = panActionFrame.getActionTime();
            } else {
                actionTime = System.currentTimeMillis();
            }
            if (!(majiangGameDbo.getState().equals(Playing.name) ||
                    majiangGameDbo.getState().equals(VotingWhenPlaying.name) ||
                    majiangGameDbo.getState().equals(VoteNotPassWhenPlaying.name))) {
                actionTime = panActionFrame.getActionTime();//?????????????????????
            }
        }
        long finalActionTime = actionTime;
        if (!tuoguanPlayerIdSet.contains(playerId)) {
            tuoguanPlayerIdSet.add(playerId);
            logger.info("??????:" + playerId + "??????????????????," + "GameID:" + gameId);
            executorService.submit(() -> {
                try {
                    int sleepTime = 0;
                    if (optionalPlay.getTuoguan() != 0) {
                        sleepTime = optionalPlay.getTuoguan();
                    } else if (optionalPlay.isLixianchengfa()) {
                        sleepTime = optionalPlay.getLixianshichang();
                    }
                    long tuoguanTime = finalActionTime + (sleepTime * 1000) - System.currentTimeMillis();//?????????????????????+??????????????????-???????????????=??????????????????
                    if (tuoguanTime > 0) {
                        Thread.sleep(tuoguanTime);
                    }
                    tuoguanPlayerIdSet.remove(playerId);
                    logger.info("??????:" + playerId + "??????????????????," + "GameID:" + gameId + ",??????????????????" + tuoguanTime + "??????");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (optionalPlay.getTuoguan() != 0) {   //????????????
                    Map<String, String> tuoguanPlayerIds = gameCmdService.playLeaveGameHosting(playerId, null, false);//gameId???null??????????????????????????????
                    boolean playerDeposit = false;
                    if (tuoguanPlayerIds != null) {
                        playerDeposit = tuoguanPlayerIds.containsKey(playerId);//???????????????????????????
                    }
                    List<PanActionFrame> panActionFrameList = null;
                    try {
                        panActionFrameList = gameCmdService.getPanActionFrame(gameId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean playerAction = false;
                    if (panActionFrameList != null) {
                        for (PanActionFrame actionFrame : panActionFrameList) {
                            if (actionFrame.getActionTime() > finalActionTime && actionFrame.getAction() != null) {
                                if (actionFrame.getAction().getActionPlayerId().equals(playerId)) {
                                    playerAction = true;//??????????????????
                                }
                            }
                        }
                    }
                    PanActionFrame panActionFrame2 = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId);
                    boolean playerOnLine = isPlayerOnLine(playerId);
                    logger.info("????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId
                            + ",playerAction:" + playerAction + ",playerDeposit:" + playerDeposit + ",isPlayerOnLine:" + playerOnLine);
                    if (!playerAction && !playerDeposit && !playerOnLine) {   //???????????????&&????????????&&?????????????????????
                        MajiangGameDbo majiangGameDbo2 = majiangGameQueryService.findMajiangGameDboById(gameId);
                        if (majiangGameDbo2.getState().equals(WaitingNextPan.name)) {  //?????????????????? ????????????????????????
                            ReadyToNextPanResult readyToNextPanResult = null;
                            Map<String, String> tuoguanzhunbeiPlayers = gameCmdService.playLeaveGameHosting(playerId, gameId, true);
                            PanResultDbo panResultDbo = majiangPlayQueryService.findPanResultDbo(gameId, majiangGameDbo.getPanNo());
                            for (ShanxiMajiangPanPlayerResultDbo shanxiMajiangPanPlayerResultDbo : panResultDbo.getPlayerResultList()) {
                                if (tuoguanzhunbeiPlayers.containsKey(shanxiMajiangPanPlayerResultDbo.getPlayerId())) {
                                    Request req = httpClient.newRequest("http://localhost:92/power/nowPowerForRemote");
                                    req.param("memberId", playerId);
                                    req.param("lianmengId", majiangGameDbo2.getLianmengId());
                                    Map resData;
                                    CommonVO resVo;
                                    try {
                                        ContentResponse res = req.send();
                                        String resJson = new String(res.getContent());
                                        resVo = gson.fromJson(resJson, CommonVO.class);
                                        resData = (Map) resVo.getData();
                                    } catch (Exception e) {
                                        return ;
                                    }
                                    if (resVo.isSuccess()) {
                                        int powerbalance = ((Double) resData.get("powerbalance")).intValue();
                                        if (shanxiMajiangPanPlayerResultDbo.getPlayerResult().getTotalScore() + powerbalance <= majiangGameDbo.getPowerLimit()) {
                                            MajiangGameValueObject gameValueObject = null;
                                            try {
                                                gameValueObject = gameCmdService.finishGameImmediately(gameId);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            List<QueryScope> queryScopes = new ArrayList<>();
                                            if (gameValueObject != null) {
                                                majiangGameQueryService.finishGameImmediately(gameValueObject, null);
                                                gameMsgService.gameFinished(gameId);
                                                JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
                                                MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo,
                                                        majiangGameDbo);
                                                huaibinMajiangResultMsgService.recordJuResult(juResult);
                                                gameMsgService.gameFinished(gameId);
                                                queryScopes.add(QueryScope.juResult);
                                            }
                                            for (MajiangPlayerValueObject majiangPlayerValueObject : panActionFrame2.getPanAfterAction().getPlayerList()) {
                                                wsNotifier.notifyToQuery(majiangPlayerValueObject.getId(), queryScopes);
                                            }
                                            logger.info("?????????????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
                                            return;
                                        }

                                    }
                                }
                            }

                            try {
                                readyToNextPanResult = majiangPlayCmdService.autoReadyToNextPan(playerId, tuoguanzhunbeiPlayers.keySet(), gameId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            List<QueryScope> queryScopes = new ArrayList<>();
                            if (readyToNextPanResult != null) {
                                majiangPlayQueryService.readyToNextPan(readyToNextPanResult);
                                if (readyToNextPanResult.getMajiangGame().getState().name().equals(Playing.name)) {
                                    queryScopes.add(QueryScope.panForMe);
                                }
                            }
                            queryScopes.add(QueryScope.gameInfo);
                            for (MajiangPlayerValueObject majiangPlayerValueObject : panActionFrame2.getPanAfterAction().getPlayerList()) {
                                wsNotifier.notifyToQuery(majiangPlayerValueObject.getId(), queryScopes);
                            }
                            logger.info("????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
                            return;
                        }

                        gameCmdService.playLeaveGameHosting(playerId, gameId, true);//?????????????????????????????????

                        MajiangGamePlayerXiapiaoDbo majiangGamePlayerXiapiaoDbo = majiangPlayQueryService.findLastPlayerXiapiaoDboByGameId(gameId);
                        if (majiangGamePlayerXiapiaoDbo != null) {
                            Map<String, MajiangPlayerXiapiaoState> playerXiapiaoStateMap = majiangGamePlayerXiapiaoDbo.getPlayerXiapiaoStateMap();
                            if (playerXiapiaoStateMap.get(playerId) != null) {
                                if (playerXiapiaoStateMap.get(playerId).equals(MajiangPlayerXiapiaoState.waitForxiapiao)) {
                                    automaticXiapiao(playerId, 0, gameId); //??????
                                    logger.info("????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
                                    return;
                                }
                            }
                        }

                        automaticAction(playerId, 1, gameId); //????????????
                        PanActionFrame panActionFrame3 = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId);
                        if (panActionFrame3.getAction() != null) {
                            MajiangPlayerAction action = panActionFrame3.getAction();
                            if (action instanceof MajiangChiAction ||
                                    action instanceof MajiangPengAction ||
                                    action instanceof MajiangGangAction ||
                                    action instanceof MajiangGuoAction) {   //?????????????????????????????????????????????????????????????????????????????????????????????
                                automaticAction(playerId, 1, gameId);
                            }
                        }
                        panActionFrame3 = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId);
                        if (panActionFrame3 != null) {
                            MajiangPlayerAction action = panActionFrame3.getAction();
                            if (action instanceof MajiangGangAction) {  //????????????????????? ????????????????????? ???????????????
                                automaticAction(playerId, 1, gameId);
                            }
                        }
                        logger.info("????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
                    }
                } else if (optionalPlay.isLixianchengfa()) {   //????????????
                    MajiangGameDbo majiangGameDbo2 = majiangGameQueryService.findMajiangGameDboById(gameId);
                    List<MajiangGamePlayerDbo> players = majiangGameDbo2.getPlayers();
                    for (MajiangGamePlayerDbo majiangGamePlayerDbo : players) {
                        if (majiangGamePlayerDbo.getPlayerId().equals(playerId) && !isPlayerOnLine(playerId)) {
                            automaticFinish(gameId, playerId, optionalPlay.getLixianchengfaScore(), players);
                            List<QueryScope> scopes = new ArrayList<>();
                            scopes.add(QueryScope.juResult);
                            for (MajiangGamePlayerDbo playerDbo : players) {
                                wsNotifier.notifyToQuery(playerDbo.getPlayerId(), scopes);
                            }
                            logger.info("????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
                        }
                    }
                }

            });
        }

    }

    /**
     * ????????????????????????
     *
     * @param gameId       ??????ID
     * @param lixianPlayer ????????????
     * @param chengfaScore ????????????
     */
    public void automaticFinish(String gameId, String lixianPlayer, double chengfaScore, List<MajiangGamePlayerDbo> players) {
        MajiangGameValueObject majiangGameValueObject = null;
        try {
            majiangGameValueObject = gameCmdService.automaticFinish(gameId);
            //???????????????
            HuaibinMajiangJuResult juResult = (HuaibinMajiangJuResult) majiangGameValueObject.getJuResult();
            List<HuaibinMajiangJuPlayerResult> playerResultList = juResult.getPlayerResultList();
            switch (playerResultList.size()) {
                case 0://?????????????????????juResult
                    switch (players.size()) {
                        case 2:
                            for (MajiangGamePlayerDbo majiangGamePlayerDbo : players) {
                                if (majiangGamePlayerDbo.getPlayerId().equals(lixianPlayer)) {
                                    HuaibinMajiangJuPlayerResult result = new HuaibinMajiangJuPlayerResult();
                                    result.setPlayerId(majiangGamePlayerDbo.getPlayerId());
                                    result.setTotalScore(-chengfaScore);
                                    playerResultList.add(result);
                                    juResult.setDatuhaoId(majiangGamePlayerDbo.getPlayerId());
                                } else {
                                    HuaibinMajiangJuPlayerResult result = new HuaibinMajiangJuPlayerResult();
                                    result.setPlayerId(majiangGamePlayerDbo.getPlayerId());
                                    result.setTotalScore(chengfaScore);
                                    playerResultList.add(result);
                                    juResult.setDayingjiaId(majiangGamePlayerDbo.getPlayerId());
                                }
                            }
                            break;
                        case 3:
                            double score2 = chengfaScore / 2;
                            for (MajiangGamePlayerDbo majiangGamePlayerDbo : players) {
                                if (majiangGamePlayerDbo.getPlayerId().equals(lixianPlayer)) {
                                    HuaibinMajiangJuPlayerResult result = new HuaibinMajiangJuPlayerResult();
                                    result.setPlayerId(majiangGamePlayerDbo.getPlayerId());
                                    result.setTotalScore(-chengfaScore);
                                    playerResultList.add(result);
                                    juResult.setDatuhaoId(majiangGamePlayerDbo.getPlayerId());
                                } else {
                                    HuaibinMajiangJuPlayerResult result = new HuaibinMajiangJuPlayerResult();
                                    result.setPlayerId(majiangGamePlayerDbo.getPlayerId());
                                    result.setTotalScore(score2);
                                    playerResultList.add(result);
                                    if (juResult.getDayingjiaId() == null) {
                                        juResult.setDayingjiaId(majiangGamePlayerDbo.getPlayerId());
                                    }
                                }
                            }
                            break;
                        case 4:
                            double score3 = chengfaScore / 3;
                            for (MajiangGamePlayerDbo majiangGamePlayerDbo : players) {
                                if (majiangGamePlayerDbo.getPlayerId().equals(lixianPlayer)) {
                                    HuaibinMajiangJuPlayerResult result = new HuaibinMajiangJuPlayerResult();
                                    result.setPlayerId(majiangGamePlayerDbo.getPlayerId());
                                    result.setTotalScore(-chengfaScore);
                                    playerResultList.add(result);
                                    juResult.setDatuhaoId(majiangGamePlayerDbo.getPlayerId());
                                } else {
                                    HuaibinMajiangJuPlayerResult result = new HuaibinMajiangJuPlayerResult();
                                    result.setPlayerId(majiangGamePlayerDbo.getPlayerId());
                                    result.setTotalScore(score3);
                                    playerResultList.add(result);
                                    if (juResult.getDayingjiaId() == null) {
                                        juResult.setDayingjiaId(majiangGamePlayerDbo.getPlayerId());
                                    }
                                }
                            }
                            break;
                    }
                    break;
                case 2:
                    for (HuaibinMajiangJuPlayerResult juPlayerResult : playerResultList) {
                        if (juPlayerResult.getPlayerId().equals(lixianPlayer)) {
                            juPlayerResult.setTotalScore(juPlayerResult.getTotalScore() - chengfaScore);
                        } else {
                            juPlayerResult.setTotalScore(juPlayerResult.getTotalScore() + chengfaScore);
                        }
                    }
                    break;
                case 3:
                    double score2 = chengfaScore / 2;
                    for (HuaibinMajiangJuPlayerResult juPlayerResult : playerResultList) {
                        if (juPlayerResult.getPlayerId().equals(lixianPlayer)) {
                            juPlayerResult.setTotalScore(juPlayerResult.getTotalScore() - chengfaScore);
                        } else {
                            juPlayerResult.setTotalScore(juPlayerResult.getTotalScore() + score2);
                        }
                    }
                    break;
                case 4:
                    double score3 = chengfaScore / 3;
                    for (HuaibinMajiangJuPlayerResult juPlayerResult : playerResultList) {
                        if (juPlayerResult.getPlayerId().equals(lixianPlayer)) {
                            juPlayerResult.setTotalScore(juPlayerResult.getTotalScore() - chengfaScore);
                        } else {
                            juPlayerResult.setTotalScore(juPlayerResult.getTotalScore() + score3);
                        }
                    }
                    break;
            }

            majiangGameQueryService.voteToFinish(majiangGameValueObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
        // ????????????

        if (majiangGameValueObject.getState().name().equals(FinishedByTuoguan.name) || majiangGameValueObject.getState().name().equals(Canceled.name)) {
            gameMsgService.gameFinished(gameId);
            if (juResultDbo != null) {
                MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
                MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
                huaibinMajiangResultMsgService.recordJuResult(juResult);
            }
        }
    }

//    /**
//     * ??????????????????
//     *
//     * @param playerId ??????ID
//     * @param gameId   ??????ID
//     */
//    public boolean isPlayerOnLine(String playerId, String gameId) {
//        boolean isOnLine = false;
//        if (gameId == null) return false;
//        MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
//        List<MajiangGamePlayerDbo> players = majiangGameDbo.getPlayers();
//        for (MajiangGamePlayerDbo majiangGamePlayerDbo : players) {
//            if (majiangGamePlayerDbo.getPlayerId().equals(playerId)) {
//                isOnLine = majiangGamePlayerDbo.getOnlineState() == GamePlayerOnlineState.online;
//            }
//        }
//        return isOnLine;
//    }

    /**
     * ??????????????????   Socket
     *
     * @param playerId ??????ID
     */
    public boolean isPlayerOnLine(String playerId) {
        return wsNotifier.hasSessionForPlayer(playerId);
    }

    /**
     * ??????
     *
     * @param playerId ??????ID
     * @param piaofen  ??????
     */
    public void automaticXiapiao(String playerId, int piaofen) {
        XiapiaoResult xiapiaoResult = null;
        try {
            xiapiaoResult = majiangPlayCmdService.xiapiao(playerId, piaofen);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (xiapiaoResult != null) {
            majiangPlayQueryService.xiapiao(xiapiaoResult);
            // ???????????????
            for (String otherPlayerId : xiapiaoResult.getMajiangGame().allPlayerIds()) {
                if (!otherPlayerId.equals(playerId)) {
                    wsNotifier.notifyToQuery(otherPlayerId, QueryScope.scopesForState(xiapiaoResult.getMajiangGame().getState().name(), xiapiaoResult.getMajiangGame().findPlayerState(otherPlayerId).name()));
                }
            }
        }

    }

    /**
     * ??????
     *
     * @param playerId ??????ID
     * @param piaofen  ??????
     * @param gameId   ??????ID
     */
    public void automaticXiapiao(String playerId, int piaofen, String gameId) {
        XiapiaoResult xiapiaoResult = null;
        try {
            xiapiaoResult = majiangPlayCmdService.xiapiao(playerId, piaofen, gameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (xiapiaoResult != null) {
            majiangPlayQueryService.xiapiao(xiapiaoResult);
            // ???????????????
            for (String otherPlayerId : xiapiaoResult.getMajiangGame().allPlayerIds()) {
                if (!otherPlayerId.equals(playerId)) {
                    wsNotifier.notifyToQuery(otherPlayerId, QueryScope.scopesForState(xiapiaoResult.getMajiangGame().getState().name(), xiapiaoResult.getMajiangGame().findPlayerState(otherPlayerId).name()));
                }
            }
        }

    }

    /**
     * ????????????????????????
     *
     * @param playerId ??????ID
     */
    public void removeTuoguanPlayerIdSet(String playerId) {
        boolean remove = tuoguanPlayerIdSet.remove(playerId);
        if (remove) {
            logger.info("???????????????????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId);
        }
    }

}
