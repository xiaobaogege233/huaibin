package com.anbang.qipai.huaibinmajiang.cqrs.c.domain;

import com.anbang.qipai.huaibinmajiang.cqrs.c.domain.listener.HuaibinMajiangPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanValueObject;
import com.dml.majiang.pan.result.CurrentPanResultBuilder;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.Hu;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.position.MajiangPosition;
import com.dml.majiang.position.MajiangPositionUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

/**
 * 盘结果构建
 */
@Data
public class HuaibinMajiangPanResultBuilder implements CurrentPanResultBuilder {

    private OptionalPlay optionalPlay; // 可选玩法
    private double difen; // 底分
    private Map<String, Integer> playerpiaofenMap = new HashMap<>(); // 玩家票分集

    /**
     * 构建当前盘结果
     * @param ju 当前局
     * @param panFinishTime 结束时间
     * @return
     */
    @Override
    public PanResult buildCurrentPanResult(Ju ju, long panFinishTime) {
        Pan currentPan = ju.getCurrentPan(); // 获取当前盘
        HuaibinMajiangPanResult latestFinishedPanResult = (HuaibinMajiangPanResult) ju.findLatestFinishedPanResult(); // 获取上局结果
        Map<String, Double> playerTotalScoreMap = new HashMap<>();  // 创建玩家总分集合
        // 判断上局结果是否存在
        if (latestFinishedPanResult != null) { // 存在
            // 遍历每个玩家结果 把这把总分设置到总分集合中
            for (HuaibinMajiangPanPlayerResult panPlayerResult : latestFinishedPanResult.getPanPlayerResultList()) {
                playerTotalScoreMap.put(panPlayerResult.getPlayerId(), panPlayerResult.getTotalScore());
            }
        }
        // 存不存在 都走下面
        // 获取碰杠统计监听器
        HuaibinMajiangPengGangActionStatisticsListener fangGangCounter = ju.getActionStatisticsListenerManager().findListener(HuaibinMajiangPengGangActionStatisticsListener.class); //碰杠统计检测器
        // 获取放杠数集合
        Map<String, Integer> playerFangGangMap = fangGangCounter.getPlayerIdFangGangShuMap();

        List<MajiangPlayer> huPlayers = currentPan.findAllHuPlayers(); // 获取胡的玩家集
        HuaibinMajiangPanResult tuidaohuPanResult = new HuaibinMajiangPanResult(); // 创建局结果
        tuidaohuPanResult.setPan(new PanValueObject(currentPan)); // 设置盘
        List<String> playerIdList = currentPan.sortedPlayerIdList(); // 玩家ID集合
        List<HuaibinMajiangPanPlayerResult> playerResultList = new ArrayList<>(); // 创建玩家结果集

        // 一炮多响 但是只能一个人胡
        if(huPlayers.size() > 1){
            //=======================一炮多响获取离放炮玩家最近的胡玩家胡，其他玩家不胡=================================
            String dianpaoPlayerId = huPlayers.get(0).getHu().getDianpaoPlayerId(); //点炮玩家
            Map<String, MajiangPlayer> majiangPlayerIdMajiangPlayerMap = currentPan.getMajiangPlayerIdMajiangPlayerMap();// 获取麻将玩家集合
            Map<MajiangPosition, String> menFengMajiangPlayerIdMap = currentPan.getMenFengMajiangPlayerIdMap(); // 获取门风玩家ID集合
            MajiangPosition dianpaoPlayerMenFeng = majiangPlayerIdMajiangPlayerMap.get(dianpaoPlayerId).getMenFeng(); // 获取点炮玩家门风
            MajiangPosition majiangPosition = MajiangPositionUtil.nextPositionAntiClockwise(dianpaoPlayerMenFeng); // 逆时针获取点炮玩家的下家位置
            // 声明胡玩家ID 也是实际能胡的玩家ID
            String huPlayerId = null;
            // 循环
            for (int i = 0; i < 4; i++) {
                huPlayerId = menFengMajiangPlayerIdMap.get(majiangPosition); // 通过下家玩家位置获取玩家ID
                boolean playerHu = false; // 定义标记玩家是否胡牌
                // 遍历所有胡牌玩家
                for (MajiangPlayer huPlayer : huPlayers) {
                    if (huPlayer.getId().equals(huPlayerId)) { // 胡牌玩家ID等于下家玩家ID
                        playerHu = true; // 标记置为true
                        break;
                    }
                }
                if (huPlayerId != null && playerHu) { // 下家玩家ID不为空并且可胡
                    // 结束
                    break;
                } else { // 不可胡
                    majiangPosition = MajiangPositionUtil.nextPositionAntiClockwise(majiangPosition); // 再找下下家
                }
            }
            // 遍历所有玩家
            for (MajiangPlayer player : majiangPlayerIdMajiangPlayerMap.values()) {
                if (!player.getId().equals(huPlayerId)) { // 该遍历到的玩家不等于胡玩家
                    player.setHu(null); // 将胡设置为不胡
                }
            }
            //==========================================================================================================
            MajiangPlayer panHuPlayer = majiangPlayerIdMajiangPlayerMap.get(huPlayerId); // 获取胡玩家

            // 调用构建一炮多响胡结果方法 并且返回
            return buildYipaoduoxiangPanResult(panHuPlayer,playerFangGangMap,ju,playerIdList,playerResultList,currentPan,latestFinishedPanResult,playerTotalScoreMap,tuidaohuPanResult,panFinishTime);

        }else if(huPlayers.size() == 1){ // 一个人胡
            MajiangPlayer panHuPlayer = huPlayers.get(0);
            // 调用构建单炮胡结果方法 并且返回
            return buildDanPaoPanResult(panHuPlayer,playerFangGangMap,ju,playerIdList,playerResultList,currentPan,latestFinishedPanResult,playerTotalScoreMap,tuidaohuPanResult,panFinishTime);
        }else{ //流局
            // 调用构建流局结果方法 并且返回
            return buildLiujuPanResult(playerFangGangMap,ju,playerIdList,playerResultList,currentPan,latestFinishedPanResult,playerTotalScoreMap,tuidaohuPanResult,panFinishTime);
        }
    }

