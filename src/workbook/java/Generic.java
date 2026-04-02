package workbook;

class People<T> {

}

class Person<T> extends People<T> {
   public T name;
}

class Box<T> {
    public T content;
    public K setContent(K content) { this.content = content; }
    public T getContent() { return content; }
}

public class Generic {
  public void ss(Box<? extends Integer> box) {
     People<String> aa = new People<>(); // 菱形语法触发类型推断，主要是在赋值上下文 与 调用上下文两个地方进行推断

     People<String>[] ss = new People<String>[10];

  }
}


// ? 无界通配符 只能读不能写
// ? extend T 上界通配符 只能读不能写，并且满足其类或子类的限制 生产者
// ? super T 下界通配符 只能写不能读，满足其类或父类的限制  消费者