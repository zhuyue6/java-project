package stage1;

public class Integer {
  public static void main(String[] args) {
    byte b = 127; // byte 是8位有符号整数，取值范围是-128到127
    short s = 32767; // short 是16位有符号整数，取值范围是-32768到32767
    int a = 2147483647;        // int 是32位有符号整数，取值范围是-2^31到2^31-1
    long b = 9223372036854775807;   // long 是64位有符号整数，取值范围是-2^63到2^63-1
    double c = 10.0; // double 是64位浮点数，取值范围是-1.7976931348623157e308到1.7976931348623157e308
    float f = 10.0f; // float 是32位浮点数，取值范围是-3.4028234663852886e38到3.4028234663852886e38
    boolean bool = true; // boolean 是布尔类型，取值范围是true或false
    char ch = 'A'; // char 是16位字符类型，取值范围是0到65535
    String str = "Hello, World!"; // String 是字符串类型，取值范围是任意字符串
    Object obj = new Object(); // Object 是所有类的基类，取值范围是任意对象
    List<String> list = new ArrayList<>(); // List 是列表类型，取值范围是任意列表
    Map<String, String> map = new HashMap<>(); // Map 是映射类型，取值范围是任意映射
  }
}
