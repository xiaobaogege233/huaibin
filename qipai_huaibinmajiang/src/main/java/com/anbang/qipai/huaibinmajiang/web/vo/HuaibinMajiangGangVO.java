package com.anbang.qipai.huaibinmajiang.web.vo;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangGang;
import lombok.Data;

@Data
public class HuaibinMajiangGangVO {
    private int zimoMingGangShu;        //点杠
    private int fangGangmingGangShu;    //明杠
    private int anGangShu;              //暗杠
    private int tiangang; // 天杠
    private int value;                  //杠分

    public HuaibinMajiangGangVO(HuaibinMajiangGang huaibinMajiangGang) {
        this.zimoMingGangShu = huaibinMajiangGang.getZimoMingGangShu();
        this.fangGangmingGangShu = huaibinMajiangGang.getFangGangmingGangShu();
        this.anGangShu = huaibinMajiangGang.getAnGangShu();
        this.value = huaibinMajiangGang.getValue();
    }



}
