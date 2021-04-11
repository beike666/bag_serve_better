package com.example.bag_serve.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bag_serve.entity.FileData;
import com.example.bag_serve.entity.Volume;
import com.example.bag_serve.service.FileDataService;
import com.example.bag_serve.service.VolumeService;
import com.example.bag_serve.util.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
public class AnswerController {

//    存放当前组价值的字符串
    private static String profit;
//    存放当前组重量的字符串
    private static String weight;

//    存放当前组价值的整型列表
    private static ArrayList<Integer> profitList=new ArrayList<>();

//    存放当前组重量的整型列表
    private static ArrayList<Integer> weightList=new ArrayList<>();

//    存放当前组数据的背包容量
    private static Integer currentVolume;


    @Resource
    private FileDataService fileDataService;

    @Resource
    private VolumeService volumeService;

    /**
     * 前端请求散点图数据
     * @param scatter
     * @return
     */
    @PostMapping("/get/answer")
    public Object answer(@RequestBody Scatter scatter) {
        JSONObject jsonObject = new JSONObject();
        int count = fileDataService.count(null);
        if(count==0){
            jsonObject.put("status",201);
            return jsonObject;
        }
        String dataFileName = scatter.getFileName();
        Integer group = scatter.getGroup();
        if(!dataFileName.equals("idkp1-10.txt")){
            if(group==11){
                jsonObject.put("status",202);
                return jsonObject;
            }
        }
        handleDataBase(dataFileName, group);
        long startTime=0;
        int answer=0;
        long endTime=0;
        if(scatter.getType()==0){
//            采用动态规划算法
            startTime=System.currentTimeMillis();   //获取开始时间
            DynamicProgram dynamicProgram = new DynamicProgram();
            answer=dynamicProgram.dp(profitList,weightList,currentVolume);
            endTime=System.currentTimeMillis(); //获取结束时间
            jsonObject.put("status",200);
            jsonObject.put("answer",answer);
            jsonObject.put("runtime",endTime-startTime);
            return jsonObject;
        }else{
//            采用回溯算法
            startTime=System.currentTimeMillis();   //获取开始时间
            BackTracking backTracking = new BackTracking();
            answer=backTracking.back(profitList,weightList,currentVolume);
            endTime=System.currentTimeMillis(); //获取结束时间
            jsonObject.put("status",200);
            jsonObject.put("answer",answer);
            jsonObject.put("runtime",endTime-startTime);
            return jsonObject;
        }

    }

    /**
     * 操作数据库
     * @param fileName
     * @param group
     * @return
     */
    private void handleDataBase(String fileName, Integer group) {
//        先清空之前的数据
        profit=null;
        weight=null;
        profitList.clear();
        weightList.clear();

        QueryWrapper<FileData> wrapper = new QueryWrapper<FileData>();
        wrapper.eq("file",fileName);
        wrapper.eq("team",group);
        List<FileData> list = fileDataService.list(wrapper);
        QueryWrapper<Volume> wrapper1 = new QueryWrapper<Volume>();
        wrapper1.eq("file",fileName);
        wrapper1.eq("team",group);
        Volume volume = volumeService.getOne(wrapper1);
        queryData(list,volume);
        HandleData handleData = new HandleData();
        handleData.splitDataIntoInteger(profitList,profit);
        handleData.splitDataIntoInteger(weightList,weight);

    }

    /**
     * 查询数据，将数据归类
     * @param list
     * @param volume
     */
    private void queryData(List<FileData> list,Volume volume) {
        for (FileData fileData : list) {
            if(fileData.getType()==0){
                weight=fileData.getData();
            }
            if(fileData.getType()==1){
                profit=fileData.getData();
            }
        }
        currentVolume=volume.getVolume();
    }


}
