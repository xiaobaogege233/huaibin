package com.dml.majiang.player.shoupai;

import com.dml.majiang.pai.MajiangPai;

import java.util.ArrayList;
import java.util.List;

public class ShoupaiPaiXing {

    private List<ShoupaiDanpai> danpaiList;
    private List<ShoupaiDuiziZu> duiziList;
    private List<ShoupaiKeziZu> keziList;
    private List<ShoupaiGangziZu> gangziList;
    private List<ShoupaiShunziZu> shunziList;

    /**
     * 通过变换‘最后弄来的牌’在不同分组的归属，分化出不同的ShoupaiPaiXing
     *
     * @param lastActionPai
     * @return
     */
    public List<ShoupaiPaiXing> differentiateShoupaiPaiXingByLastActionPai(MajiangPai lastActionPai) {
        List<ShoupaiPaiXing> result = new ArrayList<>();
        for (ShoupaiDanpai shoupaiDanpai : danpaiList) {
            if (shoupaiDanpai.getPai().getYuanPaiType().equals(lastActionPai)) {
                shoupaiDanpai.getPai().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiDanpai.getPai().setLastActionPai(false);
            }
        }

        for (ShoupaiDuiziZu shoupaiDuiziZu : duiziList) {
            if (shoupaiDuiziZu.getPai1().getYuanPaiType().equals(lastActionPai)) {
                shoupaiDuiziZu.getPai1().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiDuiziZu.getPai1().setLastActionPai(false);
            }
            // 因为有鬼牌代，所以相同的牌也要一个个看原牌
            if (shoupaiDuiziZu.getPai2().getYuanPaiType().equals(lastActionPai)) {
                shoupaiDuiziZu.getPai2().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiDuiziZu.getPai2().setLastActionPai(false);
            }
        }

        for (ShoupaiKeziZu shoupaiKeziZu : keziList) {
            if (shoupaiKeziZu.getPai1().getYuanPaiType().equals(lastActionPai)) {
                shoupaiKeziZu.getPai1().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiKeziZu.getPai1().setLastActionPai(false);
            }
            if (shoupaiKeziZu.getPai2().getYuanPaiType().equals(lastActionPai)) {
                shoupaiKeziZu.getPai2().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiKeziZu.getPai2().setLastActionPai(false);
            }
            if (shoupaiKeziZu.getPai3().getYuanPaiType().equals(lastActionPai)) {
                shoupaiKeziZu.getPai3().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiKeziZu.getPai3().setLastActionPai(false);
            }
        }

        for (ShoupaiGangziZu shoupaiGangziZu : gangziList) {
            if (shoupaiGangziZu.getPai1().getYuanPaiType().equals(lastActionPai)) {
                shoupaiGangziZu.getPai1().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiGangziZu.getPai1().setLastActionPai(false);
            }
            if (shoupaiGangziZu.getPai2().getYuanPaiType().equals(lastActionPai)) {
                shoupaiGangziZu.getPai2().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiGangziZu.getPai2().setLastActionPai(false);
            }
            if (shoupaiGangziZu.getPai3().getYuanPaiType().equals(lastActionPai)) {
                shoupaiGangziZu.getPai3().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiGangziZu.getPai3().setLastActionPai(false);
            }
            if (shoupaiGangziZu.getPai4().getYuanPaiType().equals(lastActionPai)) {
                shoupaiGangziZu.getPai4().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiGangziZu.getPai4().setLastActionPai(false);
            }
        }

        for (ShoupaiShunziZu shoupaiShunziZu : shunziList) {
            if (shoupaiShunziZu.getPai1().getYuanPaiType().equals(lastActionPai)) {
                shoupaiShunziZu.getPai1().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiShunziZu.getPai1().setLastActionPai(false);
            }
            if (shoupaiShunziZu.getPai2().getYuanPaiType().equals(lastActionPai)) {
                shoupaiShunziZu.getPai2().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiShunziZu.getPai2().setLastActionPai(false);
            }
            if (shoupaiShunziZu.getPai3().getYuanPaiType().equals(lastActionPai)) {
                shoupaiShunziZu.getPai3().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiShunziZu.getPai3().setLastActionPai(false);
            }
        }
        return result;
    }

