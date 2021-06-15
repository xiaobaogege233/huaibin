package com.dml.majiang.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.XushupaiCategory;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.pai.fenzu.Gangzi;
import com.dml.majiang.pai.fenzu.Kezi;
import com.dml.majiang.pai.fenzu.Shunzi;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.player.chupaizu.ChichuPaiZu;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;
import com.dml.majiang.player.shoupai.ShoupaiCalculator;
import com.dml.majiang.player.shoupaisort.MajiangPaiOrderShoupaiSortComparator;
import com.dml.majiang.position.MajiangPosition;
import com.dml.majiang.position.MajiangPositionUtil;
import lombok.Data;

@Data
public class MajiangPlayer {

    private String id;

    /**
     * 门风
     */
    private MajiangPosition menFeng;

    /**
     * 已放入的手牌列表（不包含鬼牌，不包含公开牌）
     */
    private List<MajiangPai> fangruShoupaiList = new ArrayList<>();

    private Comparator<MajiangPai> fangruShoupaiListSortComparator = new MajiangPaiOrderShoupaiSortComparator();

    /**
     * 已放入的鬼牌手牌列表（全部是鬼牌）
     */
    private List<MajiangPai> fangruGuipaiList = new ArrayList<>();

    /**
     * 刚摸进待处理的手牌（未放入）
     */
    private MajiangPai gangmoShoupai;

    /**
     * 公开的牌，不能行牌
     */
    private List<MajiangPai> publicPaiList = new ArrayList<>();

    private List<MajiangPai> kouShoupaiList = new ArrayList<>();

    /**
     * 标示什么牌是鬼牌
     */
    private Set<MajiangPai> guipaiTypeSet = new HashSet<>();
    /**
     * 搬子牌型
     */
    private Set<MajiangPai> banziTypeSet = new HashSet<>();

    private Map<Integer, MajiangPlayerAction> actionCandidates = new HashMap<>();

    private Map<MajiangPai, List<MajiangPai>> hupaiCandidates = new HashMap<>();

    private List<MajiangPai> kehuCandidates = new ArrayList<>();
    /**
     * 不包含鬼牌或者公开牌。刚模来的牌未处理前不加入计算器。
     */
    private ShoupaiCalculator shoupaiCalculator = new ShoupaiCalculator();

    /**
     * 打出的牌列表
     */
    private List<MajiangPai> dachupaiList = new ArrayList<>();
    private List<ChichuPaiZu> chichupaiZuList = new ArrayList<>();
    private List<PengchuPaiZu> pengchupaiZuList = new ArrayList<>();
    private List<GangchuPaiZu> gangchupaiZuList = new ArrayList<>();
    private Map<String,Integer> liangfengpaiMap = new HashMap<>(); // 亮风牌集 《亮风类型：亮几次》
    private Map<String,List<MajiangPai>> canLiangfengpaiMap = new HashMap<>(); // 可以展示的所有能亮风的选项 《亮风类型：亮风的牌》

    private Hu hu;

    private int zhadanScore;
    /**
     * 即时结算杠分
     */
    private int gangScore = 0;
    /**
     * 玩家总分
     */
    private double playerTotalScore = 0;
    /**
     * 是否听牌
     */
    private boolean tingpai = false;
    /**
     * 听牌自动出牌
     */
    private boolean tingAutoDaPai = false;
    /**
     * 听牌时已打出牌索引
     */
    private int tingPaiIndex = 0;
    /**
     * 托管状态
     */
    private boolean tuoguanStatus = false;
    /**
     * 定缺类型
     */
    private XushupaiCategory quemen;

    /**
     * 建议定缺类型
     */
    private XushupaiCategory suggestQuemen;

    /**
     * 是否有亮风
     */
    private boolean hasLiangfeng;

    public void addGuipaiType(MajiangPai guipaiType) {
        guipaiTypeSet.add(guipaiType);
    }

    public void addGuipaiType(MajiangPai guipaiType1, MajiangPai guipaiType2) {
        guipaiTypeSet.add(guipaiType1);
        guipaiTypeSet.add(guipaiType2);
    }

    public void addBanziAndPeiziType(MajiangPai banziType, MajiangPai peiziType) {
        banziTypeSet.add(banziType);
        if (peiziType != null) {
            guipaiTypeSet.add(peiziType);
        }

    }

    public void addShoupai(MajiangPai pai) {
        if (!guipaiTypeSet.contains(pai)) {
            fangruShoupaiList.add(pai);
            Collections.sort(fangruShoupaiList, fangruShoupaiListSortComparator);
            shoupaiCalculator.addPai(pai);
        } else {
            fangruGuipaiList.add(pai);
        }
    }

    public void addKoupai() {
        for (int i = 0; i < 4; i++) {
            kouShoupaiList.add(fangruShoupaiList.remove(0));
        }
        Collections.sort(fangruShoupaiList, fangruShoupaiListSortComparator);
        Collections.sort(kouShoupaiList, fangruShoupaiListSortComparator);
    }

    public void addActionCandidate(MajiangPlayerAction action) {
        int idForNewAction = actionCandidates.size() + 1;
        action.setId(idForNewAction);
        actionCandidates.put(idForNewAction, action);
    }

    public MajiangPlayerAction findActionCandidate(int actionId) {
        return actionCandidates.get(actionId);
    }

    public void addPublicPai(MajiangPai pai) {
        publicPaiList.add(pai);
    }

    /**
     * 不能打鬼牌
     */
    public void generateDaActions() {
        Set<MajiangPai> daPaiSet = new HashSet<>();
        fangruShoupaiList.forEach((shoupai) -> {
            if (!daPaiSet.contains(shoupai)) {
                addActionCandidate(new MajiangDaAction(id, shoupai));
                daPaiSet.add(shoupai);
            }
        });

        if (gangmoShoupai != null && !guipaiTypeSet.contains(gangmoShoupai) && !daPaiSet.contains(gangmoShoupai)) {
            addActionCandidate(new MajiangDaAction(id, gangmoShoupai));
        }

    }

