package com.xwb.myservlet;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : ReadXML
 * @Author : 许文滨
 * @Date: 2020-09-09 11:30
 */
public class ReadXml {
    private static ReadXml instance;

    static {
        try {
            instance = new ReadXml();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private static Map<String,String> SERVLET;

    private ReadXml() throws DocumentException {
        SERVLET = this.readServletXml();
    }

    public static Map<String,String> getServlet(){
        return SERVLET;
    }

    /**
     * 读取XML配置文件
     * @return Map
     * @throws DocumentException documentException
     */
    private Map<String,String> readServletXml() throws DocumentException {
        Map<String,String> servlet = new HashMap<>(8);
        SAXReader reader = new SAXReader();
        Document document = reader.read(ReadXml.class.getResourceAsStream("/servlet.xml"));
        //获取根节点
        Element rootElement = document.getRootElement();
        //利用Element中的方法，获取根节点下的全部子节点，返回一个List<element>
        List<Element> elements = rootElement.elements();
        for (Element element: elements){
            String name = element.elementText("name");
            String classPath = element.elementText("class");
            if (servlet.containsKey(name) || servlet.containsValue(classPath)){
                System.out.println("servlet名字或类路径重复");
                System.exit(1);
            }
            servlet.put(name,classPath);
        }
        return servlet;
    }
}
