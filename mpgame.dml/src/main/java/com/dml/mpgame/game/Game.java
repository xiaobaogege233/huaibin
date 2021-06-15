package com.dml.mpgame.game;

import com.dml.mpgame.game.back.GameBackStrategy;
import com.dml.mpgame.game.join.GameJoinStrategy;
import com.dml.mpgame.game.leave.GameLeaveStrategy;
import com.dml.mpgame.game.player.*;
import com.dml.mpgame.game.ready.GameReadyStrategy;

import java.util.*;

/**
 * 一次游戏
 *
 * @author Neo
 */
public abstract class Game {

    protected String id;
    protected String createPlayerId;
    protected GameState state;
    protected Map<String, GamePlayer> idPlayerMap = new HashMap<>();
    protected Map<String, Long> playerIdLeaveTimeMap = new HashMap<>();//记录玩家离开时间

    /**
     * 玩家主动要求离开
     */
    private GameLeaveStrategy leaveByPlayerStrategyBeforeStart;
    private GameLeaveStrategy leaveByPlayerStrategyAfterStart;

    /**
     * 挂起导致的离开。(挂起是玩家主观导致的，比如手机按黑。但是挂起的主要意图是“暂停一下”)
     */
    private GameLeaveStrategy leaveByHangupStrategyBeforeStart;
    private GameLeaveStrategy leaveByHangupStrategyAfterStart;

    /**
     * 由于离线而导致的离开。(导致离线的都是不可控的客观原因，比如网络断开、手机爆炸)
     */
    private GameLeaveStrategy leaveByOfflineStrategyBeforeStart;
    private GameLeaveStrategy leaveByOfflineStrategyAfterStart;

    private GameBackStrategy backStrategy;
    private GameReadyStrategy readyStrategy;
    private GameJoinStrategy joinStrategy;

    public void create(String id, String createPlayerId) {
        this.id = id;
        this.createPlayerId = createPlayerId;
        newPlayer(createPlayerId);
        state = new WaitingStart();
    }

    public void newPlayer(String playerId) {
        GamePlayer player = new GamePlayer();
        player.setId(playerId);
        player.setState(new PlayerJoined());
        player.setOnlineState(GamePlayerOnlineState.online);
        idPlayerMap.put(playerId, player);
    }

    public int playerCounts() {
        return idPlayerMap.size();
    }

    public <T extends GameValueObject> T join(String playerId) throws Exception {
        if (idPlayerMap.containsKey(playerId)) {
            throw new GamePlayerAlreadyInGameException();
        }
        joinStrategy.join(playerId, this);
        return toValueObject();
    }

    public abstract <T extends GameValueObject> T toValueObject();

    public <T extends GameValueObject> T leaveByPlayer(String playerId) throws Exception {
        if (state.name().equals(WaitingStart.name)) {
            leaveByPlayerStrategyBeforeStart.leave(playerId, this);
        } else {
            leaveByPlayerStrategyAfterStart.leave(playerId, this);
        }
        return toValueObject();
    }

    public GameLeaveStrategy getLeaveByHangupStrategyBeforeStart() {
        return leaveByHangupStrategyBeforeStart;
    }

    public void setLeaveByHangupStrategyBeforeStart(GameLeaveStrategy leaveByHangupStrategyBeforeStart) {
        this.leaveByHangupStrategyBeforeStart = leaveByHangupStrategyBeforeStart;
    }

    public GameLeaveStrategy getLeaveByHangupStrategyAfterStart() {
        return leaveByHangupStrategyAfterStart;
    }

    public void setLeaveByHangupStrategyAfterStart(GameLeaveStrategy leaveByHangupStrategyAfterStart) {
        this.leaveByHangupStrategyAfterStart = leaveByHangupStrategyAfterStart;
    }

    public <T extends GameValueObject> T leaveByOffline(String playerId) throws Exception {
        if (state.name().equals(WaitingStart.name)) {
            leaveByOfflineStrategyBeforeStart.leave(playerId, this);
        } else {
            leaveByOfflineStrategyAfterStart.leave(playerId, this);
        }
        return toValueObject();
    }

    public <T extends GameValueObject> T leaveByHangup(String playerId) throws Exception {
        if (state.name().equals(WaitingStart.name)) {
            leaveByHangupStrategyBeforeStart.leave(playerId, this);
        } else {
            leaveByHangupStrategyAfterStart.leave(playerId, this);
        }
        return toValueObject();
    }

    public GameBackStrategy getBackStrategy() {
        return backStrategy;
    }

    public void setBackStrategy(GameBackStrategy backStrategy) {
        this.backStrategy = backStrategy;
    }

    public <T extends GameValueObject> T quit(String playerId) throws Exception {
        if (!idPlayerMap.containsKey(playerId)) {
            throw new GamePlayerNotFoundException();
        }
        idPlayerMap.remove(playerId);
        return toValueObject();
    }

    public <T extends GameValueObject> T back(String playerId) throws Exception {
        backStrategy.back(playerId, this);
        return toValueObject();
    }

    public <T extends GameValueObject> T ready(String playerId, long currentTime) throws Exception {
        readyStrategy.ready(playerId, this, currentTime);
        return toValueObject();
    }

