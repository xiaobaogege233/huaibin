package com.dml.majiang.player.chupaizu;

import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.pai.fenzu.Gangzi;

public class GangchuPaiZu {

    private Gangzi gangzi;
    private String dachuPlayerId;
    private String gangPlayerId;
    private GangType gangType;

    public GangchuPaiZu() {
    }

    public GangchuPaiZu(Gangzi gangzi, String dachuPlayerId, String gangPlayerId, GangType gangType) {
        this.gangzi = gangzi;
        this.dachuPlayerId = dachuPlayerId;
        this.gangPlayerId = gangPlayerId;
        this.gangType = gangType;
    }

    public Gangzi getGangzi() {
        return gangzi;
    }

    public void setGangzi(Gangzi gangzi) {
        this.gangzi = gangzi;
    }

    public String getDachuPlayerId() {
        return dachuPlayerId;
    }

    public void setDachuPlayerId(String dachuPlayerId) {
        this.dachuPlayerId = dachuPlayerId;
    }

    public String getGangPlayerId() {
        return gangPlayerId;
    }

    public void setGangPlayerId(String gangPlayerId) {
        this.gangPlayerId = gangPlayerId;
    }

    public GangType getGangType() {
        return gangType;
    }

    public void setGangType(GangType gangType) {
        this.gangType = gangType;
    }

}
