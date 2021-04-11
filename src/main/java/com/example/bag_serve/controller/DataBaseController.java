package com.example.bag_serve.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.bag_serve.entity.FileData;
import com.example.bag_serve.entity.Volume;
import com.example.bag_serve.service.FileDataService;
import com.example.bag_serve.service.VolumeService;
import com.example.bag_serve.util.ReadFile;
import com.example.bag_serve.util.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-11 16:21
 **/
@RestController
@CrossOrigin
public class DataBaseController {

//    存储所有文件名的列表
    private static ArrayList<String> files=new ArrayList<>();

    @Resource
    private FileDataService fileDataService;

    @Resource
    private VolumeService volumeService;

    /**
     * D{0-1}KP 实例数据集需存储到数据库
     * @return
     * @throws IOException
     */
    @GetMapping("/database/data/insert")
    public Object insert() throws IOException {
        JSONObject jsonObject = new JSONObject();
        int count1 = fileDataService.count(null);
        int count = volumeService.count(null);
        if(count>0&&count1>0){
            jsonObject.put("status",201);
            return jsonObject;
        }
        addFile();

        for (String file : files) {
            ReadFile rf = new ReadFile();
            rf.clearData(file);
            rf.replaceCharacter();
            rf.separateVolume();
            Result result = rf.resultData();
            insertDataBase(result);
        }
        jsonObject.put("status",200);
        return jsonObject;
    }

    /**
     * 清空数据库
     * @return
     */
    @GetMapping("/database/data/remove")
    public Object remove(){
        JSONObject jsonObject = new JSONObject();
        int count1 = fileDataService.count(null);
        int count = volumeService.count(null);
        if(count==0&&count1==0){
            jsonObject.put("status",201);
            return jsonObject;
        }
        fileDataService.remove(null);
        volumeService.remove(null);
        jsonObject.put("status",200);
        return jsonObject;
    }

    /**
     * 向列表中添加文件
     */
    private void addFile() {
        files.add("idkp1-10.txt");
        files.add("sdkp1-10.txt");
        files.add("udkp1-10.txt");
        files.add("wdkp1-10.txt");
    }

    /**
     * 将数据插入到数据库
     * @param result
     */
    private void insertDataBase(Result result) {
        for (String profit : result.getProfits()) {
            System.out.println(profit);
        }
        for (String profit : result.getProfits()) {
            FileData fileData = new FileData();
            fileData.setData(profit);
            fileData.setType(1);
            fileData.setTeam(result.getProfits().indexOf(profit)+1);
            fileData.setFile(result.getFileName());
            fileDataService.save(fileData);
        }
        for (String weight : result.getWeights()) {
            FileData fileData = new FileData();
            fileData.setData(weight);
            fileData.setType(0);
            fileData.setTeam(result.getWeights().indexOf(weight)+1);
            fileData.setFile(result.getFileName());
            fileDataService.save(fileData);
        }
        for (String volume : result.getVolumes()) {
            Volume volume1 = new Volume();
            volume1.setVolume(Integer.parseInt(volume));
            volume1.setTeam(result.getVolumes().indexOf(volume)+1);
            volume1.setFile(result.getFileName());
            volumeService.save(volume1);
        }
    }

}
