package com.example.bag_serve.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bag_serve.entity.FileData;
import com.example.bag_serve.service.FileDataService;
import com.example.bag_serve.util.HandleData;
import com.example.bag_serve.util.Scatter;
import com.example.bag_serve.util.ScatterUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.activation.FileDataSource;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-11 18:22
 **/
@RestController
@CrossOrigin
public class ScatterController {

//    存放当前组价值的字符串
    private static String profit;
//    存放当前组重量的字符串
    private static String weight;

//    存放当前组价值的整型列表
    private static ArrayList<Integer> profitList=new ArrayList<>();

//    存放当前组重量的整型列表
    private static ArrayList<Integer> weightList=new ArrayList<>();


    @Resource
    private FileDataService fileDataService;

    /**
     * 前端请求散点图数据
     * @param scatter
     * @return
     */
    @PostMapping("/get/scatter/data")
    public Object getScatterData(@RequestBody Scatter scatter) {
        JSONObject jsonObject = new JSONObject();
        int count = fileDataService.count(null);
        if(count==0){
            jsonObject.put("status",201);
            return jsonObject;
        }
        String fileName = scatter.getFileName();
        Integer group = scatter.getGroup();
        if(!fileName.equals("idkp1-10.txt")){
            if(group==11){
                jsonObject.put("status",202);
                return jsonObject;
            }
        }
        QueryWrapper<FileData> wrapper = new QueryWrapper<FileData>();
        wrapper.eq("file",fileName);
        wrapper.eq("team",group);
        List<FileData> list = fileDataService.list(wrapper);
        queryData(list);
        HandleData handleData = new HandleData();
        handleData.splitDataIntoInteger(profitList,profit);
        handleData.splitDataIntoInteger(weightList,weight);
        ArrayList<ScatterUtil> scatterUtils = new ArrayList<>();
        handleData.splitDataTwoGroup(profitList,weightList,scatterUtils);

        if (scatterUtils.size()>0) {
            jsonObject.put("status",200);
            jsonObject.put("data",scatterUtils);
            return jsonObject;
        }else {
            jsonObject.put("status",203);
            return jsonObject;
        }


    }

    /**
     * 查询数据，将数据归类
     * @param list
     */
    private void queryData(List<FileData> list) {
        for (FileData fileData : list) {
            if(fileData.getType()==0){
                weight=fileData.getData();
            }
            if(fileData.getType()==1){
                profit=fileData.getData();
            }
        }
    }
}
