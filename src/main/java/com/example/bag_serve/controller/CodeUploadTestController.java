package com.example.bag_serve.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bag_serve.entity.FileData;
import com.example.bag_serve.entity.Volume;
import com.example.bag_serve.service.FileDataService;
import com.example.bag_serve.service.VolumeService;
import com.example.bag_serve.util.FileUtil;
import com.example.bag_serve.util.HandleFile;
import com.example.bag_serve.util.Judge;
import com.example.bag_serve.util.Scatter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-12 13:05
 **/
@RestController
@CrossOrigin
public class CodeUploadTestController {

    private static String currentCodeFile;

    @Resource
    private FileDataService fileDataService;

    @Resource
    private VolumeService volumeService;

//    测试代码需要传递的参数
    private static String profit;
    private static String weight;
    private static String currentVolume;

//    要写入bat文件的内容
    private static String currentBatContent;

//    用户算法求解结果
    private static String currentAnswer;

//    程序开始运行时间
    private static long startTime;
//    程序结束运行时间
    private static long endTime;
//    程序总共运行时间
    private static long runTime;



    /**
     * 代码文件上传
     * @param javaFile
     * @return
     */
    @PostMapping("/code/file/upload")
    public Object uploadFile(@RequestParam("file") MultipartFile javaFile){
//        重命名文件名
        String fileName = javaFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") +
                "code";
        File file1 = new File(filePath);
//        如果文件目录不存在，则创建目录
        if (!file1.exists()) {
            file1.mkdir();
        }
//        文件存放的实际路径
        File file = new File(filePath + System.getProperty("file.separator") + fileName);
        currentCodeFile=fileName;
        //        真正存放文件
        JSONObject jsonObject = new JSONObject();
        try {
            javaFile.transferTo(file);
            jsonObject.put("status",200);
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject.put("status",201);
        }
        return jsonObject;
    }

    /**
     * 代码测试
     * @param scatter
     * @return
     */
    @PostMapping("/code/submit/test")
    public Object codeTest(@RequestBody Scatter scatter){
        JSONObject jsonObject = new JSONObject();
        String fileName = scatter.getFileName();
        Integer group = scatter.getGroup();
        if(!fileName.equals("idkp1-10.txt")){
            if(group==11){
                jsonObject.put("status",202);
                return jsonObject;
            }
        }
//        bat文件和代码公共目录
        String publicFilePath=System.getProperty("user.dir")+System.getProperty("file.separator")
                +"code";
        File file = createBatFilePath(publicFilePath);
        HandleFile handleFile = new HandleFile();
        handleDataBase(fileName, group);
        handleFile.writeFile(file,currentBatContent);
        Process p;
        String batFilePath=publicFilePath+System.getProperty("file.separator")+"run.bat";
        String command = "cmd.exe /c start /b "+batFilePath;
        return callCommand(jsonObject, publicFilePath, command);


    }

    /**
     * 用命令行运行程序并返回结果
     * @param jsonObject
     * @param publicFilePath
     * @param command
     * @return
     */
    private Object callCommand(JSONObject jsonObject, String publicFilePath, String command) {

        Process p;
        File file1 = new File(publicFilePath);

        try {
            startTime=System.currentTimeMillis();
            //执行命令
            p = Runtime.getRuntime().exec(command,null,file1);
            endTime=System.currentTimeMillis();
            //取得命令结果的输出流
            InputStream fis=p.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr=new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            //直到读完为止
            while((line=br.readLine())!=null) {
                Judge judge = new Judge();
                boolean bool = judge.isNumeric(line);
                if(bool){
                    currentAnswer=line;
                    break;
                }

            }
            br.close();
            isr.close();
            fis.close();
            jsonObject.put("status",200);
            runTime= endTime-startTime;
            FileUtil file = createFile();
            HandleFile handleFile = new HandleFile();
            String data="最优解是："+currentAnswer+"\n"+"求解时间为："+runTime+"ms"+"\n选中的物品的价值分别是"+"\n"+
                    "选中的物品的重量分别是："+"\n";
            handleFile.writeFile(file.getFile(),data);
            jsonObject.put("runTime",runTime);
            jsonObject.put("answer",currentAnswer);
            jsonObject.put("downUrl",file.getDownUrl());

            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject.put("status",201);
            return jsonObject;
        }
    }

    /**
     * 创建bat文件目录
     * @return
     */
    private File createBatFilePath(String filePath) {
        String path=filePath+System.getProperty("file.separator")+"run.bat";
        return new File(path);
    }

    /**
     * 查询数据库并且准备向bat文件中写入的内容
     * @param fileName
     * @param group
     */
    private void handleDataBase(String fileName, Integer group) {
        QueryWrapper<FileData> wrapper = new QueryWrapper<FileData>();
        wrapper.eq("file",fileName);
        wrapper.eq("team",group);
        List<FileData> list = fileDataService.list(wrapper);
        QueryWrapper<Volume> wrapper1 = new QueryWrapper<Volume>();
        wrapper.eq("file",fileName);
        wrapper.eq("team",group);
        Volume one = volumeService.getOne(wrapper1);
        queryData(list,one);
//        取文件名的前缀，去掉后缀
        System.out.println(currentCodeFile);
        String replace = currentCodeFile.replace(".", ",");
        String[] split = replace.split(",");
        String s=split[0];
        currentBatContent="javac "+currentCodeFile+"\n"+"java "+s+" "+profit+" "+weight+" "+currentVolume+"\n"+"exit";
    }

    /**
     * 查询数据，将数据归类
     * @param list
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
        currentVolume=volume.getVolume()+"";
    }

    /**
     * 创建实验数据的存储文件
     * @return
     */
    private FileUtil createFile() {
        //文件路径
        String filePath=System.getProperty("user.dir")+System.getProperty("file.separator")
                +"file";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
//        用时间给文件起名
        Date dd=new Date();
        //时间格式化
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
        String saveFileName =sim.format(dd)+"-"+System.currentTimeMillis();
        File file1 = new File(filePath +System.getProperty("file.separator")+ saveFileName+".txt");
        String downUrl="http://localhost:8088/file/"+saveFileName+".txt";
        FileUtil fileUtil = new FileUtil();
        fileUtil.setDownUrl(downUrl);
        fileUtil.setFile(file1);
        return fileUtil;
    }
}
