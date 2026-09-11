package stage1.packageRefence;

// 包作用域修饰符
// default 本包内可以访问、本类可以访问
// public 本包内可以访问、本类内可以访问、子类可以访问、其他包可以访问
// protected 本包内可以访问、本类内可以访问、子类可以访问、其他包不能访问
// private 本类可以访问，其他包不可以访问
public class PublicPackage {
  public String name = "PublicPackage";
  public void printName() {
    // 可以访问本类中的所有成员
    System.out.println(name);
  }
}


