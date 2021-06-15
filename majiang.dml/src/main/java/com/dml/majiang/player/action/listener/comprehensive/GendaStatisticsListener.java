package com.dml.majiang.player.action.listener.comprehensive;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.listener.da.MajiangPlayerDaActionStatisticsListener;
import com.dml.majiang.player.action.listener.guo.MajiangPlayerGuoActionStatisticsListener;
import com.dml.majiang.player.action.listener.mo.MajiangPlayerMoActionStatisticsListener;
import com.dml.majiang.player.action.mo.MajiangMoAction;

import java.util.*;

public class GendaStatisticsListener implements MajiangPlayerDaActionStatisticsListener, MajiangPlayerMoActionStatisticsListener {

    private Map<String, MajiangPai> gendaPlayersPaiMap = new HashMap<>();

    @Override
    public void updateForNextPan() {
        gendaPlayersPaiMap = new HashMap<>();
    }

    @Override
    public void update(MajiangMoAction moAction, Ju ju) {
        gendaPlayersPaiMap.remove(moAction.getActionPlayerId());
    }

    @Override
    public void update(MajiangDaAction daAction, Ju ju) {
        Pan currentPan = ju.getCurrentPan();
        MajiangPlayer player = currentPan.findPlayerById(daAction.getActionPlayerId());
        if (player.getFangruGuipaiList().size() + player.getFangruShoupaiList().size() == 1) {
            gendaPlayersPaiMap.put(daAction.getActionPlayerId(), daAction.getPai());
        }

    }

    public Map<String, MajiangPai> getGendaPlayersPaiMap() {
        return gendaPlayersPaiMap;
    }

    public void setGendaPlayersPaiMap(Map<String, MajiangPai> gendaPlayersPaiMap) {
        this.gendaPlayersPaiMap = gendaPlayersPaiMap;
    }
}
