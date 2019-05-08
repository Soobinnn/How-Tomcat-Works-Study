package ch02;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

// 서블릿의 HTTP 요청을 처리하는 역할을 한다.
public class ServletProcessor1 {

	public void process(Request request, Response response) {
		String uri = request.getUri();
		String servletName = uri.substring(uri.lastIndexOf("/") + 1);
		URLClassLoader loader = null;
		try {
			// URLClassLoader의 생성
			URL[] urls = new URL[1];
			URLStreamHandler streamHandler = null;
			File classPath = new File(Constants.WEB_ROOT);
			
			/*
			 * 저장소를 만드는 부분은
			 * org.apache.catalina.loader.StandardClassLoader의 addRepository 메소드에서 차용
			 */
			String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
			
			/*
			 * URL을 만드는 부분은
			 * org.apache.catalina.startup.ClassLoaderFactory의 createClassLoader 메소드에서 차용
			 */
			urls[0] = new URL(null, repository, streamHandler);
			loader = new URLClassLoader(urls);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		
		Class myClass = null;
		try {
			myClass = loader.loadClass(servletName);
		} catch(ClassNotFoundException e) {
			System.out.println(e.toString());
		}
		
		Servlet servlet = null;
		
		try {
			servlet = (Servlet) myClass.newInstance();
			servlet.service((ServletRequest) request, (ServletResponse) response);
		} catch(Exception e) {
			System.out.println(e.toString());
		} catch(Throwable e) {
			System.out.println(e.toString());
		}
	}
}
