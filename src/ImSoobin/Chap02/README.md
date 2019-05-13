
# 2장 - 간단한 서블릿 컨테이너

### 작성자

임수빈


## 서블릿 컨테이너

: 간단한 서블릿 뿐만아니라 정적 자원도 처리할 수 있다.

서블릿 프로그래밍은 javax.servlet,  javax.servlet.http 패키지의 클래스/인터페이스를 통해 이뤄짐

__* javax.servlet.Servlet은 가장 중요한 인터페이스__

**모든 서블릿은 이 인터페이스를 구현하거나 구현한 클래스를 확장해야함**

### javax.servlet.Servlet 인터페이스의 메소드

	- public void init (ServletConfig config) throws ServletException

	- public void service(ServletRequest request, ServletResponse response)

	- public void destroy()

	- public ServletConfig getServletConfig()

	- pulbic java.lang.String getServletInfo()
	- 

## 서블릿 생명주기 관련 메소드
__* init, service, destroy는 서블릿의 생명주기와 관련된 메소드__

### 1. init 
 
 서블릿 클래스를 인스턴스화한 뒤, 서블릿 컨테이너가 호출함.
 서블릿 컨테이너는 이 메소드를 __정확히 한번 호출__ 함으로써

__해당 서블릿이 서비스를 할 수 있는 준비가 됐다는 것을 표시함.__

 서블릿이 어떤 요청을 받기 전에 성공적으로 끝나야함.

DB드라이버나 어떤 초기값을 로드하는 등 오직 한번만 실행되는 코드만 재정의할 수 있음.
(그렇지 않을 경우, 메소드는 대개 비워두는 것이 보통이다)

### 2. service

** 서블릿 컨테이너는 서블릿 요청이 있을 때마다 service메소드를 호출함. 이 때, 서블릿 컨테이너는 javax.servlet.ServletRequest 객체, javax.servlet.ServletResponse객체를 service메소드에 전달함.

ServletRequest객체에는 클라이언트의 HTTP 요청에 관한 정보가 있으며,
ServletResponse 객체는 서블릿의 응답에 관한 정보를 캡슐화함.

 service메소드는 서블릿의 생명주기 동안 자주 호출됨.

### 3. destroy

  서블릿 컨테이너는 서비스 영역으로부터 서블릿 인스턴스를 제거하기 앞서 해당 서블릿 destroy메소드를 호출함.
서블릿 컨테이너가 종료되거나, 서블릿 컨테이너가 메모리를 확보해야 할 경우 발생함.

** 서블릿의 service메소드로부터 모든 스레드가 빠져나간 뒤나 제한시간이 지난 뒤에 호출됨.

destory메소드를 호출하고 나면, 동일한 서블릿에 대해서 service메소드를 호출하지 않음.
destory는 서블릿이 소유하고 있던 메모리, 파일 핸들, 스레드 등과 같은 자원을 깨끗이 반환할 수 있는 기회를 제공

모든 영속적인 상태가 메모리상의 현재 서블릿의 상태와 확실히 동기화할 수 있게끔 함.

## 서블릿의 HTTP요청 처리 방법
1. 어떤 서블릿을 처음으로 요청을 받았을 때, 해당 서블릿 클래스를 로드, 서블릿의 init메소드를 호출
2. 
3. 각 요청에서 javax.servlet.ServletRequest, javax.servlet.ServletResponse인스턴스 생성
4. 
5. ServletRequest, ServletResponse 객체 전달 -> 서블릿의 service메소드 호출
6. 
7. 서블릿 클래스를 종료하면서 서블릿의 destroy 메소드 호출

---
### 간단한 서블릿컨테이너 (2장) 예제 코드의 하는 일
1. HTTP 요청을 기다림

2. 서블릿을 처음으로 요청받을때, 해당 서블릿 클래스 로드 -> 서블릿의 init메소드를 딱 한번 호출

3. 각 요청에서 javax.servlet.ServletRequest, javax.servlet.ServletResponse인스턴스 생성

4. ServletRequest, ServletResponse객체를 전달해 서블릿의 service메소드 호출

5. 서블릿 클래스를 종료하면서, 서블릿의 destroy메소드 호출, 서블릿 클래스 언로드

