## 런타임 환경

보통의 경우 애플리케이션 기능이 바뀌는 경우가 아니면 메타정보가 변경되는 일은 거의 없다.  
하지만 애플리케이션이 동작한느 환경에 따라서 바뀌어야 하는 경우는 자주 있을 수 있다.

#### 1. 환경에 따른 빈 설정정보 변경 전략

애플리케이션이 실행되는 환경이 달라지면 입부 빈의 경우 환경에 맞게 설정 메타정보를 다르게 구상하는 경우가 생긴다.
ex) 개발환경, 테스트 환경, 배포 환경에 따른 DataSource의 연결정보가 각각 다를 수 있다.

##### 1) 환경에 따른 여러개의 빈 설정파일

메타정보를 담은 XML이나 클래스를 각각 별도로 준비하고, 각 환경에서 실행할 때 맞춰서 변경해가면서 사용
매번 변경하면서 사용하는 경우 관리하는 것이 번거롭고 위험하기 때문에 좋은 방법이 아니다.

##### 2) 프로퍼티 파일 활용

환경에 따라 달라지는 정보만 프로퍼티 파일에 두고 XML에서 읽어서 사용  
프로퍼티 파일의 경는 \<context:property-placeholder /> 을 통해서 등록한다.

```xml
<bean id="dataSource" class="...SimpleDriverDataSource">
  <property name="driverClass" value="${db.driverclass}" />
  <property name="url" value="${db.url}" />
  <property name="username" value="${db.username}" />
  <property name="password" value="${db.password}" />
</bean>

<context:property-placeholder location="classpath:database.properties" />
```

#### 2. 프로파일

환경에 따라 아예 빈 클래스가 바뀌거나 빈 구성자체가 달라지는 경우가 있다.  
이런 경우 프로퍼티 파일만으로는 해결이 불가능하다. 스프링 3.1에서는 이 문제를 프로파일을 통해서 해결한다.

프로파일은 환경에 따라 다르게 구성되는 빈들을 다른 이름을 가진 프로파일 안에 정의하고, 애플리케이션이 시작할 때 지정된 프로파일 속 빈들만 생성된다.

```xml
<beans>
  <beans profile="spring-test">
    <!-- sprint test 환경 빈 구성 -->
  </beans>

  <beans profile="dev">
    <!-- dev 환경 빈 구성 -->
  </beans>

  <beans profile="production">
    <!-- production 환경 빈 구성 -->
  </beans>
</beans>
```

##### 프로파일 지정 방법

- ApplicationContext 생성 시 지정

  ```java
  GenericXmlApplicationContext ac = new GenericXmlApplicationContext();
  ac.getEnvironment().setActivePRofiles("dev");
  ac.load(getClass(), "applicationContext.xml");
  ac.refresh();
  ```

- JVM 커맨드라인 파라미터를 활용

  ```
  -Dspring.profiles.active=dev
  ```

- 루트 애플리케이션 컨텍스트나 서블릿 컨텍스트에 파라미터로 지정

  ```xml
  <!-- 루트 애플리케이션 컨텍스트와 서블릿 컨텍스트에 모두 적용 된다. -->
  <context-param>
    <param-name>spring.profiles.active</param-name>
    <param-value>dev</param-value>
  </context-param>

  <!-- 서블릿 컨텍스트에만 적용 -->
  <servlet>
    <servlet-name>spring</servlet-name>
    <serlvet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>spring.profiles.active</param-name>
      <param-value>dev</param-value>
    </init-param>
  </servlet>
  ```

- 애노테이션을 사용

  ```java
  @Configuration
  @Profile("dev")
  public class DevConfig {
  }
  ```

  ```java
  @Configuration
  public class AppConfig {
    @Configuration
    @Profile("spring-test")
    public static class SpringTestConfig {}


    @Configuration
    @Profile("dev")
    public static class DevConfig {}
  }
  ```

#### 3. 프로퍼티 소스

키와 값 (key=value) 형태의 데이터

- .properties 파일의 경우 한글을 지원하지 않기 때문에 한글이 필요한 경우 유니코드를 사용하거나 .xml을 사용해야 한다.

##### 스프링 프로퍼티 종류

- 환경변수
  ```
  스프링 애플리케이션이 구동되는 OS의 환경변수도 키와 값으로 표현된다.
  System.getEnv(), systemEnviroment 이름의 빈을 통해서 가져올 수 있다.
  ```
- 시스템 프로퍼티
  ```
  JVM 레벨에 정의된 프로퍼티.
  JVM이 시작될 때 시스템 관련 정보와 자바 관련 정보, 기타 JVM 관련 정보 등이 시스템 프로퍼티로 등록된다.
  JVM 시작할 때 -D로 지정한 커맨드라인 옵션도 포함된다.
  System.getProperties(), systemProperties 이름의 빈을 통해서 가져올 수 있다.
  ```
- JNDI
  ```
  특정 애플리케이션에서만 프로퍼티로 지정하고 싶다면 사용
  ```
- 서블릿 컨텍스트 파라미터
  ```
  애플리케이션 범위의 프로퍼티 등록 시
  web.xml에 컨텍스트 초기 파라미터로 <context-param>을 사용해서 등록
  ```
- 서블릿 컨픽 파라미터
  ```
  개별 서블릿을 위한 프로퍼티 등록 시
  <servlet>안에 <init-param>으로 지정
  ```

##### 프로파일의 통합과 추상화

스프링 3.0까지는 프로퍼티 값을 가져올 때 .properties파일에서 가져오는 경우 \<context:property-placeholder /> 를 JNDI로 가져오는 경우 \<jee:jndi-lookup\> 을 사용하는 것 처럼 프로퍼티 종류에 따라서 접근 방법이 달랐지만 **3.1부터는 프로퍼티 소스라는 개념으로 추상화하고, 프로퍼티의 저장 위치에 상관없이 동일한 API를 이용**해 가져올 수 있게 됐다.

- Environment.getProperty()
  직접 Enviroment 오브젝트를 주입 받아서 직접 프로퍼티 값을 가져오는 방법

- @Value

  ```java
  @Value("${db.username}") private String username;
  ```

##### 프로퍼티의 수선순위

key값이 중복되는 경우 우선순위가 높은 값을 사용

1. 서블릿 컨픽 프로퍼티
2. 서블릿 컨텍스트 프로퍼티
3. JNDI 프로퍼티
4. 시스템 프로퍼티
5. 환경변수 프로퍼티
