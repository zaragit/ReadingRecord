/**
 * 전략 패턴(스트래티지 패턴)
 * : 자신의 기능 맥락(context)에서 필요에 따라서 변경이 필요한 알고리즘을 인터페이스를 통해서 통째로 외부로 분리시키고,
 *   이를 구현한 구체적인 알고리즘 클래스를 필요에 따라서 바꿔서 사용할 수 있게 하는 디자인 패턴이다.
 */
class Bird {
  FlyStrategy flyStrategy;

  /**
   * Fly 전략을 외부에서 받아오기 때문에 Fly 전략이 추가, 수정 되어도 Bird 객체를 변경하지 않아도 된다. (변경에 대해 닫혀 있다!)
   */
  public Bird(FlyStrategy flyStrategy) {
    this.flyStrategy = flyStrategy;
  }

  public void fly() {
    flyStrategy.fly();
  }
}

/**
 * Fly 전략을 외부로 분리
 * 새로운 Fly 행위에 추가에 대해 열려있게 된다. (확장에 대해 열려 있다!)
 */
interface FlyStrategy {
  void fly();
}

class FlyNoStrategy implements FlyStrategy {
  @Override
  public void fly() {
    System.out.println("날지 못함");
  }
}

class FlyWithWingsStrategy implements FlyStrategy {
  @Override
  public void fly() {
    System.out.println("파닥파닥");
  }
}
