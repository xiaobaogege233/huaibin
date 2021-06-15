package com.dml.majiang.player.valueobj;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.XushupaiCategory;
import com.dml.majiang.pai.valueobj.MajiangPaiValueObject;
import com.dml.majiang.player.Hu;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.chupaizu.ChichuPaiZu;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;
import com.dml.majiang.position.MajiangPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MajiangPlayerValueObject {

    private String id;
    /**
     * 门风
     */
    private MajiangPosition menFeng;

    /**
     * 已放入的手牌列表（不包含鬼牌，不包含公开牌）
     */
    private List<MajiangPai> fangruShoupaiList;

    /**
     * 已放入的鬼牌手牌列表（全部是鬼牌）
     */
    private List<MajiangPai> fangruGuipaiList;

    /**
     * 扣下的手牌
     */
    private List<MajiangPai> kouShoupaiList;

    private int totalShoupaiCount;

    /**
     * 公开的牌，不能行牌
     */
    private List<MajiangPai> publicPaiList;

    /**
     * 标示什么牌是鬼牌
     */
    private List<MajiangPai> guipaiTypeList;

    private List<MajiangPlayerAction> actionCandidates;

    /**
     * 刚摸进待处理的手牌（未放入）
     */
    private MajiangPaiValueObject gangmoShoupai;

    private int guipaiCount;

    private Map<MajiangPai, List<MajiangPai>> hupaiCandidates = new HashMap<>();

    private List<MajiangPai> kehuCandidates = new ArrayList<>();

    /**
     * 打出的牌
     */
    private List<MajiangPai> dachupaiList;

    private List<ChichuPaiZu> chichupaiZuList;
    private List<PengchuPaiZu> pengchupaiZuList;
    private List<GangchuPaiZu> gangchupaiZuList;

    private Hu hu;

    private int zhadanScore;
    /**
     * 即时结算杠分
     */
    private int gangScore = 0;
    /**
     * 是否听牌
     */
    private boolean tingpai;
    /**
     * 花牌数量
     */
    private int huapaiCount;

    /**
     * 听牌自动出牌
     */
    private boolean tingAutoDaPai;
    /**
     * 玩家花牌四同数量
     */
    private int sitongCount = 0;
    /**
     * 听牌时已打出牌索引
     */
    private int tingPaiIndex = 0;
    /**
     * 托管状态
     */
    private boolean tuoguanStatus = false;
    /**
     * 听牌后打出一张牌后可见其他玩家的手牌
     */
    private boolean tingpaikejian;
    /**
     * 定缺类型
     */
    private XushupaiCategory quemen;

    public MajiangPlayerValueObject() {
    }

    public MajiangPlayerValueObject(MajiangPlayer player) {
        id = player.getId();
        menFeng = player.getMenFeng();
        fangruShoupaiList = new ArrayList<>(player.getFangruShoupaiList());
        fangruGuipaiList = new ArrayList<>(player.getFangruGuipaiList());
        totalShoupaiCount = player.countAllFangruShoupai();
        publicPaiList = new ArrayList<>(player.getPublicPaiList());
        guipaiTypeList = new ArrayList<>(player.getGuipaiTypeSet());
        hupaiCandidates = new HashMap<>(player.getHupaiCandidates());
        kehuCandidates = new ArrayList<>(player.getKehuCandidates());
        actionCandidates = new ArrayList<>(player.getActionCandidates().values());
        kouShoupaiList = new ArrayList<>(player.getKouShoupaiList());
        if (player.getGangmoShoupai() != null) {
            gangmoShoupai = new MajiangPaiValueObject(player.getGangmoShoupai());
        }
        guipaiCount = player.countGuipai();
        dachupaiList = new ArrayList<>(player.getDachupaiList());
        chichupaiZuList = new ArrayList<>(player.getChichupaiZuList());
        pengchupaiZuList = new ArrayList<>(player.getPengchupaiZuList());
        gangchupaiZuList = new ArrayList<>(player.getGangchupaiZuList());
        hu = player.getHu();
        zhadanScore = player.getZhadanScore();
        gangScore = player.getGangScore();
        tingpai = player.isTingpai();
        tingAutoDaPai = player.isTingAutoDaPai();
        tingPaiIndex = player.getTingPaiIndex();
        tuoguanStatus = player.isTuoguanStatus();
        quemen = player.getQuemen();
//        sitongCount = player.getSitongCount();
//        tingpaikejian = player.isTingpaikejian();
//        huapaiCount = player.getPublicPaiList().size() + player.getOtherHuaScore();
    }

    public boolean hasMopai() {
        if (actionCandidates == null || actionCandidates.isEmpty()) {
            return false;
        }
        for (MajiangPlayerAction majiangPlayerAction : actionCandidates) {
            if (MajiangPlayerActionType.mo.equals(majiangPlayerAction.getType())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasGang() {
        return gangchupaiZuList != null && !gangchupaiZuList.isEmpty();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MajiangPosition getMenFeng() {
        return menFeng;
    }

    public void setMenFeng(MajiangPosition menFeng) {
        this.menFeng = menFeng;
    }

    public List<MajiangPai> getFangruShoupaiList() {
        return fangruShoupaiList;
    }

    public void setFangruShoupaiList(List<MajiangPai> fangruShoupaiList) {
        this.fangruShoupaiList = fangruShoupaiList;
    }

    public List<MajiangPai> getFangruGuipaiList() {
        return fangruGuipaiList;
    }

    public void setFangruGuipaiList(List<MajiangPai> fangruGuipaiList) {
        this.fangruGuipaiList = fangruGuipaiList;
    }

    public int getTotalShoupaiCount() {
        return totalShoupaiCount;
    }

    public void setTotalShoupaiCount(int totalShoupaiCount) {
        this.totalShoupaiCount = totalShoupaiCount;
    }

    public List<MajiangPai> getPublicPaiList() {
        return publicPaiList;
    }

    public void setPublicPaiList(List<MajiangPai> publicPaiList) {
        this.publicPaiList = publicPaiList;
    }

    public List<MajiangPai> getGuipaiTypeList() {
        return guipaiTypeList;
    }

    public void setGuipaiTypeList(List<MajiangPai> guipaiTypeList) {
        this.guipaiTypeList = guipaiTypeList;
    }

    public List<MajiangPlayerAction> getActionCandidates() {
        return actionCandidates;
    }

    public void setActionCandidates(List<MajiangPlayerAction> actionCandidates) {
        this.actionCandidates = actionCandidates;
    }

    public MajiangPaiValueObject getGangmoShoupai() {
        return gangmoShoupai;
    }

    public void setGangmoShoupai(MajiangPaiValueObject gangmoShoupai) {
        this.gangmoShoupai = gangmoShoupai;
    }

    public int getGuipaiCount() {
        return guipaiCount;
    }

    public void setGuipaiCount(int guipaiCount) {
        this.guipaiCount = guipaiCount;
    }

    public List<MajiangPai> getDachupaiList() {
        return dachupaiList;
    }

    public void setDachupaiList(List<MajiangPai> dachupaiList) {
        this.dachupaiList = dachupaiList;
    }

    public List<ChichuPaiZu> getChichupaiZuList() {
        return chichupaiZuList;
    }

    public void setChichupaiZuList(List<ChichuPaiZu> chichupaiZuList) {
        this.chichupaiZuList = chichupaiZuList;
    }

    public List<PengchuPaiZu> getPengchupaiZuList() {
        return pengchupaiZuList;
    }

    public void setPengchupaiZuList(List<PengchuPaiZu> pengchupaiZuList) {
        this.pengchupaiZuList = pengchupaiZuList;
    }

    public List<GangchuPaiZu> getGangchupaiZuList() {
        return gangchupaiZuList;
    }

    public void setGangchupaiZuList(List<GangchuPaiZu> gangchupaiZuList) {
        this.gangchupaiZuList = gangchupaiZuList;
    }

    public Hu getHu() {
        return hu;
    }

    public void setHu(Hu hu) {
        this.hu = hu;
    }

    public Map<MajiangPai, List<MajiangPai>> getHupaiCandidates() {
        return hupaiCandidates;
    }

    public void setHupaiCandidates(Map<MajiangPai, List<MajiangPai>> hupaiCandidates) {
        this.hupaiCandidates = hupaiCandidates;
    }

    public List<MajiangPai> getKehuCandidates() {
        return kehuCandidates;
    }

    public void setKehuCandidates(List<MajiangPai> kehuCandidates) {
        this.kehuCandidates = kehuCandidates;
    }

    public List<MajiangPai> getKouShoupaiList() {
        return kouShoupaiList;
    }

    public void setKouShoupaiList(List<MajiangPai> kouShoupaiList) {
        this.kouShoupaiList = kouShoupaiList;
    }

    public int getZhadanScore() {
        return zhadanScore;
    }

    public void setZhadanScore(int zhadanScore) {
        this.zhadanScore = zhadanScore;
    }

    public int getGangScore() {
        return gangScore;
    }

    public void setGangScore(int gangScore) {
        this.gangScore = gangScore;
    }

    public boolean isTingpai() {
        return tingpai;
    }

    public void setTingpai(boolean tingpai) {
        this.tingpai = tingpai;
    }

    public int getHuapaiCount() {
        return huapaiCount;
    }

    public void setHuapaiCount(int huapaiCount) {
        this.huapaiCount = huapaiCount;
    }

    public boolean isTingAutoDaPai() {
        return tingAutoDaPai;
    }

    public void setTingAutoDaPai(boolean tingAutoDaPai) {
        this.tingAutoDaPai = tingAutoDaPai;
    }

    public int getSitongCount() {
        return sitongCount;
    }

    public void setSitongCount(int sitongCount) {
        this.sitongCount = sitongCount;
    }

    public int getTingPaiIndex() {
        return tingPaiIndex;
    }

    public void setTingPaiIndex(int tingPaiIndex) {
        this.tingPaiIndex = tingPaiIndex;
    }

    public boolean isTuoguanStatus() {
        return tuoguanStatus;
    }

    public void setTuoguanStatus(boolean tuoguanStatus) {
        this.tuoguanStatus = tuoguanStatus;
    }

    public boolean isTingpaikejian() {
        return tingpaikejian;
    }

    public void setTingpaikejian(boolean tingpaikejian) {
        this.tingpaikejian = tingpaikejian;
    }

    public XushupaiCategory getQuemen() {
        return quemen;
    }

    public void setQuemen(XushupaiCategory quemen) {
        this.quemen = quemen;
    }
}
