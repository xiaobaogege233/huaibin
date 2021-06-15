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
 * 托管
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
     * 自动出牌
     *
     * @param playerId 玩家ID
     * @param id       动作ID
     * @param gameId   游戏ID
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
        if (majiangActionResult.getPanResult() == null) {// 盘没结束
            queryScopes.add(QueryScope.panForMe);
            queryScopes.add(QueryScope.gameInfo);
        } else {// 盘结束了
            gameId = majiangActionResult.getMajiangGame().getId();
            MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
            if (majiangActionResult.getJuResult() != null) {// 局也结束了
                JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
                MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
                huaibinMajiangResultMsgService.recordJuResult(juResult);
                gameMsgService.gameFinished(gameId);
                queryScopes.add(QueryScope.juResult);
            } else {
                int tuoguanCount = 0;
                Map<String, String> playerIdGameIdMap = gameCmdService.playLeaveGameHosting(playerId, gameId, true);
                if (playerIdGameIdMap != null) {
                    Set<String> playerIds = playerIdGameIdMap.keySet();//托管玩家集合
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
//                    logger.info("打牌托管准备," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);

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
     * 离线托管
     *
     * @param gameId   游戏ID
     * @param playerId 玩家ID
     */
    public void offlineHosting(String gameId, String playerId) {
        logger.info("玩家离线," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
        MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
        OptionalPlay optionalPlay = majiangGameDbo.getOptionalPlay();
        if (optionalPlay.getTuoguan() == 0 && !optionalPlay.isLixianchengfa()) {
            logger.info("没有托管和离线惩罚," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
            return;//不托管或着没有离线惩罚直接返回
        }
        if (majiangGameDbo.getState().equals(WaitingStart.name)) {
            logger.info("游戏没有开始," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
            return;//游戏没有开始直接返回
        }
        PanActionFrame panActionFrame = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId);
        long actionTime = 0;//上一动作时间戳
        if (panActionFrame != null) {
            if (panActionFrame.getPanAfterAction().getPublicWaitingPlayerId().equals(playerId)) {
                actionTime = panActionFrame.getActionTime();
            } else {
                actionTime = System.currentTimeMillis();
            }
            if (!(majiangGameDbo.getState().equals(Playing.name) ||
                    majiangGameDbo.getState().equals(VotingWhenPlaying.name) ||
                    majiangGameDbo.getState().equals(VoteNotPassWhenPlaying.name))) {
                actionTime = panActionFrame.getActionTime();//上一动作时间戳
            }
        }
        long finalActionTime = actionTime;
        if (!tuoguanPlayerIdSet.contains(playerId)) {
            tuoguanPlayerIdSet.add(playerId);
            logger.info("玩家:" + playerId + "进入托管集合," + "GameID:" + gameId);
            executorService.submit(() -> {
                try {
                    int sleepTime = 0;
                    if (optionalPlay.getTuoguan() != 0) {
                        sleepTime = optionalPlay.getTuoguan();
                    } else if (optionalPlay.isLixianchengfa()) {
                        sleepTime = optionalPlay.getLixianshichang();
                    }
                    long tuoguanTime = finalActionTime + (sleepTime * 1000) - System.currentTimeMillis();//上一动作时间戳+进入托管时间-当前时间戳=剩余计时时间
                    if (tuoguanTime > 0) {
                        Thread.sleep(tuoguanTime);
                    }
                    tuoguanPlayerIdSet.remove(playerId);
                    logger.info("玩家:" + playerId + "移除托管集合," + "GameID:" + gameId + ",离线托管计时" + tuoguanTime + "毫秒");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (optionalPlay.getTuoguan() != 0) {   //离线托管
                    Map<String, String> tuoguanPlayerIds = gameCmdService.playLeaveGameHosting(playerId, null, false);//gameId传null返回当前托管玩家集合
                    boolean playerDeposit = false;
                    if (tuoguanPlayerIds != null) {
                        playerDeposit = tuoguanPlayerIds.containsKey(playerId);//当前玩家是否已托管
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
                                    playerAction = true;//玩家有过动作
                                }
                            }
                        }
                    }
                    PanActionFrame panActionFrame2 = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId);
                    boolean playerOnLine = isPlayerOnLine(playerId);
                    logger.info("玩家状态," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId
                            + ",playerAction:" + playerAction + ",playerDeposit:" + playerDeposit + ",isPlayerOnLine:" + playerOnLine);
                    if (!playerAction && !playerDeposit && !playerOnLine) {   //没有出过牌&&没有托管&&没有打到下一盘
                        MajiangGameDbo majiangGameDbo2 = majiangGameQueryService.findMajiangGameDboById(gameId);
                        if (majiangGameDbo2.getState().equals(WaitingNextPan.name)) {  //游戏没有开始 托管玩家自动准备
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
                                            logger.info("开始下一局解散," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
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
                            logger.info("托管准备," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
                            return;
                        }

                        gameCmdService.playLeaveGameHosting(playerId, gameId, true);//把托管玩家放入托管列表

                        MajiangGamePlayerXiapiaoDbo majiangGamePlayerXiapiaoDbo = majiangPlayQueryService.findLastPlayerXiapiaoDboByGameId(gameId);
                        if (majiangGamePlayerXiapiaoDbo != null) {
                            Map<String, MajiangPlayerXiapiaoState> playerXiapiaoStateMap = majiangGamePlayerXiapiaoDbo.getPlayerXiapiaoStateMap();
                            if (playerXiapiaoStateMap.get(playerId) != null) {
                                if (playerXiapiaoStateMap.get(playerId).equals(MajiangPlayerXiapiaoState.waitForxiapiao)) {
                                    automaticXiapiao(playerId, 0, gameId); //下炮
                                    logger.info("托管下漂," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
                                    return;
                                }
                            }
                        }

                        automaticAction(playerId, 1, gameId); //自动出牌
                        PanActionFrame panActionFrame3 = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId);
                        if (panActionFrame3.getAction() != null) {
                            MajiangPlayerAction action = panActionFrame3.getAction();
                            if (action instanceof MajiangChiAction ||
                                    action instanceof MajiangPengAction ||
                                    action instanceof MajiangGangAction ||
                                    action instanceof MajiangGuoAction) {   //如果玩家可以吃碰杠别人打出的牌，那么第一次是过，再次帮玩家出牌
                                automaticAction(playerId, 1, gameId);
                            }
                        }
                        panActionFrame3 = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId);
                        if (panActionFrame3 != null) {
                            MajiangPlayerAction action = panActionFrame3.getAction();
                            if (action instanceof MajiangGangAction) {  //有可能第一次碰 第二次就是补杠 所以要摸牌
                                automaticAction(playerId, 1, gameId);
                            }
                        }
                        logger.info("离线托管," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
                    }
                } else if (optionalPlay.isLixianchengfa()) {   //离线惩罚
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
                            logger.info("离线惩罚," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",gameId:" + gameId);
                        }
                    }
                }

            });
        }

    }

    /**
     * 离线惩罚结束游戏
     *
     * @param gameId       游戏ID
     * @param lixianPlayer 离线玩家
     * @param chengfaScore 惩罚分数
     */
    public void automaticFinish(String gameId, String lixianPlayer, double chengfaScore, List<MajiangGamePlayerDbo> players) {
        MajiangGameValueObject majiangGameValueObject = null;
        try {
            majiangGameValueObject = gameCmdService.automaticFinish(gameId);
            //加入惩罚分
            HuaibinMajiangJuResult juResult = (HuaibinMajiangJuResult) majiangGameValueObject.getJuResult();
            List<HuaibinMajiangJuPlayerResult> playerResultList = juResult.getPlayerResultList();
            switch (playerResultList.size()) {
                case 0://第一局玩家没有juResult
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
        // 记录战绩

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
//     * 玩家是否在线
//     *
//     * @param playerId 玩家ID
//     * @param gameId   游戏ID
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
     * 玩家是否在线   Socket
     *
     * @param playerId 玩家ID
     */
    public boolean isPlayerOnLine(String playerId) {
        return wsNotifier.hasSessionForPlayer(playerId);
    }

    /**
     * 下跑
     *
     * @param playerId 玩家ID
     * @param piaofen  炮分
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
            // 通知其他人
            for (String otherPlayerId : xiapiaoResult.getMajiangGame().allPlayerIds()) {
                if (!otherPlayerId.equals(playerId)) {
                    wsNotifier.notifyToQuery(otherPlayerId, QueryScope.scopesForState(xiapiaoResult.getMajiangGame().getState().name(), xiapiaoResult.getMajiangGame().findPlayerState(otherPlayerId).name()));
                }
            }
        }

    }

    /**
     * 下跑
     *
     * @param playerId 玩家ID
     * @param piaofen  炮分
     * @param gameId   游戏ID
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
            // 通知其他人
            for (String otherPlayerId : xiapiaoResult.getMajiangGame().allPlayerIds()) {
                if (!otherPlayerId.equals(playerId)) {
                    wsNotifier.notifyToQuery(otherPlayerId, QueryScope.scopesForState(xiapiaoResult.getMajiangGame().getState().name(), xiapiaoResult.getMajiangGame().findPlayerState(otherPlayerId).name()));
                }
            }
        }

    }

    /**
     * 从托管集合中移除
     *
     * @param playerId 玩家ID
     */
    public void removeTuoguanPlayerIdSet(String playerId) {
        boolean remove = tuoguanPlayerIdSet.remove(playerId);
        if (remove) {
            logger.info("将玩家移除托管集合," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId);
        }
    }

}
