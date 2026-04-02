package workbook;

import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// 1. 类方法注解
@Target(ElementType.METHOD)  // TYPE METHOD FIELD注解分别用于类、方法、属性
@Retention(RetentionPolicy.RUNTIME) // RUNTIME 运行时, CLASS 保留到class字节中，SOURCE 只在源码中运行
@interface Log {
  byte level() default 1;
  String message() default "";
}

// 2. 类注解
@Target(ElementType.TYPE)  // TYPE METHOD FIELD注解分别用于类、方法、属性
@Retention(RetentionPolicy.RUNTIME) // RUNTIME 运行时, CLASS 保留到class字节中，SOURCE 只在源码中运行
@interface LogClassName {
}

// 2. 属性注解
@Target(ElementType.FIELD)  // TYPE METHOD FIELD注解分别用于类、方法、属性
@Retention(RetentionPolicy.RUNTIME) // RUNTIME 运行时, CLASS 保留到class字节中，SOURCE 只在源码中运行
@interface PropsLimit {
  byte min() default 1;
  byte max() default 10;
}

// 元素
public class Annotation {
  public Annotation() {
    new AnnotationProcessor();
  }
}


final class AnnotationLog {
  @Log(level = 2, message = "非常严重的bug")
  void log() {}
}

@LogClassName()
final class AnnotationClass {
  
}


final class AnnotationField {
  @PropsLimit(min = 2, max = 10)
  byte level = 20;
}


final class AnnotationProcessor {
  public AnnotationProcessor() {
    Class<AnnotationClass> antc = AnnotationClass.class;
    Class<AnnotationLog> antl = AnnotationLog.class;
    handleClass(antc);
    handleMethod(antl);
    handleProps(new AnnotationField());
  }
  /**
   * 找到class 注解进行打印类名
   * @param classz
   */
  void handleClass(Class<?> classz) {
    if (classz.isAnnotationPresent(LogClassName.class)) {
      System.out.println(classz.getName());
    }
  }
  /**
   * 找到class 属性注解进行重新赋值
   * @param classz
   */
  void handleProps(AnnotationField classI) {
    Class<? extends AnnotationField> classz = classI.getClass();
    Field[] fList = classz.getDeclaredFields();
    for (Field f : fList) {
      if (f.isAnnotationPresent(PropsLimit.class)) {
        PropsLimit props = f.getAnnotation(PropsLimit.class);
        int value = 0;
        try {
          Object obj = f.get(classI);
          value  = (Integer) obj;
        } catch (IllegalAccessException e) {
          System.out.print(e);
        }
        if (props.min() < value || value > props.max()) {
           System.out.print("超出了范围");
        }
      }
    }
  }
  /**
   * 找到method 注解进行重新新增执行
   * @param classz
   */
  void handleMethod(Class<?> classz) {
    Method[] mList = classz.getDeclaredMethods();
    for (int i = 0; i < mList.length; i++) {
      if (mList[i].isAnnotationPresent(Log.class)) {
        Log log = mList[i].getAnnotation(Log.class);
        System.out.println("打印log等级：" + log.level());
        System.out.println("打印log信息：" + log.message());
      }
    }
  }
}