    /**
     * 泰州麻将 最后摸得牌放到顺子中间
     * @param lastActionPai
     * @return
     */
    public List<ShoupaiPaiXing> differentiateShoupaiPaiXingByLastActionPai2(MajiangPai lastActionPai) {
        List<ShoupaiPaiXing> result = new ArrayList<>();
        for (ShoupaiDanpai shoupaiDanpai : danpaiList) {
            if (shoupaiDanpai.getPai().getYuanPaiType().equals(lastActionPai)) {
//                shoupaiDanpai.getPai().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiDanpai.getPai().setLastActionPai(false);
            }
        }

        for (ShoupaiDuiziZu shoupaiDuiziZu : duiziList) {
            if (shoupaiDuiziZu.getPai1().getYuanPaiType().equals(lastActionPai)) {
//                shoupaiDuiziZu.getPai1().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiDuiziZu.getPai1().setLastActionPai(false);
            }
            // 因为有鬼牌代，所以相同的牌也要一个个看原牌
            if (shoupaiDuiziZu.getPai2().getYuanPaiType().equals(lastActionPai)) {
//                shoupaiDuiziZu.getPai2().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiDuiziZu.getPai2().setLastActionPai(false);
            }
        }

        for (ShoupaiKeziZu shoupaiKeziZu : keziList) {
            if (shoupaiKeziZu.getPai1().getYuanPaiType().equals(lastActionPai)) {
//                shoupaiKeziZu.getPai1().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiKeziZu.getPai1().setLastActionPai(false);
            }
            if (shoupaiKeziZu.getPai2().getYuanPaiType().equals(lastActionPai)) {
//                shoupaiKeziZu.getPai2().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiKeziZu.getPai2().setLastActionPai(false);
            }
            if (shoupaiKeziZu.getPai3().getYuanPaiType().equals(lastActionPai)) {
//                shoupaiKeziZu.getPai3().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiKeziZu.getPai3().setLastActionPai(false);
            }
        }

        for (ShoupaiGangziZu shoupaiGangziZu : gangziList) {
            if (shoupaiGangziZu.getPai1().getYuanPaiType().equals(lastActionPai)) {
//                shoupaiGangziZu.getPai1().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiGangziZu.getPai1().setLastActionPai(false);
            }
            if (shoupaiGangziZu.getPai2().getYuanPaiType().equals(lastActionPai)) {
                shoupaiGangziZu.getPai2().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiGangziZu.getPai2().setLastActionPai(false);
            }
            if (shoupaiGangziZu.getPai3().getYuanPaiType().equals(lastActionPai)) {
                shoupaiGangziZu.getPai3().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiGangziZu.getPai3().setLastActionPai(false);
            }
            if (shoupaiGangziZu.getPai4().getYuanPaiType().equals(lastActionPai)) {
//                shoupaiGangziZu.getPai4().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiGangziZu.getPai4().setLastActionPai(false);
            }
        }

        for (ShoupaiShunziZu shoupaiShunziZu : shunziList) {
            if (shoupaiShunziZu.getPai1().getYuanPaiType().equals(lastActionPai)) {
//                shoupaiShunziZu.getPai1().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiShunziZu.getPai1().setLastActionPai(false);
            }
            if (shoupaiShunziZu.getPai2().getYuanPaiType().equals(lastActionPai)) {
                shoupaiShunziZu.getPai2().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiShunziZu.getPai2().setLastActionPai(true);
            }
            if (shoupaiShunziZu.getPai3().getYuanPaiType().equals(lastActionPai)) {
//                shoupaiShunziZu.getPai3().setLastActionPai(true);
                // 输出一个
                result.add(copy());
                shoupaiShunziZu.getPai3().setLastActionPai(false);
            }
        }
        return result;
    }

    private ShoupaiPaiXing copy() {
        ShoupaiPaiXing newShoupaiPaiXing = new ShoupaiPaiXing();
        List<ShoupaiDanpai> danpaiList = new ArrayList<>();
        List<ShoupaiDuiziZu> duiziList = new ArrayList<>();
        List<ShoupaiKeziZu> keziList = new ArrayList<>();
        List<ShoupaiGangziZu> gangziList = new ArrayList<>();
        List<ShoupaiShunziZu> shunziList = new ArrayList<>();
        newShoupaiPaiXing.setDanpaiList(danpaiList);
        newShoupaiPaiXing.setDuiziList(duiziList);
        newShoupaiPaiXing.setGangziList(gangziList);
        newShoupaiPaiXing.setKeziList(keziList);
        newShoupaiPaiXing.setShunziList(shunziList);

        for (ShoupaiDanpai shoupaiDanpai : this.danpaiList) {
            danpaiList.add(shoupaiDanpai.copy());
        }
        for (ShoupaiDuiziZu shoupaiDuiziZu : this.duiziList) {
            duiziList.add(shoupaiDuiziZu.copy());
        }
        for (ShoupaiKeziZu shoupaiKeziZu : this.keziList) {
            keziList.add(shoupaiKeziZu.copy());
        }
        for (ShoupaiGangziZu shoupaiGangziZu : this.gangziList) {
            gangziList.add(shoupaiGangziZu.copy());
        }
        for (ShoupaiShunziZu shoupaiShunziZu : this.shunziList) {
            shunziList.add(shoupaiShunziZu.copy());
        }
        return newShoupaiPaiXing;
    }

    public int countShunzi() {
        return shunziList.size();
    }

    public int countKezi() {
        return keziList.size();
    }

    public int countGangzi() {
        return gangziList.size();
    }

