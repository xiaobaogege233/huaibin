package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.anbang.qipai.huaibinmajiang.init.Hulib;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.hu.HupaiPaixingSolutionFilter;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 胡牌提示
 */
@Data
public class HuaibinMajiangHuPaiSolutionsTipsFilter implements HupaiPaixingSolutionFilter {

    private OptionalPlay optionalPlay; // 可选玩法

    @Override
    public Map<MajiangPai, List<MajiangPai>> hupaiFilter(MajiangPlayer majiangPlayer, GouXingPanHu gouXingPanHu) {
        // 创建胡牌集合 《打的牌：胡哪些牌》
        Map<MajiangPai, List<MajiangPai>> hupaiMap = new HashMap<>();
        // 获取 万 筒 条 东南西北 东发白牌类数组
        MajiangPai[] xushupaiArray = MajiangPai.xushupaiAndZipaiArray();
        // 将牌类数组转为集合
        List<MajiangPai> majiangPaiList = new ArrayList<>();
        Collections.addAll(majiangPaiList,xushupaiArray);

        MajiangPai[] majiangPais= new MajiangPai[majiangPaiList.size()];
        majiangPaiList.toArray(majiangPais);
        // 获取手牌
        List<MajiangPai> shoupaiList = majiangPlayer.getFangruShoupaiList();
        int[] shoupai = new int[34];

        for (MajiangPai pai : shoupaiList) {
            shoupai[pai.ordinal()]++;
        }

        // 这里对刚摸手牌进行判断 可以让下面碰方法调用这个方法 以便复用
        if(majiangPlayer.getGangmoShoupai() != null){
            shoupai[majiangPlayer.getGangmoShoupai().ordinal()]++;
        }

        // 遍历手牌
        for (MajiangPai majiangpaiShoupai : majiangPlayer.getFangruShoupaiList()) {
            // 创建胡哪些牌的集合
            List<MajiangPai> hupaiList = new ArrayList<>();
            // 先移除遍历到的这张牌
            shoupai[majiangpaiShoupai.ordinal()]--;
            // 产生哪些可胡的牌
            generateKehupai(hupaiList, majiangPais, shoupai, majiangPlayer.countGuipai());
            // 再添加回来
            shoupai[majiangpaiShoupai.ordinal()]++;
            // 判断胡牌集合不为空并且胡牌map中不包含该手牌
            if (!hupaiList.isEmpty() && !hupaiMap.containsKey(majiangpaiShoupai)){ // 成立
                // 有鬼牌则直接放入胡牌集合
                for (MajiangPai majiangPai : majiangPlayer.getGuipaiTypeSet()) {
                    if (!hupaiList.contains(majiangPai)) {
                        hupaiList.add(majiangPai);
                    }
                }
                // 胡牌map添加信息
                hupaiMap.put(majiangpaiShoupai, hupaiList);
            }
        }

        return hupaiMap;
    }



    @Override
    public List<MajiangPai> kehuFilter(MajiangPlayer majiangPlayer, GouXingPanHu gouXingPanHu) {
        List<MajiangPai> hupaiList = new ArrayList<>();
        MajiangPai[] xushupaiArray = MajiangPai.xushupaiAndZipaiArray();
        List<MajiangPai> majiangPaiList = new ArrayList<>();
        Collections.addAll(majiangPaiList,xushupaiArray);

        MajiangPai[] majiangPais= new MajiangPai[majiangPaiList.size()];
        majiangPaiList.toArray(majiangPais);
        List<MajiangPai> shoupaiList = majiangPlayer.getFangruShoupaiList();
        int[] shoupai = new int[34];
        for (MajiangPai pai : shoupaiList) {
            shoupai[pai.ordinal()]++;
        }
        generateKehupai(hupaiList, majiangPais, shoupai, majiangPlayer.countGuipai());
        if (!hupaiList.isEmpty()) {
            for (MajiangPai majiangPai : majiangPlayer.getGuipaiTypeSet()) {
                if (!hupaiList.contains(majiangPai)) {
                    hupaiList.add(majiangPai);
                }
            }
        }
        return hupaiList;
    }

    /**
     * 这里的碰胡牌提示跟摸提示差不多  区别在于一个是否有刚摸手牌  在摸方法里面对刚摸手牌做了个非空判断 直接调用上面方法即可
     * @param majiangPlayer
     * @param gouXingPanHu
     * @return
     */
    @Override
    public Map<MajiangPai, List<MajiangPai>> pengHupaiFilter(MajiangPlayer majiangPlayer, GouXingPanHu gouXingPanHu) {
        return this.hupaiFilter(majiangPlayer,gouXingPanHu);
    }

    /**
     * 生成可胡牌
     * @param hupaiList 胡哪些牌
     * @param majiangPais
     * @param shoupai
     * @param guipaiCount
     */
    private void generateKehupai(List<MajiangPai> hupaiList, MajiangPai[] majiangPais, int[] shoupai, int guipaiCount) {
        for (MajiangPai majiangPai : majiangPais) {
            shoupai[majiangPai.ordinal()]++;
            if (Hulib.getInstance().get_hu_info(shoupai, guipaiCount) && !hupaiList.contains(majiangPai)) {
                hupaiList.add(majiangPai);
            }
            int shoupaiCount = 0;
            for (int i : shoupai) {
                shoupaiCount += i;
            }

            shoupai[majiangPai.ordinal()]--;
        }
    }

    private static boolean kehuQidui(int[] hand_cards, int guipaiCount, int shoupaiCount) {
        if (shoupaiCount + guipaiCount != 14) {
            return false;
        }
        int duiziCount = 0;
        for (int hand_card : hand_cards) {
            if (hand_card%2==0){
                duiziCount+=(hand_card/2);
            }else {
                duiziCount+=((hand_card/2)+1);
                guipaiCount--;
            }
        }
        if (guipaiCount != 0&&guipaiCount>=0) {
            duiziCount += (guipaiCount / 2);
        }
        return duiziCount == 7;
    }

}
