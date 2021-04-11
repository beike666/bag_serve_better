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
 * @create: 2021-04-11 17:46
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors( chain = true)
public class Volume {

    @TableId(type = IdType.AUTO)
    private Integer id;

    //    数据
    private Integer volume;

    //    第几组
    private Integer team;

    //    哪个文件
    private String file;
}
