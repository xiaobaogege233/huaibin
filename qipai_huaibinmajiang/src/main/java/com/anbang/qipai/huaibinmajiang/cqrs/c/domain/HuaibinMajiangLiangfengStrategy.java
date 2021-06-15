package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.liangfeng.LiangFengStrategy;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.shoupai.ShoupaiCalculator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: xiaobao
 * @Date: 2021/06/09/9:23
 * @Description: 亮风策略
 */
public class HuaibinMajiangLiangfengStrategy implements LiangFengStrategy {

    // TODO 亮风策略
    /**
     * 亮风
     * @param ju 当前局
     * @throws Exception
     */
    @Override
    public void liangfeng(Ju ju) throws Exception {

        Pan currentPan = ju.getCurrentPan();

        currentPan.getMajiangPlayerIdMajiangPlayerMap().values().forEach(player -> {
            // 每个玩家的风牌 中发白集合
            List<MajiangPai> dongfengList = player.getFangruShoupaiList().stream().filter(majiangPai -> majiangPai.ordinal() == 27).collect(Collectors.toList());
            List<MajiangPai> xifengList = player.getFangruShoupaiList().stream().filter(majiangPai -> majiangPai.ordinal() == 28).collect(Collectors.toList());
            List<MajiangPai> nanfengList = player.getFangruShoupaiList().stream().filter(majiangPai -> majiangPai.ordinal() == 29).collect(Collectors.toList());
            List<MajiangPai> beifengList = player.getFangruShoupaiList().stream().filter(majiangPai -> majiangPai.ordinal() == 30).collect(Collectors.toList());

            List<MajiangPai> hongzhongList = player.getFangruShoupaiList().stream().filter(majiangPai -> majiangPai.ordinal() == 31).collect(Collectors.toList());
            List<MajiangPai> facaiList = player.getFangruShoupaiList().stream().filter(majiangPai -> majiangPai.ordinal() == 32).collect(Collectors.toList());
            List<MajiangPai> baibanList = player.getFangruShoupaiList().stream().filter(majiangPai -> majiangPai.ordinal() == 33).collect(Collectors.toList());

            // 是否能亮四黑风
            boolean hasSiheifeng = dongfengList.size() > 0 && nanfengList.size() > 0 && xifengList.size() > 0 && beifengList.size() > 0;

            // 是否能亮三黑风
            boolean hasSanheifeng = dongfengList.size() > 0 && nanfengList.size() > 0 && xifengList.size() > 0 ||
                    dongfengList.size() > 0 && nanfengList.size() > 0 && beifengList.size() > 0 ||
                    nanfengList.size() > 0 && xifengList.size() > 0 && beifengList.size() > 0 ||
                    dongfengList.size() > 0 && xifengList.size() > 0 && beifengList.size() > 0;


            // 是否能亮中发白
            boolean hasZhongfabai = hongzhongList.size() > 0 && facaiList.size() > 0 && baibanList.size() >0;

            // 是否能亮风
            boolean hasLiangfeng = hasSiheifeng || hasSanheifeng || hasZhongfabai;

            player.setHasLiangfeng(hasLiangfeng);

            // 所有能够展示选择的亮风选项
            Map<String, List<MajiangPai>> canLiangfengpaiMap = canChoseLiangFengPaiList(player, dongfengList, xifengList, nanfengList, beifengList, hongzhongList, facaiList, baibanList, hasSiheifeng);

            player.setCanLiangfengpaiMap(canLiangfengpaiMap);

        });


    }

    private Map<String, List<MajiangPai>> canChoseLiangFengPaiList(MajiangPlayer player,List<MajiangPai> dongfengList, List<MajiangPai> xifengList, List<MajiangPai> nanfengList, List<MajiangPai> beifengList,
                                          List<MajiangPai> hongzhongList, List<MajiangPai> facaiList, List<MajiangPai> baibanList,boolean hasSiheifeng) {

        Map<String, List<MajiangPai>> canLiangfengpaiMap = player.getCanLiangfengpaiMap();
        // 下面展示的选项可能存在双重展示 暂时先不考虑
        List<MajiangPai> list = new ArrayList<>();
        // 四黑风的情况 能够展示的选项 不一定有
        if (hasSiheifeng){
            list.addAll(Arrays.asList(dongfengList.get(0),nanfengList.get(0),xifengList.get(0),beifengList.get(0)));
            canLiangfengpaiMap.put("四黑风",list);
            list.clear();
            list.addAll(Arrays.asList(dongfengList.get(0),nanfengList.get(0),xifengList.get(0)));
            canLiangfengpaiMap.put("三黑风东南西",list);
            list.clear();
            list.addAll(Arrays.asList(dongfengList.get(0),nanfengList.get(0),beifengList.get(0)));
            canLiangfengpaiMap.put("三黑风东南北",list);
            list.clear();
            list.addAll(Arrays.asList(nanfengList.get(0),xifengList.get(0),beifengList.get(0)));
            canLiangfengpaiMap.put("三黑风南西北",list);
            list.clear();
            list.addAll(Arrays.asList(dongfengList.get(0),xifengList.get(0),beifengList.get(0)));
            canLiangfengpaiMap.put("三黑风东西北",list);
            list.clear();
        }else{
            // 可能的三黑风选项 不一定有
            if (!Objects.isNull(dongfengList.get(0))){
                list.add(dongfengList.get(0));
            }
            if (!Objects.isNull(nanfengList.get(0))){
                list.add(nanfengList.get(0));
            }
            if (!Objects.isNull(xifengList.get(0))){
                list.add(xifengList.get(0));
            }
            if (!Objects.isNull(beifengList.get(0))){
                list.add(beifengList.get(0));
            }
            if (list.size() == 3){
                canLiangfengpaiMap.put("三黑风",list);
            }
            list.clear();
        }

        // 可能的中发白选项 不一定有
        if (!Objects.isNull(hongzhongList.get(0))){
            list.add(hongzhongList.get(0));
        }
        if (!Objects.isNull(facaiList.get(0))){
            list.add(facaiList.get(0));
        }
        if (!Objects.isNull(baibanList.get(0))){
            list.add(baibanList.get(0));
        }

        if (list.size() == 3){
            canLiangfengpaiMap.put("中发白",list);
        }
        list.clear();
        return canLiangfengpaiMap;
    }
}