    /**
     * 构建一炮多响盘结果 但是只能胡一个人
     * @param panHuPlayer 胡玩家
     * @param playerFangGangMap 玩家杠数集
     * @param ju 当前局
     * @param playerIdList 玩家ID集
     * @param playerResultList 玩家结果集
     * @param currentPan 当前盘
     * @param latestFinishedPanResult 上一盘结果
     * @param playerTotalScoreMap 玩家总分集
     * @param tuidaohuPanResult 当前盘结果
     * @param panFinishTime 盘结束时间
     * @return 当前盘结果
     */
    private HuaibinMajiangPanResult buildYipaoduoxiangPanResult(MajiangPlayer panHuPlayer,Map<String, Integer> playerFangGangMap,Ju ju,List<String> playerIdList, List<HuaibinMajiangPanPlayerResult> playerResultList,
                                Pan currentPan,HuaibinMajiangPanResult latestFinishedPanResult,Map<String, Double> playerTotalScoreMap,HuaibinMajiangPanResult tuidaohuPanResult,long panFinishTime){

        HuaibinMajiangHu hu =(HuaibinMajiangHu) panHuPlayer.getHu(); // 获取玩家的胡
        HuaibinMajiangHushu huPlayerHufen = hu.getHushu();
        if (hu.isDianpao()) { // 点炮胡
            // 计算点炮胡所有玩家分
            calculateDianpaoHuAllPlayerScore(hu,panHuPlayer,huPlayerHufen,playerFangGangMap,ju,playerIdList,playerResultList,currentPan);
        }
        if (hu.isQianggang()) { //抢杠胡
            // 计算抢杠胡所有玩家分
            calculateQiangGangHuAllPlayerScore(hu,panHuPlayer,huPlayerHufen,playerFangGangMap,ju,playerIdList,playerResultList,currentPan);
        }
        if (hu.isZimo()) { // 自摸胡
            calculateZimoHuAllPlayerScore(hu,panHuPlayer,huPlayerHufen,playerFangGangMap,ju,playerIdList,playerResultList,currentPan);
        }
        // 结算杠
        oneToOneCalculateGang(playerResultList,optionalPlay);
        // 结算风
        oneToOneCalculateFeng(playerResultList);
        // 计算当盘 总分 和 累计总分
        calculateCurrentPanAndTotalScore(playerResultList,latestFinishedPanResult,playerTotalScoreMap);
        tuidaohuPanResult.setPan(new PanValueObject(currentPan));
        tuidaohuPanResult.setPanFinishTime(panFinishTime);
        tuidaohuPanResult.setPanPlayerResultList(playerResultList);
        tuidaohuPanResult.setHu(true);
        tuidaohuPanResult.setZimo(hu.isZimo());
        tuidaohuPanResult.setDianpaoPlayerId(hu.getDianpaoPlayerId());
        return tuidaohuPanResult;
    }

