package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.listener.HuaibinMajiangPengGangActionStatisticsListener;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.piao.*;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.finish.FixedPanNumbersJuFinishiDeterminer;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.pan.publicwaitingplayer.WaitDaPlayerPanPublicWaitingPlayerDeterminer;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.action.da.DachushoupaiDaActionProcessor;
import com.dml.majiang.player.action.gang.HuFirstBuGangActionProcessor;
import com.dml.majiang.player.action.guo.DoNothingGuoActionProcessor;
import com.dml.majiang.player.action.hu.PlayerHuAndClearAllActionHuActionUpdater;
import com.dml.majiang.player.action.hu.PlayerSetHuHuActionProcessor;
import com.dml.majiang.player.action.initial.ZhuangMoPaiInitialActionUpdater;
import com.dml.majiang.player.action.listener.comprehensive.GuoHuBuHuStatisticsListener;
import com.dml.majiang.player.action.listener.comprehensive.GuoPengBuPengStatisticsListener;
import com.dml.majiang.player.action.listener.mo.MoGuipaiCounter;
import com.dml.majiang.player.action.peng.HuFirstBuPengActionProcessor;
import com.dml.majiang.player.menfeng.RandomMustHasDongPlayersMenFengDeterminer;
import com.dml.majiang.player.zhuang.MenFengDongZhuangDeterminer;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGame;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.PlayerPlaying;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏
 */
@Data
public class MajiangGame extends FixedPlayersMultipanAndVotetofinishGame {
	private int panshu; // 盘数
	private int renshu; // 人数
	private OptionalPlay optionalPlay; // 可选玩法
	private double difen; // 底分
	private Ju ju; // 当前局
	private Map<String, Double> playerTotalScoreMap = new HashMap<>(); // 玩家总分集合 <玩家ID:玩家总分>
    private Map<String, MajiangPlayerXiapiaoState> playerXiapiaoStateMap=new HashMap<>(); // 玩家下票状态集合 <玩家ID:玩家下票状态>
    private Map<String,Integer> playerpiaofenMap=new HashMap<>(); // 玩家票分集合 <玩家ID:玩家票分>
    private int powerLimit; //
    private String lianmengId; // 联盟ID
    public PanActionFrame findFirstPanActionFrame() {
        return ju.getCurrentPan().findLatestActionFrame();
    }

