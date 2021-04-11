package com.example.bag_serve.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-03-25 21:46
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Scatter {

    private String fileName;

    private Integer group;

    private Integer type;

    private String fileType;
}
