package ImSoobin.Chap02;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.io.File;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ServletProcessor1 
{

  public void process(Request request, Response response) 
  {

    String uri = request.getUri();
    String servletName = uri.substring(uri.lastIndexOf("/") + 1);
    System.out.println("서블릿이름은~?");
    URLClassLoader loader = null;

    try 
    {
      // URLClassLoader 생성
      URL[] urls = new URL[1];
      URLStreamHandler streamHandler = null;
      File classPath = new File(Constants.WEB_ROOT);
      // 저장소를 만드는 부분은
      // org.apache.catalina.startup.ClassLoaderFactory의 addRepository메소드에서 사용
      String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString() ;
      // URL을 만드는 부분은 
      // org.apache.catalina.loader.StandardClassLoader class 
      urls[0] = new URL(null, repository, streamHandler);
      loader = new URLClassLoader(urls);
    }
    catch (IOException e) 
    {
      System.out.println(e.toString());
    }
    Class myClass = null;
    try 
    {
      myClass = loader.loadClass(servletName);
    }
    catch (ClassNotFoundException e) 
    {
      System.out.println(e.toString());
    }

    Servlet servlet = null;

    try 
    {
      servlet = (Servlet) myClass.newInstance();
      servlet.service((ServletRequest) request, (ServletResponse) response);
    }
    catch (Exception e) 
    {
      System.out.println(e.toString());
    }
    catch (Throwable e) 
    {
      System.out.println(e.toString());
    }

  }
}