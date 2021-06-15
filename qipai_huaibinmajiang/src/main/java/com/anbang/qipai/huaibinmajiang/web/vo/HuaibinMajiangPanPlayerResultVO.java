package com.anbang.qipai.huaibinmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangGang;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.HuaibinMajiangHushu;
import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.ShanxiMajiangNiao;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.huaibinmajiang.cqrs.q.dbo.ShanxiMajiangPanPlayerResultDbo;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.Shunzi;
import com.dml.majiang.player.chupaizu.ChichuPaiZu;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;
import com.dml.majiang.player.shoupai.ShoupaiDanpai;
import com.dml.majiang.player.shoupai.ShoupaiDuiziZu;
import com.dml.majiang.player.shoupai.ShoupaiGangziZu;
import com.dml.majiang.player.shoupai.ShoupaiKeziZu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;
import com.dml.majiang.player.shoupai.ShoupaiShunziZu;
import lombok.Data;

@Data
public class HuaibinMajiangPanPlayerResultVO {

    private String playerId;
    private String nickname;
    private String headimgurl;
    private boolean zhuang;
    private boolean hu;
    private boolean zimo;
    private boolean dianpao;
    private List<MajiangPai> publicPaiList;
    private List<MajiangPai> caishenList;
    private List<List<ResultShoupaiVO>> resultShoupaiZuList = new ArrayList<>();
    private List<Shunzi> shunziList = new ArrayList<>();
    private List<MajiangPai> keziTypeList = new ArrayList<>();
    private List<GangchuPaiZuVO> gangchuList = new ArrayList<>();

    private HuaibinMajiangHushuVO hushu;
    private HuaibinMajiangGangVO gang;

    private List<NiaoPaiVO> niaoPaiList = new ArrayList<>();// 抓到的鸟牌

    private int niao;// 非结算鸟

    /**
     * 这个是结算分
     */
    private double score;

    public HuaibinMajiangPanPlayerResultVO(MajiangGamePlayerDbo gamePlayerDbo, String zhuangPlayerId, boolean zimo, String dianpaoPlayerId, ShanxiMajiangPanPlayerResultDbo panPlayerResultDbo) {
        playerId = gamePlayerDbo.getPlayerId();
        nickname = gamePlayerDbo.getNickname();
        headimgurl = gamePlayerDbo.getHeadimgurl();
        if (playerId.equals(zhuangPlayerId)) {
            zhuang = true;
        }
        hu = panPlayerResultDbo.getPlayer().getHu() != null;
        publicPaiList = new ArrayList<>(panPlayerResultDbo.getPlayer().getPublicPaiList());
        HuaibinMajiangHushu huaibinMajiangHushu = panPlayerResultDbo.getPlayerResult().getHushu();
        hushu = new HuaibinMajiangHushuVO(huaibinMajiangHushu);
        HuaibinMajiangGang huaibinMajiangGang = panPlayerResultDbo.getPlayerResult().getGang();
        gang = new HuaibinMajiangGangVO(huaibinMajiangGang);
        ShanxiMajiangNiao shanxiMajiangNiao = panPlayerResultDbo.getPlayerResult().getNiao();
        if (shanxiMajiangNiao != null) {
            List<MajiangPai> zhuaPai = shanxiMajiangNiao.getZhuaPai();
            List<MajiangPai> niaoPai = shanxiMajiangNiao.getNiaoPai();
            if (zhuaPai != null && niaoPai != null) {
                for (MajiangPai pai : zhuaPai) {
                    NiaoPaiVO niaoPaiVo = new NiaoPaiVO();
                    niaoPaiVo.setPai(pai);
                    if (niaoPai.contains(pai)) {
                        niaoPaiVo.setNiaoPai(true);
                    }
                    niaoPaiList.add(niaoPaiVo);
                }
            }
            niao = shanxiMajiangNiao.getTotalScore();
            score = panPlayerResultDbo.getPlayerResult().getScore();

            List<ChichuPaiZu> chichuPaiZuList = panPlayerResultDbo.getPlayer().getChichupaiZuList();
            for (ChichuPaiZu chichuPaiZu : chichuPaiZuList) {
                shunziList.add(chichuPaiZu.getShunzi());
            }

            List<PengchuPaiZu> pengchupaiZuList = panPlayerResultDbo.getPlayer().getPengchupaiZuList();
            for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
                keziTypeList.add(pengchuPaiZu.getKezi().getPaiType());
            }

            List<GangchuPaiZu> gangchupaiZuList = panPlayerResultDbo.getPlayer().getGangchupaiZuList();
            for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
                gangchuList.add(new GangchuPaiZuVO(gangchuPaiZu));
            }

