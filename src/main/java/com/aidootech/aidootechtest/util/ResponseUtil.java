package com.aidootech.aidootechtest.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseUtil<t> {
    public  Map<String,Object>  ResponseSuccess(List<t> data,String message){
        Map result=new HashMap();
        result.put("code",200);
        result.put("message",message);
        result.put("data",data);
        return result;
    }
    public  Map<String,Object>  ResponseSuccess(Object data,String message){
        Map result=new HashMap();
        result.put("code",200);
        result.put("message",message);
        result.put("data",data);
        return result;
    }
    public  Map<String,Object>  ResponseSuccess(String message){
        Map result=new HashMap();
        result.put("code",200);
        result.put("message",message);
        result.put("data","null");
        return result;
    }
    public   Map<String,Object>  ResponseError(List<t> data,String message){
        Map result=new HashMap();
        result.put("code",500);
        result.put("message",message);
        result.put("data",data);
        return result;
    }
    public   Map<String,Object>  ResponseError(Object data,String message){
        Map result=new HashMap();
        result.put("code",500);
        result.put("message",message);
        result.put("data",data);
        return result;
    }
    public   Map<String,Object>  ResponseError(String message){
        Map result=new HashMap();
        result.put("code",500);
        result.put("message",message);
        result.put("data","null");
        return result;
    }
}
