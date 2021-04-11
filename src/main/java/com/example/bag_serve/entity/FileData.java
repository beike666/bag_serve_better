package com.example.bag_serve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-10 18:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors( chain = true)
public class FileData {

    @TableId(type = IdType.AUTO)
    private Integer id;

//    数据
    private String data;

//    是重量还是价值
    private Integer type;

//    第几组
    private Integer group;

//    哪个文件
    private Integer file;
}