    /**
     * 构建单炮盘结果 因为一炮多响只能胡一个人  其实跟构建一炮多响盘结果一样 调用即可
     * @param panHuPlayer 胡玩家
     * @param playerFangGangMap 玩家杠数集
     * @param ju 当前局
     * @param playerIdList 玩家ID集
     * @param playerResultList 玩家结果集
     * @param currentPan 当前盘
     * @param latestFinishedPanResult 上一盘结果
     * @param playerTotalScoreMap 玩家总分集
     * @param tuidaohuPanResult 当前盘结果
     * @param panFinishTime 盘结束时间
     * @return 当前盘结果
     */
    private HuaibinMajiangPanResult buildDanPaoPanResult(MajiangPlayer panHuPlayer,Map<String, Integer> playerFangGangMap,Ju ju,List<String> playerIdList, List<HuaibinMajiangPanPlayerResult> playerResultList,
                                                                Pan currentPan,HuaibinMajiangPanResult latestFinishedPanResult,Map<String, Double> playerTotalScoreMap,HuaibinMajiangPanResult tuidaohuPanResult,long panFinishTime){

        return this.buildYipaoduoxiangPanResult(panHuPlayer,playerFangGangMap,ju,playerIdList,playerResultList,currentPan,latestFinishedPanResult,playerTotalScoreMap,tuidaohuPanResult,panFinishTime);
    }


    /**
     * 构建流局盘结果
     * @param playerFangGangMap 玩家杠数集
     * @param ju 当前局
     * @param playerIdList 玩家ID集
     * @param playerResultList 玩家结果集
     * @param currentPan 当前盘
     * @param latestFinishedPanResult 上一盘结果
     * @param playerTotalScoreMap 玩家总分集
     * @param tuidaohuPanResult 当前盘结果
     * @param panFinishTime 盘结束时间
     * @return 当前盘结果
     */
    private HuaibinMajiangPanResult buildLiujuPanResult(Map<String, Integer> playerFangGangMap,Ju ju,List<String> playerIdList, List<HuaibinMajiangPanPlayerResult> playerResultList,
                                                         Pan currentPan,HuaibinMajiangPanResult latestFinishedPanResult,Map<String, Double> playerTotalScoreMap,HuaibinMajiangPanResult tuidaohuPanResult,long panFinishTime){

        playerIdList.forEach((playerId) -> {
            MajiangPlayer player = currentPan.findPlayerById(playerId);
            HuaibinMajiangPanPlayerResult playerResult = new HuaibinMajiangPanPlayerResult();
            playerResult.setPlayerId(playerId);
            // 计算非胡玩家分数
            playerResult.setHushu(new HuaibinMajiangHushu());
            // 计算杠分
            HuaibinMajiangGang gang = calculateGangScore(playerFangGangMap, player, ju, playerIdList);
            playerResult.setGang(gang);
            playerResultList.add(playerResult);
        });
        oneToOneCalculateGang(playerResultList,optionalPlay);  // 结算杠
        oneToOneCalculateFeng(playerResultList); // 结算风
        calculateCurrentPanAndTotalScore(playerResultList,latestFinishedPanResult,playerTotalScoreMap); // 计算当盘总分 和 累计总分
        tuidaohuPanResult.setPan(new PanValueObject(currentPan));
        tuidaohuPanResult.setPanFinishTime(panFinishTime);
        tuidaohuPanResult.setPanPlayerResultList(playerResultList);
        tuidaohuPanResult.setHu(false);
        return tuidaohuPanResult;
    }



