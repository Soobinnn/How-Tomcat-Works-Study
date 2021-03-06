# 4장

## 배경

### 웹 서버인 아파치와 WAS인 톰캣

~~~
1. 톰캣은 정적인 페이지를 로드하기에 아파치보다 느린 처리 속도를 보인다.
2. 톰캣은 설정할 수 있는 내용들이 아파치에 비해 부족하다.
3. 톰캣은아파치보다 부하에 약하다.
4. 톰캣만으로는 서블릿/JSP밖에 서비스할 수 없다.
5. 톰캣과 아파치를 연동하여 부하분담이 가능하다. (다수의 톰캣 구성으로의 로드밸러싱의 의미가 아닌 톰캣만으로 처리가능한 static page를 apache에서 대신함으로써의 부하를 분담)
~~~

##### 그러나 1번 5번 이유는 톰캣 5.5 이상에서 httpd의 native 모듈을 사용해서 static page를 처리하는 기능을 제공함으로 이 경우 httpd와 톰캣이 같은 모듈을 사용하는 셈이니 성능에서 차이가 날 이유가 없어짐

### 그럼에도 불구하고 아파치 + 톰캣을 사용하는 이유는?

~~~
1. 80 포트의 사용권한
 - 리눅스에서 ~1024 포트까지는 root 계정만 사용이 가능하므로, tomcat을 80 포트로 운영하기에는 문제.
   (기본적으로 root 계정이 아니면 톰켓을 80 포트로 서버를 열 수 없음.)
   (열더라도 접속이 되지 않으며, 억지로 하면 방화벽을 이용하는 방법이 있긴하다.)
   (리눅스에서는 1024 이하의 포트는 root 이외의 계정에게는 권한을 주지 않음.)
   (보안이 문제인데 톰켓을 root 권한으로 80포트로 돌릴때 만약 tomcat이 뚫리면 해커는 리눅스 서버를 root 권한으로 서버를 해킹)
   (톰캣은 서버 접속자에게 같은 권한을 준다.)
2. 정적 데이터 처리
 - 톰캣의 성능이 아무리 좋아졌다고 해도, image/css 등과 같은 정적 데이터는 httpd에서 처리하는게 더 믿음직 하다.
   또한 압축등과 같은 정적 데이터를 처리하는 다양한 옵션등이 존재한다.(물론 압축 전송 옵션은 톰캣7에도 있다)
3. 로드 밸런싱
 - 동일 서버내에서 한 서비스를 여러 tomcat에서 서비스하고자 할때 사용한다. apache를 연동하는 가장 큰 목적.
~~~

#### 커넥터는 아파치와 톰캣 중간에 위치해 있으며, 아파치로 들어온 URL이 JSP 또는 DO 호출 방식이면 톰캣 커넥터로 연결되어 톰캣으로 연결되며, URL이 HTML 혹은 정적파일이라면 아파치에서 처리.

<br>

## 커넥터

카탈리나의 주요 모듈중 하나로 요청을 컨테이너에 연결해주는 모듈.

커넥터는 전달 받은 각 HTTP Request에 대해 Resquest Object와 Response Object를 구성하고 컨테이너에 전달.

이후 컨테이너는 커넥터로부터 Request, Response Object를 전달받고, 서블릿의 Service Method를 호출하는 구조.

<br>

## 톰캣의 기본 커넥터

여기서 Connector는 톰캣 혼자서(아파치-와 같은 웹서버 없이) 구동되어서 하나의 웹서버로서 활용 되기 위한 용도의 Connector

서블릿 컨테이너에 플러그인이 될 수 있는 독립적인 모듈.

코요테, mod_jk, mod_jk2, mod_webapp와 같은 다양한 커넥터가 존재함

기본적으로 톰캣 커넥터는 다음과 같은 요구 조건을 충족해야함

- org.apache.catalina.Connector 인터페이스 구현

- org.apache.catalina.Request 인터페이스를 구현하는 Request Object 생성

- org.apache.catalina.Response 인터페이스 구현하는 Response Object 생성

<br>

톰캣의 기본 커넥터도 커넥터와 비슷하게 작동.

들어오는 HTTP Request를 기다리고, Request, Response를 생성, 컨테이너로 Request, Response Object를 전달.

커넥터는 org.apache.catalina.Container 인터페이스의 invoke Method를 통해 Request/Response Object를 컨테이너로 전달.

이후 컨테이너는 invoke Method 내에서 Servlet Class Load, service Method Call, Session Management, Error Message Logging 등 다양한 일을 함.

<br>

4장의 기본 커넥터는 3장의 커넥터에 없는 몇 가지 최적화 기법이 적용됨.

첫째로 다양한 객체를 위한 풀을 제공하여 과도한 객체 생성을 방지.

