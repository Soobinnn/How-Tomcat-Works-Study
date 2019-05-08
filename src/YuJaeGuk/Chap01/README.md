# 1장

## 간단한 웹 서버

자바 웹 서버가 어떻게 동작하는지 설명하는 장.

웹 서버는 클라이언트(브라우저)와 통신할 때 HTTP를 사용하기 때문에 HTTP 서버라고도 함.

자바기반 웹 서버는 java.net.Socket 과 java.net.ServerSocket 2개의 클래스를 사용하며, HTTP 통신을 통해서 수행.

따라서 1장에서는 먼저 HTTP와 2개의 클래스를 다룸.

<br>


### HTTP

Hypertext Transfer Protocol

인터넷에서 웹 서버와 클라이언트(브라우저)가 데이터를 주고받는 데 필요한 프로토콜.

Request / Response 방식의 프로토콜으로 클라이언트가 파일을 요청하면, 웹 서버는 그 요청에 대해서만 응답하는 구조.

신뢰성 높은 TCP 연결을 사용하며, TCP 80 포트를 기본으로 사용.

HTTP에서는 항상 클라이언트가 연결을 맺고 HTTP 요청을 송신함으로써 트랜잭션이 시작.

웹 서버는 스스로 클라이언트에 연결을 맺지 않으며, 클라이언트로의 응답 연결을 시도하지도 않음.

<br>

#### HTTP Request

##### HTTP Request의 3가지 요소

###### - Method URI(Uniform Resource Identifier) Protocol/Version

###### - Request headers

###### - Entity body

<br>

##### HTTP Request Message Example

~~~
POST /examples/default.jsp HTTP/1.1
Accept: text/plain; text/html
Accept-Language: en-gb
Connection: Keep-Alive
Host: localhost
User-Agent: Mozilla/4.0 (compatible; MSIE 4.01; Windows98)
Content-Length: 33
Content-Type: application/x-www-form-urlencoded
Accept-Encoding: gzip, deflate

lastName=Franks&firstName=Michael
~~~
<br>

##### Request의 첫째 줄에는 METHOD-URI-PROTOCOL이 표시.

###### POST /examples/default.jsp HTTP/1.1

##### POST는 Method를 나타내며, /examples/default.jsp는 URI를, HTTP/1.1은 Protocol/Version을 나타냄.

##### 각각의 HTTP Request는 HTTP 표준에 명시되어있는 다양한 요청메소드 가운데 하나를 사용할 수 있음.

<br>

<b>W3의 HTTP 1.1 Method Define</b>

[https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html]

<br>

##### URI는 인터넷 자원을 지정.

##### 대부분의 경우, URI는 서버의 최상위 디렉토리의 상대경로로 나타냄으로 URI는 항상 슬래시 '/'로 시작.

##### Protocol/Version은 사용하고 있는 HTTP Protocol의 Version을 나타냄.

<br>

##### Request Headers에는 클라이언트 환경에 관한 유용한 정보를 포함.

##### 예를 들어 브라우저에 설정되어있는 언어, 문서 본체의 길이 등. 각 개별의 헤더 정보는 캐리지 리턴과 개행문자(CRLF)로 구분됨.

<br>

##### Request headers와 Entity body사이에는 빈 줄이 하나 존재하는데, 이는 HTTP Request 서식에 있어서 본체가 어디서부터 시작하는지 알려주는 역할.

##### 방금 살펴본 HTTP Request Example에서는 다음과 같은 간단한 본체가 있음.

###### lastName=Franks&firstName=Michael

###### ( 대개의 경우 HTTP Request의 본체는 이보다 훨씬 길음 )

<br>

#### HTTP Response

##### HTTP Request와 마찬가지로 HTTP Response 역시 다음 3가지 요소로 구성.

###### Protocol Status-Code Description

###### Response headers

###### Entity body

<br>

##### HTTP Response Message Example
~~~
HTTP/1.1 200 OK
Server: Microsoft-IIS/4.0
Date: Mon, 5 Jan 2004 13:13:33 GMT
Content-Type: text/html
Last-Modified: Mon, 5 Jan 2004 13:13:12 GMT
Content-Length: 112

<html>  
<head>
<title>HTTP Response Example</title>
</head>
<body>
Welcome to Brainy Software
</body>
</html>
~~~

<br>

##### Response headers의 첫 줄은 Request headers와 비슷.

##### Protocol은 HTTP Version 1.1을 사용. Request는 문제 없이 처리(200=성공 코드). 모든 사항이 이상 없음(OK).

<br>

##### Response headers는 Resquest headers와 마찬가지로 클라이언트에게 유용한 정보를 포함.

<br>

##### Response headers와 Entity Body는 빈 줄로 구분되어있으며, Entity body는 HTML 컨텐츠

<br>

### Socket Class

##### 소켓은 네트워크 연결에 있어서 End point에 해당.

##### 애플리케이션은 소켓을 사용해 네트워크에 데이터를 읽거나 씀.

##### 서로 다른 2개의 컴퓨터에 있는 소프트웨어 애플리케이션은, 네트워크 연결을 통해 Byte Stream을 전송하고 수신하며 서로간에 통신.

##### 한 애플리케이션에서 다른 애플리케이션으로 메시지를 전달하려면 상대방 컴퓨터의 IP와 상대방 애플리케이션의 소켓이 사용하는 포트 번호를 알아야 함.

##### 자바에서 소켓은 java.net.Socket 클래스에 해당.

<b>Oracle Java의 Socket Class</b>

[https://docs.oracle.com/javase/8/docs/api/java/net/Socket.html]

<br>

### ServerSocket Class

##### Socket 클래스는 어디에서든 원격 서버 애플리케이션에 연결할 수 있는 '클라이언트' 소켓을 나타냄.

##### 그러나 HTTP 서버, FTP 서버 애플리케이션을 구현할 때는 다른 방식으로 접근해야함.

##### 서버 애플리케이션은 특성상 클라이언트가 언제 접속을 시도할지 알 수 없는 상태에서 항상 대기하고 있어야 하기 때문.

##### 서버 애플리케이션이 항상 접속을 대기하게 하려면 java.net.ServerSocket 클래스를 사용함.

<b>Oracle Java의 ServerSocket Class</b>

[https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html]

<br>

<br>

### 참고 문헌

[https://ko.wikipedia.org/wiki/HTTP]

[https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html]

[https://docs.oracle.com/javase/8/docs/api/java/net/Socket.html]

[https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html]
