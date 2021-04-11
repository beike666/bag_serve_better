package com.example.bag_serve.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-03-26 10:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderUtil implements Comparable<OrderUtil>{

//    项集
    private List<ScatterUtil> item;

//    项集最后一项的比
    private Float rate;


    @Override
    public int compareTo(OrderUtil o) {
        if(this.getRate() > o.getRate()) {
            //return -1:即为正序排序
            return -1;
        }else if (this.getRate() == o.getRate()) {
            return 0;
        }else {
            //return 1: 即为倒序排序
            return 1;
        }
    }
}
