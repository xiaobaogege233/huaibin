package com.anbang.qipai.huaibinmajiang.web.vo;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.OptionalPlay;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.VoteNotPassWhenXiapiao;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.VotingWhenXiapiao;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.XiapiaoState;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGamePlayerXiapiaoDbo;
import com.dml.mpgame.game.Canceled;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.WaitingStart;
import com.dml.mpgame.game.extend.fpmpv.VoteNotPassWhenWaitingNextPan;
import com.dml.mpgame.game.extend.fpmpv.VotingWhenWaitingNextPan;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.vote.FinishedByVote;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.extend.vote.VotingWhenPlaying;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class GameVO {
	private String id;
	private int panshu;
	private int renshu;
	private double difen;
	private OptionalPlay optionalPlay;
	private List<MajiangGamePlayerVO> playerList;
	private String state;// 原来是 waitingStart, playing, waitingNextPan, finished
    private int panNo;
    private List<String> tuoguanPlayerIds=new ArrayList<>();

    public GameVO(MajiangGameDbo majiangGameDbo, MajiangGamePlayerXiapiaoDbo majiangGamePlayerXiapiaoDbo) {
        id = majiangGameDbo.getId();
        difen=majiangGameDbo.getDifen();
        optionalPlay=majiangGameDbo.getOptionalPlay();
        panshu = majiangGameDbo.getPanshu();
        renshu = majiangGameDbo.getRenshu();
        panNo = majiangGameDbo.getPanNo();
        playerList = new ArrayList<>();
        majiangGameDbo.getPlayers().forEach((dbo) -> playerList.add(new MajiangGamePlayerVO(dbo,majiangGamePlayerXiapiaoDbo)));
        String sn = majiangGameDbo.getState();
        if (sn.equals(Canceled.name)) {
            state = "canceled";
        } else if (sn.equals(Finished.name)) {
            state = "finished";
        } else if (sn.equals(FinishedByVote.name)) {
            state = "finishedbyvote";
        } else if (sn.equals(Playing.name)) {
            state = "playing";
        } else if (sn.equals(VotingWhenPlaying.name)) {
            state = "playing";
        } else if (sn.equals(VoteNotPassWhenPlaying.name)) {
            state = "playing";
        } else if (sn.equals(VotingWhenWaitingNextPan.name)) {
            state = "waitingNextPan";
        } else if (sn.equals(VoteNotPassWhenWaitingNextPan.name)) {
            state = "waitingNextPan";
        } else if (sn.equals(WaitingNextPan.name)) {
            state = "waitingNextPan";
        } else if (sn.equals(WaitingStart.name)) {
            state = "waitingStart";
        } else if (sn.equals(XiapiaoState.name)) {
            state = "xiapiao";
        } else if (sn.equals(VotingWhenXiapiao.name)) {
            state = "xiapiao";
        } else if (sn.equals(VoteNotPassWhenXiapiao.name)) {
            state = "xiapiao";
        } else {
        }
    }

    public GameVO(MajiangGameDbo majiangGameDbo) {
        id = majiangGameDbo.getId();
        difen=majiangGameDbo.getDifen();
        optionalPlay=majiangGameDbo.getOptionalPlay();
        panshu = majiangGameDbo.getPanshu();
        renshu = majiangGameDbo.getRenshu();
        panNo = majiangGameDbo.getPanNo();
        playerList = new ArrayList<>();
        majiangGameDbo.getPlayers().forEach((dbo) -> playerList.add(new MajiangGamePlayerVO(dbo)));
        String sn = majiangGameDbo.getState();
        if (sn.equals(Canceled.name)) {
            state = "canceled";
        } else if (sn.equals(Finished.name)) {
            state = "finished";
        } else if (sn.equals(FinishedByVote.name)) {
            state = "finishedbyvote";
        } else if (sn.equals(Playing.name)) {
            state = "playing";
        } else if (sn.equals(VotingWhenPlaying.name)) {
            state = "playing";
        } else if (sn.equals(VoteNotPassWhenPlaying.name)) {
            state = "playing";
        } else if (sn.equals(VotingWhenWaitingNextPan.name)) {
            state = "waitingNextPan";
        } else if (sn.equals(VoteNotPassWhenWaitingNextPan.name)) {
            state = "waitingNextPan";
        } else if (sn.equals(WaitingNextPan.name)) {
            state = "waitingNextPan";
        } else if (sn.equals(WaitingStart.name)) {
            state = "waitingStart";
        } else if (sn.equals(XiapiaoState.name)) {
            state = "xiapiao";
        } else if (sn.equals(VotingWhenXiapiao.name)) {
            state = "xiapiao";
        } else if (sn.equals(VoteNotPassWhenXiapiao.name)) {
            state = "xiapiao";
        } else {
        }
    }

}
