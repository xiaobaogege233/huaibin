package com.dml.majiang.player.chupaizu;

import com.dml.majiang.pai.fenzu.Kezi;

public class PengchuPaiZu {

    private Kezi kezi;
    private String dachuPlayerId;
    private String pengPlayerId;

    public PengchuPaiZu() {
    }

    public PengchuPaiZu(Kezi kezi, String dachuPlayerId, String pengPlayerId) {
        this.kezi = kezi;
        this.dachuPlayerId = dachuPlayerId;
        this.pengPlayerId = pengPlayerId;
    }

    public Kezi getKezi() {
        return kezi;
    }

    public void setKezi(Kezi kezi) {
        this.kezi = kezi;
    }

    public String getDachuPlayerId() {
        return dachuPlayerId;
    }

    public void setDachuPlayerId(String dachuPlayerId) {
        this.dachuPlayerId = dachuPlayerId;
    }

    public String getPengPlayerId() {
        return pengPlayerId;
    }

    public void setPengPlayerId(String pengPlayerId) {
        this.pengPlayerId = pengPlayerId;
    }

}
