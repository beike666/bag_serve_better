package com.example.bag_serve.controller;

import com.alibaba.fastjson.JSONObject;

import com.example.bag_serve.util.Scatter;
import com.example.bag_serve.util.AnswerUtil;
import com.example.bag_serve.util.OrderUtil;
import com.example.bag_serve.util.ScatterUtil;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-03-25 17:14
 **/
@RestController
@CrossOrigin
public class FileController {

//        定义存放价值项集和重量项集的数组(此时数据还都是字符串)
    private static ArrayList<String> profits=new ArrayList<>();
    private static ArrayList<String> weights=new ArrayList<>();



    /**
     * 非递增排序
     * @param scatter
     * @return
     */
    @PostMapping("/order")
    public Object order(@RequestBody Scatter scatter){
        JSONObject jsonObject = new JSONObject();
//      传的数据不满足要求，返回失败
        if(!scatter.getFileName().equals("idkp1-10.txt")){
            if(scatter.getGroup()==11){
                jsonObject.put("status",202);
                return jsonObject;
            }
        }
        ArrayList<ScatterUtil> scatterUtils = new ArrayList<>();
//      将数据分割成【重量，价值】的数组
        splitDataTwoGroup(scatter, scatterUtils);
//        返回前端的数据列表
        ArrayList<OrderUtil> orderUtils = new ArrayList<>();
        for (int i = 0; i < scatterUtils.size(); i=i+3) {
//            封装数据
            OrderUtil orderUtil = new OrderUtil();
            List<ScatterUtil> item = scatterUtils.subList(i, i + 3);
            orderUtil.setItem(item);
            float rate = (float) item.get(2).getProfit() / item.get(2).getWeight();
            orderUtil.setRate(rate);
            orderUtils.add(orderUtil);
        }
        Collections.sort(orderUtils);
//        返回结果
        if (orderUtils.size()>0) {
            jsonObject.put("status",200);
            jsonObject.put("data",orderUtils);
            return jsonObject;
        }
        jsonObject.put("status",201);
        jsonObject.put("data",null);
        return jsonObject;

    }

    /**
     * 求某一组数据的最优解
     * @param scatter
     * @return
     */
    @PostMapping("/get/answer")
    public Object answer(@RequestBody Scatter scatter){
        JSONObject jsonObject = new JSONObject();
//        定义存放价值项集和重量项集的数组(此时数据已被分割)
        ArrayList<Integer> profitsList = new ArrayList<>();
        ArrayList<Integer> weightsList = new ArrayList<>();
//        获取前端的请求数据
        String fileName=scatter.getFileName();
        Integer group = scatter.getGroup();
        readFile(fileName,group,profitsList,weightsList);
//        获取当前组的容量
//        int volume=volumeCount(scatter.getFileName(),scatter.getGroup());
        long startTime=0;
        int answer=0;
        long endTime=0;
        if(scatter.getType()==0){
//            采用动态规划算法
            startTime=System.currentTimeMillis();   //获取开始时间
//            answer=dp(profitsList,weightsList,volume);
            endTime=System.currentTimeMillis(); //获取结束时间
            jsonObject.put("status",200);
            jsonObject.put("answer",answer);
            jsonObject.put("runtime",endTime-startTime);
            return jsonObject;
        }else{
//            采用回溯算法
            startTime=System.currentTimeMillis();   //获取开始时间
//            answer=back(profitsList,weightsList,volume);
            endTime=System.currentTimeMillis(); //获取结束时间
            jsonObject.put("status",200);
            jsonObject.put("answer",answer);
            jsonObject.put("runtime",endTime-startTime);
            return jsonObject;
        }
    }