            if (hu) {
                this.zimo = zimo;
                ShoupaiPaiXing shoupaiPaiXing = panPlayerResultDbo.getPlayer().getHu().getShoupaiPaiXing();
                if (shoupaiPaiXing == null) {// 三财神胡没有牌型
                    caishenList = new ArrayList<>(panPlayerResultDbo.getPlayer().getFangruGuipaiList());
                    List<MajiangPai> shoupaiList = panPlayerResultDbo.getPlayer().getFangruShoupaiList();
                    List<ResultShoupaiVO> list = new ArrayList<>();
                    resultShoupaiZuList.add(list);
                    for (MajiangPai pai : shoupaiList) {
                        list.add(new ResultShoupaiVO(pai));
                    }
                    MajiangPai gangmoShoupai;
                    if (panPlayerResultDbo.getPlayer().getGangmoShoupai() == null) {
                        gangmoShoupai = panPlayerResultDbo.getPlayer().getGuipaiTypeList().get(0);
                    } else {
                        gangmoShoupai = panPlayerResultDbo.getPlayer().getGangmoShoupai().getPai();
                    }
                    ResultShoupaiVO lastPai = new ResultShoupaiVO(gangmoShoupai);
                    lastPai.setHupai(true);
                    if (caishenList.contains(gangmoShoupai)) {
                        lastPai.setCaishen(true);
                    }
                    list.add(lastPai);
                } else {
                    List<ShoupaiShunziZu> shunziList = shoupaiPaiXing.getShunziList();
                    for (ShoupaiShunziZu shoupaiShunziZu : shunziList) {
                        List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
                        resultShoupaiZuList.add(shoupaiList);
                        shoupaiList.add(new ResultShoupaiVO(shoupaiShunziZu.getPai1()));
                        shoupaiList.add(new ResultShoupaiVO(shoupaiShunziZu.getPai2()));
                        shoupaiList.add(new ResultShoupaiVO(shoupaiShunziZu.getPai3()));
                    }

                    List<ShoupaiKeziZu> keziList = shoupaiPaiXing.getKeziList();
                    for (ShoupaiKeziZu shoupaiKeziZu : keziList) {
                        List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
                        resultShoupaiZuList.add(shoupaiList);
                        shoupaiList.add(new ResultShoupaiVO(shoupaiKeziZu.getPai1()));
                        shoupaiList.add(new ResultShoupaiVO(shoupaiKeziZu.getPai2()));
                        shoupaiList.add(new ResultShoupaiVO(shoupaiKeziZu.getPai3()));
                    }

                    List<ShoupaiGangziZu> gangziList = shoupaiPaiXing.getGangziList();
                    for (ShoupaiGangziZu shoupaiGangziZu : gangziList) {
                        List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
                        resultShoupaiZuList.add(shoupaiList);
                        shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai1()));
                        shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai2()));
                        shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai3()));
                        shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai4()));
                    }

                    List<ShoupaiDuiziZu> duiziList = shoupaiPaiXing.getDuiziList();
                    for (ShoupaiDuiziZu shoupaiDuiziZu : duiziList) {
                        List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
                        resultShoupaiZuList.add(shoupaiList);
                        shoupaiList.add(new ResultShoupaiVO(shoupaiDuiziZu.getPai1()));
                        shoupaiList.add(new ResultShoupaiVO(shoupaiDuiziZu.getPai2()));
                    }
                    List<ShoupaiDanpai> danpaiList = shoupaiPaiXing.getDanpaiList();
                    for (ShoupaiDanpai shoupaiDanpai : danpaiList) {
                        List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
                        resultShoupaiZuList.add(shoupaiList);
                        shoupaiList.add(new ResultShoupaiVO(shoupaiDanpai.getPai()));
                    }
                }
            } else {
                if (!zimo) {
                    if (playerId.equals(dianpaoPlayerId)) {
                        dianpao = true;
                    }
                }
                List<MajiangPai> shoupaiList = panPlayerResultDbo.getPlayer().getFangruShoupaiList();
                if (panPlayerResultDbo.getPlayer().getGangmoShoupai() != null) {
                    shoupaiList.add(panPlayerResultDbo.getPlayer().getGangmoShoupai().getPai());
                }
                caishenList = new ArrayList<>(panPlayerResultDbo.getPlayer().getFangruGuipaiList());
                List<ResultShoupaiVO> list = new ArrayList<>();
                resultShoupaiZuList.add(list);
                for (MajiangPai pai : shoupaiList) {
                    list.add(new ResultShoupaiVO(pai));
                }
            }
        }


    }

}
