package com.example.bag_serve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bag_serve.entity.Volume;
import com.example.bag_serve.mapper.VolumeMapper;
import com.example.bag_serve.service.VolumeService;
import org.springframework.stereotype.Service;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-10 18:53
 **/
@Service
public class VolumeServiceImpl extends ServiceImpl<VolumeMapper, Volume> implements VolumeService {
}
