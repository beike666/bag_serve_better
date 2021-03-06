package com.example.bag_serve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bag_serve.entity.FileData;
import com.example.bag_serve.mapper.FileDataMapper;
import com.example.bag_serve.service.FileDataService;
import org.springframework.stereotype.Service;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-10 18:53
 **/
@Service
public class FileDataServiceImpl extends ServiceImpl<FileDataMapper, FileData> implements FileDataService {
}
