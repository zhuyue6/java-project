# 匿名类
一次性创建某个接口/父类没有名字使用类的实例

```java
   new 父类/接口() {
    // 重写方法
   }
  
   class Comparator <T>{
      public int compare(T s1, T s2) {
         return s1.compareTo(s2);
      }
   }
   
  
  Comparator comparator = new Comparator<String>() {
    @Override
    public int compare(T s1, T s2) {
      return s1.compareTo(s2);
    }
  }

```

# lambda表达式
是函数式接口的简写，用来快速传递一段代码

```java
   a -> a
   (a, b) -> {
     int sum = a + b
     return sum
   }

```

# 方法简写

```java
  class Test {
    static int test() {

    }
  }

  // 对象::实例方法
  // System.out:print =  (a) -> System.out.print(a)

  // 类::静态方法
  // Integer::compare =  (a, b) -> Integer.compare(a, b)

  // 类::实例方法
  // String::equals = (s1, s2) -> s1.equals(s2)
  // (s1, s2, s3) -> s1.equals(s2, s3)

  // 构造器引用 Class::new
  // ArrayList::new = () -> new ArrayList(a)
  // 引用的是构造器，而不是实例对象
```

# 流式api
使用的是Steam, ！**不存储数据**，只是对源数据做计算、过滤、转换、聚合

## 创建流

```java
  // 数组 -> 流
  String[] arr = {"x","y"};
  Stream<String> arrStream = Arrays.stream(arr);
  
  // 集合 -> 流

  List<Integer> list = new ArrayList<>()
  list.stream()
```

## 常用的流方法
```java
  // 过滤保留满足条件
  filter()
  // 映射类型
  map()
  // 排序
  sorted()
  // 限制
  limit()
  
  // 终止操作
  collect()

  List<Integer> newlist = list.stream().filter(a -> a.getValue() != 1)
               .sorted(Comparator.comparing(User::getAge))
               .limit(10)
               .collect(Collectors.toList())
```