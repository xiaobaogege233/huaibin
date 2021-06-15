package com.anbang.qipai.huaibinmajiang.cqrs.c.domain.test;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.avaliablepai.AvaliablePaiFiller;

import java.util.ArrayList;
import java.util.List;

public class TestLongyanMajiangRandomAvaliablePaiFiller implements AvaliablePaiFiller {

    private long seed;
    private boolean youtongzi;
    public TestLongyanMajiangRandomAvaliablePaiFiller() {
    }

    public TestLongyanMajiangRandomAvaliablePaiFiller(long seed  ) {
        this.seed = seed;
    }

    @Override
    public void fillAvaliablePai(Ju ju) throws Exception {
        MajiangPai[] allMajiangPaiArray = MajiangPai.values();
        List<MajiangPai> playPaiTypeList = new ArrayList<>();
        List<MajiangPai> allPaiList = new ArrayList<>();
//        allPaiList.add(MajiangPai.qiwan);
        allPaiList.add(MajiangPai.erwan);
        allPaiList.add(MajiangPai.santong);
        allPaiList.add(MajiangPai.liuwan);
        allPaiList.add(MajiangPai.baiban);
        allPaiList.add(MajiangPai.wutiao);
        allPaiList.add(MajiangPai.santiao);
        allPaiList.add(MajiangPai.liutiao);
        allPaiList.add(MajiangPai.batong);
        allPaiList.add(MajiangPai.santiao);
        allPaiList.add(MajiangPai.hongzhong);
        allPaiList.add(MajiangPai.yiwan);
        allPaiList.add(MajiangPai.qitong);
        allPaiList.add(MajiangPai.batong);
        allPaiList.add(MajiangPai.liutiao);
        allPaiList.add(MajiangPai.yitiao);





//        Collections.shuffle(allPaiList new Random(seed + ju.countFinishedPan()));
        ju.getCurrentPan().setAvaliablePaiList(allPaiList);
        ju.getCurrentPan().setPaiTypeList(playPaiTypeList);
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public boolean isYoutongzi() {
        return youtongzi;
    }

    public void setYoutongzi(boolean youtongzi) {
        this.youtongzi = youtongzi;
    }
}
