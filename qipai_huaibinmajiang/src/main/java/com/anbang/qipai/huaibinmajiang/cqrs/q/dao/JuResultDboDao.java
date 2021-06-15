package com.anbang.qipai.huaibinmajiang.cqrs.q.dao;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.JuResultDbo;

public interface JuResultDboDao {

	void save(JuResultDbo juResultDbo);

	JuResultDbo findByGameId(String gameId);

	void removeByTime(long endTime);

}
