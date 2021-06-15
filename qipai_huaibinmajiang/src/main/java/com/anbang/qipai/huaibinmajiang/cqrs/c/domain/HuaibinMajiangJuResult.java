package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.result.JuResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HuaibinMajiangJuResult implements JuResult {

    private int finishedPanCount;

    private List<HuaibinMajiangJuPlayerResult> playerResultList = new ArrayList<>();

    private String dayingjiaId;

    private String datuhaoId;

}
