package com.example.bag_serve.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.File;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-11 23:16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileUtil {

    private File file;

    private String downUrl;

}
