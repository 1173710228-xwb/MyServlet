package com.xwb.mytomcat;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析用户的请求
 *
 * @date 2020-9-8
 * @author 许文滨
 */
public class Request {
    Map<String,String> header;
    Map<String,String> params;
    private InputStream inputStream;
    private StringBuilder content;

    public Request(){
        this(null);
    }

    public Request(InputStream inputStream){
        this.inputStream = inputStream;
        init();
    }

    /**
     * 初始化方法
     */
    private void init(){
        this.header = new HashMap<>(16,1);
        header.put("Method",null);
        header.put("Uri",null);
        header.put("HttpVersion",null);
        header.put("Host",null);
        header.put("Connection",null);
        header.put("UserAgent",null);
        header.put("AcceptLanguage",null);
        header.put("Content-Length",null);
        params = new HashMap<>(8);
        content = new StringBuilder();
    }

    /**
     * 设置请求的输入流
     * @param inputStream InputStream
     */
    public void setRequestStream(InputStream inputStream){
        this.inputStream = inputStream;
    }

    /**
     * 解析请求信息
     */
    public void parseRequest(){
        //存储请求头信息
        StringBuilder requestStr = new StringBuilder();
        int len;
        byte[] buffer = new byte[1024];
        try {
            len = inputStream.read(buffer);
        }catch (IOException e){
            e.printStackTrace();
            len = -1;
        }
        for (int j = 0;j<len;j++){
            requestStr.append((char)buffer[j]);
        }
//        System.out.println(requestStr);
        String[] lines = requestStr.toString().split("\n");
        String[] lines0 = lines[0].split(" ");
        header.put("Method",lines0[0]);
        System.out.println(lines[0]);
        if (lines0[1].contains("?")){
            String[] ss = lines0[1].split("\\?");
            header.put("Uri",ss[0]);
            String[] sss = ss[1].split("&");
            for (int i=0;i<sss.length;i++){
                String[] tmp = sss[i].split("=");
                params.put(tmp[0],tmp[1]);
            }
        }else {
            header.put("Uri",lines0[1]);
        }
        header.put("HttpVersion",lines0[2]);
        //判断下面的信息是否是post的请求体
        boolean isContent = false;
        //根据请求头信息，将其存储到header中
        for (int i=1;i<lines.length;i++){
            String[] linesi = lines[i].split(": ");
            if (isContent){
                content.append(lines[i]).append("\n");
            }else if ("Host".equals(linesi[0])){
                header.put("Host",linesi[1].trim());
            }else if ("Connection".equals(linesi[0])){
                header.put("Connection",linesi[1].trim());
            }else if ("User-Agent".equals(linesi[0])){
                header.put("UserAgent",linesi[1].trim());
            }else if ("Accept-Language".equals(linesi[0])){
                header.put("AcceptLanguage",linesi[1].split(";")[0].trim());
            }else if ("Content-Length".equals(linesi[0])){
                header.put("Content-Length",linesi[1].trim());
            }else if ("".equals(linesi[0])){
                isContent = true;
            }
        }
    }

    /**
     * 获取请求头信息
     * @return 请求头数据
     */
    public Map<String,String> getHeader(){
        return header;
    }

    /**
     * 获取请求的String格式
     * @return String
     */
    public String getHeaderString(){
        StringBuilder stringBuilder = new StringBuilder();
        header.forEach((k,v)->{
            stringBuilder.append(k).append(":").append(v).append("\n");
        });
        return stringBuilder.toString();
    }

    /**
     * 获取请求体信息
     * @return String
     */
    public String getContent(){
        return content.toString();
    }

    /**
     * 获取请求参数
     * @return params
     */
    public Map<String,String> getParams(){
        return params;
    }
}