둘째로 코드 내의 다양한 곳에서 문자열 대신 문자의 배열을 사용함으로 애플리케이션 성능을 향상시킴.

~~~
1. String은 자바에서 불변하므로 암호가 일반 텍스트로 저장되는 경우 가비지 컬렉터가 암호를 지울 때까지 메모리에서 사용될 수 있으며, 문자열이 재사용성을 위해 String Pool에서 사용되므로 남이있을 가능성이 크고, 장기간 동안 메모리에 저장되어 보안 위협이 됨.

2. 메모리 덤프에 액세스 권한이 있는 사용자는 일반 텍스트로 암호를 찾을 수 있게 됨. 이가, 일반 텍스트보다 암호화 된 암호를 사용하는 또 다른 이유. 따라서 Char Array에 암호를 저장하면 암호를 도용하는 보안 위험에서 완화 됨.

3. Char Array를 사용하면 명시적으로 데이터를 지우거나, 덮을 쓸 수 있어 시스템의 어느 위치에도 암호가 나타나지 않음. 일반 문자열을 사용하게되면, 비밀번호를 로그에 프린트할 확률이 훨씬 높아짐.
~~~

또 한 가지 주목할 것은, HTTP 0.9와 HTTP 1.0 뿐만 아니라 HTTP 1.1의 새로운 기능까지도 모두 지원하게 되므로, HTTP 커넥터의 내부 작동 원리를 이해하기 위해 HTTP 1.1의 새로운 세 가지 특징을 알아보아야 함.

<br>

## HTTP 1.1의 새로운 특징

### 지속 연결성

HTTP 1.1 이전에는 웹 서버가 요청받은 자원을 브라우저에게 전송하면 즉시 연결을 닫게 했었음. 그러나 브라우저가 페이지를 요청할 때, 페이지와 그 밖의 자원에 대해 각각 연결해서 다운로드 되어야 한다면 이 과정은 전체적으로 느려질 수 밖에 없음. 이것이 HTTP 1.1에서 지속 연결을 지원하는 이유.

지속 연결은 HTTP 1.1의 기본 연결 방법으로, connection Request Header에 keep-alive 값을 지정함으로 명시적으로 지속 연결을 사용
~~~
connection: keep-alive
~~~

### 청크 인코딩

지속 연결성으로 서버는 여러 자원에서 바이트 스트림을 전송할 수 있게 됐고, 클라이언트는 동일한 연결을 다중 요청할 수 있게 됐음.

따라서 데이터를 전송하는 측에서는 수신하는 측이 올바르게 바이트를 해석할 수 있게 요청이나 응답의 컨텐츠 길이를 헤더에 포함해야하나, 전송하는 측에서 바이트를 얼마나 전송할지 미리 알 수 없는 경우가 많다.

~~~
HTTP 1.0 에서는 서버는 단지 content-length 헤더를 무시하고 데이터를 계속 쓰기만 하며, 클라이언트는 파일의 끝에 도달했다는 의미인 '-1' 값을 받을 때까지 계속 읽었음
~~~

HTTP 1.1에서는 transfer-encoding이라는 특별헨 헤더를 통해 바이트 스트림이 chunk 단위로 전송될 것임을 가리키며, chunk에서는 실제 데이터가 전송되기 전에 16진수로 된 길이 값과 캐리지 리턴/개행 문자가 먼저 전송되고 chunk의 길이가 0이면 하나의 트랜잭션이 종료.

~~~
38바이트의 데이터를 2개의 chunk로 전송하는 예
1D\r\n --> 16진수로 첫번째자리 1 = 16, 두번째자리 D = 13
I'm as helpless as a kitten u --> 총 29바이트   
9\r\n --> 9를 나타냄
p a tree. --> 총 9바이트
0\r\n --> 트랜잭션 종료
~~~

### 100 상태 코드의 사용

HTTP 1.1 클라이언트에서는 서버에게 데이터 수신 거절 여부를 알아보고자 Request Body를 전송하는 낭비를 없애기 위해, Expect: 100-continue 헤더를 전송해 Request Body를 전송하기에 앞서 서버측의 승인을 기다림. 이 방법은 클라이언트가 전송하고자 하는 요청 본체의 양이 많을 경우, 서버가 이를 받아들일 수 있는지 먼저 확인하고자 할 때 흔히 사용함.

~~~
클라이언트 -> Expect: 100-continue 데이터를 보내도 될까요?
서버 -> HTTP/1.1 100 Continue 네, 보내세요
~~~

<br>

## Connector 인터페이스
커넥터는 반드시 org.apache.catalina.Connector 인터페이스를 구현해야 함.

주로 컨테이너를 연결할 때 사용하는 setContainer, 커넥터와 연결되어있는 컨테이너를 리턴하는 getContainer, HTTP Request에 대한 Request Object를 생성하는 createRequest, Response Object를 생성하는 createResponse가 있다.
