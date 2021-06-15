package com.anbang.qipai.huaibinmajiang.init;

public class ProbabilityItemTable {
    ProbabilityItem[][] m = new ProbabilityItem[4][5];
    public int array_num;
    public int[] m_num;

    public ProbabilityItemTable() {
        for (int i = 0; i < m.length; i++) for (int j = 0; j < m[i].length; j++) m[i][j] = new ProbabilityItem();
        array_num = 0;
        m_num = new int[]{0, 0, 0, 0};
    }
}