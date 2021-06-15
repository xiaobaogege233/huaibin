package com.dml.majiang.player.action.ting;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;

public class PlayerTingActionProcessor implements MajiangPlayerTingActionProcessor {

    @Override
    public void process(MajiangTingAction tingAction, Ju ju) throws Exception {
        Pan currentPan = ju.getCurrentPan();
        currentPan.playerClearActionCandidates(tingAction.getActionPlayerId());
        MajiangPlayer player = currentPan.findPlayerById(tingAction.getActionPlayerId());

        player.setTingpai(true);
        player.setTingPaiIndex(player.getDachupaiList().size());

        player.generateTingpaiAction();
    }

}
