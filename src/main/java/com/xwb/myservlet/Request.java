package com.xwb.myservlet;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 解析用户的请求
 *
 * @date 2020-9-8
 * @author 许文滨
 */
public class Request implements ServletRequest {
    /**
     * 存储部分请求头信息
     */
    Map<String,String> header;
    /**
     * 请求请求参数
     */
    Map<String,String> params;
    private InputStream inputStream;
    /**
     * 请求实体
     */
    private StringBuilder content;
    /**
     * Cookie
     */
    private Cookie[] cookies;

    public Request(){
        this(null);
    }

    public Request(InputStream inputStream){
        this.inputStream = inputStream;
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        this.header = new HashMap<>(16);
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
    public void parseRequest() throws Exception{
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
        if (requestStr == null || requestStr.length() == 0){
            throw new Exception();
        }
        String[] lines = requestStr.toString().split("\n");
        String[] lines0 = lines[0].split(" ");
        header.put("Method",lines0[0]);
        System.out.println(requestStr);
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
                //存储请求体信息
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
            }else if ("Cookie".equals(linesi[0])){
                //提取cookie
                String[] cookie = linesi[1].trim().split(";");
                cookies = new Cookie[cookie.length];
                for (int j=0;j<cookie.length;j++){
                    String name = cookie[j].substring(0,cookie[j].indexOf("=")).trim();
                    String value = cookie[j].substring(cookie[j].indexOf("=")+1).trim();
                    cookies[j] = new Cookie(name,value);
                }
            } else if ("".equals(linesi[0])){
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
     * 获取 uri
     * @return Uri
     */
    public String getUri(){
        return header.get("Uri");
    }

    /**
     * 获取 cookies
     * @return
     */
    public Cookie[] getCookies(){
        return cookies;
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

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}