    /**
     * 结算风
     * @param playerResultList 玩家结果集
     */
    private void oneToOneCalculateFeng(List<HuaibinMajiangPanPlayerResult> playerResultList) {
        //两两结算三黑风中发白四黑风打出风分
        for (int i = 0; i < playerResultList.size(); i++) {
            HuaibinMajiangPanPlayerResult playerResult_one = playerResultList.get(i);
            HuaibinMajiangLiangFeng liangFeng_one = playerResult_one.getLiangFeng();
            for (int j = (i + 1); j < playerResultList.size(); j++) {
                HuaibinMajiangPanPlayerResult playerResult_tow = playerResultList.get(j);
                HuaibinMajiangLiangFeng liangFeng_two = playerResult_tow.getLiangFeng();
                // 结算风
                int sanheifengCount_one = liangFeng_one.getSanheifengCount();
                int zhongfabaiCount_one = liangFeng_one.getZhongfabaiCount();
                int siheifengCount_one = liangFeng_one.getSiheifengCount();
                int dachupaifengScore_one = liangFeng_one.getDachupaifengScore();

                int sanheifengCount_two = liangFeng_two.getSanheifengCount();
                int zhongfabaiCount_two = liangFeng_two.getZhongfabaiCount();
                int siheifengCount_two = liangFeng_two.getSiheifengCount();
                int dachupaifengScore_two = liangFeng_two.getDachupaifengScore();

                liangFeng_one.jiesuan(-sanheifengCount_two * 2 - zhongfabaiCount_two * 4 - siheifengCount_two * 6 - dachupaifengScore_two);
                liangFeng_two.jiesuan(-sanheifengCount_one * 2 - zhongfabaiCount_one * 4 - siheifengCount_one * 6 - dachupaifengScore_one);
            }
        }
    }



    /**
     * 杠结算
     * @param playerResultList 玩家结果集
     */
    private void oneToOneCalculateGang(List<HuaibinMajiangPanPlayerResult> playerResultList,OptionalPlay optionalPlay){
        //两两结算暗杠明杠天杠  分别为 2 1 3 分
        for (int i = 0; i < playerResultList.size(); i++) {
            HuaibinMajiangPanPlayerResult playerResult_one = playerResultList.get(i);
            HuaibinMajiangGang gang_one = playerResult_one.getGang();
            for (int j = (i + 1); j < playerResultList.size(); j++) {
                HuaibinMajiangPanPlayerResult playerResult_tow = playerResultList.get(j);
                HuaibinMajiangGang gang_two = playerResult_tow.getGang();
                // 结算杠分
                int anGangShu_one = gang_one.getAnGangShu();
                int fangGangmingGangShu_one = gang_one.getFangGangmingGangShu();
                int zimoMingGangShu_one = gang_one.getZimoMingGangShu();
                int tianGang_one = gang_one.getTianGang();

                int anGangShu_two = gang_two.getAnGangShu();
                int fangGangmingGangShu_two = gang_two.getFangGangmingGangShu();
                int zimoMingGangShu_two = gang_two.getZimoMingGangShu();
                int tianGang_two = gang_two.getTianGang();

                if(optionalPlay.isLaojiuzui()){
                    gang_one.jiesuan(-anGangShu_two * 2 - zimoMingGangShu_two - fangGangmingGangShu_two - tianGang_two * 3);
                    gang_two.jiesuan(-anGangShu_one * 2 - zimoMingGangShu_one - fangGangmingGangShu_one - tianGang_one * 3);
                }
                if(optionalPlay.isPingtui()){
                    gang_one.jiesuan(-anGangShu_two * 4 - (zimoMingGangShu_two + fangGangmingGangShu_two) * 2 - tianGang_two * 6);
                    gang_two.jiesuan(-anGangShu_one * 4 - (zimoMingGangShu_one + fangGangmingGangShu_one) * 2 - tianGang_one * 6);
                }

            }
        }
    }

