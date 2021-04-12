package com.example.bag_serve.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-11 23:04
 **/
public class HandleFile {

    /**
     * 写文件
     * @param file
     * @param data
     * @return
     */
    public String writeFile(File file, String data){
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
