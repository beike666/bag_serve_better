package com.example.bag_serve.util;

/**
 * @program: bag_serve
 * @description
 * @author: BeiKe
 * @create: 2021-04-12 16:33
 **/
public class Judge {

    public  boolean isNumeric(String str){

        if (str != null){
            str = str.trim();
        }
        try{
            Integer.parseInt(str);
            return true;
        }catch(NumberFormatException e){
            return false;

        }

    }
}
