# List 特点
解决了数组的固定长度无法扩容
1. 有序
2. 可重复
3. 支持下标
4. 可以泛型约束

```java
 // 声明, 默认创建10容量，
  List<Integer> list = new ArrayList<>()
  
```


## 常用方法
```java
  // 添加元素
  list.add(a)

  // 删除元素
  list.remove(a)

  // 判断是否包含元素
  list.contains(a)

  // 获取长度
  list.size()

  // 通过下标获取元素
  list.get(1)

  // 通过下标设置元素
  list.set(1, 1)

  // 清空元素
  list.clear()

```

## 与数组的转换

```java
   List<Integer> list = new ArrayList<>(Arrays.asList(arr))

   int[] arr = list.toArray(new String[2]))
```