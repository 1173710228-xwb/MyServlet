package com.xwb.mytomcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器类
 * @date 2020-9-8
 * @author 许文滨
 */
public class Server {
    private static ServerSocket SERVER_SOCKET;

    static {
        try {
            SERVER_SOCKET = new ServerSocket(8080,1, InetAddress.getByName("127.0.0.1"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void startServer(){
        while (true){
            try(Socket accept = SERVER_SOCKET.accept();
                InputStream inputStream = accept.getInputStream();
                OutputStream outputStream = accept.getOutputStream()) {
                //解析请求
                Request request = new Request();
                request.setRequestStream(inputStream);
                request.parseRequest();
//                System.out.println(request.getHeaderString());
//                System.out.println(request.getContent());
                //生成相应对象并响应静态资源
                Response response = new Response(outputStream,request);
                response.accessStaticResources();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