6. 서블릿 로드

  - 클래스 로드 생성 후 클래스 경로알려줌
  * 서블릿 로드 시 java.net.URLClassLoader클래스를 사용할 수 있음.

이 클래스는 java.lang.ClassLoader의 자식 클래스임. URLClassLoader의 인스턴스를 생성한 뒤에 로드할 수 있음.

* 서블릿 컨테이너에서는 클래스 로더가 서블릿 클래스를 찾는 위치를 저장소라고 부름

__** 2장에서의 서블릿 컨테이너는 서블릿이 요청을 받을 때마다 항상 서블릿 클래스를 로드한다.__

### 코드예제 
###  1. 애플리케이션1
#### -  HTTPServer1
```java
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class HttpServer1 
{
  /** WEB_ROOT는 HTML 또는 그 밖의 파일이 위치하는 디렉토리이다.
   *  이 애플리케이션에서의 WEB_ROOT는 작업 디렉토리 아래의 "webroot"라는 디렉토리이다.
   *  작업 디렉토리는 java 명령이 실행되는 현재의 위치를 말한다.
   */
  // 종료명령
  private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

  // 종료 명령을 받았는지 여부
  private boolean shutdown = false;
  
  public static void main(String[] args) 
  {
    HttpServer1 server = new HttpServer1();
    server.await();
  }

  public void await() 
  {
    ServerSocket serverSocket = null;
    int port = 8080;
    
    try 
    {
      serverSocket =  new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
    }
    catch (IOException e) 
    {
      e.printStackTrace();
      System.exit(1);
    }

    // 요청을 기다리는 루프
    while (!shutdown) 
    {
      Socket socket = null;
      InputStream input = null;
      OutputStream output = null;
      try 
      {
        socket = serverSocket.accept();
        input = socket.getInputStream();
        output = socket.getOutputStream();

        // Request 객체 생성 및 parse 호출
        Request request = new Request(input);
        request.parse();

        // Response 객체 생성
        Response response = new Response(output);
        response.setRequest(request);

        // 서블릿에 대한 요청인지 정적 자원에 대한 요청인지 확인
        // 서블릿에 대한 요청은 "/servlet"으로 시작
        if (request.getUri().startsWith("/servlet/")) 
        {
          ServletProcessor1 processor = new ServletProcessor1();
          processor.process(request, response);
        }
        else 
        {
          StaticResourceProcessor processor = new StaticResourceProcessor();
          processor.process(request, response);
        }
        
        // 소켓 닫기
        socket.close();
        // URL이 종료 명령이었는지 확인
        shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
      }
      catch (Exception e) 
      {
        e.printStackTrace();
        System.exit(1);
      }
    }
  }
}
```