    /**
     * 计算风分
     * @param player 玩家
     * @param playerIdList 玩家ID集
     * @param optionalPlay 可选玩法
     * @return 该玩家亮风对象
     */
    private HuaibinMajiangLiangFeng calculateFengScore(MajiangPlayer player, List<String> playerIdList, OptionalPlay optionalPlay) {
        HuaibinMajiangLiangFeng liangFeng = new HuaibinMajiangLiangFeng(player);
        liangFeng.calculate(playerIdList.size(),optionalPlay,player);
        return liangFeng;
    }


    /**
     * 计算杠分
     * @param playerFangGangMap 玩家放杠集合
     * @param player 玩家
     * @param ju 当前局
     * @param playerIdList 玩家ID集
     * @return 杠对象
     */
    private HuaibinMajiangGang calculateGangScore(Map<String, Integer> playerFangGangMap,MajiangPlayer player,Ju ju,List<String> playerIdList){
        Integer fangGangCount = playerFangGangMap.get(player.getId()); // 获取玩家杠数
        // 判断是否有杠
        if (fangGangCount == null) {
            fangGangCount = 0;
        }
        HuaibinMajiangGang gang = new HuaibinMajiangGang(player, ju); // 获取玩家杠对象
        // 计算杠分
        gang.calculate(playerIdList.size(),optionalPlay);
        return gang;
    }

