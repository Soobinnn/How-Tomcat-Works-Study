
# 7장 - 로거

### 작성자

임수빈


## 로거

메시지를 기록해주는 컴포넌트
* 카탈리나에서의 로거는 컨테이너와 연결되어있으며, 다른 컴포넌트에 비해 상대적으로 간단하다
org.apache.catalina.logger 패키지에 다양한 로거 제공함.

* 모든 로거는 org.apache.catalina.logger 인터페이스를 구현해야함.

logger인터페이스는 이 인터페이스를 구현하는 클래스에서 선택할 수 있는 다양한 종류의 log메소드를 정의해놓고 있다.

단 하나의 String을 전달받는 메소드인데, 문자열은 기록할 메시지 

log 메소드 2개 출력수준 (verbosity level)을 받는 메소드
클래스의 인스턴스에 지정된 출력 수준보다 낮은 수준의 값을 log메소드에 전달하면 로그메시지가 기록되며,
그렇지 않은 경우 메시지는 무시됨.

출력 수준
FATAL, ERROR, WARNING, INFORMATION, DEBUG 
getVerbosity, setVerbosity메소드는 출력수준의 값을 얻거나 설정할 때 사용

추가로 Logger인터페이스는 연결돼있는 컨테이너와 관련한 getContainer와 setContainer메소드를 제공함

//로거그림
톰캣은 3개의 로거 제공함.
FileLogger, SystemErrLogger, SystemOutLogger 제공
모두 org.apache.catalina.logger 패키지에 있으며, org.apache.catalina.logger.LoggerBase 클래스를 확장하고 있다.

톰캣4에서는 LoggerBase클래스가 org.apache.catalina.Logger인터페이스를 구현했다.
톰캣5에서의 LoggerBase클래스는 추가로 LifeCycle과 MBeanRegistration 인터페이스를 구현함.

톰캣 4의 LoggerBase클

7장은 톰캣4로거 기준
LoggerBase클래스는 log(String msg)메소드를 제외한 Logger인터페이스의 모든 메소드를 구현해놓은 추상 클래스
* public abstract void log(String msg);
하위 클래스는 서로 다른 대상에 메시지를 기록할 수 있기 때문이다.
따라서 이 메소드는 LoggerBase클래스에서 구현되어있지 않다