#### - Request
```java
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;


public class Request implements ServletRequest 
{
  private InputStream input;
  private String uri;

  public Request(InputStream input) 
  {
    this.input = input;
  }

  public String getUri() 
  {
    return uri;
  }

  private String parseUri(String requestString) 
  {
    int index1, index2;
    index1 = requestString.indexOf(' ');
    System.out.println("인덱스 테스트"+index1);
    if (index1 != -1) 
    {
      index2 = requestString.indexOf(' ', index1 + 1);
      if (index2 > index1)
      {
    	  System.out.println("인덱스 테스트2"+requestString.substring(index1 + 1, index2));
    	  return requestString.substring(index1 + 1, index2);
      }
    }
    return null;
  }

  public void parse() 
  {
    // 소켓으로부터 일련의 문자들을 읽음
    StringBuffer request = new StringBuffer(2048);
    int i;
    byte[] buffer = new byte[2048];
    try 
    {
      i = input.read(buffer);
    }
    catch (IOException e) 
    {
      e.printStackTrace();
      i = -1;
    }
    for (int j=0; j<i; j++) 
    {
      request.append((char) buffer[j]);
    }
    System.out.println("[파싱]");
    System.out.print(request.toString());
    uri = parseUri(request.toString());
  }

  /* implementation of the ServletRequest*/
  ...
```
#### - Response
```java
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;

public class Response implements ServletResponse 
{
  private static final int BUFFER_SIZE = 1024;
  Request request;
  OutputStream output;
  PrintWriter writer;

  public Response(OutputStream output) 
  {
    this.output = output;
  }

  public void setRequest(Request request) 
  {
    this.request = request;
  }

  /* 이 메소드는 정적 페이지를 서비스 할때 사용됨.*/
  public void sendStaticResource() throws IOException 
  {
    byte[] bytes = new byte[BUFFER_SIZE];
    FileInputStream fis = null;
    try 
    {
      /* request.getUri has been replaced by request.getRequestURI */
      File file = new File(Constants.WEB_ROOT, request.getUri());
      fis = new FileInputStream(file);
      /*
         HTTP Response = Status-Line
           *(( general-header | response-header | entity-header ) CRLF)
           CRLF
           [ message-body ]
         Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
      */
      int ch = fis.read(bytes, 0, BUFFER_SIZE);
      while (ch!=-1) 
      {
        output.write(bytes, 0, ch);
        ch = fis.read(bytes, 0, BUFFER_SIZE);
      }
    }
    catch (FileNotFoundException e) 
    {
      String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
        "Content-Type: text/html\r\n" +
        "Content-Length: 23\r\n" +
        "\r\n" +
        "<h1>File Not Found</h1>";
      output.write(errorMessage.getBytes());
    }
    finally 
    {
      if (fis!=null)
        fis.close();
    }
  }

  /** implementation of ServletResponse  */
  ...
```
#### - StaticResourceProcessor
```java
import java.io.IOException;

public class StaticResourceProcessor 
{
  public void process(Request request, Response response) 
  {
    try 
    {
      response.sendStaticResource();
    }
    catch (IOException e) 
    {
      e.printStackTrace();
    }
  }
}
```
#### - ServletProcessor1
```java

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
```

### 애플리케이션2

** 위의 애플리케이션1에는 심각한 문제 존재함.
ServletProcessor1에서 process 메소드는 javax.servlet.ServletRequest로 업캐스팅해서 서블릿의 service메소드로 전달함. (Response도 동일)

__이와 같은 방식은 보안 관련한 문제를 야기함__

이 서블릿 컨테이너의 내부를 알고 있는 프로그래머라면, ServletRequest, ServletResponse를 각각 다운캐스팅해서
public 메소드를 호출할 수 있을 것이다.
Request 인스턴스로 parse메소드, Response 인스턴스로 sendStaticResource 메소드를 호출하는 것이 가능하다.

** 그렇다고 parse, sendStaticResource를 private로 만들 수 없음.
(Httpserver에서 이들 메소드를 호출해야 하기 때문에)

두 메소드는 서블릿내에서 사용되는 것을 전제로 하지 않음.

__해결방법__
1. Request, Response클래스에 디폴트 접근 지정자를 적용하고 패키지 외부에서는 사용할 수 없게 하는 것
2. ** 퍼사드 클래스를 사용하는 것

```java
import java.io.IOException;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

public class RequestFacade implements ServletRequest 
{

  private ServletRequest request = null;

  public RequestFacade(Request request) 
  {
    this.request = request;
  }
  /* implementation of the ServletRequest*/
  public Object getAttribute(String attribute) 
  {
    return request.getAttribute(attribute);
  }
  public Enumeration getAttributeNames() 
  {
    return request.getAttributeNames();
  }
  
  ...
```
```java
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;

public class ResponseFacade implements ServletResponse {

  private ServletResponse response;
  
  public ResponseFacade(Response response) {
    this.response = response;
  }

  public void flushBuffer() throws IOException {
    response.flushBuffer();
  }

  public int getBufferSize() {
    return response.getBufferSize();
  }
  
...
```
[퍼사드 패턴](https://github.com/Soobinnn/Design-Pattern-Study/blob/master/src/facade/facade.md)
 
 : 일련의 복잡한 클래스들을 단순화
 서로 긴밀하게 연결되지 않아도 되고. **최소 지식 원칙**(Principle of Least Knowledge)**** 을 준수하는데도 도움

** 여기서는 인터페이스의 단순화가 아닌, 클래스에 대한 접근 제어를 목적으로 이용
 

