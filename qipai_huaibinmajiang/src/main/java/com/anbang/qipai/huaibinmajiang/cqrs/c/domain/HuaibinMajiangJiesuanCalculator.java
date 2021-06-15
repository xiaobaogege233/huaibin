package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.XushupaiCategory;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;
import com.dml.majiang.player.shoupai.*;
import com.dml.majiang.player.shoupai.gouxing.GouXing;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;
import com.dml.majiang.position.MajiangPosition;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 胡结算
 */
public class HuaibinMajiangJiesuanCalculator {

    // 自摸胡
    public static HuaibinMajiangHu calculateBestZimoHu(GouXingPanHu gouXingPanHu, MajiangPlayer player, OptionalPlay optionalPlay, Pan currentPan ) {
        // 玩家获取手牌计数器
        ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
        // 获取鬼牌
        List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做
        MajiangPai hupai;
        if (!player.gangmoGuipai()) {
            shoupaiCalculator.addPai(player.getGangmoShoupai());
            // 胡牌就是刚摸的牌
            hupai=player.getGangmoShoupai();
        }else {
            hupai=guipaiList.get(guipaiList.size()-1);
        }
        //
        List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList,
                shoupaiCalculator, player, gouXingPanHu, player.getGangmoShoupai());
        if (!player.gangmoGuipai()) {
            shoupaiCalculator.removePai(player.getGangmoShoupai());
        }
        if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型
            // 要选出分数最高的牌型
            // 先计算和手牌型无关的参数
            ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player, null);
            HuaibinMajiangHushu bestScore = null;
            ShoupaiPaiXing bestHuShoupaiPaiXing = null;
            for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
                HuaibinMajiangHushu score = calculateHushu(shoupaixingWuguanJiesuancanshu, true, true,
                         optionalPlay, shoupaiPaiXing, hupai, player,currentPan);
                if (bestScore == null || bestScore.getNormalValue() < score.getNormalValue()) {
                    bestScore = score;
                    bestHuShoupaiPaiXing = shoupaiPaiXing;
                }
            }
            return new HuaibinMajiangHu(bestHuShoupaiPaiXing, bestScore);
        } else {// 不成胡
            return null;
        }
    }

    // 点炮胡
    public static HuaibinMajiangHu calculateBestDianpaoHu(GouXingPanHu gouXingPanHu, MajiangPlayer player, MajiangPai hupai,
                                                          OptionalPlay optionalPlay,Pan currentPan) {
        ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
        List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

        List<ShoupaiPaiXing> huPaiShoupaiPaiXingList;

        shoupaiCalculator.addPai(hupai);
        huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList, shoupaiCalculator, player, gouXingPanHu, hupai);
        shoupaiCalculator.removePai(hupai);

        if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型
            // 要选出分数最高的牌型
            // 先计算和手牌型无关的参数
            ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player, hupai);
            HuaibinMajiangHushu bestHushu = null;
            ShoupaiPaiXing bestHuShoupaiPaiXing = null;
            for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
                HuaibinMajiangHushu hushu = calculateHushu(shoupaixingWuguanJiesuancanshu, true, false,optionalPlay,shoupaiPaiXing,hupai, player,currentPan);
                if (bestHushu == null || bestHushu.getNormalValue() < hushu.getNormalValue()) {
                    bestHushu = hushu;
                    bestHuShoupaiPaiXing = shoupaiPaiXing;
                }
            }
            return new HuaibinMajiangHu(bestHuShoupaiPaiXing, bestHushu);
        } else {// 不成胡
            return null;
        }
    }

    // 抢杠胡 其实跟点炮胡差不多  调用即可
    public static HuaibinMajiangHu calculateBestQianggangHu(GouXingPanHu gouXingPanHu, MajiangPlayer player, MajiangPai hupai,
                                                            OptionalPlay optionalPlay,Pan currentPan) {
        return calculateBestDianpaoHu(gouXingPanHu,player,hupai,optionalPlay,currentPan);
    }

    //胡牌提示
    public static boolean calculateHu(GouXingPanHu gouXingPanHu, MajiangPlayer player, MajiangPai hupai) {
        ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
        List<MajiangPai> guipaiList = player.getFangruGuipaiList();// TODO 也可以用统计器做
        List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList, shoupaiCalculator, player, gouXingPanHu, hupai);
        if (!huPaiShoupaiPaiXingList.isEmpty()) {
            for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
                if ((shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.erwan) || shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.wuwan)
                        || shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.bawan) ||
                        shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.ertong) || shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.wutong)
                        || shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.batong) ||
                        shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.ertiao) || shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.wutiao)
                        || shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.batiao))) {
                    return false;
                }
            }

        }
        return true;
    }


    private static HuaibinMajiangHushu calculateHushu(ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu,
                                                      boolean hu, boolean zimoHu, OptionalPlay optionalPlay,
                                                      ShoupaiPaiXing shoupaiPaiXing, MajiangPai hupai,MajiangPlayer player,Pan currentPan) {
        // 创建胡分对象
        HuaibinMajiangHushu hushu = new HuaibinMajiangHushu();
        // 设置是否胡 是否自摸
        hushu.setHu(hu);
        hushu.setZimoHu(zimoHu);
        List<PengchuPaiZu> pengchupaiZuList = player.getPengchupaiZuList();
        List<GangchuPaiZu> gangchupaiZuList = player.getGangchupaiZuList();
        ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
        //==============================================================================================================
        // 判断是否勾选老九嘴
        if(optionalPlay.isLaojiuzui()){
            // 判断扣 TODO
            boolean kou = false;
            // 判断跑  跑默认胡就是跑
            hushu.setPao(true);
            // 判断摸
            hushu.setZimoHu(zimoHu);
            // 判断夹 TODO
            boolean jia = isJia(shoupaiPaiXing, hupai);
            hushu.setJia(jia);
            // 牌分
            int paiScore = calculatePaiScore(jia, hupai, optionalPlay);
            hushu.setPaifen(paiScore);


            // 判断十一张 十一张相同花色或以上
            boolean shiyizhang = isShiyizhang(pengchupaiZuList,gangchupaiZuList,shoupaiCalculator,hupai);
            hushu.setShiyizhang(shiyizhang);
            // 判断门前清
            boolean menqianqing = (shoupaiPaiXing.getDuiziList().size() * 2 +shoupaiPaiXing.getDanpaiList().size() + shoupaiPaiXing.getGangziList().size() * 4
                    + shoupaiPaiXing.getKeziList().size() * 3 + shoupaiPaiXing.getShunziList().size() * 3) >= 13;
            hushu.setMenqianqing(menqianqing);
            // 判断幺九独五 这里的手牌计算器在上面十一张的方法里已经把碰杠的牌添加进去了
            boolean yaojiuduwu = isYaojiuduwu(shoupaiCalculator, kou, shoupaiPaiXing, hupai);
            hushu.setYaojiuduwu(yaojiuduwu);
            // 判断三碰
            boolean sanpeng = pengchupaiZuList.size() == 3 && gangchupaiZuList.size() == 0;
            hushu.setSanpeng(sanpeng);
            // 判断一碰砸倒 TODO

            // 判断卅卅哩
            boolean sasali = isSasali(pengchupaiZuList, hupai);
            hushu.setSasali(sasali);
            // 判断绝张 绝张不与卅卅哩叠加
            boolean juezhang = isJuezhang(hupai, currentPan);
            hushu.setJuezhang(juezhang && !sasali);
            // 四归一
            shoupaiCalculator = player.getShoupaiCalculator();
            List<MajiangPai> shoupaiList = player.getFangruShoupaiList();
            boolean siguiyi = isSiguiyi(shoupaiCalculator, pengchupaiZuList, shoupaiList);
            hushu.setSiguiyi(siguiyi);

        }
        // 判断是否勾选的是平推
        if(optionalPlay.isPingtui()){
            // 判断夹
            boolean jia = isJia(shoupaiPaiXing, hupai);
            // 牌分
            int paiScore = calculatePaiScore(jia, hupai, optionalPlay);
            hushu.setPaifen(paiScore);
        }
        //==============================================================================================================
        return hushu;
    }

    /**
     * 计算牌分
     * @param jia 是否夹
     * @param hupai 胡的那张牌
     * @return 牌分
     */
    private static int calculatePaiScore(boolean jia, MajiangPai hupai,OptionalPlay optionalPlay) {
        int paiScore = 0;
        // 胡的那张牌是否为小夹
        boolean xiaojia = hupai.ordinal() == 2 || hupai.ordinal() == 3 || hupai.ordinal() == 5 || hupai.ordinal() == 6
                || hupai.ordinal() == 11 || hupai.ordinal() == 12 || hupai.ordinal() == 14 || hupai.ordinal() == 15
                || hupai.ordinal() == 20 || hupai.ordinal() == 21 || hupai.ordinal() == 23 || hupai.ordinal() == 24;
        // 胡的那张牌是否为大夹  下面似乎也不需要用到  感觉可以干掉这个代码
        boolean dajia = hupai.ordinal() == 1 || hupai.ordinal() == 4 || hupai.ordinal() == 7
                || hupai.ordinal() == 10 || hupai.ordinal() == 13 || hupai.ordinal() == 16
                || hupai.ordinal() == 19 || hupai.ordinal() == 22 || hupai.ordinal() == 25;

        if(optionalPlay.isLaojiuzui()){ // 为勾选老九嘴
            if(jia){
                if(xiaojia){
                    paiScore = 2;
                }else{
                    paiScore = 3;
                }
            }else{
                paiScore = 1;
            }
        }
        if(optionalPlay.isPingtui()){ // 为勾选平推
            if(jia){
                if(xiaojia){
                    paiScore = 4;
                }else{
                    paiScore = 6;
                }
            }else{
                paiScore = 2;
            }
        }
        return paiScore;
    }


    private static boolean isJia(ShoupaiPaiXing shoupaiPaiXing, MajiangPai hupai) {
        List<ShoupaiShunziZu> shunziList = shoupaiPaiXing.getShunziList();
        boolean isJia = false;
        for (ShoupaiShunziZu shoupaiShunziZu : shunziList) {
            if(shoupaiShunziZu.containsLastActionPaiAndIsJia()){
                isJia = true;
            }
        }
        return isJia;
    }

    /**
     * 是否为绝张
     * @param hupai 胡的那张牌
     * @param currentPan 当前盘
     * @return true or false
     */
    private static boolean isJuezhang(MajiangPai hupai, Pan currentPan) {
        int dachupaiCount = 0;
        for (MajiangPlayer player : currentPan.getMajiangPlayerIdMajiangPlayerMap().values()) {
            for (MajiangPai majiangPai : player.getDachupaiList()) {
                if(majiangPai.equals(hupai)){
                    dachupaiCount++;
                }
            }
        }
        // 绝张 打出牌中有三张
        return dachupaiCount == 3;
    }

    /**
     * 判断是否为卅卅哩
     * @param pengchupaiZuList 碰出牌组
     * @param hupai 胡的那张牌
     * @return true or false
     */
    private static boolean isSasali(List<PengchuPaiZu> pengchupaiZuList, MajiangPai hupai) {
        boolean hasSasali = false;
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            if(pengchuPaiZu.getKezi().getPaiType().equals(hupai)){
                hasSasali = true;
            }
        }
        // 卅卅哩
        return hasSasali;
    }

    /**
     * 判断是否为四归一
     * @param shoupaiCalculator 手牌计数器
     * @param pengchupaiZuList 碰出的牌组
     * @param shoupaiList 手牌
     * @return true or false
     */
    private static boolean isSiguiyi(ShoupaiCalculator shoupaiCalculator, List<PengchuPaiZu> pengchupaiZuList, List<MajiangPai> shoupaiList) {
        // 获取手牌有四个的牌集
        Set<MajiangPai> hasFourSamePaiSet = shoupaiList.stream().filter(majiangPai -> {
            return shoupaiCalculator.count(majiangPai) == 4;
        }).collect(Collectors.toSet());

        // 获取手牌中是否还抓取一个碰出的牌
        boolean hasPengKeziSamePai = false;
        for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
            for (MajiangPai majiangPai : shoupaiList) {
                if(pengchuPaiZu.getKezi().getPaiType().equals(majiangPai)){
                    hasPengKeziSamePai =true;
                }
            }
        }
        // 四归一 碰的牌组手上有第四张做配牌或者手牌有杠但是没杠
        return hasPengKeziSamePai || hasFourSamePaiSet.size() > 0;

    }

    /**
     * 判断是否为幺九独五
     * @param shoupaiCalculator 手牌计算器
     * @param kou 是否为扣
     * @param shoupaiPaiXing 手牌牌型
     * @param hupai 胡的那张牌
     * @return true or false
     */
    private static boolean isYaojiuduwu(ShoupaiCalculator shoupaiCalculator, boolean kou, ShoupaiPaiXing shoupaiPaiXing, MajiangPai hupai) {
        // 是否是一九风做对子
        boolean isYijiufeng = shoupaiPaiXing.hasKeziForPaiType(MajiangPai.yiwan) || shoupaiPaiXing.hasKeziForPaiType(MajiangPai.jiuwan) || shoupaiPaiXing.hasKeziForPaiType(MajiangPai.yitong) ||
                shoupaiPaiXing.hasKeziForPaiType(MajiangPai.jiutong) || shoupaiPaiXing.hasKeziForPaiType(MajiangPai.yitiao) || shoupaiPaiXing.hasKeziForPaiType(MajiangPai.jiutiao) ||
                shoupaiPaiXing.hasKeziForPaiType(MajiangPai.dongfeng) || shoupaiPaiXing.hasKeziForPaiType(MajiangPai.nanfeng) || shoupaiPaiXing.hasKeziForPaiType(MajiangPai.xifeng) ||
                shoupaiPaiXing.hasKeziForPaiType(MajiangPai.beifeng);
        // 把胡的牌放入手牌计算器
        shoupaiCalculator.addPai(hupai);
        // 手牌计算器中是否有独五
        boolean shoupaiHasduwu = (shoupaiCalculator.count(MajiangPai.wuwan) == 1 && shoupaiCalculator.count(MajiangPai.wutiao) == 0 && shoupaiCalculator.count(MajiangPai.wuwan) == 0) ||
                (shoupaiCalculator.count(MajiangPai.wuwan) == 0 && shoupaiCalculator.count(MajiangPai.wutiao) == 1 && shoupaiCalculator.count(MajiangPai.wuwan) == 0) ||
                (shoupaiCalculator.count(MajiangPai.wuwan) == 0 && shoupaiCalculator.count(MajiangPai.wutiao) == 0 && shoupaiCalculator.count(MajiangPai.wuwan) == 1);
        // 胡的那种牌是否为一九风
        boolean hupaiIsyijiufeng = hupai.ordinal() == 1 || hupai.ordinal() == 8 || hupai.ordinal() == 9 || hupai.ordinal() == 17 || hupai.ordinal() == 18 || hupai.ordinal() == 26 ||
                hupai.ordinal() == 27 || hupai.ordinal() == 28 || hupai.ordinal() == 29 || hupai.ordinal() == 30;
        // 胡的那种牌是否为五
        boolean hupaiIswu = hupai.ordinal() == 4 || hupai.ordinal() == 13 || hupai.ordinal() == 22;

        // 当有一九风做对子时 手牌只有一个五 并且 胡的那种牌为5 要扣也要满足
        if(isYijiufeng){
            return shoupaiHasduwu && hupaiIswu && kou;
        }
        // 把胡的牌移除手牌计算器
        shoupaiCalculator.removePai(hupai);
        // 不是一九风做对子时 手牌只有一个五 并且 胡的牌为单吊一九风 要扣也要满足
        return shoupaiHasduwu && hupaiIsyijiufeng && kou;
    }

    /**
     * 判断是否为十一张
     * @param pengchupaiZuList 碰出牌集
     * @param gangchupaiZuList 杠出牌集
     * @param shoupaiCalculator 手牌计算器
     * @param hupai 胡的那张牌
     * @return true or false
     */
    private static boolean isShiyizhang(List<PengchuPaiZu> pengchupaiZuList, List<GangchuPaiZu> gangchupaiZuList, ShoupaiCalculator shoupaiCalculator,MajiangPai hupai) {
        // 把碰的牌放入手牌计算器
        pengchupaiZuList.forEach(pengchuPaiZu -> {
            shoupaiCalculator.addPai(pengchuPaiZu.getKezi().getPaiType());
            shoupaiCalculator.addPai(pengchuPaiZu.getKezi().getPaiType());
            shoupaiCalculator.addPai(pengchuPaiZu.getKezi().getPaiType());
        });
        // 把杠的牌放入手牌计算器
        gangchupaiZuList.forEach(gangchuPaiZu -> {
            shoupaiCalculator.addPai(gangchuPaiZu.getGangzi().getPaiType());
            shoupaiCalculator.addPai(gangchuPaiZu.getGangzi().getPaiType());
            shoupaiCalculator.addPai(gangchuPaiZu.getGangzi().getPaiType());
            shoupaiCalculator.addPai(gangchuPaiZu.getGangzi().getPaiType());
        });
        // 把胡的牌放入手牌计算器
        shoupaiCalculator.addPai(hupai);
        // 获取不同花色的张数
        Map<XushupaiCategory, Integer> huaseCountMap = shoupaiCalculator.getAllPaiCategoryCount();
        Integer wanCount = huaseCountMap.get(XushupaiCategory.wan);
        Integer tongCount = huaseCountMap.get(XushupaiCategory.tong);
        Integer tiaoCount = huaseCountMap.get(XushupaiCategory.tiao);
        // 把胡的牌移除手牌计算器
        shoupaiCalculator.removePai(hupai);
        // 十一张就是相同花色是否有11张或以上
        return wanCount >= 11 || tongCount >= 11 || tiaoCount >= 11;
    }


    // 其实点炮,抢杠胡,也包含自摸的意思，也调用这个
    private static List<ShoupaiPaiXing> calculateZimoHuPaiShoupaiPaiXingList(List<MajiangPai> guipaiList, ShoupaiCalculator shoupaiCalculator, MajiangPlayer player, GouXingPanHu gouXingPanHu, MajiangPai huPai) {
        if (!guipaiList.isEmpty()) {// 有财神
            return calculateHuPaiShoupaiPaiXingListWithCaishen(guipaiList, shoupaiCalculator, player, gouXingPanHu, huPai);
        } else {// 没财神
            return calculateHuPaiShoupaiPaiXingListWithoutCaishen(shoupaiCalculator, player, gouXingPanHu, huPai);
        }
    }


    private static List<ShoupaiPaiXing> calculateHuPaiShoupaiPaiXingListWithoutCaishen(ShoupaiCalculator shoupaiCalculator, MajiangPlayer player, GouXingPanHu gouXingPanHu, MajiangPai huPai) {
        List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
        // 计算构型
        List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();

        int chichuShunziCount = player.countChichupaiZu();
        int pengchuKeziCount = player.countPengchupaiZu();
        int gangchuGangziCount = player.countGangchupaiZu();
        // 遍历构型
        for (GouXing gouXing : gouXingList) {
            boolean hu = gouXingPanHu.panHu(gouXing.getGouXingCode(), chichuShunziCount, pengchuKeziCount, gangchuGangziCount);
            if (hu) {
                // 计算牌型
                List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
                for (PaiXing paiXing : paiXingList) {
                    ShoupaiPaiXing shoupaiPaiXing = paiXing.generateAllBenPaiShoupaiPaiXing();
                    // 对ShoupaiPaiXing还要变换最后弄进的牌
                    List<ShoupaiPaiXing> shoupaiPaiXingListWithDifftentLastActionPaiInZu = shoupaiPaiXing.differentiateShoupaiPaiXingByLastActionPai(huPai);
                    huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingListWithDifftentLastActionPaiInZu);
                }
            }
        }
        return huPaiShoupaiPaiXingList;
    }

    private static List<ShoupaiPaiXing> calculateHuPaiShoupaiPaiXingListWithCaishen(List<MajiangPai> guipaiList,ShoupaiCalculator shoupaiCalculator,
                                                                                    MajiangPlayer player, GouXingPanHu gouXingPanHu,
                                                                                    MajiangPai huPai) {
        int chichuShunziCount = player.countChichupaiZu();
        int pengchuKeziCount = player.countPengchupaiZu();
        int gangchuGangziCount = player.countGangchupaiZu();
        List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
        MajiangPai[] paiTypesForGuipaiAct = calculatePaiTypesForGuipaiAct(player);// 鬼牌可以扮演的牌类
        // 开始循环财神各种当法，算构型
        List<ShoupaiWithGuipaiDangGouXingZu> shoupaiWithGuipaiDangGouXingZuList = calculateShoupaiWithGuipaiDangGouXingZuList(
                guipaiList, paiTypesForGuipaiAct, shoupaiCalculator);
        // 对于可胡的构型，计算出所有牌型
        for (ShoupaiWithGuipaiDangGouXingZu shoupaiWithGuipaiDangGouXingZu : shoupaiWithGuipaiDangGouXingZuList) {
            GuipaiDangPai[] guipaiDangPaiArray = shoupaiWithGuipaiDangGouXingZu.getGuipaiDangPaiArray();
            List<GouXing> gouXingList = shoupaiWithGuipaiDangGouXingZu.getGouXingList();
            for (GouXing gouXing : gouXingList) {
                boolean hu = gouXingPanHu.panHu(gouXing.getGouXingCode(), chichuShunziCount, pengchuKeziCount,
                        gangchuGangziCount);
                if (hu) {
                    // 先把所有当的鬼牌加入计算器
                    for (GuipaiDangPai guipaiDangPai : guipaiDangPaiArray) {
                        shoupaiCalculator.addPai(guipaiDangPai.getDangpai());
                    }
                    // 计算牌型
                    huPaiShoupaiPaiXingList.addAll(calculateAllShoupaiPaiXingForGouXingWithHupai(gouXing,
                            shoupaiCalculator, guipaiDangPaiArray, huPai));
                    // 再把所有当的鬼牌移出计算器
                    for (GuipaiDangPai guipaiDangPai : guipaiDangPaiArray) {
                        shoupaiCalculator.removePai(guipaiDangPai.getDangpai());
                    }
                }

            }
        }
        return huPaiShoupaiPaiXingList;
    }

    private static List<ShoupaiPaiXing> calculateAllShoupaiPaiXingForGouXingWithHupai(GouXing gouXing, ShoupaiCalculator shoupaiCalculator, GuipaiDangPai[] guipaiDangPaiArray, MajiangPai huPai) {
        boolean sancaishen = (guipaiDangPaiArray.length >= 3);
        List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
        // 计算牌型
        List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
        for (PaiXing paiXing : paiXingList) {
            List<ShoupaiPaiXing> shoupaiPaiXingList = paiXing.generateShoupaiPaiXingByDangPai(guipaiDangPaiArray);
            // 过滤暗杠或暗刻有两个财神当的
            Iterator<ShoupaiPaiXing> i = shoupaiPaiXingList.iterator();
            while (i.hasNext()) {
                ShoupaiPaiXing shoupaiPaiXing = i.next();
                for (ShoupaiKeziZu shoupaiKeziZu : shoupaiPaiXing.getKeziList()) {
                    if (shoupaiKeziZu.countGuipaiDangQitapai() > (sancaishen ? 2 : 1)) {
                        i.remove();
                        break;
                    }
                }
                for (ShoupaiGangziZu shoupaiGangziZu : shoupaiPaiXing.getGangziList()) {
                    if (shoupaiGangziZu.countGuipaiDangQitapai() > (sancaishen ? 2 : 1)) {
                        i.remove();
                        break;
                    }
                }
            }

            // 对于每一个ShoupaiPaiXing还要变换最后弄进的牌
            for (ShoupaiPaiXing shoupaiPaiXing : shoupaiPaiXingList) {
                List<ShoupaiPaiXing> shoupaiPaiXingListWithDifftentLastActionPaiInZu = shoupaiPaiXing
                        .differentiateShoupaiPaiXingByLastActionPai(huPai);
                huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingListWithDifftentLastActionPaiInZu);
            }

        }
        return huPaiShoupaiPaiXingList;
    }

    private static List<ShoupaiWithGuipaiDangGouXingZu> calculateShoupaiWithGuipaiDangGouXingZuList(//
                                                                                                    // 鬼牌当可以抽象到majiang.dml
                                                                                                    List<MajiangPai> guipaiList, MajiangPai[] paiTypesForGuipaiAct,
                                                                                                    ShoupaiCalculator shoupaiCalculator) {
        // 两个鬼牌只要套两层循环分别遍历所有的当法，三个鬼牌套三层循环，更多个鬼牌以此类推。
        // 出于通用考虑，这样写死几层循环的算法实现起来代码会很长，很不合理。
        // 这里改用一个面向通用的n个鬼牌的算法：
        // n个鬼牌，每个鬼牌都有不同的变化，这样组合起来最终就有很多种组合法。
        // 比如第一个鬼牌当一筒，第二个鬼牌当六万是一种。第一个鬼牌当九条，第二个鬼牌当南风又是一种。所以(假设总共一万种组合)可以给每种组合编号0,1,2,3,4,......,9998,9999
        // 我们的思路是，不管几个鬼牌，这种编号是扁平的（不需要考虑套几层循环），只是最大编号不同而已。最大编号问题就是总共几种组合法的问题。
        // 解总共几种组合法的问题非常简单，假设一个鬼牌有14种当法（扮演14种不同的牌），那两个鬼牌就是14*14种组合法，三个就是14*14*14种，n个就是14的n次方种。
        // 所以我们可以一趟循环走组合编号。那么现在唯一可以利用的就是编号值本身，我们需要从编号值推断出具体的组合方案。
        // 我们考虑人工罗列这些方案是怎么做的。假设两个鬼牌，可以扮演一万到九万。那第一个鬼牌先取一万，第二个鬼牌从一万开始一个个按顺序取过来取到九万，
        // 接着就是第一个鬼牌取二万，第二个鬼牌再次从一万开始一个个按顺序取过来取到九万，然后第一个鬼牌取三万......
        // 我们想想这不就是进位翻牌器吗？翻牌器也就是一个计数器，能覆盖0到n的所有数字。
        // 翻牌器的原理其实就是10进制。所以我们利用进制来实现 从编号值推断出具体的组合方案。
        // 我们还是来看下10进制的情况，假设三个鬼牌，一个组合编码，也就是一个数字,x,那他的百位的数值可以用来代表第一个鬼牌的当法，十位的数值可以用来代表第二个鬼牌的当法，
        // 个位的数值可以用来代表第三个鬼牌的当法。
        // 比如编码123,那就意味着 第一个鬼牌当二万，第二个鬼牌当三万，第三个鬼牌当四万。所以现在的问题是从一个数字中取出它百位的数值，十位的数值和个位的数值。
        // 我们来解这个问题，要知道123的百位数值也就是要知道123里面有几个100（这个100是事先算好的模），所以123除以100得到的商是1，这个1就是结果了，
        // 然后余数是23，这个23不要丢掉，这个余数去除以10得到的商恰好就是十位的值，2，个位以此类推......
        // 当然麻将他不是10进制的，不管几进制，可以证明此算法是通用的。

        List<ShoupaiWithGuipaiDangGouXingZu> shoupaiWithGuipaiDangGouXingZuList = new ArrayList<>();
        int guipaiCount = guipaiList.size();
        int maxZuheCode = (int) Math.pow(paiTypesForGuipaiAct.length, guipaiCount);
        int[] modArray = new int[guipaiCount];
        for (int i = 0; i < guipaiCount; i++) {
            modArray[i] = (int) Math.pow(paiTypesForGuipaiAct.length, guipaiCount - 1 - i);
        }
        for (int zuheCode = 0; zuheCode < maxZuheCode; zuheCode++) {
            GuipaiDangPai[] guipaiDangPaiArray = new GuipaiDangPai[guipaiCount];
            int temp = zuheCode;
            int previousGuipaiDangIdx = 0;
            for (int i = 0; i < guipaiCount; i++) {
                int mod = modArray[i];
                int shang = temp / mod;
                if (shang >= previousGuipaiDangIdx) {
                    int yu = temp % mod;
                    guipaiDangPaiArray[i] = new GuipaiDangPai(guipaiList.get(i), paiTypesForGuipaiAct[shang]);
                    temp = yu;
                    previousGuipaiDangIdx = shang;
                } else {
                    guipaiDangPaiArray = null;
                    break;
                }
            }
            if (guipaiDangPaiArray != null ) {
                // 先把所有当的鬼牌加入计算器
                for (int i = 0; i < guipaiDangPaiArray.length; i++) {
                    shoupaiCalculator.addPai(guipaiDangPaiArray[i].getDangpai());
                }
                // 计算构型
                List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();
                // 再把所有当的鬼牌移出计算器
                for (int i = 0; i < guipaiDangPaiArray.length; i++) {
                    shoupaiCalculator.removePai(guipaiDangPaiArray[i].getDangpai());
                }
                ShoupaiWithGuipaiDangGouXingZu shoupaiWithGuipaiDangGouXingZu = new ShoupaiWithGuipaiDangGouXingZu();
                shoupaiWithGuipaiDangGouXingZu.setGouXingList(gouXingList);
                GuipaiDangPai[] dangPaiArray = new GuipaiDangPai[guipaiCount];
                for (int i = 0; i < guipaiDangPaiArray.length; i++) {
                    dangPaiArray[i] = guipaiDangPaiArray[i];
                }
                shoupaiWithGuipaiDangGouXingZu.setGuipaiDangPaiArray(dangPaiArray);
                shoupaiWithGuipaiDangGouXingZuList.add(shoupaiWithGuipaiDangGouXingZu);
            }

        }
        return shoupaiWithGuipaiDangGouXingZuList;
    }

    /**
     * 鬼牌可当牌牌型
     * 只包含手中的牌以及前一张和后一张牌型
     *
     * @param player 玩家
     */
    private static MajiangPai[] calculatePaiTypesForGuipaiAct(MajiangPlayer player) {
        ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
        if (player.getGangmoShoupai() != null) {
            shoupaiCalculator.addPai(player.getGangmoShoupai());
        }
        int[] paiQuantityArray = shoupaiCalculator.getPaiQuantityArray();
        MajiangPai[] xushupaiArray = MajiangPai.xushupaiAndZipaiArray();
        Set<MajiangPai> set = new HashSet<>();
        int guipaiCount = player.getFangruGuipaiList().size();
        if (player.gangmoGuipai()) {
            guipaiCount++;
        }
        for (int i = 0; i < xushupaiArray.length; i++) {
            if (paiQuantityArray[i] != 0) {
                for (int j = 1; j <= guipaiCount; j++) {
                    if (i - j >= 0) {
                        set.add(xushupaiArray[i - j]);
                    }
                }
                set.add(xushupaiArray[i]);
                for (int j = 1; j <= guipaiCount; j++) {
                    if (i + j <= 33) {
                        set.add(xushupaiArray[i + j]);
                    }
                }
            }
        }
        MajiangPai[] paiTypesForGuipaiAct = new MajiangPai[set.size()];
        set.toArray(paiTypesForGuipaiAct);
        if (player.getGangmoShoupai() != null) {
            shoupaiCalculator.removePai(player.getGangmoShoupai());
        }
        return paiTypesForGuipaiAct;
    }


}
