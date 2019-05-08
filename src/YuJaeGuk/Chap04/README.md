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

## 커넥터는 아파치와 톰캣 중간에 위치해 있으며, 아파치로 들어온 URL이 JSP 또는 DO 호출 방식이면 톰캣 커넥터로 연결되어 톰캣으로 연결되며, URL이 HTML 혹은 정적파일이라면 아파치에서 처리.

<br>

## 커넥터

카탈리나의 주요 모듈중 하나로 요청을 컨테이너에 연결해주는 모듈.

커넥터는 전달 받은 각 HTTP Request에 대해 Resquest Object와 Response Object를 구성하고 컨테이너에 전달.

이후 컨테이너는 커넥터로부터 Request, Response Object를 전달받고, 서블릿의 Service Method를 호출하는 구조.

<br>

## 톰캣의 기본 커넥터

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



### 참고 문헌

[https://kshmc.tistory.com/entry/Apache-Tomcat-%EC%97%B0%EB%8F%99%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0]

[https://f10024.tistory.com/7]
