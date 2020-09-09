## 基于servlet容器的web服务器实现

开发一个web服务器，内部基于servlet-api实现servlet容器，支持静态资源获取，简单http请求处理

#### web服务器

1. 基于socket实现，默认端口8080
2. 多线程并发控制，在高并发情况下能保证服务器正常

#### servlet容器

1. http解析器，支持http报文解析，可解析请求参数、请求头、cookie，生成ServletRequest实例
2. 静态文件处理，匹配url识别为静态文件，返回文件流，如html，图片等
3. ServletRequest实现，可获取请求参数getParameter，请求头getHeader，cookies会话getCookies
4. ServletResponse实现，可处理http响应，写入socket输出流
5. http请求分发器，匹配请求url分发到对应的servlet实例中处理