	/**
	 * 创建局并且开始第一把
	 * @param currentTime 当前时间
	 * @throws Exception
	 */
	public void createJuAndStartFirstPan(long currentTime) throws Exception {
    	// 创建新的一局对象
		ju = new Ju();
		// 创建开启第一把流程对象
        HuaibinMajiangStartFirstPanProcess huaibinMajiangStartFirstPanProcess = new HuaibinMajiangStartFirstPanProcess();
        // 将可选玩法设置进去
        huaibinMajiangStartFirstPanProcess.setOptionalPlay(optionalPlay);
        // 把第一把流程对象设置到局中
		ju.setStartFirstPanProcess(huaibinMajiangStartFirstPanProcess);
		// 创建开启下一把流程对象
        HuaibinMajiangStartNextPanProcess huaibinMajiangStartNextPanProcess = new HuaibinMajiangStartNextPanProcess();
		// 将可选玩法设置进去
        huaibinMajiangStartNextPanProcess.setOptionalPlay(optionalPlay);
		// 把下一把流程对象设置到局中
		ju.setStartNextPanProcess(huaibinMajiangStartNextPanProcess);
		// 设置第一盘门风对象
		ju.setPlayersMenFengDeterminerForFirstPan(new RandomMustHasDongPlayersMenFengDeterminer(currentTime));
		// 设置下一把门风对象
		ju.setPlayersMenFengDeterminerForNextPan(new HuaibinMajiangPlayersMenFengDeterminer());
		// 设置第一把庄家决定对象
		ju.setZhuangDeterminerForFirstPan(new MenFengDongZhuangDeterminer());
		// 设置下一把庄家决定对象
		ju.setZhuangDeterminerForNextPan(new MenFengDongZhuangDeterminer());
		// 设置可用牌过滤器
		ju.setAvaliablePaiFiller(new HuaibinMajiangRandomAvaliablePaiFiller(currentTime + 1,true,true));
		// 设置鬼牌
        ju.setGuipaiDeterminer(new HuaibinMajiangRandomGuipaiDeterminer(currentTime + 2,true));
        // 设置发牌策略
		ju.setFaPaiStrategy(new HuaibinMajiangFaPaiStrategy(13));
		// 设置亮风策略
		ju.setLiangFengStrategy(new HuaibinMajiangLiangfengStrategy());
		// 设置当前盘结束
		ju.setCurrentPanFinishiDeterminer(new HuaibinMajiangPanFinishiDeterminer());

		// 有七对时设置
		/*if(optionalPlay.isKehuQiduiBuJiafan()||optionalPlay.isKehuQiduiJiafan()){
            ju.setGouXingPanHu(new NoDanpaiQiDuiziGouXingPanHu());
        }else {
            ju.setGouXingPanHu(new NoDanpaiGouXingPanHu());
        }*/


		// 设置等待玩家打牌
		ju.setCurrentPanPublicWaitingPlayerDeterminer(new WaitDaPlayerPanPublicWaitingPlayerDeterminer());
		// 创建盘结果构建者对象
		HuaibinMajiangPanResultBuilder huaibinMajiangPanResultBuilder = new HuaibinMajiangPanResultBuilder();
		// 设置可选玩法
		huaibinMajiangPanResultBuilder.setOptionalPlay(optionalPlay);
		// 设置底分
		huaibinMajiangPanResultBuilder.setDifen(difen);
		// 设置票分
		huaibinMajiangPanResultBuilder.setPlayerpiaofenMap(playerpiaofenMap);
		// 把盘结果构建者对象设置倒局对象中
		ju.setCurrentPanResultBuilder(huaibinMajiangPanResultBuilder);
		// 创建胡牌方案提示对象
		HuaibinMajiangHuPaiSolutionsTipsFilter huaibinMajiangHuPaiSolutionsTipsFilter = new HuaibinMajiangHuPaiSolutionsTipsFilter();
		// 胡牌策略设置可选玩法
		huaibinMajiangHuPaiSolutionsTipsFilter.setOptionalPlay(optionalPlay);
		// 设置到当前局中
        ju.setHupaiPaixingSolutionFilter(huaibinMajiangHuPaiSolutionsTipsFilter);
		// 设置完成局
		ju.setJuFinishiDeterminer(new FixedPanNumbersJuFinishiDeterminer(panshu));
		// 设置局结果构建
		ju.setJuResultBuilder(new HuaibinMajiangJuResultBuilder());
		// 设置初始化动作
		ju.setInitialActionUpdater(new ZhuangMoPaiInitialActionUpdater());
		// 设置摸动作过程
		ju.setMoActionProcessor(new HuaibinMajiangMoActionProcessor());
		ju.setMoActionUpdater(new HuaibinMajiangMoActionUpdater());
		// 设置打动作过程
		ju.setDaActionProcessor(new DachushoupaiDaActionProcessor());
        ju.setDaActionUpdater(new HuaibinMajiangDaActionUpdater());
		// 设置碰动作过程
		ju.setPengActionProcessor(new HuFirstBuPengActionProcessor());
		ju.setPengActionUpdater(new HuaibinMajiangPengActionUpdater());
		// 设置杠动作过程
		ju.setGangActionProcessor(new HuFirstBuGangActionProcessor());
		ju.setGangActionUpdater(new HuaibinMajiangGangActionUpdater());
		// 设置过动作过程
		ju.setGuoActionProcessor(new DoNothingGuoActionProcessor());
		ju.setGuoActionUpdater(new HuaibinMajiangGuoActionUpdater());
		// 设置胡动作过程
		ju.setHuActionProcessor(new PlayerSetHuHuActionProcessor());
		ju.setHuActionUpdater(new PlayerHuAndClearAllActionHuActionUpdater());

		ju.addActionStatisticsListener(new GuoHuBuHuStatisticsListener());
		ju.addActionStatisticsListener(new HuaibinMajiangPengGangActionStatisticsListener());
		ju.addActionStatisticsListener(new MoGuipaiCounter());
		ju.addActionStatisticsListener(new GuoPengBuPengStatisticsListener());
		// 开始第一盘


        Pan firstPan = new Pan();
        firstPan.setNo(1);
        allPlayerIds().forEach(firstPan::addPlayer);
        ju.setCurrentPan(firstPan);
        if (optionalPlay.getPaofen()!=5){
            allPlayerIds().forEach((pid) -> playerpiaofenMap.put(pid, optionalPlay.getPaofen()));
            ju.startFirstPan(allPlayerIds());
        }else {
            allPlayerIds().forEach((pid) -> playerpiaofenMap.put(pid, 0));
            allPlayerIds().forEach((pid) -> playerXiapiaoStateMap.put(pid, MajiangPlayerXiapiaoState.waitForxiapiao));
        }
	}