    /**
     * 可以打鬼牌
     */
    public void generateDaGuipaiActions() {
        Set<MajiangPai> daPaiSet = new HashSet<>();
        fangruShoupaiList.forEach((shoupai) -> {
            if (!daPaiSet.contains(shoupai)) {
                addActionCandidate(new MajiangDaAction(id, shoupai));
                daPaiSet.add(shoupai);
            }
        });
        fangruGuipaiList.forEach((shoupai) -> {
            if (!daPaiSet.contains(shoupai)) {
                addActionCandidate(new MajiangDaAction(id, shoupai));
                daPaiSet.add(shoupai);
            }
        });
        if (gangmoShoupai != null && !daPaiSet.contains(gangmoShoupai)) {
            addActionCandidate(new MajiangDaAction(id, gangmoShoupai));
        }

    }

    public void generateTingpaiAction() {
        for (MajiangPai pai : hupaiCandidates.keySet()) {
            if (!guipaiTypeSet.contains(pai)) {
                addActionCandidate(new MajiangDaAction(id, pai));
            }
        }
    }

    public void generateTingpaiDaAction() {
        addActionCandidate(new MajiangDaAction(id, gangmoShoupai));
    }

    public void clearActionCandidates() {
        actionCandidates.clear();
    }

    public void clearHupaiCandidates() {
        hupaiCandidates.clear();
    }

    public void clearKehuCandidates() {
        kehuCandidates.clear();
    }

    /**
     * 通常不能打鬼牌
     *
     * @param pai
     */
    public void daChuPai(MajiangPai pai) {
        if (!guipaiTypeSet.contains(pai)) {
            fangruShoupai();
            fangruShoupaiList.remove(pai);
            dachupaiList.add(pai);
            shoupaiCalculator.removePai(pai);
        } else {
            fangruGuipai();
            fangruGuipaiList.remove(pai);
            dachupaiList.add(pai);
        }
    }

    /**
     * 将手中的牌放入公共牌
     *
     * @param pai 花牌
     */
    public void shouPaiBuHua(MajiangPai pai) {
        publicPaiList.add(pai);
        shoupaiCalculator.removePai(pai);
    }

    /**
     * 把刚摸的牌放入手牌
     */
    public void fangruShoupai() {
        if (gangmoShoupai != null) {
            addShoupai(gangmoShoupai);
            gangmoShoupai = null;
        }
    }

    public void fangruGuipai() {
        if (gangmoShoupai != null) {
            addShoupai(gangmoShoupai);
            gangmoShoupai = null;
        }
    }

    /**
     * 把刚摸的牌放入公开牌
     */
    public void fangruPublicPai() {
        if (gangmoShoupai != null) {
            publicPaiList.add(gangmoShoupai);
            gangmoShoupai = null;
        }
    }

    public void chiPai(MajiangPlayer dachupaiPlayer, MajiangPai chijinpai, Shunzi chifaShunzi) {
        dachupaiPlayer.removeLatestDachupai();
        MajiangPai pai1 = chifaShunzi.getPai1();
        if (!pai1.equals(chijinpai)) {
            fangruShoupaiList.remove(pai1);
            shoupaiCalculator.removePai(pai1);
        }
        MajiangPai pai2 = chifaShunzi.getPai2();
        if (!pai2.equals(chijinpai)) {
            fangruShoupaiList.remove(pai2);
            shoupaiCalculator.removePai(pai2);
        }
        MajiangPai pai3 = chifaShunzi.getPai3();
        if (!pai3.equals(chijinpai)) {
            fangruShoupaiList.remove(pai3);
            shoupaiCalculator.removePai(pai3);
        }
        ChichuPaiZu chichuPaiZu = new ChichuPaiZu(chijinpai, chifaShunzi, dachupaiPlayer.getId(), id);
        chichupaiZuList.add(chichuPaiZu);
    }

    public void pengPai(MajiangPlayer dachupaiPlayer, MajiangPai pai) {
        dachupaiPlayer.removeLatestDachupai();
        // 统计这张牌在手牌中的数量
        int amountOfPai = Collections.frequency(fangruShoupaiList, pai);
        if (amountOfPai > 2) {
            amountOfPai = 2;
        }
        for (int i = 0; i < amountOfPai; i++) {
            fangruShoupaiList.remove(pai);
        }
        for (int i = 0; i < 2 - amountOfPai; i++) {
            kouShoupaiList.remove(pai);
        }
        shoupaiCalculator.removePai(pai, 2);
        PengchuPaiZu pengchuPaiZu = new PengchuPaiZu(new Kezi(pai), dachupaiPlayer.getId(), id);
        pengchupaiZuList.add(pengchuPaiZu);
    }

    public void gangDachupai(MajiangPlayer dachupaiPlayer, MajiangPai pai) {
        dachupaiPlayer.removeLatestDachupai();
        int amountOfPai = Collections.frequency(fangruShoupaiList, pai);
        for (int i = 0; i < amountOfPai; i++) {
            fangruShoupaiList.remove(pai);
        }

        for (int i = 0; i < 3 - amountOfPai; i++) {
            kouShoupaiList.remove(pai);
        }
        shoupaiCalculator.removePai(pai, 3);
        GangchuPaiZu gangchuPaiZu = new GangchuPaiZu(new Gangzi(pai), dachupaiPlayer.getId(), id, GangType.gangdachu);
        gangchupaiZuList.add(gangchuPaiZu);
    }