    /**
     * 计算点炮胡所有玩家分
     * @param hu 胡对象
     * @param panHuPlayer 胡玩家
     * @param huPlayerHufen 胡分
     * @param playerFangGangMap 玩家杠数集合
     * @param ju 当前局
     * @param playerIdList 玩家ID集
     * @param playerResultList 玩家结果集
     * @param currentPan 当前盘
     */
    private void calculateDianpaoHuAllPlayerScore(HuaibinMajiangHu hu,MajiangPlayer panHuPlayer,HuaibinMajiangHushu huPlayerHufen, Map<String, Integer> playerFangGangMap,
                                                  Ju ju,List<String> playerIdList,List<HuaibinMajiangPanPlayerResult> playerResultList,Pan currentPan){

        HuaibinMajiangPanPlayerResult huPlayerResult = new HuaibinMajiangPanPlayerResult(); // 创建胡玩家结果对象
        huPlayerResult.setPlayerId(panHuPlayer.getId());         // 设置胡玩家ID
        huPlayerResult.setHushu(huPlayerHufen);         // 设置胡分
        // 放炮玩家输给胡家的分
        int delta = huPlayerHufen.getScore(); // 获取胡玩家胡分
        // 计算杠分
        HuaibinMajiangGang hu_gang = calculateGangScore(playerFangGangMap, panHuPlayer, ju, playerIdList);
        huPlayerResult.setGang(hu_gang); // 胡玩家结果设置杠对象
        // 计算风分
        HuaibinMajiangLiangFeng hu_liangfeng = calculateFengScore(panHuPlayer, playerIdList, optionalPlay);
        huPlayerResult.setLiangFeng(hu_liangfeng);
        // 玩家结果集添加胡玩家结果
        playerResultList.add(huPlayerResult);

        playerIdList.forEach((playerId) -> {
            if (playerId.equals(panHuPlayer.getId())) {
                // 胡家已经计算过了
            } else if (playerId.equals(hu.getDianpaoPlayerId())) { //计算点炮玩家分数
                MajiangPlayer dianpaoPlayer = currentPan.findPlayerById(playerId); // 获取点炮玩家
                HuaibinMajiangPanPlayerResult dianpaoPlayerResult = new HuaibinMajiangPanPlayerResult(); // 创建点炮玩家结果对象
                dianpaoPlayerResult.setPlayerId(playerId); // 设置点炮玩家ID
                dianpaoPlayerResult.setHushu(new HuaibinMajiangHushu()); // 设置胡分
                HuaibinMajiangHushu hufen = dianpaoPlayerResult.getHushu(); // 获取点炮玩家的胡分
                hufen.jiesuan(-delta); // 计算分 减去胡玩家的胡分
                // 计算杠分
                HuaibinMajiangGang dianpao_gang = calculateGangScore(playerFangGangMap, dianpaoPlayer, ju, playerIdList);
                dianpaoPlayerResult.setGang(dianpao_gang);
                // 计算风分
                HuaibinMajiangLiangFeng diaopao_liangfeng = calculateFengScore(dianpaoPlayer, playerIdList, optionalPlay);
                dianpaoPlayerResult.setLiangFeng(diaopao_liangfeng);

                playerResultList.add(dianpaoPlayerResult);
            } else { // 计算非胡玩家分数
                MajiangPlayer buHuPlayer = currentPan.findPlayerById(playerId); // 获取非胡玩家
                HuaibinMajiangPanPlayerResult buHuPlayerResult = new HuaibinMajiangPanPlayerResult(); // 创建不胡玩家结果对象
                buHuPlayerResult.setPlayerId(playerId); // 设置不胡玩家ID
                buHuPlayerResult.setHushu(new HuaibinMajiangHushu());  //  设置胡分
                // 计算杠分
                HuaibinMajiangGang buhu_gang = calculateGangScore(playerFangGangMap, buHuPlayer, ju, playerIdList);
                buHuPlayerResult.setGang(buhu_gang);
                // 计算风分
                HuaibinMajiangLiangFeng buhu_liangfeng = calculateFengScore(buHuPlayer, playerIdList, optionalPlay);
                buHuPlayerResult.setLiangFeng(buhu_liangfeng);

                playerResultList.add(buHuPlayerResult);
            }
        });
    }

    /**
     * 计算抢杠胡所有玩家分  其实抢杠胡也是点炮胡的一种 直接调用点炮胡即可
     * @param hu 胡对象
     * @param panHuPlayer 胡玩家
     * @param huPlayerHufen 胡分
     * @param playerFangGangMap 玩家杠数集合
     * @param ju 当前局
     * @param playerIdList 玩家ID集
     * @param playerResultList 玩家结果集
     * @param currentPan 当前盘
     */
    private void calculateQiangGangHuAllPlayerScore(HuaibinMajiangHu hu,MajiangPlayer panHuPlayer,HuaibinMajiangHushu huPlayerHufen, Map<String, Integer> playerFangGangMap,
                                                    Ju ju,List<String> playerIdList,List<HuaibinMajiangPanPlayerResult> playerResultList,Pan currentPan){

        this.calculateDianpaoHuAllPlayerScore(hu,panHuPlayer,huPlayerHufen,playerFangGangMap,ju,playerIdList,playerResultList,currentPan);

    }

