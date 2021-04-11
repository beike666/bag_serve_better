package com.example.bag_serve.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-03-30 09:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnswerUtil {

//    最优解
    private int answer;

//    最优解的运行时间
    private double runTime;

//    最优解的路径
    private String bestPath;
}
