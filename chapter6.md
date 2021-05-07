# Chapter6 - AOP

AOP는 IoC/DI, PSA와 더불어 스프링 3대 기반기술의 하나이다.  
스프링 기술 중에서 가장 이해하기 힘든 기술로 악명이 높다.

<br/>

## 등장 배경과 AOP를 도입한 이유

비즈니스 로직을 구현하다 보면 비즈니스 로직과 직접적으로 연관되지는 않지만, 예외처리나 트랜잭션 관리와 같이 처리되어야 하는 기술적인 코드가 생기게 된다. 이런 기술적인 코드의 관심사 분리가 이뤄지지 않은 상태로 비즈니스 로직과 같이 작성되어 있다면 코드의 품질을 저하시켜 유지보수를 어렵게 만들고, 동일한 기술을 사용하는 여러 메소드에서 중복되어 작성되게 된다.

AOP는 이러한 핵심 로직과 직접적으로 연관되지 않는 부가기능(기술코드)을 외부로 분리시킬 수 있도록 하기 위해서 사용하는 기술이다.

#### upgradeLevels() 메소드에서 생긴 문제점

1. 트랜잭션 코드로 인해서 유지보수가 어렵다.
2. 다른 기능이 추가 될 때 동일한 트랜잭션 처리 코드가 중복되어 작성 될 수 있다.
3. 의존관계가 복잡하게 얽혀있어 단위테스트 작성이 어렵다.

<br/>

## AOP와 고립된 단위 테스트

코드를 작성하다보면 의존관계가 복잡하게 얽혀있게 된다. upgradeLevels() 메소드에 대한 테스트를 작성하기 위해서
DataSource, Transaction 등 의존하고 있는 여러 객체까지 신경써서 작성하게 되는데 이렇게 되면 더이상 단위 테스트로써 의미를 잃어버리게 된다.  
AOP를 사용해서 비즈니스 로직과 직접적으로 관계가 없는 부가기능들을 외부로 완전히 분리시키면 테스트하고자 하는 로직에만 집중한 단위 테스트를 작성할 수 있게 된다.

#### Mock 프레임워크

단위 테스트를 만들기 위해서 스텁이나 목 오브젝트를 만들어서 사용하는 프레임워크로 대표적으로 **Mockito**가 있다.
**Mockito**는 매번 테스트를 위해서 목 클래스를 작성할 필요없이 간단한 메소드 호출만으로 다이내믹하게 목 오브젝트를 만들 수 있게 해준다.

```Java
  @Test
  public void mockUpgradeLevels() throws Exception {
    UserServiceImpl userServiceImpl = new UserServiceImpl();

    UserDao mockUserDao = mock(UserDao.class);
    when(mockUserDao.getAll()).thenReturn(this.users);
    userServiceImpl.setUserDao(mockUserDao);

    userServiceImpl.upgradeLevels();

    verify(mockUserDao, times(2)).update(ArgumentMatchers.any(User.class));
    verify(mockUserDao).update(users.get(1));
    assertThat(users.get(1).getLevel(), is(Level.SILVER));
    verify(mockUserDao).update(users.get(3));
    assertThat(users.get(3).getLevel(), is(Level.GOLD));
  }
```

<br/>

## 프록시와 프록시 패턴, 데코레이터 패턴

프록시 패턴과 데코레이터 패턴은 실제 코드의 구조는 유사한 구조를 가지고 있지만 사용하는 방법과 목적에 따라서 구분되어 진다.  
두 패턴 모두 타깃(실제 실행되어야 할 클래스)을 가지며, 타깃과 동일한 인터페이스를 구현해서 타깃의 대리자와 같은 역할을 한다.

- **프록시 패턴**
  프록시 패턴은 타깃에 대한 접근방법을 제어하기 위한 목적으로 사용된다.

- **데코레이터 패턴**
  타깃에 추가적으로 부가기능을 추가하기 위한 목적으로 사용된다.

#### 다이내믹 프록시

프록시 팩토리에 의해서 런타임 시 타깃의 인터페이스를 통해서 다이내믹하게 프록시 오브젝트를 만든다.

그냥 팩토리 패턴과 데코레이터 패턴을 사용해서 코드를 분리하게 되면 "upgradeLevels() 메소드에서 생긴 문제점"에 1번과 3번의 경우 해소되지만 2번 중복의 문제는 계속해서 발생한다. 여러 메소드에 대해서 매번 메소드를 추가 작성해야하기 때문이다.  
다이내믹 프록시를 사용하게 되면 매번 프록시를 만들 때 메소드를 작성하고 인터페이스를 하나씩 구현해 가면서 클래스파일을 생성하는 수고를 덜 수 있다.
