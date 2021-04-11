package com.example.bag_serve.util;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-11 18:26
 **/
public class HandleData {

    /**
     * 分割具体某一组数据,并转为整型
     * @param divided
     * @param undivided
     */
    public void splitDataIntoInteger(ArrayList<Integer> divided, String undivided) {
        String[] split = undivided.split(",");
        for (String s : split) {
            divided.add(Integer.parseInt(s));
        }
        for (Integer integer : divided) {
            if(integer.equals("")){
                divided.remove(divided.indexOf(integer));
            }
        }
    }

    /**
     * 将数据分割成【重量，价值】的数组
     * @param profitsList
     * @param weightsList
     * @param scatterUtils
     */
    public void splitDataTwoGroup(ArrayList<Integer> profitsList,ArrayList<Integer> weightsList,ArrayList<ScatterUtil> scatterUtils) {
        for (int i = 0; i < profitsList.size(); i++) {
            ScatterUtil scatterUtil = new ScatterUtil();
            scatterUtil.setWeight(weightsList.get(i));
            scatterUtil.setProfit(profitsList.get(i));
            scatterUtils.add(scatterUtil);
        }
    }
}
