package com.example.bag_serve.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: 回溯算法
 * @description
 * @author: BeiKe
 * @create: 2021-04-11 22:13
 **/
public class BackTracking {

    /**
     * 回溯处理数据
     * @param profitsList
     * @param weightsList
     * @param volume
     * @return
     */
    public int back(ArrayList<Integer> profitsList, ArrayList<Integer> weightsList, int volume){
        //        组数
        int N=profitsList.size()/3;
//        每一个项集的元素个数
        int S=3;
        int[][] p = new int[N+1][S];        // 价值（每S个为一组）
        int[][] w = new int[N+1][S];        // 重量（每S个为一组）
//        将数据存为动态规划需要的格式
        int index=0;
        for (int i = 0; i <= N; i++) {
            if(i==0){
                int[] head=new int[]{0,0,0};
                p[i]=head;
                w[i]=head;
            }else{
                List<Integer> pi = profitsList.subList(index, index + 3);
                int[] pa = pi.stream().mapToInt(Integer::intValue).toArray();
                p[i]=pa;
                List<Integer> wi = weightsList.subList(index, index + 3);
                int[] wa = wi.stream().mapToInt(Integer::intValue).toArray();
                w[i]=wa;
                index=index+3;
            }
        }
        ArrayList<Integer> ret=new ArrayList<>();
        int totalProfit=0;
        int totalWeight=0;
        recursion(ret,volume,p,w,totalProfit,totalWeight,0,0);
        Collections.sort(ret);
        return ret.get(ret.size()-1);
    }

    /**
     * 回溯算法
     * @param ret
     * @param volume
     * @param p
     * @param w
     * @param totalProfit
     * @param totalWeight
     * @param i
     * @param j
     */
    public void  recursion(ArrayList<Integer> ret,int volume,int[][] p,int[][] w,int totalProfit,int totalWeight,int i,int j){
        if(j!=3){
//            相当于不选当前的项集
            totalProfit=totalProfit+p[i][j];
            totalWeight=totalWeight+w[i][j];
        }


//        如果加上当前物品时总重量超过了背包容量则返回上一级
        if(totalWeight>volume){
            return;
        }

        if(i==p.length-1){
            ret.add(totalProfit);
            return;
        }

        for (int k = 0; k <4 ; k++) {
            recursion(ret,volume,p,w,totalProfit,totalWeight,i+1,k);
        }
    }

}
