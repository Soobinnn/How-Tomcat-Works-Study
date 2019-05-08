package ch02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RequestFacade implements ServletRequest {
	private ServletRequest request = null;
	
	public RequestFacade(Request request) {
		this.request = request;
	}
	
	/* 이하 ServletRequest의 구현 */
	// facade 클래스를 이용하여 보안 문제 해결
	@Override
	public AsyncContext getAsyncContext() {
		return request.getAsyncContext();
	}
	@Override
	public Object getAttribute(String arg0) {
		return request.getAttribute(arg0);
	}
	@Override
	public Enumeration<String> getAttributeNames() {
		return request.getAttributeNames();
	}
	@Override
	public String getCharacterEncoding() {
		return null;
	}
	@Override
	public int getContentLength() {
		return 0;
	}
	@Override
	public String getContentType() {
		return null;
	}
	@Override
	public DispatcherType getDispatcherType() {
		return null;
	}
	@Override
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}
	@Override
	public String getLocalAddr() {
		return null;
	}
	@Override
	public String getLocalName() {
		return null;
	}
	@Override
	public int getLocalPort() {
		return 0;
	}
	@Override
	public Locale getLocale() {
		return null;
	}
	@Override
	public Enumeration<Locale> getLocales() {
		return null;
	}
	@Override
	public String getParameter(String arg0) {
		return null;
	}
	@Override
	public Map<String, String[]> getParameterMap() {
		return null;
	}
	@Override
	public Enumeration<String> getParameterNames() {
		return null;
	}
	@Override
	public String[] getParameterValues(String arg0) {
		return null;
	}
	@Override
	public String getProtocol() {
		return null;
	}
	@Override
	public BufferedReader getReader() throws IOException {
		return null;
	}
	@Override
	public String getRealPath(String arg0) {
		return null;
	}
	@Override
	public String getRemoteAddr() {
		return null;
	}
	@Override
	public String getRemoteHost() {
		return null;
	}
	@Override
	public int getRemotePort() {
		return 0;
	}
	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}
	@Override
	public String getScheme() {
		return null;
	}
	@Override
	public String getServerName() {
		return null;
	}
	@Override
	public int getServerPort() {
		return 0;
	}
	@Override
	public ServletContext getServletContext() {
		return null;
	}
	@Override
	public boolean isAsyncStarted() {
		return false;
	}
	@Override
	public boolean isAsyncSupported() {
		return false;
	}
	@Override
	public boolean isSecure() {
		return false;
	}
	@Override
	public void removeAttribute(String arg0) {
	}
	@Override
	public void setAttribute(String arg0, Object arg1) {
	}
	@Override
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
	}
	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return null;
	}
	@Override
	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) throws IllegalStateException {
		return null;
	}
}
