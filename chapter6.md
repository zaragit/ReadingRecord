# AOP

비즈니스 로직에 필요한 부가기능을 독립적으로 모듈화하는 기술을 말한다.

**AOP의 장점**

- 비즈니스 로직에 추가되는 부가기능을 로직으로 부터 완전히 독립시킬 수 있다.
- 고립된 단위 테스트 코드 작성이 쉽다.

---

## 프록시(Proxy)

프록시는 클라이언트와 서버 사이에서 요청을 중계하는 객체를 말한다.  
Target 객체와 같은 인터페이스의 동일한 메소드를 구현해서 클라이언트의 요청이 Proxy Object를 통해서 Target에 접근하도록 한다.

![Screen Shot 2021-05-15 at 07 17 30 AM](https://user-images.githubusercontent.com/74804564/118336966-b9d84600-b54d-11eb-9785-e01053653d4d.png)

### 프록시를 이용하는 디자인 패턴

프록시를 사용하는 디자인 패턴에는 프록시 패턴과 데코레이터 패턴이 있다.  
두 패턴 모두 구현 방식은 프록시를 사용하지만 구현 목적에 따라서 구분해서 부른다.

- **프록시 패턴**
  Target에 대한 접근 요청에 대한 흐름을 제어하는 목적으로 사용한다.
  ex)

  1. Target에 진짜 필요한 순간에 Target을 생성하기 위해서
  2. 권한에 따른 접근을 제한하기 위해서
  3. 실제 처리에 직접적인 연관이 없지만 리소스가 드는 작업 (로깅, 백업 등등)
     <br>

- **데코레이터 패턴**
  Target에 대한 부가적인 기능을 추가하는 것을 목적으로 사용한다.
  ex)
  1. 트랜잭션 처리

---

## 리플렉션

클래스 타입을 알지 못해도 해당 클래스의 정보를 알 수 있도록 도와주는 Java API이다.  
자바에서 클래스 자체를 추상화해서 접근할 수 있도록 해준다.  
취득한 클래스 정보를 사용해서 런타임 중에 동적으로 객체를 생성할 수 있다.

**리플렉션으로 알 수 있는 내용**

- 클래스명
- 접근제한자
- 패키지 정보
- 상속 클래스
- 구현 인터페이스
- 생성자
- 메소드
- 어노테이션

---

## 다이내믹 프록시

프록시 팩토리에 의해 런타임 중에 다이내믹 하게 프록시 객체를 만든다.  
프록시에서 필요한 부가기능은 InvocationHandler를 구현해서 처리한다.

```java
Hello proxiedHello = (Hello)Proxy.newProxyInstance(
  getClass().getClassLoader(), // 다이내믹 클래스의 로딩에 사용할 클래스 로더
  new Class[] { Hello.class }, // 타깃 인터페이스
  new UppercaseHandler(new HelloTarget()) // InvocationHandler의 구현체와 타깃 객체
);
```

