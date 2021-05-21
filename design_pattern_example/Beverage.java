/**
 * 템플릿 메소드 패턴
 * : 슈퍼클래스에서 기본적인 로직을 구현하고, 일부 기능에 대해서 추상 메소드나 오버라이딩이 가능한 protected 메소드 형태로 만든 뒤
 *   서브클래스에서 필요에 맞게 구현, 오버라이딩해서 사용하도록 하는 디자인 패턴
 */
abstract class Beverage {
  public final void makeBeverage() {
      prepareWater();
      addCondiments();
  }

  /**
   * 선택적으로 오버라이드해서 사용
   */
  protected void prepareWater() {
      System.out.println("물을 컵에 준비");
  }

  /**
   * 서브클래스에서 구현해서 사용
   */
  public abstract void addCondiments();
}

class IceTea extends Beverage {
  public void addCondiments() {
      System.out.println("아이스티 가루 첨가");
  }
}

class MixCoffee extends Beverage {
  @Override
  protected void prepareWater() {
      System.out.println("물을 끓여서 준비");
  }

  public void addCondiments() {
      System.out.println("커피 가루 첨가");
  }
}