    public void sanbanziGangDachupai(MajiangPlayer dachupaiPlayer, MajiangPai pai) {
        dachupaiPlayer.removeLatestDachupai();
        int amountOfPai = Collections.frequency(fangruShoupaiList, pai);
        for (int i = 0; i < amountOfPai; i++) {
            fangruShoupaiList.remove(pai);
        }
        shoupaiCalculator.removePai(pai, 2);
        GangchuPaiZu gangchuPaiZu = new GangchuPaiZu(new Gangzi(pai), dachupaiPlayer.getId(), id, GangType.sanbanziminggang);
        gangchupaiZuList.add(gangchuPaiZu);
    }

    public void gangMopai(MajiangPai pai) {
        if (guipaiTypeSet.contains(pai)) {
            for (int i = 0; i < 3; i++) {
                fangruGuipaiList.remove(pai);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                fangruShoupaiList.remove(pai);
            }
            shoupaiCalculator.removePai(pai, 3);
        }
        GangchuPaiZu gangchuPaiZu = new GangchuPaiZu(new Gangzi(pai), null, id, GangType.shoupaigangmo);
        gangchupaiZuList.add(gangchuPaiZu);
        gangmoShoupai = null;
    }

    public void sanbanziGangMopai(MajiangPai pai) {
        int amountOfPai = Collections.frequency(fangruShoupaiList, pai);
        for (int i = 0; i < amountOfPai; i++) {
            fangruShoupaiList.remove(pai);
        }
        shoupaiCalculator.removePai(pai, 2);
        GangchuPaiZu gangchuPaiZu = new GangchuPaiZu(new Gangzi(pai), null, id, GangType.sanbanziangangmo);
        gangchupaiZuList.add(gangchuPaiZu);
        gangmoShoupai = null;
    }

    public void gangSigeshoupai(MajiangPai pai) {
        if (guipaiTypeSet.contains(pai)) {
            for (int i = 0; i < 4; i++) {
                fangruGuipaiList.remove(pai);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                fangruShoupaiList.remove(pai);
            }
            shoupaiCalculator.removePai(pai, 4);
        }
        GangchuPaiZu gangchuPaiZu = new GangchuPaiZu(new Gangzi(pai), null, id, GangType.gangsigeshoupai);
        gangchupaiZuList.add(gangchuPaiZu);
        fangruShoupai();
    }

    public void sanbanziGangshoupai(MajiangPai pai) {
        int amountOfPai = Collections.frequency(fangruShoupaiList, pai);
        for (int i = 0; i < amountOfPai; i++) {
            fangruShoupaiList.remove(pai);
        }
        for (int i = 0; i < 4 - amountOfPai; i++) {
            kouShoupaiList.remove(pai);
        }
        shoupaiCalculator.removePai(pai, 3);
        GangchuPaiZu gangchuPaiZu = new GangchuPaiZu(new Gangzi(pai), null, id, GangType.sanbanziangangshoupai);
        gangchupaiZuList.add(gangchuPaiZu);
        fangruShoupai();
    }

    public void keziGangMopai(MajiangPai pai) {
        Iterator<PengchuPaiZu> i = pengchupaiZuList.iterator();
        String dachuPlayer = null;
        while (i.hasNext()) {
            PengchuPaiZu pengchuPai = i.next();
            if (pengchuPai.getKezi().getPaiType().equals(pai)) {
                i.remove();
                dachuPlayer = pengchuPai.getDachuPlayerId();
                break;
            }
        }
        GangchuPaiZu gangchuPaiZu = new GangchuPaiZu(new Gangzi(pai), dachuPlayer, id, GangType.kezigangmo);
        gangchupaiZuList.add(gangchuPaiZu);
        gangmoShoupai = null;
    }

    public void keziGangShoupai(MajiangPai pai) {
        Iterator<PengchuPaiZu> i = pengchupaiZuList.iterator();
        String dachuPlayer = null;
        while (i.hasNext()) {
            PengchuPaiZu pengchuPai = i.next();
            if (pengchuPai.getKezi().getPaiType().equals(pai)) {
                i.remove();
                dachuPlayer = pengchuPai.getDachuPlayerId();
                break;
            }
        }
        GangchuPaiZu gangchuPaiZu = new GangchuPaiZu(new Gangzi(pai), dachuPlayer, id, GangType.kezigangshoupai);
        gangchupaiZuList.add(gangchuPaiZu);
        fangruShoupaiList.remove(pai);
        shoupaiCalculator.removePai(pai);
        fangruShoupai();
    }

    private void removeLatestDachupai() {
        dachupaiList.remove(dachupaiList.size() - 1);
    }

    public void tryChiAndGenerateCandidateActions(String dachupaiPlayerId, MajiangPai pai) {
        if (MajiangPai.isXushupai(pai)) {
            Shunzi shunzi1 = shoupaiCalculator.tryAndMakeShunziWithPai1(pai);
            if (shunzi1 != null) {
                addActionCandidate(new MajiangChiAction(id, dachupaiPlayerId, pai, shunzi1));
            }

            Shunzi shunzi2 = shoupaiCalculator.tryAndMakeShunziWithPai2(pai);
            if (shunzi2 != null) {
                addActionCandidate(new MajiangChiAction(id, dachupaiPlayerId, pai, shunzi2));
            }

            Shunzi shunzi3 = shoupaiCalculator.tryAndMakeShunziWithPai3(pai);
            if (shunzi3 != null) {
                addActionCandidate(new MajiangChiAction(id, dachupaiPlayerId, pai, shunzi3));
            }

        }
    }

    public void taizhouMajiangTryPengAndGenerateCandidateAction(String dachupaiPlayerId, MajiangPai pai) {
        if (fangruShoupaiList.size() <= 4) {    //泰州麻将手牌小于等于4张是不能碰杠
            return;
        }
        if (tingpai) {
            return;
        }
        int count = shoupaiCalculator.count(pai);
        if (count >= 2) {
            addActionCandidate(new MajiangPengAction(id, dachupaiPlayerId, pai));
        }
    }

