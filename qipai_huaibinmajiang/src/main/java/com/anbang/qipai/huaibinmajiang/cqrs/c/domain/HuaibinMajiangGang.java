package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 杠对象
 */
@Data
@NoArgsConstructor
public class HuaibinMajiangGang {

    private int zimoMingGangShu; // 明杠
    private int fangGangmingGangShu; // 点杠
    private int anGangShu; // 暗杠
    private int tianGang; //天杠

    private int value; // 杠分

    private String playerId; // 杠玩家ID
    // private String huPlayerId; // 胡玩家ID

    public HuaibinMajiangGang(MajiangPlayer player, Ju ju) {
        zimoMingGangShu = 0;
        fangGangmingGangShu = 0;
        anGangShu = 0;
        tianGang = 0;
        for (GangchuPaiZu gangchuPaiZu : player.getGangchupaiZuList()) {
            if (gangchuPaiZu.getGangType().equals(GangType.gangdachu)) {
                fangGangmingGangShu++;
            } else if (gangchuPaiZu.getGangType().equals(GangType.kezigangmo) || gangchuPaiZu.getGangType().equals(GangType.kezigangshoupai)) {
                zimoMingGangShu++;
            } else if (gangchuPaiZu.getGangType().equals(GangType.shoupaigangmo) || gangchuPaiZu.getGangType().equals(GangType.gangsigeshoupai)) {
                anGangShu++;
            }else if (gangchuPaiZu.getGangType().equals(GangType.tiangang)) {
                tianGang++;
            }
        }
        this.playerId = player.getId();
    }

    /**
     * 计算杠分
     * @param playerCount 玩家人数
     */
    public void calculate(int playerCount,OptionalPlay optionalPlay) {
        if(optionalPlay.isLaojiuzui()){
            value = (anGangShu * 2 + zimoMingGangShu+fangGangmingGangShu+tianGang * 3) * (playerCount - 1) ;
        }
        if(optionalPlay.isPingtui()){
            value = (anGangShu * 4 + (zimoMingGangShu+fangGangmingGangShu) * 2 +tianGang * 6) * (playerCount - 1) ;
        }
    }

    public void jiesuan(int delta) {
        value += delta;
    }
}
