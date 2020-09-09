package com.xwb.process;

import com.xwb.myservlet.ReadXml;
import com.xwb.myservlet.Request;
import com.xwb.myservlet.Response;

import javax.servlet.Servlet;

/**
 * @ClassName : ServletProcess
 * @Author : 许文滨
 * @Date: 2020-09-09 09:29
 */
public class ServletProcess {

    /**
     * 利用反射机制生成servlet类
     * @param request Request
     * @param response Response
     * @throws Exception exception
     */
    public void process(Request request, Response response) throws Exception {
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/")+1);
        String className = ReadXml.getServlet().getOrDefault(servletName,null);
        //判断根据
        if (className == null){
            response.accessStaticResources();
        }else {
            Class servletClass = Class.forName(ReadXml.getServlet().get(servletName));
            Servlet servlet = (Servlet)servletClass.newInstance();
            response.getWriter().println(new String(response.responseToByte(200,"OK")));
            servlet.service(request,response);
        }
    }
}