    /**
     * 计算自摸胡所有玩家分
     * @param hu 胡对象
     * @param panHuPlayer 胡玩家
     * @param huPlayerHufen 胡分
     * @param playerFangGangMap 玩家杠数集合
     * @param ju 当前局
     * @param playerIdList 玩家ID集
     * @param playerResultList 玩家结果集
     * @param currentPan 当前盘
     */
    private void calculateZimoHuAllPlayerScore(HuaibinMajiangHu hu,MajiangPlayer panHuPlayer,HuaibinMajiangHushu huPlayerHufen, Map<String, Integer> playerFangGangMap,
                                               Ju ju,List<String> playerIdList,List<HuaibinMajiangPanPlayerResult> playerResultList,Pan currentPan){
        HuaibinMajiangPanPlayerResult huPlayerResult = new HuaibinMajiangPanPlayerResult(); // 创建胡玩家结果
        // 设置胡玩家ID
        huPlayerResult.setPlayerId(panHuPlayer.getId()); // 设置胡玩家ID
        int delta = huPlayerHufen.getScore(); // 声明其他人输给胡家的分
        huPlayerHufen.setScore(delta *( playerIdList.size() - 1)); // 自摸玩家胡分 * 玩家 - 1
        huPlayerResult.setHushu(huPlayerHufen); // 胡玩家结果设置胡分
        // 计算杠分
        HuaibinMajiangGang hu_gang = calculateGangScore(playerFangGangMap, panHuPlayer, ju, playerIdList);
        huPlayerResult.setGang(hu_gang);
        // 计算风分
        HuaibinMajiangLiangFeng hu_liangfeng = calculateFengScore(panHuPlayer, playerIdList, optionalPlay);
        huPlayerResult.setLiangFeng(hu_liangfeng);
        playerResultList.add(huPlayerResult);

        for (String playerId : playerIdList) {
            if (playerId.equals(panHuPlayer.getId())) {
                // 胡家已经计算过了
            } else {
                HuaibinMajiangPanPlayerResult buHuPlayerResult = new HuaibinMajiangPanPlayerResult();
                MajiangPlayer buHuPlayer = currentPan.findPlayerById(playerId);
                buHuPlayerResult.setPlayerId(playerId);
                // 计算非胡玩家分数
                buHuPlayerResult.setHushu(new HuaibinMajiangHushu());
                HuaibinMajiangHushu hufen = buHuPlayerResult.getHushu();
                hufen.jiesuan(-delta);

                // 计算杠分
                HuaibinMajiangGang buhu_gang = calculateGangScore(playerFangGangMap, buHuPlayer, ju, playerIdList);
                buHuPlayerResult.setGang(buhu_gang);
                // 风分
                HuaibinMajiangLiangFeng buhu_liangfeng = calculateFengScore(buHuPlayer, playerIdList, optionalPlay);
                buHuPlayerResult.setLiangFeng(buhu_liangfeng);
                playerResultList.add(buHuPlayerResult);
            }
        }
    }

    /**
     * 计算当盘总分 和 累计总分
     * @param playerResultList 玩家结果集
     * @param latestFinishedPanResult 上把结果
     * @param playerTotalScoreMap 玩家总分集
     */
    private void calculateCurrentPanAndTotalScore(List<HuaibinMajiangPanPlayerResult> playerResultList,HuaibinMajiangPanResult latestFinishedPanResult,
                                                  Map<String, Double> playerTotalScoreMap){
        playerResultList.forEach((playerResult) -> {
            // 计算当盘总分
            int score = playerResult.getHushu().getScore() + playerResult.getGang().getValue() + playerResult.getLiangFeng().getValue();
            playerResult.setScore(new BigDecimal(Double.toString(difen)).multiply(new BigDecimal(Double.toString(score))).doubleValue());
            // 计算累计总分
            if (latestFinishedPanResult != null) {
                playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId()) + playerResult.getScore());
            } else {
                playerResult.setTotalScore(playerResult.getScore());
            }
        });
    }

}