![Screen Shot 2021-05-15 at 08 51 29 AM](https://user-images.githubusercontent.com/74804564/118341618-c7e09380-b55a-11eb-80f2-3192afef6aad.png)

### 팩토리 빈

FactoryBean은 스프링을 대신해서 오브젝트를 생성할 수 있는 방법 중 하나이다.  
FactoryBean 인터페이스를 구현하면 직접 빈으로 만들 수 있다.  
다이내믹 프록시는 런타임 중에 코드가 실행되면서 클래스가 정해지기 때문에 일반적인 방법으로는 스프링 빈에 등록할 수 없기 때문에 FactoryBean을 사용해서 빈으로 등록한다.

```java
public interface FactoryBean<T> {
  T getObject() throws Exception;
  Class<?> getObjectType();
  boolean isSingleton();
}
```

<br>

**프록시 팩토리 빈 방식의 장점**

1. 매번 Target이 구현하고 있는 인터페이스를 구현하는 프록시를 만들지 않아도 된다.
2. 여러 메소드에서 반복적으로 중복되는 코드를 작성해야하는 번거로움이 없다.

**프록시 팩토리 빈 방식의 단점**

1. 여러 개의 메소드에 대한 부가기능은 처리가 가능하지만 여러 클래스에 대한 처리는 불가능하다.
2. 부가기능의 개수만큼 빈 설정을 작성해야 한다는 번거로움이 있다.

### 스프링의 ProxyFactoryBean

스프링은 ProxyFactoryBean을 통해서 일관된 방법으로 프록시를 만들 수 있게 도와준다.  
ProxyFactoryBean은 이름에서 알 수 있듯이 Proxy를 만들어서 빈으로 등록해 주는 팩토리빈이다.

순전히 Proxy를 생성해서 빈으로 등록하는 작업만 담당하고, 실제 프록시에서 제공할 부가기능은 MethodInterceptor 인터페이스를 구현해서 제공받는다.  
(서비스 추상화 계층을 통해서 부가기능이란 관심사를 Proxy 생성 로직에서 분리)

#### MethodInterceptor

```Java
public Object invoke(MethodInvocation invocation) throws Throwable {
  String ret = (String)invocation.proceed();
  return ret.toUpperCase(); // 문자를 대문자로 변환해서 반환하는 부가기능
}
```

MethodInterceptor는 부가기능을 처리하는 인터페이스이다. invoke 메소드의 파라미터로 전달되는 MethodInvocation은 일종의 콜백 오브젝트이다.  
타겟 객체에 메소드를 실행 시키는 콜백을 proceed() 메소드로 구현해서 전달 받기 때문에 invoke메소드에서는 부가기능에만 집중 할 수 있다.

---

## AOP 용어 정리

#### 어드바이스(Advice)

AOP에서 프록시에서 제공할 부가기능을 어드바이스라고 한다.

#### 포인트 컷(Pointcut)

타겟 객체에서 어드바이스가 실행되어야 할 메소드나 클래슬르 지정하는 것을 포인트 컷이라고 한다.

#### 어드바이저(Advisor)

포인트 컷을 등록할 때 어느 어드바이스에 대해서 적용할 포인트 컷인지를 알 수 있도록 묶어서 등록을 하는데 이것을 어드바이저라고 한다.  
어드바이저 = 포인트 컷 + 어드바이스

---

## 자동 프록시

ProxyFactoryBean을 사용함으로서 새로운 부가기능을 추가하거나 타켓 객체가 변경되어도 코드를 수정해야할 일은 없게 되었지만 아직 새로운 타겟마다 설정값을 추가해야한다는 불편함이 있다.

```xml
<!-- 타겟 마다 작성되어야 하는 설정 -->
<bean id="userService" class="org.springframework.aop.framework.ProxyFactoryBean">
  <property name="target" ref="userServiceImpl">
  <property name="interceptorNames">
    <list>
      <value>transactionAdvisor</value>
    </list>
  </property>
</bean>
```

### 빈 후처리기

빈 후처리기는 빈이 생성되고 난 뒤 후처리를 해주는 오브젝트로 빈의 내용을 변경하거나 빈 자체를 교체하는 등의 작업을 할 때 사용한다.  
스프링에서는 DefaultAdvisorAutoProxyCreator라는 빈 후처리기를 제공해서 별다른 설정을 작성하지 않아도 자동으로 프록시를 생성해 준다.

#### DefaultAdvisorAutoProxyCreator의 동작

1. 스프링에 의해 빈 설정파일에 작성된 빈 오브젝트가 생성된다.
2. 생성 된 빈들은 빈 후처리기로 보내진다.
3. 빈 후처리기는 Advisor 인터페이스를 구현한 빈을 스캔한다.
4. 등록 된 빈중에서 Advisor에 해당하는 타겟들을 찾는다.
5. 해당되는 타겟 빈들에 대한 프록시 객체를 만들고 타겟 빈을 프록시 객체로 변경한다.
6. 타켓 객체에 의존하는 경우 변경된 프록시 객체를 사용하게 된다.
