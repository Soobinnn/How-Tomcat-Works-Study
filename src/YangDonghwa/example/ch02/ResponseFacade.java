package ch02;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ResponseFacade implements ServletResponse {
	private ServletResponse response = null;
	
	public ResponseFacade(Response response) {
		this.response = response;
	}
	
	/* 이하 ServletResponse의 구현 */
	// facade 클래스를 이용하여 보안 문제 해결
	@Override
	public void flushBuffer() throws IOException {
	}
	@Override
	public int getBufferSize() {
		return response.getBufferSize();
	}
	@Override
	public String getCharacterEncoding() {
		return response.getCharacterEncoding();
	}
	@Override
	public String getContentType() {
		return response.getContentType();
	}
	@Override
	public Locale getLocale() {
		return null;
	}
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return null;
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		return response.getWriter();
	}
	
	@Override
	public boolean isCommitted() {
		return false;
	}
	@Override
	public void reset() {
	}
	@Override
	public void resetBuffer() {
	}
	@Override
	public void setBufferSize(int arg0) {
	}
	@Override
	public void setCharacterEncoding(String arg0) {
	}
	@Override
	public void setContentLength(int arg0) {
	}
	@Override
	public void setContentType(String arg0) {
	}
	@Override
	public void setLocale(Locale arg0) {
	}
}
