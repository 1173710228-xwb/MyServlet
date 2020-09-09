package com.xwb.mytomcat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * 响应类
 * @date 2020-9-8
 * @author 许文滨
 */
public class Response {
    private static final URL url = MyServlet.class.getClassLoader().getResource("statics");
    private OutputStream outputStream;
    private Request request;

    public Response(OutputStream outputStream,Request request){
        this.outputStream = outputStream;
        this.request = request;
    }

    /**
     * 根据URI获取静态文件
     */
    public void accessStaticResources() throws IOException {
        //根据URI获取资源文件
        File file = new File(url.getPath().substring(1)+request.getHeader().get("Uri"));
        if(file.exists() && file.isFile()){
            outputStream.write(responseToByte(200,"OK"));
        }else {
            file = new File(url.getPath().substring(1)+"/404.html");
            outputStream.write(responseToByte(404,"NOT FOUNT"));
        }
        write(file);
    }

    /**
     * 生成响应头，并将其转换为byte数组
     * @param status 状态码
     * @param desc 描述
     * @return byte[]
     */
    private byte[] responseToByte(int status,String desc){
        return new StringBuilder()
                .append(request.getHeader().get("HttpVersion"))
                .append(" ")
                .append(status)
                .append(" ")
                .append(desc)
                .append("\r\n\r\n")
                .toString().getBytes();
    }

    /**
     * 将文件写入输出流
     * @param file File
     */
    private void write(File file){
        try(FileInputStream fis = new FileInputStream(file)){
            byte[] cache = new byte[1024];
            int read;
            while ((read = fis.read(cache,0,1024)) != -1){
                outputStream.write(cache,0,read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
