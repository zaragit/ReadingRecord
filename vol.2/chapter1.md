# IoC 컨테이너와 DI

---

## IOC 컨테이너

스프링에서 IoC를 담당하는 컨테이너를 애플리케이션 컨텍스트라고 부른다.
애플리케이션 컨텍스트는 그 자체로 IoC와 DI를 위한 빈 팩토리이면서 그 이상의 기능을 가지고 있다.
일반적으로 IoC 컨테이너는 애플리케이션 컨텍스트를 말한다.

### IoC 컨테이너를 이용한 애플리케이션 개발

IoC 컨테이너가 동작하려면 POJO 클래스와 설정 메타정보를 필요로 한다.

#### POJO

유연한 확장성을 고려하고 자신의 기능에 충실한 POJO를 작성한다.  
POJO 클래스는 관계를 맺고 사용되는데 필요한 최소한의 정보만 인터페이스를 통해서 공유한다. (느슨한 관계)

#### 설정 메타정보

작성된 POJO 클래스 중에서 애플리케이션에서 사용할 것을 IoC 컨테이너가 제어할 수 있도록 적절한 메타정보를 만들어서 제공한다.  
메타정보 항목에는 대부분 디폴트 값이 있다.

**메타정보 항목**

- 빈 아이디, 이름, 별칭
- 클래스
- 스코프
- 프로퍼티 값
- 생성자 파라미터 값
- 지연된 로딩 여부, 우선 빈 여부, 자동와이어링 여부, 부모 빈 정보, 빈팩토리 이름 등

---

### IoC 컨테이너의 종류와 사용 방법

스프링에는 다양한 용도의 ApplicationContext 구현 클래스(IoC 컨테이너)가 존재한다.

#### StaticApplicationContext

코드를 통해서 빈 메타정보를 등록하기 위해서 사용한다.  
스프링의 기능에 대한 학습 테스트를 만들거나 검증해보고 싶을 때 사용한다.

#### GenericApplicationContext

가장 일반적며, 실정에서 사용될 수 있는 모든 기능을 갖추고 있는 ApplicationContext의 구현체이다.  
XML 파일과 프로퍼티 파일 같은 외부 리소스에 있는 빈 메타정보를 리더를 통해서 읽어들여서 사용한다.  
JUnit 테스트는 테스트 내에서 애플리케이션 컨텍스트를 자동으로 만들어주는데 이때 사용하는 컨텍스트가 바로 GenericApplicationContext이다.

#### GenericXmlApplicationContext

GenericApplicationContext에 XmlBeanDefinitionReader가 결합된 컨텍스트이다.  
설정 메타정보가 XML형식으로 고정되어 있다면 번거로움을 덜기위해서 GenericXmlApplicationContext를 사용하는게 좋다.

#### WebApplicationContext

웹 애플리케이션 개발에 가장 많이 사용되는 애플리케이션 컨텍스트이다.

---

### IoC 컨테이너 계층구조

한 개 이상의 IoC 컨테이너를 만들어두고 사용해야 하는 경우가 있는데 바로 트리 구모양의 계층구조를 만들 때다.

#### 부모 컨텍스트를 이용한 계층구조

애플리케이션 컨텍스트는 부모 애플리케이션 컨텍스트를 가질 수 있다.  
애플리케이션 컨텐스트별로 독립적인 설정정보를 갖고 있지만 DI를 위해서 빈을 찾을 때는 부모가 가진 빈까지 모두 검색한다.
용도와 성격이 달라서 여러 개의 웹 모듈을 분리했지만 핵심 로직을 담은 코드는 공유하고 싶을 때 이런 식을 구성한다.

---

### 웹 애플리케이션의 IoC 컨테이너 구성

웹 애플리케이션에서 동작하는 IoC 컨테이너는 서블릿 단계에서와 웹 애플리케이션 레벨에서 두 가지 방법으로 만들어진다.

#### 웹 애플리케이션의 컨텍스트 계층구조

웹 애플리케이션에서 컨텍스트는 두 가지 방법으로 생성이 될 수 있다.  
첫 번째는 웹 애플리케이션 레벨에서 생성되는 방법이고, 두 번째 방법은 서블릿 별로 생성되는 방법이다.  
굳이 두 가지 방법으로 나눈 이유는 웹 기술에 의존적인 부분과 그렇지 않은 부분을 구분하기 위해서다.

#### 웹 애플리케이션의 구성 방법

- 서블릿 컨텍스트 -> 루트 애플리케이션 컨텍스트 (계층구조)
- 루트 애플리케이션 컨텍스트 단일구조
- 서블릿 컨텍스트 단일구조

#### 컨텍스트 등록

##### 루트 애플리케이션 컨텍스트 등록

web.xml에 ContextLoaderListener를 등록한다.  
기본값이 있기 때문에 별다른 파라미터를 작성하지 않아도 되지만 다른내용이 있다면 context-param을 작성하면 된다.

- **contextConfigLocation:** 메타정보 파일의 위치 지정
- **contextClass:** ApplicationContext 구현체를 지정

```xml
<!-- 생략이 가능하다. -->
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>
    /WEB-INF/applicationContext.xml
  </param-value>
</context-param>

<!-- 생략이 가능하다. -->
<context-param>
  <param-name>contextClass</param-name>
  <param-value>
    org.springframework.web.context.support.AnnotationConfigWebApplicationContext
  </param-value>
</context-param>

<listener>
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

##### 서블릿 애플리케이션 컨텍스트 등록

web.xml에 등록한다.

- **servlet-name:** 서블릿의 네임스페이스이다. xml 파일의 기본값에 파일명으로 사용되기 때문에 중요하다.
- **load-on-startup:** 서블릿을 언제 만들고 초기화할지, 또 그 순서는 어떻게 되는지를 지정하는 정수 값.

```xml
<servlet>
  <servlet-name>spring</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <load-on-startup>1</load-on-startup>
  <!-- 생략이 가능하다. -->
  <init-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>
      /WEB-INF/applicationContext.xml
    </param-value>
  </init-param>
</servlet>
```
