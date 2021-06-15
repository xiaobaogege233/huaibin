package com.dml.mpgame.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.GameNotFoundException;
import com.dml.mpgame.game.GameValueObject;
import com.dml.mpgame.game.player.PlayerNotInGameException;

public class GameServer {

	private String id;
	private Map<String, Game> gameIdGameMap = new HashMap<>();

	/**
	 * 一台服务器对于同一个用户（一个socket）同时只能玩一场游戏。
	 */
	private Map<String, String> playerIdGameIdMap = new HashMap<>();

	public void playerCreateGame(Game newGame, String playerId) {
		gameIdGameMap.put(newGame.getId(), newGame);
		playerIdGameIdMap.put(playerId, newGame.getId());
	}

	public <T extends GameValueObject> T join(String playerId, String gameId) throws Exception {
		Game game = gameIdGameMap.get(gameId);
		if (game == null) {
			throw new GameNotFoundException();
		}
		T valueObject = game.join(playerId);
		playerIdGameIdMap.put(playerId, gameId);
		return valueObject;
	}

	public <T extends GameValueObject> T ready(String playerId, long currentTime) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		Game game = gameIdGameMap.get(gameId);
		return game.ready(playerId, currentTime);
	}

	public <T extends GameValueObject> T cancelReady(String playerId, long currentTime) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		Game game = gameIdGameMap.get(gameId);
		return game.cancelReady(playerId, currentTime);
	}

	public <T extends GameValueObject> T leaveByPlayer(String playerId) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new GameNotFoundException();
		}
		Game game = gameIdGameMap.get(gameId);
		T valueObject = game.leaveByPlayer(playerId);
		playerIdGameIdMap.remove(playerId);
		return valueObject;
	}

	public <T extends GameValueObject> T leaveByOffline(String playerId) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new GameNotFoundException();
		}
		Game game = gameIdGameMap.get(gameId);
		T valueObject = game.leaveByOffline(playerId);
		playerIdGameIdMap.remove(playerId);
		return valueObject;
	}

	public <T extends GameValueObject> T leaveByHangup(String playerId) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new GameNotFoundException();
		}
		Game game = gameIdGameMap.get(gameId);
		T valueObject = game.leaveByHangup(playerId);
		playerIdGameIdMap.remove(playerId);
		return valueObject;
	}

	public <T extends GameValueObject> T quit(String playerId) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new GameNotFoundException();
		}
		Game game = gameIdGameMap.get(gameId);
		T valueObject = game.quit(playerId);
		playerIdGameIdMap.remove(playerId);
		return valueObject;
	}

	public <T extends GameValueObject> T back(String playerId, String gameId) throws Exception {
		Game game = gameIdGameMap.get(gameId);
		if (game == null) {
			throw new GameNotFoundException();
		}
		T valueObject = game.back(playerId);
		playerIdGameIdMap.put(playerId, gameId);
		return valueObject;
	}

	public <T extends GameValueObject> T getInfo(String playerId, String gameId) throws Exception {
		Game game = gameIdGameMap.get(gameId);
		if (game == null) {
			throw new GameNotFoundException();
		}
		T valueObject = game.toValueObject();
		return valueObject;
	}

	/**
	 * 游戏结束，从服务器中注销
	 */
	public void finishGame(String gameId) throws Exception {
		Game game = gameIdGameMap.get(gameId);
		if (game == null) {
			throw new GameNotFoundException();
		}
		gameIdGameMap.remove(gameId);
		game.allPlayerIds().forEach((pid) -> {
			if (gameId.equals(playerIdGameIdMap.get(pid))) {
				playerIdGameIdMap.remove(pid);
			}
		});
	}

	public List<String> findAllPlayerIdsForGame(String gameId) {
		Game game = gameIdGameMap.get(gameId);
		if (game != null) {
			return game.allPlayerIds();
		} else {
			return null;
		}
	}

	public void bindPlayer(String playerId, String gameId) throws Exception {
		Game game = gameIdGameMap.get(gameId);
		if (game == null) {
			throw new GameNotFoundException();
		}
		game.back(playerId);
		playerIdGameIdMap.put(playerId, gameId);
	}

	public String findBindGameId(String playerId) {
		return playerIdGameIdMap.get(playerId);
	}

	public Game findGame(String gameId) throws Exception {
		Game game = gameIdGameMap.get(gameId);
		if (game == null) {
			throw new GameNotFoundException();
		}
		return game;
	}

	public Game findGamePlayerPlaying(String playerId) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		return gameIdGameMap.get(gameId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Game> getGameIdGameMap() {
		return gameIdGameMap;
	}

	public void setGameIdGameMap(Map<String, Game> gameIdGameMap) {
		this.gameIdGameMap = gameIdGameMap;
	}

	public Map<String, String> getPlayerIdGameIdMap() {
		return playerIdGameIdMap;
	}

	public void setPlayerIdGameIdMap(Map<String, String> playerIdGameIdMap) {
		this.playerIdGameIdMap = playerIdGameIdMap;
	}

}
