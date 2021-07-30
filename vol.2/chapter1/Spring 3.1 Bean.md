# Bean(스프링 3.1)

스프링 3.1에서 자바 코드를 이용한 빈 메타정보 작성 기능이 확장되어 XML을 사용하지 않거나 최소한으로만 사용하고도 스프링 애플리케이션 개발이 가능해졌다.

## 1. 빈의 종류

### 1) 애플리케이션 로직 빈

- 일반적으로 애플리케이션 로직을 담고 있는 주요 클래스
- DAO, Service, Controller

### 2) 애플리케이션 인프라 빈

- 다른 빈과 관계를 맺어서 동작
- 애플리케이션 로직을 직접 담당하지 않고 **애플리케이션 로직 빈을 지원**

### 3) 컨테이너 인프라 빈

- 스프링 컨테이너의 기능에 관여
- **컨테이너의 기능을 확장**해서 빈의 생성, 관계설정, 초기화 등의 작업에 참여하는 빈
- 보통 직접 \<bean\> 태그를 이용해서 등록하지 않고 전용 태그를 사용한다.

<br/>

## 2. 빈의 역할

컨테이너가 빈을 메타정보 인터페이스 BeanDefinition은 ROLE\_로 시작하는 상수를 가지고 있다. 스프링의 빈을 역할에 따라서 구분한 값이다.

```java
int ROLE_APPLICATION = 0;    // 애플리케이션 로직 빈, 애플리케이션 인프라 빈
int ROLE_SUPPORT = 1;        // 복합 구조의 빈을 정의할 때 보조적으로 사용되는 빈 (거의 사용 X)
int ROLE_INFRASTRUCTURE = 2; // 전용 태그에 의해서 등록되는 컨테이너 인프라 빈
```

<br/>

## 3. 자바 코드를 이용한 빈 등록

### @ComponentScan

@Configuration이 붙은 클래스에 @ComponentScan을 사용하면 \<context:component-scan\> 전용 태그를 사용한 것처럼 스테레오타입 애노테이션이 붙은 빈을 자동으로 스캔한다.

- 사용 방법

  1. 패키지 경로를 지정
     입력한 패키지 하위 클래스 중에서 탐색

     ```java
     @Configuration
     @ComponentScan("springbook.learningtest.spring31.ioc.scanner")
     public class AppConfig {
     }
     ```

  2. 마커 클래스나 인터페이스를 사용
     클래스나 인터페이스를 지정  
     지정한 클래스나 인터페이스가 있는 패키지를 탐색

     ```java
     @Configuration
     // ServiceMarker.class 클래스가 있는 패키지를 탐색
     @ComponentScan(basePackageClasses=ServiceMarker.class)
     public class AppConfig {
     }
     ```

  3. 특정 애노테이션을 제외

     ```java
     @Configuration
     @ComponentScan(basePackages="myproject", excludeFilters=@Filter(Configuration.class)
     public class AppConfig {
     }
     ```

  4. 특정 클래스를 제외

     ```java
     @Configuration
     @ComponentScan(basePackages="myproject"
             , excludeFilters=@Filter(type=FilterType.ASSIGNABLE_TYPE, value=AppConfig.class)
     public class AppConfig {
     }
     ```

### @Import

@Configuration 설정에 다른 @Configuration를 추가  
(성격이 다른 빈 설정을 여러 개의 파일로 분리해서 관리하는 경우)

```java
@Configuration
@Import(DataConfig.class)
public class AppConfig {
}

@Configuration
public class DataConfig {
}
```

### @ImportResource

@Configuration 설정에 XML 파일의 빈 설정을 가져와서 추가  
(스프링 시큐리티 처럼 XML 설정만 지원이 되는 경우에 사용)

```java
@Configuration
@ImportResource("/myproject/config/security.xml")
public class AppConfig {
}
```
