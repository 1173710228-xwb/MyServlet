package com.xwb.thread;

import com.xwb.myservlet.Request;
import com.xwb.myservlet.Response;
import com.xwb.myservlet.Server;
import com.xwb.process.ServletProcess;
import com.xwb.process.StaticResourceProcess;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @ClassName : SocketThread
 * @Author : 许文滨
 * @Date: 2020-09-09 18:39
 */
public class SocketThread implements Runnable{
    private final Socket SOCKET;

    public SocketThread(Socket socket){
        this.SOCKET = socket;
    }

    @SneakyThrows
    @Override
    public void run() {
        InputStream inputStream = SOCKET.getInputStream();
        OutputStream outputStream = SOCKET.getOutputStream();

        //创建 request 对象并解析 request 请求
        Request request = new Request(inputStream);
        try {
            request.parseRequest();
        }catch (Exception e){
            //关闭输入输出流
            inputStream.close();
            outputStream.close();
            //关闭 socket
            SOCKET.close();
            return;
        }

        //创建 response 对象
        Response response = new Response(outputStream,request);

        //判断该请求是 servlet 还是 static resource
        //servlet 请求开始为("/servlet/")
        if (request.getUri() != null && request.getUri().startsWith("/servlet/")){
            ServletProcess process = new ServletProcess();
            process.process(request,response);
        }else {
            StaticResourceProcess process = new StaticResourceProcess();
            process.process(request,response);
        }

        //判断请求是否是一个 shutdown 命令
        Server.SHUTDOWN = request.getUri().equals(Server.SHUTDOWN_COMMAND);

        //关闭输入输出流
        inputStream.close();
        outputStream.close();
        //关闭 socket
        SOCKET.close();
    }
}
