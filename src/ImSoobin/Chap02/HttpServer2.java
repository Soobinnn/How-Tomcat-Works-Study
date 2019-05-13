package ImSoobin.Chap02;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class HttpServer2 
{
  // 종료명령
  private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

  // 종료 명령을 받았는지 여부
  private boolean shutdown = false;

  public static void main(String[] args) 
  {
    HttpServer2 server = new HttpServer2();
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

        // create Request object and parse
        Request request = new Request(input);
        request.parse();

        // create Response object
        Response response = new Response(output);
        response.setRequest(request);

        // 서블릿에 대한 요청인지 정적 자원에 대한 요청인지 확인
        // 서블릿에 대한 요청은 "/servlet"으로 시작
        if (request.getUri().startsWith("/servlet/")) 
        {
          ServletProcessor2 processor = new ServletProcessor2();
          processor.process(request, response);
        }
        else {
          StaticResourceProcessor processor = new StaticResourceProcessor();
          processor.process(request, response);
        }
        
        // 소켓 닫기
        socket.close();
        // URL이 종료 명령이었는지 확인
        shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
      }
      catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
  }
}
