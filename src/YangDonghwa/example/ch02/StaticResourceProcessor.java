package ch02;

import java.io.IOException;

// 정적 자원에 대한 요청을 처리하는 클래스
public class StaticResourceProcessor {

	public void process(Request request, Response response) {
		try {
			response.sendStaticResource();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
