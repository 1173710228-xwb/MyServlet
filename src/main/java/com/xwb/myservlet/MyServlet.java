package com.xwb.myservlet;

import org.dom4j.DocumentException;

/**
 * @date 2020-9-8
 * @author 许文滨
 */
public class MyServlet {

    public static void main(String[] args) throws DocumentException {
        Server server = new Server();
        server.await();
    }
}
