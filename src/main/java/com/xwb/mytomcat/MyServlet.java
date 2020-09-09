package com.xwb.mytomcat;

import java.io.IOException;

/**
 * @date 2020-9-8
 * @author 许文滨
 */
public class MyServlet {


    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
}