    public XiapiaoResult xiapiao(String playerId, int piaofen) throws Exception {
        XiapiaoResult xiapiaoResult = new XiapiaoResult();
        List<String> playerIdList = new ArrayList<>(this.playerpiaofenMap.keySet());
        playerpiaofenMap.put(playerId,piaofen);
        this.playerpiaofenMap.put(playerId,piaofen);
        this.playerXiapiaoStateMap.put(playerId,MajiangPlayerXiapiaoState.over);
        if (state.name().equals(VoteNotPassWhenXiapiao.name)) {
            state = new XiapiaoState();
        }
        updatePlayerState(playerId, new PlayerAfterXiapiao());
        boolean start = true;
        int xiaopiaoOverPlayerCount=0;
        Map<String, String> depositPlayerList = ju.getDepositPlayerList();
        for (String pid : playerIdList) {
            if (MajiangPlayerXiapiaoState.waitForxiapiao.equals(this.playerXiapiaoStateMap.get(pid))) {
                start = false;
            }else if (MajiangPlayerXiapiaoState.over.equals(this.playerXiapiaoStateMap.get(pid))){
                xiaopiaoOverPlayerCount++;
            }
        }
        if (xiaopiaoOverPlayerCount+depositPlayerList.size()==ju.getCurrentPan().getMajiangPlayerIdMajiangPlayerMap().size()){
            start=true;
            for (String tuogaunPlayerId:depositPlayerList.keySet()) {
                this.playerXiapiaoStateMap.put(tuogaunPlayerId,MajiangPlayerXiapiaoState.over);
            }
        }
        if (start) {
            if (ju.getCurrentPan().getNo()==1){
                ju.startFirstPan(allPlayerIds());
            }else {
                ju.startNextPan();
            }
            state = new Playing();
            updateAllPlayersState(new PlayerPlaying());
            PanActionFrame firstActionFrame=ju.getCurrentPan().findLatestActionFrame();
            xiapiaoResult.setFirstActionFrame(firstActionFrame);
        }
        MajiangGameValueObject majiangGame = new MajiangGameValueObject(this);
        majiangGame.setPlayerXiapiaoStateMap(playerXiapiaoStateMap);
        majiangGame.setPlayerpiaofenMap(playerpiaofenMap);
        xiapiaoResult.setMajiangGame(majiangGame);
        return xiapiaoResult;
    }

	public MajiangActionResult action(String playerId, int actionId, int actionNo, long actionTime) throws Exception {
		PanActionFrame panActionFrame = ju.action(playerId, actionId, actionNo, actionTime);
		MajiangActionResult result = new MajiangActionResult();
		result.setPanActionFrame(panActionFrame);
		if (state.name().equals(VoteNotPassWhenPlaying.name)) {
			state = new Playing();
		}
		checkAndFinishPan();

		if (state.name().equals(WaitingNextPan.name) || state.name().equals(Finished.name)) {// 盘结束了
			HuaibinMajiangPanResult panResult = (HuaibinMajiangPanResult) ju.findLatestFinishedPanResult();
			for (HuaibinMajiangPanPlayerResult huaibinMajiangPanPlayerResult : panResult.getPanPlayerResultList()) {
				playerTotalScoreMap.put(huaibinMajiangPanPlayerResult.getPlayerId(),
						huaibinMajiangPanPlayerResult.getTotalScore());
			}
			result.setPanResult(panResult);
			if (state.name().equals(Finished.name)) {// 局结束了
				result.setJuResult((HuaibinMajiangJuResult) ju.getJuResult());
			}
		}
		result.setMajiangGame(new MajiangGameValueObject(this));
		return result;
	}

