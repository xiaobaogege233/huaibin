package com.anbang.qipai.huaibinmajiang.web.controller;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.Automatic;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.OptionalPlay;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.MajiangPlayerXiapiaoState;
import com.anbang.qipai.huaibinmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.huaibinmajiang.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.*;
import com.anbang.qipai.huaibinmajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.huaibinmajiang.cqrs.q.service.MajiangPlayQueryService;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.MajiangHistoricalJuResult;
import com.anbang.qipai.huaibinmajiang.msg.service.*;
import com.anbang.qipai.huaibinmajiang.plan.bean.MemberGoldBalance;
import com.anbang.qipai.huaibinmajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.huaibinmajiang.plan.service.MemberGoldBalanceService;
import com.anbang.qipai.huaibinmajiang.plan.service.PlayerInfoService;
import com.anbang.qipai.huaibinmajiang.utils.CommonVoUtil;
import com.anbang.qipai.huaibinmajiang.utils.SpringUtil;
import com.anbang.qipai.huaibinmajiang.web.vo.*;
import com.anbang.qipai.huaibinmajiang.websocket.GamePlayWsNotifier;
import com.anbang.qipai.huaibinmajiang.websocket.QueryScope;
import com.anbang.qipai.huaibinmajiang.websocket.WatchQueryScope;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.mpgame.game.*;
import com.dml.mpgame.game.extend.fpmpv.VoteNotPassWhenWaitingNextPan;
import com.dml.mpgame.game.extend.vote.FinishedByVote;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.dml.mpgame.game.player.PlayerReadyToStart;
import com.dml.mpgame.game.watch.WatchRecord;
import com.dml.mpgame.game.watch.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * ??????????????????
 *
 * @author neo
 */
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameCmdService gameCmdService;

    @Autowired
    private MajiangGameQueryService majiangGameQueryService;

    @Autowired
    private PlayerAuthService playerAuthService;

    @Autowired
    private MajiangPlayQueryService majiangPlayQueryService;

    @Autowired
    private GamePlayWsNotifier wsNotifier;

    @Autowired
    private HuaibinMajiangGameMsgService gameMsgService;

    @Autowired
    private HuaibinMajiangResultMsgService huaibinMajiangResultMsgService;

    @Autowired
    private MemberGoldBalanceService memberGoldBalanceService;

    @Autowired
    private MemberGoldsMsgService memberGoldsMsgService;

    @Autowired
    private PlayerInfoService playerInfoService;

    @Autowired
    private WiseCrackMsgServcie wiseCrackMsgServcie;

    @Autowired
    private WatchRecordMsgService watchRecordMsgService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final Automatic automatic = SpringUtil.getBean(Automatic.class);

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * ???????????????
     */
    @RequestMapping(value = "/newgame")
    @ResponseBody
    public CommonVO newgame(@RequestParam(required = false) String lianmengId, String playerId, int panshu, int renshu, double difen, OptionalPlay optionalPlay,
                            @RequestParam(required = false) int powerLimit) {
        CommonVO vo = new CommonVO();
        String newGameId = UUID.randomUUID().toString();
        MajiangGameValueObject majiangGameValueObject = gameCmdService.newMajiangGame(newGameId, lianmengId, playerId, panshu, renshu, difen, powerLimit, optionalPlay);
        majiangGameQueryService.newMajiangGame(majiangGameValueObject);
        notReadyQuit(playerId,  majiangGameValueObject);
        String token = playerAuthService.newSessionForPlayer(playerId);
        Map data = new HashMap();
        data.put("gameId", newGameId);
        data.put("token", token);
        gameMsgService.newSessionForPlayer(playerId, token, newGameId);
        vo.setData(data);
        return vo;
    }


    /**
     * ????????????
     */
    @RequestMapping(value = "/joingame")
    @ResponseBody
    public CommonVO joingame(String playerId, String gameId) {
        CommonVO vo = new CommonVO();
        MajiangGameValueObject majiangGameValueObject;
        try {
            majiangGameValueObject = gameCmdService.joinGame(playerId, gameId);
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMsg(e.getClass().toString());
            return vo;
        }
        majiangGameQueryService.joinGame(majiangGameValueObject);
        notReadyQuit(playerId,  majiangGameValueObject);

        // ???????????????
        for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
            if (!otherPlayerId.equals(playerId)) {
                wsNotifier.notifyToQuery(otherPlayerId,
                        QueryScope.scopesForState(majiangGameValueObject.getState().name(), majiangGameValueObject.findPlayerState(otherPlayerId).name()));

            }
        }

        String token = playerAuthService.newSessionForPlayer(playerId);
        Map data = new HashMap();
        data.put("token", token);
        gameMsgService.newSessionForPlayer(playerId, token, gameId);
        vo.setData(data);
        return vo;
    }

    /**
     * ????????????
     */
    @RequestMapping(value = "/joinwatch")
    @ResponseBody
    public CommonVO joinWatch(String playerId, String gameId) {
        MajiangGameValueObject majiangGameValueObject;
        String nickName = "";
        String headimgurl = "";

        // ????????????
        try {
            PlayerInfo playerInfo = playerInfoService.findPlayerInfoById(playerId);
            nickName = playerInfo.getNickname();
            headimgurl = playerInfo.getHeadimgurl();
            majiangGameValueObject = gameCmdService.joinWatch(playerId, nickName, headimgurl, gameId);
        } catch (CrowdLimitsException e) {
            return CommonVoUtil.error("too many watchers");
        } catch (Exception e) {
            return CommonVoUtil.error(e.getClass().toString());
        }

        // ??????????????????
        for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
            wsNotifier.notifyWatchInfo(otherPlayerId, "input", playerId, nickName, headimgurl);
        }
        // ?????????????????????
        Map<String, Watcher> map = gameCmdService.getwatch(gameId);
        if (!CollectionUtils.isEmpty(map)) {
            for (Watcher list : map.values()) {
                if (!list.getId().equals(playerId)) {
                    wsNotifier.notifyWatchInfo(list.getId(), "input", playerId, nickName, headimgurl);
                }
            }
        }

        // ????????????token
        String token = playerAuthService.newSessionForPlayer(playerId);

        Watcher watcher = new Watcher();
        watcher.setId(playerId);
        watcher.setHeadimgurl(headimgurl);
        watcher.setNickName(nickName);
        watcher.setState("join");
        watcher.setJoinTime(System.currentTimeMillis());
        WatchRecord watchRecord = majiangGameQueryService.saveWatchRecord(gameId, watcher);
        watchRecordMsgService.joinWatch(watchRecord);

        Map data = new HashMap();
        data.put("token", token);
        gameMsgService.newSessionForPlayer(playerId, token, gameId);
        return CommonVoUtil.success(data, "join watch success");
    }

    /**
     * ????????????
     */
    @RequestMapping(value = "/leavewatch")
    @ResponseBody
    public CommonVO leaveWatch(String token, String gameId) {
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            return CommonVoUtil.error("invalid token");
        }
        MajiangGameValueObject majiangGameValueObject;
        String nickName = "";
        String headimgurl = "";

        try {
            nickName = playerInfoService.findPlayerInfoById(playerId).getNickname();
            majiangGameValueObject = gameCmdService.leaveWatch(playerId, gameId);
        } catch (Exception e) {
            return CommonVoUtil.error(e.getClass().toString());
        }

        // ??????????????????
        for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
            wsNotifier.notifyWatchInfo(otherPlayerId, "leave", playerId, nickName, headimgurl);
        }
        // ???????????????
        Map<String, Watcher> map = gameCmdService.getwatch(gameId);
        if (!CollectionUtils.isEmpty(map)) {
            for (Watcher list : map.values()) {
                if (!list.getId().equals(playerId)) {
                    wsNotifier.notifyWatchInfo(list.getId(), "input", playerId, nickName, headimgurl);
                }
            }
        }

        Watcher watcher = new Watcher();
        watcher.setId(playerId);
        watcher.setHeadimgurl(headimgurl);
        watcher.setNickName(nickName);
        watcher.setState("leave");
        WatchRecord watchRecord = majiangGameQueryService.saveWatchRecord(gameId, watcher);
        watchRecordMsgService.leaveWatch(watchRecord);

        return CommonVoUtil.success("leave success");
    }

    /**
     * ???????????????????????????
     */
    @RequestMapping(value = "/queryWatch")
    @ResponseBody
    public CommonVO queryWatch(String gameId) {
        Map<String, Watcher> map = gameCmdService.getwatch(gameId);
        if (CollectionUtils.isEmpty(map)) {
            return CommonVoUtil.success("queryWatch success");
        }
        return CommonVoUtil.success(map.values(), "queryWatch success");
    }

    /**
     * ????????????????????????
     */
    @RequestMapping(value = "/watchinginfo")
    @ResponseBody
    public CommonVO watchingInfo(String gameId) {
        CommonVO vo = new CommonVO();
        MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
        MajiangGamePlayerXiapiaoDbo majiangGamePlayerXiapiaoDbo = majiangPlayQueryService.findLastPlayerXiapiaoDboByGameId(gameId);
        GameVO gameVO;
        if (majiangGamePlayerXiapiaoDbo != null) {
            gameVO = new GameVO(majiangGameDbo, majiangGamePlayerXiapiaoDbo);
        } else {
            gameVO = new GameVO(majiangGameDbo);
        }
        Map data = new HashMap();
        data.put("game", gameVO);
        vo.setData(data);
        return vo;
    }

    /**
     * ???????????????????????????????????????
     */
    @RequestMapping(value = "/hangup")
    @ResponseBody
    public CommonVO hangup(String token) {
        CommonVO vo = new CommonVO();
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }

        String gameIdByPlayerId = gameCmdService.getGameIdByPlayerId(playerId);
        if (gameIdByPlayerId != null) { //????????????
            automatic.offlineHosting(gameIdByPlayerId, playerId);
        }

        MajiangGameValueObject majiangGameValueObject;
        String endFlag = "query";
        try {
            majiangGameValueObject = gameCmdService.leaveGameByHangup(playerId);
            if (majiangGameValueObject == null) {
                vo.setSuccess(true);
                return vo;
            }
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMsg(e.getClass().getName());
            return vo;
        }
        majiangGameQueryService.leaveGame(majiangGameValueObject);
        if (majiangGameValueObject.getState().name().equals(WaitingStart.name)) {
            notReadyQuit(playerId, majiangGameValueObject);
        }
        // ???????????????socket
        wsNotifier.closeSessionForPlayer(playerId);
        String gameId = majiangGameValueObject.getId();
        JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
        // ????????????
        if (juResultDbo != null) {
            MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
            MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
            huaibinMajiangResultMsgService.recordJuResult(juResult);
        }
        if (majiangGameValueObject.getState().name().equals(FinishedByVote.name)
                || majiangGameValueObject.getState().name().equals(Canceled.name)) {
            gameMsgService.gameFinished(gameId);
            endFlag = WatchQueryScope.watchEnd.name();
        } else {
            gameMsgService.gamePlayerLeave(majiangGameValueObject, playerId);

        }
        // ??????????????????
        for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
            if (!otherPlayerId.equals(playerId)) {
                List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState().name(), majiangGameValueObject.findPlayerState(otherPlayerId).name());
                scopes.remove(QueryScope.panResult);
                if (majiangGameValueObject.getState().name().equals(VoteNotPassWhenPlaying.name)
                        || majiangGameValueObject.getState().name().equals(VoteNotPassWhenWaitingNextPan.name)) {
                    scopes.remove(QueryScope.gameFinishVote);
                }
                wsNotifier.notifyToQuery(otherPlayerId, scopes);
            }
        }

        hintWatcher(gameId, endFlag);

        return vo;
    }

    /**
     * ????????????(?????????,???????????????)
     */
    @RequestMapping(value = "/leavegame")
    @ResponseBody
    public CommonVO leavegame(String token) {
        CommonVO vo = new CommonVO();
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }

        String gameIdByPlayerId = gameCmdService.getGameIdByPlayerId(playerId);
        if (gameIdByPlayerId != null) { //????????????
            automatic.offlineHosting(gameIdByPlayerId, playerId);
        }

        MajiangGameValueObject majiangGameValueObject;
        try {
            majiangGameValueObject = gameCmdService.leaveGame(playerId);
            if (majiangGameValueObject == null) {
                vo.setSuccess(true);
                return vo;
            }
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMsg(e.getClass().getName());
            return vo;
        }
        majiangGameQueryService.leaveGame(majiangGameValueObject);
        if (majiangGameValueObject.getState().name().equals(WaitingStart.name)) {
            notReadyQuit(playerId, majiangGameValueObject);
        }
        // ???????????????socket
        wsNotifier.closeSessionForPlayer(playerId);
        String gameId = majiangGameValueObject.getId();
        JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
        // ????????????
        if (juResultDbo != null) {
            MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
            MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
            huaibinMajiangResultMsgService.recordJuResult(juResult);
        }
        if (majiangGameValueObject.getState().name().equals(FinishedByVote.name)
                || majiangGameValueObject.getState().name().equals(Canceled.name)) {
            gameMsgService.gameFinished(gameId);
        } else if (majiangGameValueObject.getState().name().equals(Finished.name)) {
            gameMsgService.gameCanceled(gameId, playerId);
        } else {
            gameMsgService.gamePlayerLeave(majiangGameValueObject, playerId);

        }
        // ??????????????????
        for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
            if (!otherPlayerId.equals(playerId)) {
                List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState().name(), majiangGameValueObject.findPlayerState(otherPlayerId).name());
                if (!majiangGameValueObject.getState().name().equals(Finished.name)) {
                    scopes.remove(QueryScope.panResult);
                }
                if (majiangGameValueObject.getState().name().equals(VoteNotPassWhenPlaying.name)
                        || majiangGameValueObject.getState().name().equals(VoteNotPassWhenWaitingNextPan.name)) {
                    scopes.remove(QueryScope.gameFinishVote);
                }
                wsNotifier.notifyToQuery(otherPlayerId, scopes);
            }
        }

        return vo;
    }

    /**
     * ????????????
     */
    @RequestMapping(value = "/backtogame")
    @ResponseBody
    public CommonVO backtogame(String playerId, String gameId) {
        // ??????????????????token
        Map<String, Watcher> map = gameCmdService.getwatch(gameId);
        if (!CollectionUtils.isEmpty(map) && map.containsKey(playerId)) {
            List<String> playerIds = new ArrayList<>();
            playerIds.add(playerId);
            wsNotifier.notifyToWatchQuery(playerIds, "query");

            Map data = new HashMap();
            String token = playerAuthService.newSessionForPlayer(playerId);
            data.put("token", token);
            return CommonVoUtil.success(data, "backtogame success");
        }

        CommonVO vo = new CommonVO();
        Map data = new HashMap();
        vo.setData(data);
        MajiangGameValueObject majiangGameValueObject;
        MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
        try {
            majiangGameValueObject = gameCmdService.backToGame(playerId, gameId);
        } catch (Exception e) {
            // ???????????????game??????????????????????????????(????????????????????????)???game
            if (e instanceof GameNotFoundException) {
                if (majiangGameDbo != null && (majiangGameDbo.getState().equals(FinishedByVote.name) || majiangGameDbo.getState().equals(Finished.name))) {
                    data.put("queryScope", QueryScope.juResult);
                    return vo;
                }
            }
            vo.setSuccess(false);
            vo.setMsg(e.getClass().toString());
            return vo;
        }

        majiangGameQueryService.backToGame(playerId, majiangGameValueObject);
        // ???????????????
        for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
            if (!otherPlayerId.equals(playerId)) {
                List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState().name(), majiangGameValueObject.findPlayerState(otherPlayerId).name());
                scopes.remove(QueryScope.panResult);
                if (majiangGameValueObject.getState().name().equals(VoteNotPassWhenPlaying.name)
                        || majiangGameValueObject.getState().name().equals(VoteNotPassWhenWaitingNextPan.name)) {
                    scopes.remove(QueryScope.gameFinishVote);
                }
                wsNotifier.notifyToQuery(otherPlayerId, scopes);
            }
        }
        String token = playerAuthService.newSessionForPlayer(playerId);
        data.put("token", token);
        gameMsgService.newSessionForPlayer(playerId, token, gameId);

        gameCmdService.playLeaveGameHosting(playerId, gameId, false);//????????????????????????

        return vo;
    }


    @RequestMapping(value = "/quit")
    @ResponseBody
    public CommonVO quit(String token) {
        CommonVO vo = new CommonVO();
        Map data = new HashMap();
        vo.setData(data);
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }

        MajiangGameValueObject majiangGameValueObject;
        String endFlag = "query";
        try {
            majiangGameValueObject = gameCmdService.quit(playerId, System.currentTimeMillis(), null);
            if (!majiangGameValueObject.getState().name().equals(WaitingStart.name)) {
                vo.setSuccess(false);
                vo.setMsg("gameStart");
                return vo;
            }
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMsg(e.getClass().getName());
            return vo;
        }
        majiangGameQueryService.quit(majiangGameValueObject);
        String gameId = majiangGameValueObject.getId();
        data.put("queryScope", null);
        gameMsgService.gamePlayerLeave(majiangGameValueObject, playerId);

        // ????????????????????????
        for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
            if (!otherPlayerId.equals(playerId)) {
                GamePlayerOnlineState onlineState = majiangGameValueObject.findPlayerOnlineState(otherPlayerId);
                if (onlineState.equals(GamePlayerOnlineState.online)) {
                    List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState().name(),
                            majiangGameValueObject.findPlayerState(otherPlayerId).name());
                    scopes.remove(QueryScope.panResult);
                    wsNotifier.notifyToQuery(otherPlayerId, scopes);
                }
            }
        }
        // ???????????????????????????
        hintWatcher(gameId, endFlag);
        return vo;
    }

    /**
     * ?????????????????????,????????????
     *
     * @param gameId
     * @return
     */
    @RequestMapping(value = "/info")
    @ResponseBody
    public CommonVO info(String gameId) {
        CommonVO vo = new CommonVO();
        MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
        MajiangGamePlayerXiapiaoDbo majiangGamePlayerXiapiaoDbo = majiangPlayQueryService.findLastPlayerXiapiaoDboByGameId(gameId);
        GameVO gameVO;
        if (majiangGamePlayerXiapiaoDbo != null) {
            gameVO = new GameVO(majiangGameDbo, majiangGamePlayerXiapiaoDbo);
        } else {
            gameVO = new GameVO(majiangGameDbo);
        }

        try {
            tuoguan(gameId, gameVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map data = new HashMap();
        data.put("game", gameVO);
        vo.setData(data);
        return vo;
    }

    /**
     * ??????????????????,???????????????????????????
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/ready")
    @ResponseBody
    public CommonVO ready(String token) {
        CommonVO vo = new CommonVO();
        Map data = new HashMap();
        vo.setData(data);
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }

        ReadyForGameResult readyForGameResult;
        try {
            readyForGameResult = gameCmdService.readyForGame(playerId, System.currentTimeMillis());
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMsg(e.getClass().getName());
            return vo;
        }

        majiangPlayQueryService.readyForGame(readyForGameResult);// TODO ?????????????????????????????????????????????.??????????????????
        // ???????????????
        for (String otherPlayerId : readyForGameResult.getMajiangGame().allPlayerIds()) {
            if (!otherPlayerId.equals(playerId)) {
                wsNotifier.notifyToQuery(otherPlayerId,
                        QueryScope.scopesForState(readyForGameResult.getMajiangGame().getState().name(), readyForGameResult.getMajiangGame().findPlayerState(otherPlayerId).name()));

            }
        }

        List<QueryScope> queryScopes = new ArrayList<>();
        queryScopes.add(QueryScope.gameInfo);
        if (readyForGameResult.getMajiangGame().getState().name().equals(Playing.name)) {
            queryScopes.add(QueryScope.panForMe);
            gameMsgService.start(readyForGameResult.getMajiangGame().getId());
        }
        data.put("queryScopes", queryScopes);
        return vo;
    }

    /**
     * ????????????????????????,???????????????????????????
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/cancelready")
    @ResponseBody
    public CommonVO cancelReady(String token) {
        CommonVO vo = new CommonVO();
        Map data = new HashMap();
        vo.setData(data);
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }

        ReadyForGameResult readyForGameResult;
        try {
            readyForGameResult = gameCmdService.cancelReadyForGame(playerId, System.currentTimeMillis());
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMsg(e.getClass().getName());
            return vo;
        }

        majiangPlayQueryService.readyForGame(readyForGameResult);// TODO ?????????????????????????????????????????????.??????????????????
        // ???????????????
        MajiangGameValueObject majiangGameValueObject = readyForGameResult.getMajiangGame();
        notReadyQuit(playerId,  majiangGameValueObject);
        for (String otherPlayerId : readyForGameResult.getMajiangGame().allPlayerIds()) {
            if (!otherPlayerId.equals(playerId)) {
                wsNotifier.notifyToQuery(otherPlayerId,
                        QueryScope.scopesForState(readyForGameResult.getMajiangGame().getState().name(), readyForGameResult.getMajiangGame().findPlayerState(otherPlayerId).name()));

            }
        }

        List<QueryScope> queryScopes = new ArrayList<>();
        queryScopes.add(QueryScope.gameInfo);
        if (readyForGameResult.getMajiangGame().getState().name().equals(Playing.name)) {
            queryScopes.add(QueryScope.panForMe);
        }
        data.put("queryScopes", queryScopes);
        return vo;
    }

    @RequestMapping(value = "/finish")
    @ResponseBody
    public CommonVO finish(String token) {
        CommonVO vo = new CommonVO();
        Map data = new HashMap();
        vo.setData(data);
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }

        MajiangGameValueObject majiangGameValueObject;
        String endFlag = "query";
        try {
            majiangGameValueObject = gameCmdService.finish(playerId, System.currentTimeMillis());
            if (majiangGameValueObject.getState().name().equals(WaitingStart.name)) {
                vo.setSuccess(false);
                vo.setMsg("waitStart");
                return vo;
            } else if (majiangGameValueObject.getOptionalPlay().isBanJiesan()) {
                vo.setSuccess(false);
                vo.setMsg("banJiesan");
                return vo;
            }
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMsg(e.getClass().getName());
            return vo;
        }
        majiangGameQueryService.finish(majiangGameValueObject);
        String gameId = majiangGameValueObject.getId();
        JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
        // ????????????
        if (juResultDbo != null) {
            MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
            MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
            huaibinMajiangResultMsgService.recordJuResult(juResult);
        }
        if (majiangGameValueObject.getState().name().equals(FinishedByVote.name)
                || majiangGameValueObject.getState().name().equals(Canceled.name)) {
            data.put("queryScope", QueryScope.gameInfo);
            gameMsgService.gameFinished(gameId);
            endFlag = WatchQueryScope.watchEnd.name();
        } else {
            // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (majiangGameValueObject.allPlayerIds().contains(playerId)) {
                data.put("queryScope", QueryScope.gameFinishVote);
            } else {
                data.put("queryScope", null);
                gameMsgService.gamePlayerLeave(majiangGameValueObject, playerId);
            }
        }

        // ????????????????????????
        for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
            if (!otherPlayerId.equals(playerId)) {
                GamePlayerOnlineState onlineState = majiangGameValueObject.findPlayerOnlineState(otherPlayerId);
                if (onlineState.equals(GamePlayerOnlineState.online)) {
                    List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState().name(),
                            majiangGameValueObject.findPlayerState(otherPlayerId).name());
                    scopes.remove(QueryScope.panResult);
                    wsNotifier.notifyToQuery(otherPlayerId, scopes);
                }
            }
        }

        hintWatcher(gameId, endFlag);
        return vo;
    }

    @RequestMapping(value = "/vote_to_finish")
    @ResponseBody
    public CommonVO votetofinish(String token, boolean yes) {
        CommonVO vo = new CommonVO();
        Map data = new HashMap();
        vo.setData(data);
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }

        MajiangGameValueObject majiangGameValueObject;
        String endFlag = "query";
        try {
            majiangGameValueObject = gameCmdService.voteToFinish(playerId, yes);
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMsg(e.getClass().getName());
            return vo;
        }
        majiangGameQueryService.voteToFinish(majiangGameValueObject);
        String gameId = majiangGameValueObject.getId();
        JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
        // ????????????
        if (juResultDbo != null) {
            MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
            MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
            huaibinMajiangResultMsgService.recordJuResult(juResult);
        }
        if (majiangGameValueObject.getState().name().equals(FinishedByVote.name)
                || majiangGameValueObject.getState().name().equals(Canceled.name)) {
            gameMsgService.gameFinished(gameId);
            endFlag = WatchQueryScope.watchEnd.name();
        }

        data.put("queryScope", QueryScope.gameFinishVote);
        // ????????????????????????????????????
        for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
            if (!otherPlayerId.equals(playerId)) {
                GamePlayerOnlineState onlineState = majiangGameValueObject.findPlayerOnlineState(otherPlayerId);
                if (onlineState.equals(GamePlayerOnlineState.online)) {
                    List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState().name(),
                            majiangGameValueObject.findPlayerState(otherPlayerId).name());
                    scopes.remove(QueryScope.panResult);
                    wsNotifier.notifyToQuery(otherPlayerId, scopes);
                }
            }
        }

        hintWatcher(gameId, endFlag);
        return vo;

    }

    /**
     * ???????????????????????????
     */
    @RequestMapping(value = "/timeover_to_waiver")
    @ResponseBody
    public CommonVO timeoverToWaiver(String token) {
        CommonVO vo = new CommonVO();
        Map data = new HashMap();
        vo.setData(data);
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }

        MajiangGameValueObject majiangGameValueObject;
        String endFlag = "query";
        try {
            majiangGameValueObject = gameCmdService.voteToFinishByTimeOver(playerId, System.currentTimeMillis());
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMsg(e.getClass().getName());
            return vo;
        }
        majiangGameQueryService.voteToFinish(majiangGameValueObject);
        String gameId = majiangGameValueObject.getId();
        JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
        // ????????????
        if (juResultDbo != null) {
            MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
            MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
            huaibinMajiangResultMsgService.recordJuResult(juResult);
        }
        if (majiangGameValueObject.getState().name().equals(FinishedByVote.name)
                || majiangGameValueObject.getState().name().equals(Canceled.name)) {
            gameMsgService.gameFinished(gameId);
            endFlag = WatchQueryScope.watchEnd.name();
        }

        data.put("queryScope", QueryScope.gameFinishVote);
        // ????????????????????????????????????
        for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
            if (!otherPlayerId.equals(playerId)) {
                GamePlayerOnlineState onlineState = majiangGameValueObject.findPlayerOnlineState(otherPlayerId);
                if (onlineState.equals(GamePlayerOnlineState.online)) {
                    List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject.getState().name(),
                            majiangGameValueObject.findPlayerState(otherPlayerId).name());
                    scopes.remove(QueryScope.panResult);
                    wsNotifier.notifyToQuery(otherPlayerId, scopes);
                }
            }
        }

        hintWatcher(gameId, endFlag);
        return vo;

    }

    @RequestMapping(value = "/finish_vote_info")
    @ResponseBody
    public CommonVO finishvoteinfo(String gameId) {
        CommonVO vo = new CommonVO();
        GameFinishVoteDbo gameFinishVoteDbo = majiangGameQueryService.findGameFinishVoteDbo(gameId);
        Map data = new HashMap();
        data.put("vote", new GameFinishVoteVO(gameFinishVoteDbo.getVote()));
        vo.setData(data);
        return vo;
    }

    @RequestMapping(value = "/playback")
    @ResponseBody
    public CommonVO playback(String gameId, int panNo) {
        CommonVO vo = new CommonVO();
        Map data = new HashMap();
        vo.setData(data);
        List<PanActionFrameDbo> frameList = majiangPlayQueryService.findPanActionFrameDboForBackPlay(gameId, panNo);
        List<PanActionFrameVO> frameVOList = new ArrayList<>();
        for (PanActionFrameDbo frame : frameList) {
            frame.getPanActionFrame().getPanAfterAction().getAvaliablePaiList().setPaiList(null);
            frameVOList.add(new PanActionFrameVO(frame.getPanActionFrame()));
        }
        MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboByIdForPlayBack(gameId);
        majiangGameDbo.setPanNo(panNo);
        MajiangGamePlayerXiapiaoDbo majiangGamePlayerXiapiaoDbo = majiangPlayQueryService.findLastPlayerXiapiaoDboByGameId(gameId);
        GameVO gameVO;
        if (majiangGamePlayerXiapiaoDbo != null) {
            gameVO = new GameVO(majiangGameDbo, majiangGamePlayerXiapiaoDbo);
        } else {
            gameVO = new GameVO(majiangGameDbo);
        }
        PanResultDbo panResultDbo = majiangPlayQueryService.findPanResultDboForBackPlay(gameId, panNo);
        data.put("panResult", new PanResultVO(panResultDbo, majiangGameDbo));
        data.put("game", gameVO);
        data.put("framelist", frameVOList);
        return vo;
    }

    @RequestMapping(value = "/wisecrack")
    @ResponseBody
    public CommonVO wisecrack(String token, String gameId, String ordinal) {
        CommonVO vo = new CommonVO();
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }
        MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
        if (!ordinal.contains("qiaopihuafy")) {
            // ???????????????
            for (MajiangGamePlayerDbo otherPlayer : majiangGameDbo.getPlayers()) {
                if (!otherPlayer.getPlayerId().equals(playerId)) {
                    wsNotifier.notifyToListenWisecrack(otherPlayer.getPlayerId(), ordinal, playerId);
                }
            }
            wiseCrackMsgServcie.wisecrack(playerId);
            vo.setSuccess(true);
            return vo;
        }
        MemberGoldBalance account = memberGoldBalanceService.findByMemberId(playerId);
        if (account.getBalanceAfter() > 10) {
            memberGoldsMsgService.withdraw(playerId, 10, "wisecrack");
            // ???????????????
            for (MajiangGamePlayerDbo otherPlayer : majiangGameDbo.getPlayers()) {
                if (!otherPlayer.getPlayerId().equals(playerId)) {
                    wsNotifier.notifyToListenWisecrack(otherPlayer.getPlayerId(), ordinal, playerId);
                }
            }
            wiseCrackMsgServcie.wisecrack(playerId);
            vo.setSuccess(true);
            return vo;
        }
        vo.setSuccess(false);
        vo.setMsg("InsufficientBalanceException");
        return vo;
    }

    @RequestMapping(value = "/speak")
    @ResponseBody
    public CommonVO speak(String token, String gameId, String wordId) {
        CommonVO vo = new CommonVO();
        String playerId = playerAuthService.getPlayerIdByToken(token);
        if (playerId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }
        MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
        List<MajiangGamePlayerDbo> playerList = majiangGameDbo.getPlayers();
        for (MajiangGamePlayerDbo player : playerList) {
            if (!player.getPlayerId().equals(playerId)) {
                wsNotifier.notifyToListenSpeak(player.getPlayerId(), wordId, playerId, true);
            }
        }
        // ?????????????????????
        Map<String, Object> map = gameCmdService.getwatch(gameId);
        if (!CollectionUtils.isEmpty(map)) {
            List<String> playerIds = map.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
            for (String list : playerIds) {
                if (!list.equals(playerId)) {
                    wsNotifier.notifyToListenSpeak(list, wordId, playerId, false);
                }
            }
        }

        vo.setSuccess(true);
        return vo;
    }

    /**
     * ????????????????????????
     */
    @RequestMapping(value = "/quitAllGame")
    public String quitAllGame(String token) {
        if (!"2019".equals(token)) {
            return "blank";
        }
        try {
            Set<String> stringSet = gameCmdService.listGameId();
            if (!CollectionUtils.isEmpty(stringSet)) {
                for (String gameId : stringSet) {
                    MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
                    if (majiangGameDbo == null) {
                        continue;
                    }
                    // ???????????????????????????
                    MajiangGameValueObject gameValueObject = gameCmdService.finishGameImmediately(gameId);
                    majiangGameQueryService.finishGameImmediately(gameValueObject, null);
                    gameMsgService.gameFinished(gameId);
                    JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
                    if (juResultDbo != null) {
                        MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
                        huaibinMajiangResultMsgService.recordJuResult(juResult);
                    }
                }
            }
            return "success";
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return "error";
    }

    /**
     * ???????????????
     */
    private void hintWatcher(String gameId, String flag) {
        Map<String, Object> map = gameCmdService.getwatch(gameId);
        if (!CollectionUtils.isEmpty(map)) {
            List<String> playerIds = map.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
            wsNotifier.notifyToWatchQuery(playerIds, flag);
            if (WatchQueryScope.watchEnd.name().equals(flag)) {
                gameCmdService.recycleWatch(gameId);
            }
        }
    }

    /**
     * ???????????????????????????
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void removeGameData() {
        long endTime = System.currentTimeMillis() - 5L * 24 * 60 * 60 * 1000;
        majiangGameQueryService.removeGameData(endTime);
    }

    /**
     * ??????????????????
     */
    @RequestMapping(value = "/quitGame")
    public CommonVO quitGame(String gameId) {
        CommonVO vo = new CommonVO();
        try {
            MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
            if (majiangGameDbo == null) {
                vo.setSuccess(false);
                vo.setMsg("invalid gameId");
                return vo;
            }
            // ???????????????????????????
            MajiangGameValueObject gameValueObject = gameCmdService.finishGameImmediately(gameId);
            majiangGameQueryService.finishGameImmediately(gameValueObject, null);
            gameMsgService.gameFinished(gameId);
            JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
            if (juResultDbo != null) {
                MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
                huaibinMajiangResultMsgService.recordJuResult(juResult);
            }
            for (String otherPlayerId : gameValueObject.allPlayerIds()) {
                wsNotifier.notifyToQuery(otherPlayerId,
                        QueryScope.scopesForState(gameValueObject.getState().name(), gameValueObject.findPlayerState(otherPlayerId).name()));

            }
            vo.setSuccess(true);
            vo.setMsg("quit game success");
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        vo.setSuccess(false);
        vo.setMsg("SysException");
        return vo;
    }

    @RequestMapping(value = "/deposit")
    @ResponseBody
    public CommonVO deposit(String playerId, String gameId, boolean deposit) throws Exception {
        CommonVO vo = new CommonVO();
        Map data = new HashMap();
        List<String> queryScopes = new ArrayList<>();
        data.put("queryScopes", queryScopes);
        vo.setData(data);

        queryScopes.add(QueryScope.gameInfo.name());
        queryScopes.add(QueryScope.panForMe.name());

        Map<String, String> tuoguanPlayerIds = gameCmdService.playLeaveGameHosting(playerId, null, false);//gameId???null??????????????????????????????
        if (!tuoguanPlayerIds.containsKey(playerId) && deposit) { //??????????????????&&?????????
            MajiangGamePlayerXiapiaoDbo majiangGamePlayerXiapiaoDbo = majiangPlayQueryService.findLastPlayerXiapiaoDboByGameId(gameId);
            if (majiangGamePlayerXiapiaoDbo != null) {
                Map<String, MajiangPlayerXiapiaoState> playerXiapiaoStateMap = majiangGamePlayerXiapiaoDbo.getPlayerXiapiaoStateMap();
                if (playerXiapiaoStateMap.get(playerId) != null) {
                    if (playerXiapiaoStateMap.get(playerId).equals(MajiangPlayerXiapiaoState.waitForxiapiao)) {
                        automatic.automaticXiapiao(playerId, 0);
                        logger.info("??????????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",deposit:" + deposit + ",gameId:" + gameId);
                        return vo;
                    }
                }
            }

            automatic.automaticAction(playerId, 1, gameId);
            PanActionFrame panActionFrame = majiangPlayQueryService.findAndFilterCurrentPanValueObjectForPlayer(gameId);
            if (panActionFrame != null) {
                MajiangPlayerAction action = panActionFrame.getAction();
                if (action instanceof MajiangPengAction ||
                        action instanceof MajiangGangAction ||
                        action instanceof MajiangGuoAction) {
                    automatic.automaticAction(playerId, 1, gameId);   //?????????????????????????????????????????????????????????????????????????????????????????????
                }
            }

        }
        gameCmdService.playLeaveGameHosting(playerId, gameId, deposit);
        logger.info("????????????," + "Time:" + System.currentTimeMillis() + ",playerId:" + playerId + ",deposit:" + deposit + ",gameId:" + gameId);
        return vo;
    }

    /**
     * ??????????????????
     *
     * @param gameId ??????ID
     * @param gameVO ??????????????????
     */
    private void tuoguan(String gameId, GameVO gameVO) {
        Map<String, String> tuoguanPlayerIds = gameCmdService.playLeaveGameHosting(null, gameId, false);
        List<MajiangGamePlayerVO> gameVOPlayerList = gameVO.getPlayerList();
        if (tuoguanPlayerIds != null) {
            Set<String> playerIds = tuoguanPlayerIds.keySet();
            for (MajiangGamePlayerVO majiangGamePlayerVO : gameVOPlayerList) {
                if (playerIds.contains(majiangGamePlayerVO.getPlayerId())) {
                    majiangGamePlayerVO.setDeposit(true);    //??????????????????
                }
            }
        }
        gameVO.getTuoguanPlayerIds().clear();
        for (MajiangGamePlayerVO majiangGamePlayerVO : gameVOPlayerList) {
            if (majiangGamePlayerVO.getPlayerXiapiaoState() != null) {
                if (majiangGamePlayerVO.getPlayerXiapiaoState().equals(MajiangPlayerXiapiaoState.waitForxiapiao)) { //?????????????????????????????????????????????????????????
                    gameVO.getTuoguanPlayerIds().add(majiangGamePlayerVO.getPlayerId());
                }
            }
        }

    }

    private void notReadyQuit(String playerId, MajiangGameValueObject majiangGameValueObject) {
        if (majiangGameValueObject.getOptionalPlay().getBuzhunbeituichushichang() != 0) {
            executorService.submit(()->{
                try {
                    int sleepTime = majiangGameValueObject.getOptionalPlay().getBuzhunbeituichushichang();
                    Thread.sleep((sleepTime + 1) * 1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(majiangGameValueObject.getId());
                for (MajiangGamePlayerDbo player : majiangGameDbo.getPlayers()) {
                    if (player.getPlayerId().equals(playerId)) {
                        if (majiangGameDbo.getState().equals(WaitingStart.name)) {
                            if (!PlayerReadyToStart.name.equals(player.getState())) {
                                MajiangGameValueObject majiangGameValueObject1 = null;
                                try {
                                    majiangGameValueObject1 = gameCmdService.quit(playerId, System.currentTimeMillis(), majiangGameValueObject.getId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                majiangGameQueryService.quit(majiangGameValueObject1);
                                for (String otherPlayerId : majiangGameValueObject1.allPlayerIds()) {
                                    List<QueryScope> scopes = QueryScope.scopesForState(majiangGameValueObject1.getState().name(),
                                            majiangGameValueObject1.findPlayerState(otherPlayerId).name());
                                    wsNotifier.notifyToQuery(otherPlayerId, scopes);
                                }
                                if (majiangGameValueObject1.getPlayers().size() == 0) {
                                    gameMsgService.gameFinished(majiangGameValueObject.getId());
                                }
                                gameMsgService.gamePlayerLeave(majiangGameValueObject1, playerId);
                                wsNotifier.sendMessageToQuit(playerId);
                            }
                        }
                    }
                }
            });

        }
    }

}
