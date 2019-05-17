


# 12장 - StandardContext

### 작성자

임수빈

## StandardContext

카탈리나에서의 Context 인터페이스의 표준 구현 클래스
: org.apache.catalina.core.StandardContext

StandardContext 인스턴스가 생성된 후,
__들어오는 HTTP요청을 서비스할 수 있도록 start 메소드가 반드시 호출되야함__

** StandardContext 객체는 Context가 %CATALINA_HOME%/conf 디렉토리에 있는 기본 web.xml 내용을 
  파싱해서, 배치돼있는 모든 애플리케이션에 적용할 수 있도록 준비됨.

### StandardContext 동작순서
 1. StandardContext인스턴스 생성(초기화)

 2. start 메소드 호출

 3. 생명주기 리스너(이벤트) 발생

 4. 이벤트에서 리스너 호출 -> StandardContext 인스턴스 설정

5. 설정이 올바르게 완료됨. - configured = true
설정이 올바르게 완료안됨. - configured =false 
configured = false -> HTTP 요청을 서비스하지 못함 - stop메소드를 호출
>** configured : 올바르게 설정됬는지 여부를 나타내는 boolean타입의 변수

6. 인스턴스 올바르게 설정되고 관련 하위 컨테이너와 컴포넌트들이 성공적으로 구동시
      -> available = true StandardContext
            available = false는 반대의 의미

#### 1. StandardContext인스턴스 생성(초기화)
-- StandardContext 생성자
```java
public StandardContext()
{
	super();
	pipeline.setBasic(new StandardContextValve());
	namingResources.setContainer(this);
}
```
org.apache.catalina.core.StandardContextValve 타입의 기본 밸브가 주어짐
커넥터를 통해 들어오는 모든 HTTP 요청들을 처리할 것임

#### 2. start 메소드 호출
StandardContext의 start메소드가 호출되면, 생명주기 이벤트가 발생함.
> ** standardContext는 자신의 설정자(configurator)로서 이벤트 리스너를 사용함.

start메소드가 성공적으로 수행되기 위해 StandardContext객체가 올바르게 설정되야함.

>** StandardContext를 설정하는 생명주기 리스너는 org.apache.catalina.startup.ContexstConfig타입의 리스너(15장)

#### **- StandardContext 클래스의 start메소드**
	1 - BEFORE_START_EVENT 이벤트 발생

	2 -1 available 특성이 값을 false로 지정

	  -2 configured 특성의 값을 false로 지정

	3 - 지원 준비

	  -1 로더 준비

	  -2 매니저 준비

	4 - 문자셋 맵퍼 초기화

	5 - 이 컨텍스트와 연관된 다른 컴포넌트 구동

	6 - 하위 컨테이너(래퍼) 구동

	7 - 파이프라인 구동

	8 - 매니저 구동

	9 - START_EVENT 이벤트 발생 - 리스너가 설정작업 수행 (완료시 configured = true)

	10 -1 configured값 확인 true라면, postWelcomePages 메소드 호출,

	      구동 시에 로드할 하위 래퍼들을 로드, available특성을 true로 지정

	   -1 configured값이 false라면, stop메소드 호출

	11 - AFTER_START_EVENT 이벤트 발생

### StandardContextMapper
  들어오는 요청에 대해서는 StandardContext 파이프라인에 있는 기본 밸브의 invoke 메소드가 호출됨.
  invoke메소드에서 가장 먼저 필요한 것은 요청을 처리할 래퍼를 얻는 것임
 적절한 래퍼를 찾기 위해 사용하는 것이 StandardContextMapper
 
### 재로딩 지원
reloadable 특성을 사용함. (default : false)
재로드하는데 있어서 자신의 로더에 의존함.
모든 클래스들과 JAR파일들으 타임스탬프를 확인하는 스레드를 갖고 있어
로더와 StandardContext에 연결하여 지속적으로 확인하는 스레드를 이용하여 재로딩 수행
** 서버에 부담이 갈 수있으므로 지양

### backgroundProcess 메소드
 Context는 로더나 메니저와 같은 다른 컴포넌트들의 도움이 필요함.
 이러한 컴포넌트들은 백그라운드에서 일을 처리하기 위해 별도의 스레드를 실행해야함.
 > EX) 매니저 컴포넌트의 경우, 자신이 관리하는 세션 객체의 만료 시간을 확인하기 위한 스레드 필요
 
 ** 톰캣 4 - 컴포넌트가 자신의 스레드를 사용해 일을 처리함
 ** 톰캣 5 - 자원을 절약하기 위해 모든 백그라운드 프로세스는 동일한 스레드를 공유 시킴
 