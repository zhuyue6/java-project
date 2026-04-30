

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Reflection {
  public static void main (String[] args) {
    Method[] Ms =  A.class.getDeclaredMethods();
    for (Method M : Ms) {
      if (M.isAnnotationPresent(Log.class)) {
        Log log = M.getAnnotation(Log.class);
        System.out.print(log.value());
      }
    }
  }
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Log {
  String value() default "print log"; 
}



class A {
  @Log()
  void test() {

  }
}


