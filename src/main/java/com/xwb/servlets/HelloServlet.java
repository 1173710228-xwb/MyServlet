package com.xwb.servlets;

import com.sun.corba.se.spi.activation.Server;

import javax.servlet.*;
import java.io.IOException;

/**
 * @ClassName : HelloServlet
 * @Author : 许文滨
 * @Date: 2020-09-09 19:09
 */
public class HelloServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        servletResponse.getWriter().println("Hello HelloServlet!");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
