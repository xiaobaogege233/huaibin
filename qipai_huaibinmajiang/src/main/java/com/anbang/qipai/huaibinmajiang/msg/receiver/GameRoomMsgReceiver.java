package com.anbang.qipai.huaibinmajiang.msg.receiver;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.huaibinmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.huaibinmajiang.cqrs.q.service.MajiangPlayQueryService;
import com.anbang.qipai.huaibinmajiang.msg.channel.sink.GameRoomSink;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.CommonMO;
import com.anbang.qipai.huaibinmajiang.msg.msjobj.MajiangHistoricalJuResult;
import com.anbang.qipai.huaibinmajiang.msg.service.HuaibinMajiangGameMsgService;
import com.anbang.qipai.huaibinmajiang.msg.service.HuaibinMajiangResultMsgService;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.google.gson.Gson;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.util.ArrayList;
import java.util.List;

@EnableBinding(GameRoomSink.class)
public class GameRoomMsgReceiver {

	@Autowired
	private GameCmdService gameCmdService;

	@Autowired
	private MajiangGameQueryService majiangGameQueryService;

	@Autowired
	private MajiangPlayQueryService majiangPlayQueryService;

	@Autowired
	private HuaibinMajiangResultMsgService huaibinMajiangResultMsgService;

	@Autowired
	private HuaibinMajiangGameMsgService huaibinMajiangGameMsgService;

	private Gson gson = new Gson();

	@StreamListener(GameRoomSink.HONGZHONGMAJIANGMAJIANGGAMEROOM)
	public void removeGameRoom(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("gameIds".equals(msg)) {
			List<String> gameIds = gson.fromJson(json, ArrayList.class);
			for (String gameId : gameIds) {
				try {
					if (StringUtil.isBlank(gameId)) {
						continue;
					}
					MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
					if (majiangGameDbo == null) {
						huaibinMajiangGameMsgService.gameFinished(gameId);
						continue;
					}
					boolean playerOnline = false;
					for (MajiangGamePlayerDbo player : majiangGameDbo.getPlayers()) {
						if (GamePlayerOnlineState.online.equals(player.getOnlineState())) {
							playerOnline = true;
						}
					}
					if (playerOnline) {
						huaibinMajiangGameMsgService.delay(gameId);
					} else {
						huaibinMajiangGameMsgService.gameFinished(gameId);
						MajiangGameValueObject gameValueObject = gameCmdService.finishGameImmediately(gameId);
						majiangGameQueryService.finishGameImmediately(gameValueObject,null);
						JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
						if (juResultDbo != null) {
							MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo,
									majiangGameDbo);
							huaibinMajiangResultMsgService.recordJuResult(juResult);
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

}
