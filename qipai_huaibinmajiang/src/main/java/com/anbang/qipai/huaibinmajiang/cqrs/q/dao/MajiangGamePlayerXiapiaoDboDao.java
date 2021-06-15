package com.anbang.qipai.huaibinmajiang.cqrs.q.dao;


import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.MajiangPlayerXiapiaoState;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGamePlayerXiapiaoDbo;

import java.util.Map;

public interface MajiangGamePlayerXiapiaoDboDao {

	void addMajiangGamePlayerXiapiaoDbo(MajiangGamePlayerXiapiaoDbo dbo);

	void updateMajiangGamePlayerXiapiaoDbo(String gameId, int panNo,
                                           Map<String, MajiangPlayerXiapiaoState> playerMaidiStateMap,
                                           Map<String, Integer> playerpiaofenMap);

	MajiangGamePlayerXiapiaoDbo findLastByGameId(String gameId);

	MajiangGamePlayerXiapiaoDbo findByGameIdAndPanNo(String gameId, int panNo);

	void removeByTime(long endTime);
}
