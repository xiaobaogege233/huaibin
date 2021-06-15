package com.dml.mpgame.game;

import java.util.ArrayList;
import java.util.List;

import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.dml.mpgame.game.player.GamePlayerState;
import lombok.Data;

@Data
public abstract class GameValueObject {

	private String id;
	private String createPlayerId;
	private GameState state;
	private List<GamePlayerValueObject> players;

	public GameValueObject(Game game) {
		id = game.getId();
		createPlayerId = game.getCreatePlayerId();
		state = game.getState();
		players = new ArrayList<>();
		game.getIdPlayerMap().values().forEach((player) -> players.add(new GamePlayerValueObject(player)));
	}

	public GamePlayerOnlineState findPlayerOnlineState(String playerId) {
		for (GamePlayerValueObject player : players) {
			if (player.getId().equals(playerId)) {
				return player.getOnlineState();
			}
		}
		return null;
	}

	public GamePlayerState findPlayerState(String playerId) {
		for (GamePlayerValueObject player : players) {
			if (player.getId().equals(playerId)) {
				return player.getState();
			}
		}
		return null;
	}

	public List<String> allPlayerIds() {
		List<String> allPlayerIds = new ArrayList<>();
		players.forEach((player) -> allPlayerIds.add(player.getId()));
		return allPlayerIds;
	}


}
