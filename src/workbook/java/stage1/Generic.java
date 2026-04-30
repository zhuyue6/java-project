package workbook;

class People<Object> {
   void run(T s) {

   }
}

class P<String> extends People<String> {
   void run(Object s) {

   }
   @Override
   void run(String s) {
      run(s)
   }
}

class Box<T> {
    public T content;
    public K setContent(K content) { this.content = content; }
    public T getContent() { return content; }
}

public class Generic {
  public void ss(Box<? extends Integer> box) {
     P<String> p =new P<String>();
  }
}


// ? 无界通配符 只能读不能写
// ? extends T 上界通配符 只能读不能写，并且满足其类或子类的限制 生产者
// ? super T 下界通配符 只能写不能读，满足其类或父类的限制  消费者