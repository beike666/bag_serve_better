package com.example.bag_serve.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-11 17:02
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Result {

    private String fileName;

    private ArrayList<String> profits;

    private ArrayList<String> weights;

    private ArrayList<String> volumes;


}
