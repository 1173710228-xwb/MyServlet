package com.xwb.myservlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import java.io.*;
import java.util.*;

/**
 * 响应类
 * @date 2020-9-8
 * @author 许文滨
 */
public class Response implements ServletResponse {
    private OutputStream outputStream;
    private Request request;
    private List<Cookie> cookies;

    public Response(OutputStream outputStream,Request request){
        this.outputStream = outputStream;
        this.request = request;
        cookies = new LinkedList<>();
    }

    /**
     * 根据URI获取静态文件
     */
    public void accessStaticResources() throws IOException {
        //根据URI获取资源文件
        File file = new File(Server.STATICS+request.getUri());
        if(file.exists() && file.isFile()){
            outputStream.write(responseToByte(200,"OK"));
        }else {
            file = new File(Server.STATICS+"/404.html");
            outputStream.write(responseToByte(404,"File Not Found"));
        }
        write(file);
    }

    /**
     * 生成响应头，并将其转换为byte数组
     * @param status 状态码
     * @param desc 描述
     * @return byte[]
     */
    public byte[] responseToByte(int status,String desc){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Http/1.1 ").append(status).append(" ").append(desc).append("\r\nConnection: keep-alive")
                .append("\r\nDate: ").append(new Date().toString());
        cookies.forEach(cookie -> {
            stringBuilder.append("\r\nSet-Cookie: ").append(cookie.getName()).append("=").append(cookie.getValue());
            if (cookie.getMaxAge() != -1){
                stringBuilder.append("; Max-Age=").append(cookie.getMaxAge())
                .append("; Expires=").append(new Date().toString());
            }
        });
        stringBuilder.append("\r\nserver: MyServlet/1.0\r\n\r\n");
        return stringBuilder.toString().getBytes();
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

    /**
     * 添加cookie
     * @param cookie
     */
    public void addCookie(Cookie cookie){
        cookies.add(cookie);
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        PrintWriter writer = new PrintWriter(outputStream,true);
        return writer;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