    public boolean hasShunziWithDangTimes(String dangType, int dangTimes) {
        for (ShoupaiShunziZu shunziZu : shunziList) {
            if (shunziZu.countDangPai(dangType) == dangTimes) {
                return true;
            }
        }
        return false;
    }

    public boolean hasKeziForPaiType(MajiangPai paiType) {
        for (ShoupaiKeziZu shoupaiKeziZu : keziList) {
            if (shoupaiKeziZu.getKezi().getPaiType().equals(paiType)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasYuanPaiKeziZuForPaiType(MajiangPai paiType) {
        for (ShoupaiKeziZu shoupaiKeziZu : keziList) {
            if (shoupaiKeziZu.getKezi().getPaiType().equals(paiType) && shoupaiKeziZu.yuanPaiFenZu()) {
                return true;
            }
        }
        return false;
    }

    public ShoupaiKeziZu findFirstKeziZuForPaiType(MajiangPai paiType) {
        for (ShoupaiKeziZu shoupaiKeziZu : keziList) {
            if (shoupaiKeziZu.getKezi().getPaiType().equals(paiType)) {
                return shoupaiKeziZu;
            }
        }
        return null;
    }

    /**
     * 鬼牌当的情况下一种刻子可能有多个
     */
    public List<ShoupaiKeziZu> findAllKeziZuForPaiType(MajiangPai paiType) {
        List<ShoupaiKeziZu> result = new ArrayList<>();
        for (ShoupaiKeziZu shoupaiKeziZu : keziList) {
            if (shoupaiKeziZu.getKezi().getPaiType().equals(paiType)) {
                result.add(shoupaiKeziZu);
            }
        }
        return result;
    }

    public boolean hasGangziForPaiType(MajiangPai paiType) {
        for (ShoupaiGangziZu shoupaiGangziZu : gangziList) {
            if (shoupaiGangziZu.getGangzi().getPaiType().equals(paiType)) {
                return true;
            }
        }
        return false;
    }

    public ShoupaiGangziZu findFirstGangziZuForPaiType(MajiangPai paiType) {
        for (ShoupaiGangziZu shoupaiGangziZu : gangziList) {
            if (shoupaiGangziZu.getGangzi().getPaiType().equals(paiType)) {
                return shoupaiGangziZu;
            }
        }
        return null;
    }

    public boolean hasDuiziForPaiType(MajiangPai paiType) {
        for (ShoupaiDuiziZu shoupaiDuiziZu : duiziList) {
            if (shoupaiDuiziZu.getDuiziType().equals(paiType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有没有财神代替的对子
     */
    public boolean hasDuiziForPaiTypeNoCaishen(MajiangPai paiType) {
        for (ShoupaiDuiziZu shoupaiDuiziZu : duiziList) {
            if (shoupaiDuiziZu.yuanPaiFenZu() && shoupaiDuiziZu.getDuiziType().equals(paiType)) {
                return true;
            }
        }
        return false;
    }

    public ShoupaiDuiziZu findDuiziZuHasLastActionPai() {
        for (ShoupaiDuiziZu shoupaiDuiziZu : duiziList) {
            if (shoupaiDuiziZu.getPai1().isLastActionPai()) {
                return shoupaiDuiziZu;
            }
            if (shoupaiDuiziZu.getPai2().isLastActionPai()) {
                return shoupaiDuiziZu;
            }
        }
        return null;
    }


    public ShoupaiShunziZu findShunziZuHasLastActionPai() {
        for (ShoupaiShunziZu shoupaiShunziZu : shunziList) {
            if (shoupaiShunziZu.getPai1().isLastActionPai()) {
                return shoupaiShunziZu;
            }
            if (shoupaiShunziZu.getPai2().isLastActionPai()) {
                return shoupaiShunziZu;
            }
            if (shoupaiShunziZu.getPai3().isLastActionPai()) {
                return shoupaiShunziZu;
            }
        }
        return null;
    }

    public List<ShoupaiDanpai> getDanpaiList() {
        return danpaiList;
    }

    public void setDanpaiList(List<ShoupaiDanpai> danpaiList) {
        this.danpaiList = danpaiList;
    }

    public List<ShoupaiDuiziZu> getDuiziList() {
        return duiziList;
    }

    public void setDuiziList(List<ShoupaiDuiziZu> duiziList) {
        this.duiziList = duiziList;
    }

    public List<ShoupaiKeziZu> getKeziList() {
        return keziList;
    }

    public void setKeziList(List<ShoupaiKeziZu> keziList) {
        this.keziList = keziList;
    }

    public List<ShoupaiGangziZu> getGangziList() {
        return gangziList;
    }

    public void setGangziList(List<ShoupaiGangziZu> gangziList) {
        this.gangziList = gangziList;
    }

    public List<ShoupaiShunziZu> getShunziList() {
        return shunziList;
    }

    public void setShunziList(List<ShoupaiShunziZu> shunziList) {
        this.shunziList = shunziList;
    }

}