    public <T extends GameValueObject> T cancelReady(String playerId, long currentTime) throws Exception {
        readyStrategy.cancelReady(playerId, this, currentTime);
        return toValueObject();
    }

    public void cancel() throws Exception {
        if (state.name().equals(WaitingStart.name)) {
            state = new Canceled();
        } else {
            throw new IllegalOperationException();
        }
    }

    public void updatePlayerState(String playerId, GamePlayerState playerState) throws GamePlayerNotFoundException {
        GamePlayer player = idPlayerMap.get(playerId);
        if (player == null) {
            throw new GamePlayerNotFoundException();
        }
        player.setState(playerState);
    }

    public void updatePlayerOnlineState(String playerId, GamePlayerOnlineState onlineState)
            throws GamePlayerNotFoundException {
        GamePlayer player = idPlayerMap.get(playerId);
        if (player == null) {
            throw new GamePlayerNotFoundException();
        }
        player.setOnlineState(onlineState);
    }

    public void removePlayer(String playerId) throws GamePlayerNotFoundException {
        if (!idPlayerMap.containsKey(playerId)) {
            throw new GamePlayerNotFoundException();
        }
        idPlayerMap.remove(playerId);
    }

    public GamePlayerState playerState(String playerId) {
        GamePlayer player = idPlayerMap.get(playerId);
        if (player != null) {
            return player.getState();
        } else {
            return null;
        }
    }

    public boolean allPlayersReady() {
        for (GamePlayer player : idPlayerMap.values()) {
            if (!player.getState().name().equals(PlayerReadyToStart.name)) {
                return false;
            }
        }
        return true;
    }

    public abstract void start(long currentTime) throws Exception;

    public abstract void finish() throws Exception;

    public void updateAllPlayersState(GamePlayerState playerState) {
        for (GamePlayer player : idPlayerMap.values()) {
            player.setState(playerState);
        }
    }

    public List<String> allPlayerIds() {
        return new ArrayList<>(idPlayerMap.keySet());
    }

    public GamePlayer findPlayer(String playerId) {
        return idPlayerMap.get(playerId);
    }

    public Set<String> findOnlinePlayerIds() {
        Set<String> onlinePlayerIds = new HashSet<>();
        for (GamePlayer player : idPlayerMap.values()) {
            if (player.getOnlineState().equals(GamePlayerOnlineState.online)) {
                onlinePlayerIds.add(player.getId());
            }
        }
        return onlinePlayerIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatePlayerId() {
        return createPlayerId;
    }

    public void setCreatePlayerId(String createPlayerId) {
        this.createPlayerId = createPlayerId;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Map<String, GamePlayer> getIdPlayerMap() {
        return idPlayerMap;
    }

    public void setIdPlayerMap(Map<String, GamePlayer> idPlayerMap) {
        this.idPlayerMap = idPlayerMap;
    }

    public GameLeaveStrategy getLeaveByPlayerStrategyBeforeStart() {
        return leaveByPlayerStrategyBeforeStart;
    }

    public void setLeaveByPlayerStrategyBeforeStart(GameLeaveStrategy leaveByPlayerStrategyBeforeStart) {
        this.leaveByPlayerStrategyBeforeStart = leaveByPlayerStrategyBeforeStart;
    }

    public GameLeaveStrategy getLeaveByPlayerStrategyAfterStart() {
        return leaveByPlayerStrategyAfterStart;
    }

    public void setLeaveByPlayerStrategyAfterStart(GameLeaveStrategy leaveByPlayerStrategyAfterStart) {
        this.leaveByPlayerStrategyAfterStart = leaveByPlayerStrategyAfterStart;
    }

    public GameLeaveStrategy getLeaveByOfflineStrategyBeforeStart() {
        return leaveByOfflineStrategyBeforeStart;
    }

    public void setLeaveByOfflineStrategyBeforeStart(GameLeaveStrategy leaveByOfflineStrategyBeforeStart) {
        this.leaveByOfflineStrategyBeforeStart = leaveByOfflineStrategyBeforeStart;
    }

    public GameLeaveStrategy getLeaveByOfflineStrategyAfterStart() {
        return leaveByOfflineStrategyAfterStart;
    }

    public void setLeaveByOfflineStrategyAfterStart(GameLeaveStrategy leaveByOfflineStrategyAfterStart) {
        this.leaveByOfflineStrategyAfterStart = leaveByOfflineStrategyAfterStart;
    }

    public GameReadyStrategy getReadyStrategy() {
        return readyStrategy;
    }

    public void setReadyStrategy(GameReadyStrategy readyStrategy) {
        this.readyStrategy = readyStrategy;
    }

    public GameJoinStrategy getJoinStrategy() {
        return joinStrategy;
    }

    public void setJoinStrategy(GameJoinStrategy joinStrategy) {
        this.joinStrategy = joinStrategy;
    }

    public Map<String, Long> getPlayerIdLeaveTimeMap() {
        return playerIdLeaveTimeMap;
    }

    public void setPlayerIdLeaveTimeMap(Map<String, Long> playerIdLeaveTimeMap) {
        this.playerIdLeaveTimeMap = playerIdLeaveTimeMap;
    }
}
