# 10장

## 보안

특정 컨텐츠에 대한 접근을 제한하기 위한 웹 애플리케이션의 보안에 대해 설명.

10장은 보안과 관련해 보안 주체(pincipals), 보안 역할(roles), 로그인 설정(login config), 인증자(authenticators) 등에 대한 개념을 다룸.

<br>

서블릿 기술은 web.xml 파일의 설정을 통해 원하는 컨텐츠에 보안 제약을 적용할 수 있도록 지원.

서블릿 컨테이너는 인증자(authenticator)라는 밸브를 통해 보안 제약 기능을 지원.

서블릿 컨테이너가 구동될 때 인증자 밸브는 컨텍스트의 파이프라인에 추가되고, 래퍼 밸브보다 이전에 호출되어.

사용자가 올바른 사용자 이름과 암호를 입력했다면 인증자 밸브는 요청을 처리할 다음 밸브를 호출하고, 인증에 실패하면 인증자 밸브는 다음 밸브를 호출하지 않고 리턴되어 사용자는 요청한 서블릿을 볼 수 없음.

이 과정에서 인증자 밸브는 유효한 사용자 이름과 암호의 집합을 가지고 있는 영역(Realm) 컴포넌트에 존재하는 authenticate 메소드에 사용자 이름과 암호를 전달하여 호출.

<br>

### Realm(영역)

Realm은 사용자를 인증하는 데 필요한 컴포넌트. 

보통 하나의 컨텍스트에 연결되며, 하나의 컨테이너는 하나의 영역만을 가질 수 있음.

영역은 사용자의 이름과 암호를 지닐 수 있으며, 또는 사용자 이름과 암호가 저장된 저장소에 접근할 수 있음.

~~~
보통 톰캣에서는 tomcat-users.xml 파일에 저장하지만, 관계형 데이터베이스 같은 다른 자원을 이용해 인증할 수 있는 영역을 구현할 수 있다.
~~~

카탈리나에서는 "org.apache.catalina.Realm" 인터페이스로 대변되며, 이 인터페이스의 가장 중요한 4개의 메소드는 주체를 리턴받는 authenticate 메소드를 가지고 있음.

~~~
public principal authenticate(String username, String credentials); // -- 일반적으로 사용되는 메소드
public principal authenticate(String username, byte[] credentials);
public principal authenticate(String username, String digest, String nonce,
                              String nc, String cnonce, String qop, String realm, String md5a2);
public principal authenticate(X509Certificate certs[]);
~~~

<br>

### GenericPrincipal(주체)

주체란 인증을 받는 실체를 말하며, 사용자나 그룹 등 인증을 받는 어떠한 대상을 가리키며, "java.security.principal" 인터페이스로 나타며, 생성자 메소드에서 볼 수 있듯이 영역과 항상 연결되어있어야 함.

~~~
// 카탈리나에서 principal의 구현 클래스는 org.apache.catalina.realm.GenericPrincipal 클래스
public GenericPrincipal(Realm realm, String name, String password){
.
.

public GenericPrincipal(Realm realm, String name, String password, List roles){
.
.
~~~

영역과 반드시 이름과 암호를 갖고있어야 하며, 추가로 역할의 목록을 전달할 수 있고, hasRole 메소드를 통하여 이 주체가 지정된 역할을 가지고 있는지 여부를 판단할 수 있음.
~~~
public boolean hasRole(String role){
  if (role==null)
    return false;
  return (Arrays.binarySearch(roles, role) >= 0);
~~~

<br>

### loginConfig (로그인 설정)

![](https://github.com/Soobinnn/How-Tomcat-Works-Study/blob/master/src/YuJaeGuk/Chap10/LoginConfig.png)

톰캣은 시작시 web.xml 파일을 읽어들이며, web.xml 파일에 login-config 요소를 포함하고 있으며 그 특성을 설정함.

~~~
web.xml

  <login-config>
      <auth-method>BASIC</auth-method>
  </login-config>
  
  <security-role>
      <role-name>name</role-name>
  </security-role>
.
.
~~~

인증방식(auth-method)은 BASIC이며, 컨테이너에 지정한 내용(tomcat-user.xml)과 맵핑하는 정보(security-role)를 기술.

<br>

### Authenticator (인증자)

org.apache.catalina.Authenticator 인터페이스는 인증자를 대변하며, 이 인터페이스는 아무런 메소드를 가지고 있지 않은 마커 인터페이스로 다른 컴포넌트가 어떤 컴포넌트에 대해 instance of를 수행해 그 컴포넌트가 인증자인지 여부를 확인하는 역할을 함.

<br>

마커 인터페이스 참고 - https://sumin172.tistory.com/136
