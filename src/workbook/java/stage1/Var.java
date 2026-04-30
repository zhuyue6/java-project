package workbook;

public class Var {
  // 基础类型, 直接存储值本身，存储在栈内存中，效率高, 包括整数型、浮点型、字符型、布尔型
  public double varDouble = 0.1000; // 	约 15-16 位十进制有效数字, 占8字节64位
  public float varFloat = 0.1000f;  // 	约 6-7 位十进制有效数字， 占4字节32位
  public char varChat = 'A'; // 单字符2字节16位 字符串"  单字符'
  public byte varByte = 1; // 1字节
  public short varShort = 2; // 2字节
  public int varInt = 4; // 4字节
  public long varLong = 8; // 8字节
  public boolean varBoolean = true; // 1或4字节
  public Character vCharacter = new Character('1');

  // 引用类型 Class Interface Array Enum Annotation, 引入类型所有的基类指向Object
  
  // Class 
  public String varString = "A"; // 字符串
  public int[] varArray = {1, 2, 3}; // 数组
  public void ss() {
    int ss = (int) varLong;
    Integer sb = 1;
    
    
  }
}

enum Season {
    sp1,
    sp2
}

// 接口抽象定义引入
interface FF {
  public void fly();
} 

class Brid implements FF {
  public void fly() {

  }
}

