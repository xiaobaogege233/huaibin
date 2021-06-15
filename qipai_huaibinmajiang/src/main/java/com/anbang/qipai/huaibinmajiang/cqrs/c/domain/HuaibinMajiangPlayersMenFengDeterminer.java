package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.menfeng.PlayersMenFengDeterminer;
import com.dml.majiang.position.MajiangPosition;
import com.dml.majiang.position.MajiangPositionUtil;
import lombok.Data;

/**
 * 决定庄家的类
 */
@Data
public class HuaibinMajiangPlayersMenFengDeterminer implements PlayersMenFengDeterminer {

	private String zhuangPlayerId; // 要设置的庄家玩家ID

    /**
     * 决定玩家位置
     * @param ju 当前局
     * @throws Exception
     */
	@Override
	public void determinePlayersMenFeng(Ju ju) throws Exception {
	    // 获取当前盘
        Pan currentPan = ju.getCurrentPan();
        // 获取最近完成的盘结果
        PanResult latestFinishedPanResult = ju.findLatestFinishedPanResult();
        // 获取完成的盘结果庄家ID
        String zhuangPlayerId = latestFinishedPanResult.findZhuangPlayerId();
        // 赋值给成员变量
        this.zhuangPlayerId = zhuangPlayerId;
        // 判断是否有胡
        if (!latestFinishedPanResult.hasHu()) {// 表示没有胡
            // 判断是否有杠
            if (latestFinishedPanResult.hasGang()) {// 表示有杠
                int n = setZhuangMenFeng(latestFinishedPanResult);
                // 最后给所有玩家设置门风
                setOtherPlayerMenFeng(n,latestFinishedPanResult,currentPan);
            } else {
                // 位置不变
                noChangeMenFeng(latestFinishedPanResult,currentPan);
            }
        } else if (!latestFinishedPanResult.ifPlayerHu(zhuangPlayerId)) {// 庄没有胡
            int n = setZhuangMenFeng(latestFinishedPanResult);
            // 最后给所有玩家设置门风
            setOtherPlayerMenFeng(n,latestFinishedPanResult,currentPan);
        } else {
            // 位置不变
            noChangeMenFeng(latestFinishedPanResult,currentPan);
        }
	}

    /**
     * 设置庄门风
     * @param latestFinishedPanResult 最近完成的盘结果
     * @return 新庄移动到东需要几步
     */
    private int setZhuangMenFeng(PanResult latestFinishedPanResult)throws Exception{
        // 先找出庄的下家
        String zhuangXiajiaPlayerId = latestFinishedPanResult.findXiajiaPlayerId(zhuangPlayerId);
        // 把下家ID赋值给这把庄家ID
        this.zhuangPlayerId = zhuangXiajiaPlayerId;
        // 再计算要顺时针移几步到东
        MajiangPosition p = latestFinishedPanResult.playerMenFeng(zhuangXiajiaPlayerId);

        int n = 0;
        while (true) {
            MajiangPosition np = MajiangPositionUtil.nextPositionClockwise(p);
            n++;
            if (np.equals(MajiangPosition.dong)) {
                break;
            } else {
                p = np;
            }
        }
        return n;
    }

    /**
     * 设置庄家以外的玩家位置
     * @param n 需要移动的步数
     * @param latestFinishedPanResult 最近一盘的结果
     * @param currentPan 当前盘
     * @throws Exception
     */
    private void setOtherPlayerMenFeng(int n,PanResult latestFinishedPanResult, Pan currentPan)throws Exception{
        List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
        for (String playerId : allPlayerIds) {
            MajiangPosition playerMenFeng = latestFinishedPanResult.playerMenFeng(playerId);
            MajiangPosition newPlayerMenFeng = playerMenFeng;
            for (int i = 0; i < n; i++) {
                newPlayerMenFeng = MajiangPositionUtil.nextPositionClockwise(newPlayerMenFeng);
            }
            currentPan.updatePlayerMenFeng(playerId, newPlayerMenFeng);
        }
    }

    /**
     * 位置不变
     * @param latestFinishedPanResult 最近一盘的结果
     * @param currentPan 当前盘
     * @throws Exception
     */
    private void noChangeMenFeng(PanResult latestFinishedPanResult,Pan currentPan)throws Exception{
        List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
        for (String playerId : allPlayerIds) {
            MajiangPosition playerMenFeng = latestFinishedPanResult.playerMenFeng(playerId);
            currentPan.updatePlayerMenFeng(playerId, playerMenFeng);
        }
    }

}
