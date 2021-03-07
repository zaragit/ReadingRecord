/**
 * 펙토리 메소드 패턴
 * : 서브 클래스에서 어떤 객체의 인스턴스를 생성할지 결정하게 하는 디자인 패턴
 * + 일반적인 팩토리구조에서 객체 생성 방법을 서브클래스로 위임한 형태
 */
class ShapeFactory {
  public Shape getShape(String type) {
      switch (type) {
          case "circle":
              return new Circle();
          case "rectangle":
              return new Rectangle();
          default:
              return null;
      }
  }
}

/**
* 팩토리 메소드 패턴에서는 객체 생성을 위해서 인터페이스/추상클래스의 정의가 필요하다. (다형성이 필요하기 때문)
*/
interface Shape {
  void draw();
}

class Circle implements Shape {
  @Override
  public void draw() {
      System.out.println("원");
  }
}

class Rectangle implements Shape {
  @Override
  public void draw() {
      System.out.println("사각형");
  }
}
