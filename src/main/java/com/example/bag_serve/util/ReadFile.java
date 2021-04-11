package com.example.bag_serve.util;

import com.example.bag_serve.entity.FileData;
import com.example.bag_serve.service.FileDataService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-11 15:34
 **/
@RestController
public class ReadFile {

//    存放价值和重量、容量的列表
    private static ArrayList<String> profits=new ArrayList<String>();
    private static ArrayList<String> weights=new ArrayList<String>();
    private static ArrayList<String> volumes=new ArrayList<String>();

//    当前的文件名
    private static String file;

    @Resource
    private FileDataService fileDataService;

    /**
     * 将需要的数据从文件中分离出来，并且添加到数据库中
     * @param fileName
     * @throws IOException
     */
    public void clearData(String fileName) throws IOException {
//        给当前的文件名赋值
        file=fileName;
//        先将列表清空
        profits.clear();
        weights.clear();
        volumes.clear();
        //        所有数据文件的公共路径
        String publicFilePath=System.getProperty("user.dir")+System.getProperty("file.separator")
                +"data";
        //具体文件路径
        String filePath=publicFilePath+System.getProperty("file.separator")+fileName;
//        创建一个文件对象
        File file = new File(filePath);

//        用BufferedReader类来进行读取内容
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s;
//            读取数据的标志字符串
        String profit="The profit of";
        String weight="The weight of";
        String volume="the cubage of knapsack is";
//            读取数据的标志位
        int flag1=0;
        int flag2=0;

        while ((s = reader.readLine()) != null) {
            if (s.contains(profit)) {
                flag1=1;
                continue;
            }else if (s.contains(weight)) {
                flag2=1;
                continue;
            }else if (s.contains(volume)) {
                volumes.add(s);
                continue;
            }
            if(flag1==1){
                profits.add(s);
                flag1=0;
            }else if(flag2==1){
                weights.add(s);
                flag2=0;
            }
        }
    }

    /**
     * 将数据字符串中的.替换为""(空)
     */
    public void replaceCharacter(){
        for (int i = 0; i < profits.size(); i++) {
            profits.set(i,profits.get(i).replace(".",""));
        }
        for (int i = 0; i < weights.size(); i++) {
            weights.set(i,weights.get(i).replace(".",""));
        }
        for (int i = 0; i < volumes.size(); i++) {
            volumes.set(i,volumes.get(i).replace(".",""));
        }
    }

    /**
     * 将容量从行中分离出来
     */
    public void separateVolume(){
        for (int i = 0; i < volumes.size(); i++) {
            String s = volumes.get(i);
            String[] s1 = s.split(" ");
            volumes.set(i,s1[s1.length-1]);
        }
    }


    /**
     * 向接口返回需要插入到数据库中的数据
     * @return
     */
    public Result resultData(){
        Result result = new Result();
        result.setProfits(profits);
        result.setWeights(weights);
        result.setFileName(file);
        result.setVolumes(volumes);
        return result;
    }



}