    public void tingNotTryPengAndGenerateCandidateAction(String dachupaiPlayerId, MajiangPai pai) {
        if (tingpai) {
            return;
        }
        int count = shoupaiCalculator.count(pai);
        if (count >= 2) {
            addActionCandidate(new MajiangPengAction(id, dachupaiPlayerId, pai));
        }
    }

    public void tryPengAndGenerateCandidateAction(String dachupaiPlayerId, MajiangPai pai) {
        int count = shoupaiCalculator.count(pai);
        if (guipaiTypeSet.contains(pai)) return;//配子(鬼牌)不能被碰
        if (count >= 2) {
            addActionCandidate(new MajiangPengAction(id, dachupaiPlayerId, pai));
        }
    }

    public void taizhouMajiangTryGangdachuAndGenerateCandidateAction(String dachupaiPlayerId, MajiangPai pai) {
        if (fangruShoupaiList.size() <= 4) {    //泰州麻将手牌小于等于4张是不能碰杠
            return;
        }
        int count = shoupaiCalculator.count(pai);
        if (count >= 3) {
            addActionCandidate(new MajiangGangAction(id, dachupaiPlayerId, pai, GangType.gangdachu));
        }
    }

    public void normalTryGangdachuAndGenerateCandidateAction(String dachupaiPlayerId, MajiangPai pai) {
        int count = shoupaiCalculator.count(pai);
        if (count >= 3) {
            addActionCandidate(new MajiangGangAction(id, dachupaiPlayerId, pai, GangType.gangdachu));
        }
    }

    public void tryGangdachuAndGenerateCandidateAction(String dachupaiPlayerId, MajiangPai pai) {
        int count = shoupaiCalculator.count(pai);
        if (guipaiTypeSet.contains(pai)) return;//配子(鬼牌)不能被杠
        if (count >= 3) {
            addActionCandidate(new MajiangGangAction(id, dachupaiPlayerId, pai, GangType.gangdachu));
        } else if (count == 2 && banziTypeSet.contains(pai)) {  //三搬子算杠
            addActionCandidate(new MajiangGangAction(id, dachupaiPlayerId, pai, GangType.sanbanziminggang));
        }
    }

    /**
     * 杠摸来的手牌 即暗杠
     */
    public void tryShoupaigangmoAndGenerateCandidateAction() {
        int count = shoupaiCalculator.count(gangmoShoupai);
        if (count >= 3) {
            addActionCandidate(new MajiangGangAction(id, null, gangmoShoupai, GangType.shoupaigangmo));
        } else if (count == 2 && banziTypeSet.contains(gangmoShoupai)) {
            addActionCandidate(new MajiangGangAction(id, null, gangmoShoupai, GangType.sanbanziangangmo));
        }
        if (fangruGuipaiList.size() == 3 && guipaiTypeSet.contains(gangmoShoupai)) {
            addActionCandidate(new MajiangGangAction(id, null, gangmoShoupai, GangType.shoupaigangmo));
        }
    }

    public void gaoyouMajiangTryShoupaigangmoAndGenerateCandidateAction() {
        int count = shoupaiCalculator.count(gangmoShoupai);
        if (count >= 3) {
            addActionCandidate(new MajiangGangAction(id, null, gangmoShoupai, GangType.shoupaigangmo));
        } else if (count == 2 && banziTypeSet.contains(gangmoShoupai)) {
            addActionCandidate(new MajiangGangAction(id, null, gangmoShoupai, GangType.sanbanziangangmo));
        }
    }

    public void taizhouMajiangTryShoupaigangmoAndGenerateCandidateAction() {
        if (fangruShoupaiList.size() <= 4) {    //泰州麻将手牌小于等于4张是不能碰杠
            return;
        }
        int count = shoupaiCalculator.count(gangmoShoupai);
        if (count >= 3) {
            addActionCandidate(new MajiangGangAction(id, null, gangmoShoupai, GangType.shoupaigangmo));
        }
    }

    public void normalTryShoupaigangmoAndGenerateCandidateAction() {
        int count = shoupaiCalculator.count(gangmoShoupai);
        if (count >= 3) {
            addActionCandidate(new MajiangGangAction(id, null, gangmoShoupai, GangType.shoupaigangmo));
        }
    }

    /**
     * 杠手上的杠牌 不算刚摸牌
     */
    public void tryGangsigeshoupaiAndGenerateCandidateAction() {
        List<MajiangPai> gangpaiList = shoupaiCalculator.findAllPaiQuantityIsFour();
        if (banziTypeSet.size() > 0) {
            //三搬子算杠
            List<MajiangPai> guipaiList = new ArrayList(banziTypeSet);
            MajiangPai banziPai = guipaiList.get(0);
            int count = shoupaiCalculator.count(banziPai);
            if (count == 3) {
                addActionCandidate(new MajiangGangAction(id, null, banziPai, GangType.sanbanziangangshoupai));
            }
        }
        gangpaiList.forEach((gangpai) -> addActionCandidate(new MajiangGangAction(id, null, gangpai, GangType.gangsigeshoupai)));
        if (fangruGuipaiList.size() == 4) {
            addActionCandidate(new MajiangGangAction(id, null, fangruGuipaiList.get(0), GangType.gangsigeshoupai));
        }
    }

    public void gaoyouMajiangTryGangsigeshoupaiAndGenerateCandidateAction() {
        List<MajiangPai> gangpaiList = shoupaiCalculator.findAllPaiQuantityIsFour();
        //三搬子算杠
        List<MajiangPai> guipaiList = new ArrayList(banziTypeSet);
        MajiangPai banziPai = guipaiList.get(0);
        int count = shoupaiCalculator.count(banziPai);
        if (count == 3) {
            addActionCandidate(new MajiangGangAction(id, null, banziPai, GangType.sanbanziangangshoupai));
        }
        gangpaiList.forEach((gangpai) -> addActionCandidate(new MajiangGangAction(id, null, gangpai, GangType.gangsigeshoupai)));
    }

