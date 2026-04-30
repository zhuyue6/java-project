package src.workbook.java.stage2;

import java.math.BigDecimal;

public class Var {
  private char s = 's'; // 单字符，2字节  对应包装类型  Character
  private byte n0 = 0;  // 1 字节（8位数） 2^8        对应包装类型  Byte    
  private short n1 = 0; // 2字节 -2^8 - 2^16
  private int n2 = 0;   // 3 字节    -2^32 - 2^32
  private long n3 = 0l;   // 4字节    -2^64 - 2^64 默认是int
  private float fn = 0.1f; // 6-7位 指数位  整数+小数 
  private double bn = 0.1; // 15-16位 整数+小数  // 浮点计算精度丢失（二进制存储） 乘2取整，直到小数部分为 0 或达到存储位数限制 
  private boolean bl = false;    // 浮点计算使用Bigdecimal

  public static void main (String[] args) {
    // BigDecimal 使用十进制进行存储 整数+小数
    BigDecimal a = new BigDecimal("1.1");
    BigDecimal b = new BigDecimal("1.2");
    // a = a.subtract(b); // 减
    // a.add(b);         // 加
    // a.multiply(b);    // 乘
    // a.divide(b);       // 除法
    // a.equals(b) ;  // 大小对  == 
    // a.compareTo(b);   // -1 0 1
    int g =  a.intValue(); 
    System.out.print(g);
  


  }
}


// 引用类型

// 类、枚举、注解、接口、数组
// Class 

// enum

// @interface 
// interface 

// Array