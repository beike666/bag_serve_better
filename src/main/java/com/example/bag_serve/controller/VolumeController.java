package com.example.bag_serve.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bag_serve.entity.Volume;
import com.example.bag_serve.service.VolumeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-19 15:36
 **/
@RestController
@CrossOrigin
public class VolumeController {

    @Resource
    private VolumeService volumeService;

    @GetMapping("/volume/getAllData")
    public Object getAllData(HttpServletRequest request){
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String queryInfo = request.getParameter("query");
        Page<Volume> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Volume> wrapper = new QueryWrapper<>();
        if (!queryInfo.trim().equals("")) {
            wrapper.like("file",queryInfo);
        }
        IPage<Volume> volumeIPage = volumeService.page(page, wrapper);
        int count = volumeService.count(wrapper);
        List<Volume> records = volumeIPage.getRecords();
        JSONObject jsonObject = new JSONObject();
        if (records.size()>0) {
            jsonObject.put("status",200);
            jsonObject.put("data",records);
            jsonObject.put("total",count);
            return jsonObject;
        }
        jsonObject.put("status",201);
        jsonObject.put("data",null);
        jsonObject.put("total",null);
        return jsonObject;
    }
}