    @PostMapping("/get/answer/save")
    public Object answerAndSave(@RequestBody Scatter scatter){
        JSONObject jsonObject = new JSONObject();
//        定义存放价值项集和重量项集的数组(此时数据已被分割)
        ArrayList<Integer> profitsList = new ArrayList<>();
        ArrayList<Integer> weightsList = new ArrayList<>();
//        获取前端的请求数据
        String dataFileName=scatter.getFileName();
        Integer group = scatter.getGroup();
        String suffix=scatter.getFileType();

//        读取文件
        readFile(dataFileName,group,profitsList,weightsList);
//        获取当前组的容量
//        int volume=volumeCount(scatter.getFileName(),scatter.getGroup());
        long startTime=0;
        int answer=0;
        long endTime=0;
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
        File file1 = new File(filePath +System.getProperty("file.separator")+ saveFileName+suffix);
//        运行时间
        double runTime=0;
        String downUrl="http://localhost:8088/file/"+saveFileName+suffix;
        if(scatter.getType()==0){
//            采用动态规划算法
            startTime=System.currentTimeMillis();   //获取开始时间
//            answer=dp(profitsList,weightsList,volume);
            endTime=System.currentTimeMillis(); //获取结束时间
            runTime=(endTime-startTime)/1000.0;
            if(suffix.equals(".txt")){
                String data="最优解："+answer+"\n"+
                        "求最优解的时间："+runTime+"\n";
                writeTxt(file1,data);
            }else if(suffix.equals(".xlsx")){
                AnswerUtil answerUtil = new AnswerUtil();
                answerUtil.setAnswer(answer);
                answerUtil.setRunTime(runTime);
                writeExcel(file1,answerUtil);
            }
            jsonObject.put("status",200);
            jsonObject.put("answer",answer);
            jsonObject.put("runtime",runTime);
            jsonObject.put("downUrl",downUrl);
            return jsonObject;
        }else{
//            采用回溯算法
            startTime=System.currentTimeMillis();   //获取开始时间
//            answer=back(profitsList,weightsList,volume);
            endTime=System.currentTimeMillis(); //获取结束时间
            runTime=(endTime-startTime)/1000.0;
            if(suffix.equals(".txt")){
                String data="最优解："+answer+"\n"+
                        "求最优解的时间："+runTime+"\n";
                writeTxt(file1,data);
            }else if(suffix.equals(".xlsx")){
                AnswerUtil answerUtil = new AnswerUtil();
                answerUtil.setAnswer(answer);
                answerUtil.setRunTime(runTime);
                writeExcel(file1,answerUtil);
            }
            jsonObject.put("status",200);
            jsonObject.put("answer",answer);
            jsonObject.put("runtime",runTime);
            jsonObject.put("downUrl",downUrl);
            return jsonObject;
        }
    }

    /**
     * 动态规划算法
     * @param profitsList
     * @param weightsList
     * @param volume
     */
    private int dp(ArrayList<Integer> profitsList, ArrayList<Integer> weightsList, int volume) {
//        组数
        int N=profitsList.size()/3;
//        每一个项集的元素个数
        int S=3;
        int[][] p = new int[N+1][S];        // 价值（每S个为一组）
        int[][] w = new int[N+1][S];        // 重量（每S个为一组）
//        将数据存为动态规划需要的格式
        int index=0;
        for (int i = 1; i <= N; i++) {
            List<Integer> pi = profitsList.subList(index, index + 3);
            int[] pa = pi.stream().mapToInt(Integer::intValue).toArray();
            p[i]=pa;
            List<Integer> wi = weightsList.subList(index, index + 3);
            int[] wa = wi.stream().mapToInt(Integer::intValue).toArray();
            w[i]=wa;
            index=index+3;
        }
//        算法
        int[] dp = new int[volume+1];
        for (int i = 1; i <= N; i++) {
            for (int j = volume; j >= 0; j--) {
                for (int k = 0; k < 3; k++) {
                    if(j>=w[i][k]) {
                        dp[j] = Math.max(dp[j], dp[j - w[i][k]] + p[i][k]);
                    }
                }
            }
        }


        return dp[volume];

    }

    /**
     * 回溯处理数据
     * @param profitsList
     * @param weightsList
     * @param volume
     * @return
     */
    private int back(ArrayList<Integer> profitsList, ArrayList<Integer> weightsList, int volume){
        //        组数
        int N=profitsList.size()/3;
//        每一个项集的元素个数
        int S=3;
        int[][] p = new int[N+1][S];        // 价值（每S个为一组）
        int[][] w = new int[N+1][S];        // 重量（每S个为一组）
//        将数据存为动态规划需要的格式
        int index=0;
        for (int i = 0; i <= N; i++) {
            if(i==0){
                int[] head=new int[]{0,0,0};
                p[i]=head;
                w[i]=head;
            }else{
                List<Integer> pi = profitsList.subList(index, index + 3);
                int[] pa = pi.stream().mapToInt(Integer::intValue).toArray();
                p[i]=pa;
                List<Integer> wi = weightsList.subList(index, index + 3);
                int[] wa = wi.stream().mapToInt(Integer::intValue).toArray();
                w[i]=wa;
                index=index+3;
            }
        }
        ArrayList<Integer> ret=new ArrayList<>();
        int totalProfit=0;
        int totalWeight=0;
        recursion(ret,volume,p,w,totalProfit,totalWeight,0,0);
        Collections.sort(ret);
        return ret.get(ret.size()-1);
    }

