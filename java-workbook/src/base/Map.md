# Map 特点
属于结合key-value存储
1. 无下标
2. 无序
3. 不能直接for each

```java
   Map<String, Integer> map = new HashMap<>();
   
```


## 常用方法

```java
  // 设置
  map.put('111', 12)

  // 获取
  map.get('111')

  // 删除
  map.remove('111')

  // 包含key/值
  map.containsKey('111'); 
  map.containsValue(12)

  // 清除
  map.clear()

  // 返回 键值对Set
  map.entrySet()

   // 返回 键Set
  map.KeySet() 

```

## 如何遍历
```java
  // 不能for each, 需要转换
  for (Map.Entry<String, Integer> entry : map.entrySet()) {
      entry.getKey()
      entry.getValue()
  }

  for (String key: map.keySet()) {
      map.get(key)
  }
```
