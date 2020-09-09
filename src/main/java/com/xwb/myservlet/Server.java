package com.xwb.myservlet;

import com.xwb.process.ServletProcess;
import com.xwb.process.StaticResourceProcess;
import com.xwb.thread.SocketThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 服务器类
 * @date 2020-9-8
 * @author 许文滨
 */
public class Server {
    private static ServerSocket SERVER_SOCKET;
    private static final int PORT = 8080;
    public static boolean SHUTDOWN = false;
    public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
    public static final String STATICS;
    public static final String XML;
    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(10,20,30, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>(20));

    static {
        try {
            SERVER_SOCKET = new ServerSocket(PORT,1, InetAddress.getByName("127.0.0.1"));
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        URL statics = Server.class.getClassLoader().getResource("statics");
        STATICS = Optional.ofNullable(statics).orElseThrow(()->
                new IllegalStateException("Can't find user web root file."))
                .getFile().substring(1);
        URL xml = Server.class.getClassLoader().getResource("servlet.xml");
        XML = Optional.ofNullable(xml).orElseThrow(()->
                new IllegalStateException("Can't find user web root file."))
                .getFile().substring(1);
    }

    public void await(){
        while (!SHUTDOWN){
            Socket socket = null;
            try{
                socket = SERVER_SOCKET.accept();
                POOL.submit(new SocketThread(socket));
//                InputStream inputStream = socket.getInputStream();
//                OutputStream outputStream = socket.getOutputStream();
//
//                //创建 request 对象并解析 request 请求
//                Request request = new Request(inputStream);
//                try {
//                    request.parseRequest();
//                }catch (Exception e){
//                    socket.close();
//                }
//
//                //创建 response 对象
//                Response response = new Response(outputStream,request);
//
//                //判断该请求是 servlet 还是 static resource
//                //servlet 请求开始为("/servlet/")
//                if (request.getUri() != null && request.getUri().startsWith("/servlet/")){
//                    ServletProcess process = new ServletProcess();
//                    process.process(request,response);
//                }else {
//                    StaticResourceProcess process = new StaticResourceProcess();
//                    process.process(request,response);
//                }
//
//                //判断请求是否是一个 shutdown 命令
//                Server.SHUTDOWN = request.getUri().equals(Server.SHUTDOWN_COMMAND);
//
//                //关闭 socket
//                socket.close();
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
        POOL.shutdown();
    }
}
