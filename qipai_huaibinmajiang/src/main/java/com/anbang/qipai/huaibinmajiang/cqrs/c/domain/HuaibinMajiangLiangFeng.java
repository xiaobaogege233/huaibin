package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.MajiangPlayer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: xiaobao
 * @Date: 2021/06/10/17:32
 * @Description:
 */
@Data
@NoArgsConstructor
public class HuaibinMajiangLiangFeng {

    private boolean hasLiangfeng; // 是否有亮风

    private int sanheifengCount; // 三黑风数
    private int siheifengCount; // 四黑风数
    private int zhongfabaiCount; // 中发白数

    private int dachupaifengScore; // 打出风牌分
    private int value; // 风总分
    private String playerId; // 玩家ID

    public HuaibinMajiangLiangFeng(MajiangPlayer player) {
        sanheifengCount = 0;
        siheifengCount = 0;
        zhongfabaiCount = 0;

        player.getLiangfengpaiMap().keySet().forEach( key -> {
            if(key.equals("sanheifeng")){
                sanheifengCount = player.getLiangfengpaiMap().get(key);
            }
            if(key.equals("siheifeng")){
                siheifengCount = player.getLiangfengpaiMap().get(key);
            }
            if(key.equals("zhongfabai")){
                zhongfabaiCount = player.getLiangfengpaiMap().get(key);
            }
        });

        this.playerId = player.getId();
    }

    /**
     * 计算风
     * @param playerCount 玩家人数
     * @param optionalPlay 可选玩法
     * @param player 玩家
     */
    public void calculate(int playerCount,OptionalPlay optionalPlay,MajiangPlayer player) {
        if (optionalPlay.isLiangfeng()){
            List<MajiangPai> dachupaiList = player.getDachupaiList();
            // 统计玩家打出风
            int dachufengCount = 0;
            for (MajiangPai majiangPai : dachupaiList) {
                if (majiangPai.ordinal() > 26 && majiangPai.ordinal() <= 30){
                    dachufengCount++;
                }
            }
            if(dachufengCount >= 8){
                dachupaifengScore = 5 + 2 * (dachufengCount - 8);
            }
            // 计算风
            value += (sanheifengCount * 2 + zhongfabaiCount * 4 + siheifengCount * 6) * (playerCount - 1) + dachupaifengScore;
        }
    }

    public void jiesuan(int delta) {
        value += delta;
    }
}