    /**
     * 回溯算法
     * @param ret
     * @param volume
     * @param p
     * @param w
     * @param totalProfit
     * @param totalWeight
     * @param i
     * @param j
     */
    public void  recursion(ArrayList<Integer> ret,int volume,int[][] p,int[][] w,int totalProfit,int totalWeight,int i,int j){
        if(j!=3){
//            相当于不选当前的项集
            totalProfit=totalProfit+p[i][j];
            totalWeight=totalWeight+w[i][j];
        }


//        如果加上当前物品时总重量超过了背包容量则返回上一级
        if(totalWeight>volume){
            return;
        }

        if(i==p.length-1){
            ret.add(totalProfit);
            return;
        }

        for (int k = 0; k <4 ; k++) {
            recursion(ret,volume,p,w,totalProfit,totalWeight,i+1,k);
        }
    }


    /**
     * 将数据分割成【重量，价值】的数组
     * @param scatter
     * @param scatterUtils
     */
    private void splitDataTwoGroup(@RequestBody Scatter scatter, ArrayList<ScatterUtil> scatterUtils) {
//        定义存放价值项集和重量项集的数组(此时数据已被分割)
        ArrayList<Integer> profitsList = new ArrayList<>();
        ArrayList<Integer> weightsList = new ArrayList<>();
//        获取前端的请求数据
        String fileName=scatter.getFileName();
        Integer group = scatter.getGroup();
        readFile(fileName,group,profitsList,weightsList);

        for (int i = 0; i < profitsList.size(); i++) {
            ScatterUtil scatterUtil = new ScatterUtil();
            scatterUtil.setWeight(weightsList.get(i));
            scatterUtil.setProfit(profitsList.get(i));
            scatterUtils.add(scatterUtil);
        }
    }



    /**
     * 创建文件对象，清洗数据的前期准备
     */
//    抛异常的注解
    @SneakyThrows
    public void readFile(String fileName,Integer group,ArrayList<Integer> profitsList,ArrayList<Integer> weightsList) {
//        清洗数据
//        clearData();


//        分割数据为整形
        splitData(group, profitsList, profits);
        splitData(group, weightsList, weights);
    }

    /**
     * 分割具体某一组数据
     * @param group
     * @param divided
     * @param undivided
     */
    private void splitData(Integer group, ArrayList<Integer> divided, ArrayList<String> undivided) {
        String[] profitSplit = undivided.get(group - 1).split(",");
        String number=profitSplit[profitSplit.length-1];
        String a = number.replace(".", "");
        profitSplit[profitSplit.length-1]=a;
        for (String s : profitSplit) {
            divided.add(Integer.parseInt(s));
        }
    }




    /**
     * 写文件
     * @param file
     * @param data
     * @return
     */
    public String writeTxt(File file,String data){
        Writer writer=null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
//            写入内容
            writer.write(data);
            // 换行
            writer.write("\r\n");
            writer.close();
            return "write success";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "write failed";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "write failed";
        } catch (IOException e) {
            e.printStackTrace();
            return "write failed";
        }
    }

    /**
     * 写文件为excel
     * @param file
     * @param answerUtil
     * @return
     */
    public void writeExcel(File file,AnswerUtil answerUtil){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //创建excel
        Workbook workbook = new HSSFWorkbook();
        //创建sheet--子工作表
        Sheet sheet = workbook.createSheet("D{0-1}背包求解");
        //获取子工作表的第一行<设置标题>
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("最优解");
        row.createCell(1).setCellValue("求解时间");
        row.createCell(2).setCellValue("解向量");
        row = sheet.createRow(1);
        row.createCell(0).setCellValue(answerUtil.getAnswer());
        row.createCell(1).setCellValue(answerUtil.getRunTime());
        row.createCell(2).setCellValue(answerUtil.getBestPath());
        try {
            workbook.write(fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
