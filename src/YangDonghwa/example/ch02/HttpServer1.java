package ch02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer1 {
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";	// 종료 명령
	private boolean shutdown = false;	// 종료 명령을 받았는가?
	
	public static void main(String[] args) {
		HttpServer1 server = new HttpServer1();
		server.await();
	}

	private void await() {
		ServerSocket serverSocket = null;
		int port = 8080;
		String host = "127.0.0.1";
		
		try {
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName(host));
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// 요청을 기다리는 루프
		while(!shutdown) {
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try {
				socket = serverSocket.accept();
				input = socket.getInputStream();
				output = socket.getOutputStream();
				
				// request 객체 생성 및 parse 호출
				Request request = new Request(input);
				request.parse();
				
				// response 객체 생성
				Response response = new Response(output);
				response.setRequest(request);
				
				// 서블릿에 대한 요청("/servlet/")인지 정적 자원에 대한 요청인지 확인
				if(request.getUri().startsWith("/servlet/")) {	// 서블릿
					ServletProcessor1 processor = new ServletProcessor1();
					processor.process(request, response);
				} else {	// 정적 자원
					StaticResourceProcessor processor = new StaticResourceProcessor();
					processor.process(request, response);
				}
				
				// 소켓 닫기
				socket.close();
				// URL이 종료 명령이었는지 확인
				shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