	public MajiangActionResult automaticAction(String playerId, int actionId, long actionTime) throws Exception {
		PanActionFrame panActionFrame = ju.automaticAction(playerId, actionId, actionTime);
		MajiangActionResult result = new MajiangActionResult();
		result.setPanActionFrame(panActionFrame);
		if (state.name().equals(VoteNotPassWhenPlaying.name)) {
			state = new Playing();
		}
		checkAndFinishPan();

		if (state.name().equals(WaitingNextPan.name) || state.name().equals(Finished.name)) {// 盘结束了
			HuaibinMajiangPanResult panResult = (HuaibinMajiangPanResult) ju.findLatestFinishedPanResult();
			for (HuaibinMajiangPanPlayerResult tongchengMajiangPanPlayerResult : panResult.getPanPlayerResultList()) {
				playerTotalScoreMap.put(tongchengMajiangPanPlayerResult.getPlayerId(),
						tongchengMajiangPanPlayerResult.getTotalScore());
			}
			result.setPanResult(panResult);
			if (state.name().equals(Finished.name)) {// 局结束了
				result.setJuResult((HuaibinMajiangJuResult) ju.getJuResult());
			}
		}
		result.setMajiangGame(new MajiangGameValueObject(this));
		return result;
	}

	@Override
	protected boolean checkToFinishGame() throws Exception {
		return ju.getJuResult() != null;
	}

	@Override
	protected boolean checkToFinishCurrentPan() throws Exception {
		return ju.getCurrentPan() == null;
	}

	@Override
	protected void startNextPan() throws Exception {
        ju.getActionStatisticsListenerManager().updateListenersForNextPan();
        Pan nextPan = new Pan();
        nextPan.setNo(ju.countFinishedPan() + 1);
        PanResult latestFinishedPanResult = ju.findLatestFinishedPanResult();
        List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
        allPlayerIds.forEach(nextPan::addPlayer);
        ju.setCurrentPan(nextPan);
        if (optionalPlay.getPaofen()!=5){
            state = new Playing();
            updateAllPlayersState(new PlayerPlaying());
            ju.startNextPan();
        }else {
            allPlayerIds().forEach((pid) -> playerpiaofenMap.put(pid, 0));
            allPlayerIds().forEach((pid) -> playerXiapiaoStateMap.put(pid, MajiangPlayerXiapiaoState.waitForxiapiao));
            state=new XiapiaoState();
            updateAllPlayersState(new PlayerXiapiao());
        }
	}

	@Override
	protected void updateToExtendedVotingState() {
        if (state.name().equals(XiapiaoState.name) || state.name().equals(VoteNotPassWhenXiapiao.name)) {
            state = new VotingWhenXiapiao();
        }
	}

	@Override
	protected void updatePlayerToExtendedVotingState(GamePlayer player) {
        if (player.getState().name().equals(PlayerXiapiao.name)) {
            player.setState(new PlayerVotingWhenXiapiao());
        } else if (player.getState().name().equals(PlayerAfterXiapiao.name)) {
            player.setState(new PlayerVotingWhenAfterXiapiao());
        }
	}

	@Override
	protected void updateToVoteNotPassStateFromExtendedVoting() throws Exception {
        if (state.name().equals(VotingWhenXiapiao.name)) {
            state = new VoteNotPassWhenXiapiao();
        }
	}

	@Override
	protected void recoveryPlayersStateFromExtendedVoting() throws Exception {
        if (state.name().equals(VoteNotPassWhenXiapiao.name)) {
            for (GamePlayer player : idPlayerMap.values()) {
                if (player.getState().name().equals(PlayerVotingWhenXiapiao.name)
                        || player.getState().name().equals(PlayerVotedWhenXiapiao.name)) {
                    updatePlayerState(player.getId(), new PlayerXiapiao());
                } else if (player.getState().name().equals(PlayerVotingWhenAfterXiapiao.name)
                        || player.getState().name().equals(PlayerVotedWhenAfterXiapiao.name)) {
                    updatePlayerState(player.getId(), new PlayerAfterXiapiao());
                }
            }
        }
	}

	@Override
	public void start(long currentTime) throws Exception {
        createJuAndStartFirstPan(currentTime);
        if (optionalPlay.getPaofen()!=5){
            state = new Playing();
            updateAllPlayersState(new PlayerPlaying());
        }else {
            state=new XiapiaoState();
            updateAllPlayersState(new PlayerXiapiao());
        }
	}

	@Override
	protected void updatePlayerToExtendedVotedState(GamePlayer player) {
        if (player.getState().name().equals(PlayerVotingWhenXiapiao.name)) {
            player.setState(new PlayerVotedWhenXiapiao());
        } else if (player.getState().name().equals(PlayerVotingWhenAfterXiapiao.name)) {
            player.setState(new PlayerVotedWhenAfterXiapiao());
        }
	}

	@Override
	public void finish() throws Exception {
		if (ju != null) {
			ju.finish();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public MajiangGameValueObject toValueObject() {
		return new MajiangGameValueObject(this);
	}


}
