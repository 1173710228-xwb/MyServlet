package com.xwb.process;

import com.xwb.myservlet.Request;
import com.xwb.myservlet.Response;

import java.io.IOException;

/**
 * @ClassName : StaticResourceProcess
 * @Author : 许文滨
 * @Date: 2020-09-09 09:29
 */
public class StaticResourceProcess {

    public void process(Request request, Response response){
        try {
            response.accessStaticResources();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
