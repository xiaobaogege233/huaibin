package com.anbang.qipai.huaibinmajiang.cqrs.q.dao;

import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.PanActionFrameDbo;

import java.util.List;

public interface PanActionFrameDboDao {

	void save(PanActionFrameDbo dbo);

	void save(List<PanActionFrameDbo> frameList);

	List<PanActionFrameDbo> findByGameIdAndPanNo(String gameId, int panNo);

	void removeByTime(long endTime);

    PanActionFrameDbo findLatestPanActionFrame(String gameId);
}