    public void taizhouMajiangTryGangsigeshoupaiAndGenerateCandidateAction() {
        if (fangruShoupaiList.size() <= 4) {    //泰州麻将手牌小于等于4张是不能碰杠
            return;
        }
        List<MajiangPai> gangpaiList = shoupaiCalculator.findAllPaiQuantityIsFour();
        gangpaiList.forEach((gangpai) -> addActionCandidate(new MajiangGangAction(id, null, gangpai, GangType.gangsigeshoupai)));
    }

    public void taizhouMajiangTryTingGangsigeshoupaiAndGenerateCandidateAction(MajiangPai gangpai) {
        if (fangruShoupaiList.size() <= 4) {    //泰州麻将手牌小于等于4张是不能碰杠
            return;
        }
        addActionCandidate(new MajiangGangAction(id, null, gangpai, GangType.gangsigeshoupai));
    }

    public void normalTryGangsigeshoupaiAndGenerateCandidateAction() {
        List<MajiangPai> gangpaiList = shoupaiCalculator.findAllPaiQuantityIsFour();
        gangpaiList.forEach((gangpai) -> addActionCandidate(new MajiangGangAction(id, null, gangpai, GangType.gangsigeshoupai)));
    }

    public void tryKezigangshoupaiAndGenerateCandidateAction() {
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            for (MajiangPai fangruShoupai : fangruShoupaiList) {
                if (pengchuPaiZu.getKezi().getPaiType().equals(fangruShoupai)) {
                    addActionCandidate(new MajiangGangAction(id, pengchuPaiZu.getDachuPlayerId(), fangruShoupai, GangType.kezigangshoupai));
                    break;
                }
            }
        }
    }

    public void taizhouMajiangTryKezigangshoupaiAndGenerateCandidateAction() {
        if (fangruShoupaiList.size() <= 4) {    //泰州麻将手牌小于等于4张是不能碰杠
            return;
        }
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            for (MajiangPai fangruShoupai : fangruShoupaiList) {
                if (pengchuPaiZu.getKezi().getPaiType().equals(fangruShoupai)) {
                    addActionCandidate(new MajiangGangAction(id, pengchuPaiZu.getDachuPlayerId(), fangruShoupai, GangType.kezigangshoupai));
                    break;
                }
            }
        }
    }

    public void normalTryKezigangshoupaiAndGenerateCandidateAction() {
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            for (MajiangPai fangruShoupai : fangruShoupaiList) {
                if (pengchuPaiZu.getKezi().getPaiType().equals(fangruShoupai)) {
                    addActionCandidate(new MajiangGangAction(id, pengchuPaiZu.getDachuPlayerId(), fangruShoupai, GangType.kezigangshoupai));
                    break;
                }
            }
        }
    }

    /**
     * 杠手牌 明杠
     */
    public void tryKezigangmoAndGenerateCandidateAction() {
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            if (pengchuPaiZu.getKezi().getPaiType().equals(gangmoShoupai)) {
                addActionCandidate(new MajiangGangAction(id, pengchuPaiZu.getDachuPlayerId(), gangmoShoupai, GangType.kezigangmo));
                return;
            }
        }
    }

    public void normalTryKezigangmoAndGenerateCandidateAction() {
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            if (pengchuPaiZu.getKezi().getPaiType().equals(gangmoShoupai)) {
                addActionCandidate(new MajiangGangAction(id, pengchuPaiZu.getDachuPlayerId(), gangmoShoupai, GangType.kezigangmo));
                return;
            }
        }
    }

    public void checkAndGenerateGuoCandidateAction() {
        for (int i = 1; i <= actionCandidates.size(); i++) {
            MajiangPlayerAction action = actionCandidates.get(i);
            if (action.getType().equals(MajiangPlayerActionType.chi)
                    || action.getType().equals(MajiangPlayerActionType.peng)
                    || action.getType().equals(MajiangPlayerActionType.gang)
                    || action.getType().equals(MajiangPlayerActionType.hu)
                    || action.getType().equals(MajiangPlayerActionType.ting)) {
                addActionCandidate(new MajiangGuoAction(id));
                return;
            }
        }
    }

    /**
     * 刚摸的是否是鬼牌
     *
     * @return
     */
    public boolean gangmoGuipai() {
        if (gangmoShoupai != null) {
            return guipaiTypeSet.contains(gangmoShoupai);
        } else {
            return false;
        }
    }

    /**
     * 通常鬼牌即使是字牌也不加入计算
     *
     * @return
     */
    public boolean hasZipai() {
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            if (MajiangPai.isZipai(pengchuPaiZu.getKezi().getPaiType())) {
                return true;
            }
        }
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            if (MajiangPai.isZipai(gangchuPaiZu.getGangzi().getPaiType())) {
                return true;
            }
        }
        for (MajiangPai shoupai : fangruShoupaiList) {
            if (MajiangPai.isZipai(shoupai)) {
                return true;
            }
        }
        if (gangmoShoupai != null && !guipaiTypeSet.contains(gangmoShoupai)) {
            if (MajiangPai.isZipai(gangmoShoupai)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 有序数牌
     *
     * @return
     */
    public boolean hasXushupai() {
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            if (MajiangPai.isXushupai(pengchuPaiZu.getKezi().getPaiType())) {
                return true;
            }
        }
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            if (MajiangPai.isXushupai(gangchuPaiZu.getGangzi().getPaiType())) {
                return true;
            }
        }
        for (MajiangPai shoupai : fangruShoupaiList) {
            if (MajiangPai.isXushupai(shoupai)) {
                return true;
            }
        }
        if (gangmoShoupai != null && !guipaiTypeSet.contains(gangmoShoupai)) {
            if (MajiangPai.isXushupai(gangmoShoupai)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOneOfPaiInSet(Set<MajiangPai> paiSet) {
        for (ChichuPaiZu chichuPaiZu : chichupaiZuList) {
            if (paiSet.contains(chichuPaiZu.getShunzi().getPai1())) {
                return true;
            }
            if (paiSet.contains(chichuPaiZu.getShunzi().getPai2())) {
                return true;
            }
            if (paiSet.contains(chichuPaiZu.getShunzi().getPai3())) {
                return true;
            }
        }
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            if (paiSet.contains(pengchuPaiZu.getKezi().getPaiType())) {
                return true;
            }
        }
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            if (paiSet.contains(gangchuPaiZu.getGangzi().getPaiType())) {
                return true;
            }
        }
        for (MajiangPai shoupai : fangruShoupaiList) {
            if (paiSet.contains(shoupai)) {
                return true;
            }
        }
        if (gangmoShoupai != null) {
            if (paiSet.contains(gangmoShoupai)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 序数牌都是万牌，或者筒牌，或者条牌。通常鬼牌加入计算
     *
     * @return
     */
    public boolean allXushupaiInSameCategory(MajiangPai huPai) {
        Set<XushupaiCategory> cSet = new HashSet<>();
        if (huPai != null && !guipaiTypeSet.contains(huPai)) {  //不算刚摸入鬼牌的花色
            XushupaiCategory pai1XushupaiCategory = XushupaiCategory.getCategoryforXushupai(huPai);
            if (pai1XushupaiCategory != null) {
                cSet.add(pai1XushupaiCategory);
            }
        }
        for (ChichuPaiZu chichuPaiZu : chichupaiZuList) {
            XushupaiCategory pai1XushupaiCategory = XushupaiCategory.getCategoryforXushupai(chichuPaiZu.getShunzi().getPai1());
            if (pai1XushupaiCategory != null) {
                cSet.add(pai1XushupaiCategory);
            }

            XushupaiCategory pai2XushupaiCategory = XushupaiCategory.getCategoryforXushupai(chichuPaiZu.getShunzi().getPai2());
            if (pai2XushupaiCategory != null) {
                cSet.add(pai2XushupaiCategory);
            }

            XushupaiCategory pai3XushupaiCategory = XushupaiCategory.getCategoryforXushupai(chichuPaiZu.getShunzi().getPai3());
            if (pai3XushupaiCategory != null) {
                cSet.add(pai3XushupaiCategory);
            }
        }

        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(pengchuPaiZu.getKezi().getPaiType());
            if (xushupaiCategory != null) {
                cSet.add(xushupaiCategory);
            }
        }

        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(gangchuPaiZu.getGangzi().getPaiType());
            if (xushupaiCategory != null) {
                cSet.add(xushupaiCategory);
            }
        }

        for (MajiangPai shoupai : fangruShoupaiList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(shoupai);
            if (xushupaiCategory != null) {
                cSet.add(xushupaiCategory);
            }
        }
        //不算鬼牌的花色
//        for (MajiangPai guipai : fangruGuipaiList) {
//            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(guipai);
//            if (xushupaiCategory != null) {
//                cSet.add(xushupaiCategory);
//            }
//        }
        for (MajiangPai koupai : kouShoupaiList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(koupai);
            if (xushupaiCategory != null) {
                cSet.add(xushupaiCategory);
            }
        }
        return cSet.size() == 1;
    }


    /**
     * 序数牌都是万牌，或者筒牌，或者条牌。通常鬼牌即使是序数牌也不加入计算
     *
     * @return
     */
    public boolean allXushupaiWithoutGuipaiInSameCategory(MajiangPai huPai) {
        Set<XushupaiCategory> cSet = new HashSet<>();
        if (huPai != null && !guipaiTypeSet.contains(huPai)) {
            XushupaiCategory pai1XushupaiCategory = XushupaiCategory.getCategoryforXushupai(huPai);
            if (pai1XushupaiCategory != null) {
                cSet.add(pai1XushupaiCategory);
            }
        }
        for (ChichuPaiZu chichuPaiZu : chichupaiZuList) {
            XushupaiCategory pai1XushupaiCategory = XushupaiCategory
                    .getCategoryforXushupai(chichuPaiZu.getShunzi().getPai1());
            if (pai1XushupaiCategory != null) {
                cSet.add(pai1XushupaiCategory);
            }

            XushupaiCategory pai2XushupaiCategory = XushupaiCategory
                    .getCategoryforXushupai(chichuPaiZu.getShunzi().getPai2());
            if (pai2XushupaiCategory != null) {
                cSet.add(pai2XushupaiCategory);
            }

            XushupaiCategory pai3XushupaiCategory = XushupaiCategory
                    .getCategoryforXushupai(chichuPaiZu.getShunzi().getPai3());
            if (pai3XushupaiCategory != null) {
                cSet.add(pai3XushupaiCategory);
            }
        }

        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory
                    .getCategoryforXushupai(pengchuPaiZu.getKezi().getPaiType());
            if (xushupaiCategory != null) {
                cSet.add(xushupaiCategory);
            }
        }

        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory
                    .getCategoryforXushupai(gangchuPaiZu.getGangzi().getPaiType());
            if (xushupaiCategory != null) {
                cSet.add(xushupaiCategory);
            }
        }

        for (MajiangPai shoupai : fangruShoupaiList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(shoupai);
            if (xushupaiCategory != null) {
                cSet.add(xushupaiCategory);
            }
        }
        if (gangmoShoupai != null && !guipaiTypeSet.contains(gangmoShoupai)) {
            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(gangmoShoupai);
            if (xushupaiCategory != null) {
                cSet.add(xushupaiCategory);
            }
        }
        return cSet.size() == 1;
    }

    /**
     * 序数牌都是万牌，或者筒牌，或者条牌。通常鬼牌加入计算
     *
     * @return
     */
    public Map<XushupaiCategory, Integer> allXushupaiCount(MajiangPai huPai) {
        int wanCount = 0;
        int tongCount = 0;
        int tiaoCount = 0;
        Map<XushupaiCategory, Integer> xushupaiCategoryIntegerMap = new HashMap<>();
        if (huPai != null) {
            XushupaiCategory pai1XushupaiCategory = XushupaiCategory.getCategoryforXushupai(huPai);
            if (pai1XushupaiCategory != null) {
                switch (pai1XushupaiCategory) {
                    case wan:
                        wanCount++;
                        break;
                    case tiao:
                        tiaoCount++;
                        break;
                    case tong:
                        tongCount++;
                        break;
                }
            }
        }
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory
                    .getCategoryforXushupai(pengchuPaiZu.getKezi().getPaiType());
            if (xushupaiCategory != null) {
                switch (xushupaiCategory) {
                    case wan:
                        wanCount += 3;
                        break;
                    case tiao:
                        tiaoCount += 3;
                        break;
                    case tong:
                        tongCount += 3;
                        break;
                }
            }
        }
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory
                    .getCategoryforXushupai(gangchuPaiZu.getGangzi().getPaiType());
            if (xushupaiCategory != null) {
                switch (xushupaiCategory) {
                    case wan:
                        wanCount += 4;
                        break;
                    case tiao:
                        tiaoCount += 4;
                        break;
                    case tong:
                        tongCount += 4;
                        break;
                }
            }
        }
        for (MajiangPai shoupai : fangruShoupaiList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(shoupai);
            if (xushupaiCategory != null) {
                switch (xushupaiCategory) {
                    case wan:
                        wanCount++;
                        break;
                    case tiao:
                        tiaoCount++;
                        break;
                    case tong:
                        tongCount++;
                        break;
                }
            }
        }
        for (MajiangPai shoupai : kouShoupaiList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(shoupai);
            if (xushupaiCategory != null) {
                switch (xushupaiCategory) {
                    case wan:
                        wanCount++;
                        break;
                    case tiao:
                        tiaoCount++;
                        break;
                    case tong:
                        tongCount++;
                        break;
                }
            }
        }
        if (gangmoShoupai != null) {
            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(gangmoShoupai);
            if (xushupaiCategory != null) {
                switch (xushupaiCategory) {
                    case wan:
                        wanCount++;
                        break;
                    case tiao:
                        tiaoCount++;
                        break;
                    case tong:
                        tongCount++;
                        break;
                }
            }
        }
        xushupaiCategoryIntegerMap.put(XushupaiCategory.wan, wanCount);
        xushupaiCategoryIntegerMap.put(XushupaiCategory.tong, tongCount);
        xushupaiCategoryIntegerMap.put(XushupaiCategory.tiao, tiaoCount);
        return xushupaiCategoryIntegerMap;
    }

    /**
     * 序数牌都是万牌，或者筒牌，或者条牌。通常鬼牌加入计算
     *
     * @return
     */
    public Map<XushupaiCategory, Integer> allXushupaiCountForTips(MajiangPai huPai) {
        int wanCount = 0;
        int tongCount = 0;
        int tiaoCount = 0;
        Map<XushupaiCategory, Integer> xushupaiCategoryIntegerMap = new HashMap<>();
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory
                    .getCategoryforXushupai(pengchuPaiZu.getKezi().getPaiType());
            if (xushupaiCategory != null) {
                switch (xushupaiCategory) {
                    case wan:
                        wanCount += 3;
                        break;
                    case tiao:
                        tiaoCount += 3;
                        break;
                    case tong:
                        tongCount += 3;
                        break;
                }
            }
        }
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory
                    .getCategoryforXushupai(gangchuPaiZu.getGangzi().getPaiType());
            if (xushupaiCategory != null) {
                switch (xushupaiCategory) {
                    case wan:
                        wanCount += 4;
                        break;
                    case tiao:
                        tiaoCount += 4;
                        break;
                    case tong:
                        tongCount += 4;
                        break;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            wanCount += shoupaiCalculator.getPaiQuantityArray()[i];
        }
        for (int i = 9; i < 18; i++) {
            tongCount += shoupaiCalculator.getPaiQuantityArray()[i];
        }
        for (int i = 18; i < 27; i++) {
            tiaoCount += shoupaiCalculator.getPaiQuantityArray()[i];
        }
        xushupaiCategoryIntegerMap.put(XushupaiCategory.wan, wanCount);
        xushupaiCategoryIntegerMap.put(XushupaiCategory.tong, tongCount);
        xushupaiCategoryIntegerMap.put(XushupaiCategory.tiao, tiaoCount);
        return xushupaiCategoryIntegerMap;
    }


    public int countGangWithoutZhupai(XushupaiCategory zhupai) {
        int count = 0;
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory
                    .getCategoryforXushupai(gangchuPaiZu.getGangzi().getPaiType());
            if (xushupaiCategory != null && !xushupaiCategory.equals(zhupai)) {
                count++;
            }
        }
        return count;
    }


    public List<MajiangPai> findGuipaiList() {
        List<MajiangPai> guipaiShoupaiList = new ArrayList<>(fangruGuipaiList);
        if (gangmoShoupai != null && guipaiTypeSet.contains(gangmoShoupai)) {
            guipaiShoupaiList.add(gangmoShoupai);
        }
        return guipaiShoupaiList;
    }

    public int countGuipai() {
        int count = fangruGuipaiList.size();
        if (gangmoShoupai != null && guipaiTypeSet.contains(gangmoShoupai)) {
            count++;
        }
        return count;
    }

    public int countChichupaiZu() {
        return chichupaiZuList.size();
    }

    public int countPengchupaiZu() {
        return pengchupaiZuList.size();
    }

    public int countGangchupaiZu() {
        return gangchupaiZuList.size();
    }

    public int countAnGangchupaiZu() {
        int count = 0;
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            if (gangchuPaiZu.getGangType().equals(GangType.shoupaigangmo) || gangchuPaiZu.getGangType().equals(GangType.gangsigeshoupai)) {
                count++;
            }
        }
        return count;
    }

    public int countZimoMingGangchupaiZu() {
        int count = 0;
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            if (gangchuPaiZu.getGangType().equals(GangType.kezigangmo) || gangchuPaiZu.getGangType().equals(GangType.kezigangshoupai)) {
                count++;
            }
        }
        return count;
    }

    public int countDianMingGangchupaiZu() {
        int count = 0;
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            if (gangchuPaiZu.getGangType().equals(GangType.gangdachu)) {
                count++;
            }
        }
        return count;
    }

    public int countPublicPai() {
        return publicPaiList.size();
    }

    /**
     * 包含放入的鬼牌
     */
    public int countAllFangruShoupai() {
        return fangruShoupaiList.size() + fangruGuipaiList.size();
    }

    public int countDachupai() {
        return dachupaiList.size();
    }

    public boolean ifPengchu(MajiangPai paiType) {
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            if (pengchuPaiZu.getKezi().getPaiType().equals(paiType)) {
                return true;
            }
        }
        return false;
    }

    public boolean ifGangchu(MajiangPai paiType) {
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            if (gangchuPaiZu.getGangzi().getPaiType().equals(paiType)) {
                return true;
            }
        }
        return false;
    }

    public boolean ifGangchu(MajiangPai paiType, GangType gangType) {
        for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
            if (gangchuPaiZu.getGangzi().getPaiType().equals(paiType) && gangchuPaiZu.getGangType().equals(gangType)) {
                return true;
            }
            // 碰后杠牌算杠打出(扬州麻将不算分)
//            if (GangType.gangdachu.equals(gangType) &&
//                    gangchuPaiZu.getDachuPlayerId() != null &&
//                    gangchuPaiZu.getGangzi().getPaiType().equals(paiType) &&
//                    gangchuPaiZu.getGangType().equals(GangType.kezigangshoupai)) {
//                return true;
//            }
        }
        return false;
    }

    public MajiangPai fengpaiForMenfeng() {
        return MajiangPositionUtil.getFengpaiByPosition(menFeng);
    }

    public Set<MajiangPlayerActionType> collectActionCandidatesType() {
        Set<MajiangPlayerActionType> typesSet = new HashSet<>();
        for (MajiangPlayerAction xiajiaAction : actionCandidates.values()) {
            typesSet.add(xiajiaAction.getType());
        }
        return typesSet;
    }

    public boolean hasActionCandidateForType(MajiangPlayerActionType actionType) {
        for (MajiangPlayerAction xiajiaAction : actionCandidates.values()) {
            if (xiajiaAction.getType().equals(actionType)) {
                return true;
            }
        }
        return false;
    }

    public void disableActionCandidateForType(MajiangPlayerActionType actionType) {
        for (MajiangPlayerAction xiajiaAction : actionCandidates.values()) {
            if (xiajiaAction.getType().equals(actionType)) {
                xiajiaAction.setDisabledByHigherPriorityAction(true);
            }
        }
    }

    public boolean banbanGang() {
        for (GangchuPaiZu gangchuPaiZu : getGangchupaiZuList()) {
            if (gangchuPaiZu.getGangzi().getPaiType().equals(MajiangPai.baiban)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 玩家花牌四同判断
     */
    public int sitongCount() {
        int[] huapaiZu = new int[42];
        for (MajiangPai huapai : publicPaiList) {
            huapaiZu[huapai.ordinal()]++;
        }
        int sitongCount = 0;
        for (int count : huapaiZu) {
            if (count == 4) {
                sitongCount++;
            }
        }
        if (huapaiZu[34] + huapaiZu[35] + huapaiZu[36] + huapaiZu[37] == 4) {
            sitongCount++;
        }
        if (huapaiZu[38] + huapaiZu[39] + huapaiZu[40] + huapaiZu[41] == 4) {
            sitongCount++;
        }
        return sitongCount;
    }

    public void calculatePlayerTotalScore(double score) {
        playerTotalScore += score;
    }

    public XushupaiCategory calculateQuemen() {
        int wanCount = 0;
        int tongCount = 0;
        int tiaoCount = 0;
        for (MajiangPai shoupai : fangruShoupaiList) {
            XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(shoupai);
            if (xushupaiCategory != null) {
                switch (xushupaiCategory) {
                    case tong:
                        tongCount++;
                        break;
                    case tiao:
                        tiaoCount++;
                        break;
                    case wan:
                        wanCount++;
                        break;
                }
            }
        }
        if (gangmoShoupai != null) {
                XushupaiCategory xushupaiCategory = XushupaiCategory.getCategoryforXushupai(gangmoShoupai);
                if (xushupaiCategory != null) {
                    switch (xushupaiCategory) {
                        case tong:
                            tongCount++;
                            break;
                        case tiao:
                            tiaoCount++;
                            break;
                        case wan:
                            wanCount++;
                            break;
                    }
                }
        }
        XushupaiCategory min;
        int minCount;

        if (wanCount > tongCount) {
            min = XushupaiCategory.tong;
            minCount = tongCount;
        } else {
            min = XushupaiCategory.wan;
            minCount = wanCount;
        }
        if (minCount > tiaoCount) {
            return XushupaiCategory.tiao;
        } else {
            return min;
        }

    }


}
