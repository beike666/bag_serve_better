package com.example.bag_serve.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: 动态规划算法
 * @description
 * @author: BeiKe
 * @create: 2021-04-11 22:09
 **/
public class DynamicProgram {

    /**
     * 动态规划算法
     */
    public int dp(ArrayList<Integer> profitList, ArrayList<Integer> weightList, Integer currentVolume) {
//        组数
        int N=profitList.size()/3;
//        每一个项集的元素个数
        int S=3;
        int[][] p = new int[N+1][S];        // 价值（每S个为一组）
        int[][] w = new int[N+1][S];        // 重量（每S个为一组）
//        将数据存为动态规划需要的格式
        int index=0;
        for (int i = 1; i <= N; i++) {
            List<Integer> pi = profitList.subList(index, index + 3);
            int[] pa = pi.stream().mapToInt(Integer::intValue).toArray();
            p[i]=pa;
            List<Integer> wi = weightList.subList(index, index + 3);
            int[] wa = wi.stream().mapToInt(Integer::intValue).toArray();
            w[i]=wa;
            index=index+3;
        }
//        算法
        int[] dp = new int[currentVolume+1];
        for (int i = 1; i <= N; i++) {
            for (int j = currentVolume; j >= 0; j--) {
                for (int k = 0; k < 3; k++) {
                    if(j>=w[i][k]) {
                        dp[j] = Math.max(dp[j], dp[j - w[i][k]] + p[i][k]);
                    }
                }
            }
        }
        return dp[currentVolume];

    }
}
