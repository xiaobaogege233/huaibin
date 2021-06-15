package com.dml.majiang.pan.frame;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.valueobj.PaiListValueObject;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.cursor.PaiCursor;
import com.dml.majiang.player.valueobj.MajiangPlayerValueObject;
import com.dml.majiang.position.MajiangPosition;
import com.dml.majiang.position.MajiangPositionUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PanValueObject {

    /**
     * 编号，代表一局中的第几盘
     */
    private int no;

    private List<MajiangPlayerValueObject> playerList;

    private String zhuangPlayerId;

    private PaiListValueObject avaliablePaiList;

    /**
     * 公示的鬼牌集合,不能行牌
     */
    private List<MajiangPai> publicGuipaiList;
    /**
     * 公开搬子牌型
     */
    private List<MajiangPai> publicBanziList;
    /**
     * 公开搭子跟牌型
     */
    private List<MajiangPai> publicDazigenSet;
    /**
     * 给用户看得到的等待箭头，实际等的不一定是他
     */
    private String publicWaitingPlayerId;

    /**
     * 当前活跃的那张牌的定位
     */
    private PaiCursor activePaiCursor;
    private boolean hasMopai;//有人摸牌
    private String mopaiPlayerId;//摸牌玩家id

    public PanValueObject() {
    }

    public PanValueObject(Pan pan) {
        no = pan.getNo();
        playerList = new ArrayList<>();
        pan.getMajiangPlayerIdMajiangPlayerMap().values().forEach((player) -> {
            MajiangPlayerValueObject majiangPlayerValueObject = new MajiangPlayerValueObject(player);
            if (majiangPlayerValueObject.hasMopai()) {
                hasMopai = true;
                mopaiPlayerId = majiangPlayerValueObject.getId();
            }
            playerList.add(majiangPlayerValueObject);
        });
        zhuangPlayerId = pan.getZhuangPlayerId();
        avaliablePaiList = new PaiListValueObject(pan.getAvaliablePaiList());
        publicGuipaiList = new ArrayList<>(pan.getPublicGuipaiSet());
        publicBanziList = new ArrayList<>(pan.getPublicBanziSet());
        publicWaitingPlayerId = pan.getPublicWaitingPlayerId();
        activePaiCursor = pan.getActivePaiCursor();
        publicDazigenSet = new ArrayList<>(pan.getPublicDazigenSet());
    }

    public boolean ifPlayerHu(String playerId) {
        for (MajiangPlayerValueObject player : playerList) {
            if (player.getId().equals(playerId)) {
                return player.getHu() != null;
            }
        }
        return false;
    }

    public boolean hasHu() {
        for (MajiangPlayerValueObject player : playerList) {
            if (player.getHu() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean ifPlayerGang(String playerId) {
        for (MajiangPlayerValueObject player : playerList) {
            if (player.getId().equals(playerId)) {
                return player.hasGang();
            }
        }
        return false;
    }

    public boolean hasGang() {
        for (MajiangPlayerValueObject player : playerList) {
            if (player.hasGang()) {
                return true;
            }
        }
        return false;
    }

    public MajiangPosition playerMenFeng(String playerId) {
        for (MajiangPlayerValueObject player : playerList) {
            if (player.getId().equals(playerId)) {
                return player.getMenFeng();
            }
        }
        return null;
    }

    public String findXiajiaPlayerId(String playerId) {
        MajiangPosition playerMenFeng = playerMenFeng(playerId);
        MajiangPosition xiajiaMenFeng = MajiangPositionUtil.nextPositionAntiClockwise(playerMenFeng);
        String xiajiaPlayerId = findPlayerIdByMenFeng(xiajiaMenFeng);
        while (xiajiaPlayerId == null) {
            xiajiaMenFeng = MajiangPositionUtil.nextPositionAntiClockwise(xiajiaMenFeng);
            xiajiaPlayerId = findPlayerIdByMenFeng(xiajiaMenFeng);
        }
        return xiajiaPlayerId;
    }

    private String findPlayerIdByMenFeng(MajiangPosition menFeng) {
        for (MajiangPlayerValueObject player : playerList) {
            if (player.getMenFeng().equals(menFeng)) {
                return player.getId();
            }
        }
        return null;
    }

    public List<String> allPlayerIds() {
        List<String> list = new ArrayList<>();
        for (MajiangPlayerValueObject player : playerList) {
            list.add(player.getId());
        }
        return list;
    }

    public int playerGuipaiCount(String playerId) {
        for (MajiangPlayerValueObject player : playerList) {
            if (player.getId().equals(playerId)) {
                return player.getGuipaiCount();
            }
        }
        return 0;
    }

    public MajiangPlayerValueObject findPlayer(String playerId) {
        for (MajiangPlayerValueObject player : playerList) {
            if (player.getId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }


}
