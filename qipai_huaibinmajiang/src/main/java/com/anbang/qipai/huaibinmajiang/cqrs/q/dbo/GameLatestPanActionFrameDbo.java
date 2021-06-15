package com.anbang.qipai.huaibinmajiang.cqrs.q.dbo;

import com.dml.majiang.pan.frame.PanActionFrame;
import lombok.Data;

@Data
public class GameLatestPanActionFrameDbo {
    private String id;// 就是gameid
    private PanActionFrame panActionFrame;

}
