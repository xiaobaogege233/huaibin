package com.dml.majiang.player.action.listener.comprehensive;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.listener.chi.MajiangPlayerChiActionStatisticsListener;
import com.dml.majiang.player.action.listener.da.MajiangPlayerDaActionStatisticsListener;
import com.dml.majiang.player.action.listener.gang.MajiangPlayerGangActionStatisticsListener;
import com.dml.majiang.player.action.listener.mo.MajiangPlayerMoActionStatisticsListener;
import com.dml.majiang.player.action.listener.peng.MajiangPlayerPengActionStatisticsListener;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;

/**
 * 检测天胡和点炮地胡可能。点炮地胡是最常见的地胡。 <br/>
 * 需要联合所有动作检测庄家打出的牌没有了。
 *
 * @author Neo
 */
public class TianHuAndDihuOpportunityDetector implements MajiangPlayerDaActionStatisticsListener, MajiangPlayerChiActionStatisticsListener, MajiangPlayerPengActionStatisticsListener,
        MajiangPlayerGangActionStatisticsListener, MajiangPlayerMoActionStatisticsListener {

    /**
     * 0初始，1庄打了第一张牌，2庄打的第一张牌没有了
     */
    private int state;


    public boolean ifDihuOpportunity() {
        return state == 1;
    }

    public boolean ifTianhuOpportunity() {
        return state == 0;
    }

    @Override
    public void updateForNextPan() {
        state = 0;
    }

    @Override
    public void update(MajiangDaAction daAction, Ju ju) {
        if (state == 0) {
            Pan currentPan = ju.getCurrentPan();
            MajiangPlayer daPlayer = currentPan.findPlayerById(daAction.getActionPlayerId());
            if (currentPan.getZhuangPlayerId().equals(daPlayer.getId())) {
                state = 1;
            }
        } else if (state == 1) {
            state = 2;
        }
    }

    @Override
    public void update(MajiangMoAction moAction, Ju ju) {
        if (state == 1) {
            state = 2;
        }
    }

    @Override
    public void update(MajiangGangAction gangAction, Ju ju) {
        if (state == 1) {
            state = 2;
        }
    }

    @Override
    public void update(MajiangPengAction pengAction, Ju ju) {
        if (state == 1) {
            state = 2;
        }
    }

    @Override
    public void update(MajiangChiAction chiAction, Ju ju) {
        if (state == 1) {
            state = 2;
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
