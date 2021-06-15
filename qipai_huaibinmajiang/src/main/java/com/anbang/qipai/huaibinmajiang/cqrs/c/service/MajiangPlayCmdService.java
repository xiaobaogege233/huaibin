package com.anbang.qipai.huaibinmajiang.cqrs.c.service;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.XiapiaoResult;

import java.util.Set;


public interface MajiangPlayCmdService {

	MajiangActionResult action(String playerId, Integer actionId, Integer actionNo, Long actionTime) throws Exception;

	MajiangActionResult automaticAction(String playerId, Integer actionId, Long actionTime, String gameId) throws Exception;

	ReadyToNextPanResult readyToNextPan(String playerId) throws Exception;

    XiapiaoResult xiapiao(String playerId, Integer piaofen) throws Exception;

    XiapiaoResult xiapiao(String playerId, Integer piaofen, String gameId) throws Exception;

	ReadyToNextPanResult readyToNextPan(String playerId, Set<String> playerIds) throws Exception;

	ReadyToNextPanResult autoReadyToNextPan(String playerId, Set<String> playerIds, String gameId) throws Exception;

}
