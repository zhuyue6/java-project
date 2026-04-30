import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

import java.util.ArrayDeque;
import java.util.*;
import java.util.Deque;

public class LT {
    public static void main(String[] args) {
        // 数组
        int[] numList = { 1, 2, 3};
        int[] numList1 = new int[3];

        
        // 有序列表List  ArrayList  LinkedList
        // 代码
        List<String> als = new ArrayList<>(); // 查询慢，增删快，
        List<String> lls = new LinkedList<>(); // 查询慢，增删快，
        als.add("array");
        lls.add("linked");

        System.out.print(als);
        System.out.print(lls);

        // 无序列表去重首选 Set HashSet  linkedSet TreeSet
        // 代码
        Set<String> hs =  new HashSet<>();   // 存储速度快， 
        hs.add("set");
        // als.clear();
        System.out.print(hs);


        // 队列 先进先出  ArrayDeque LinkedList PriorityQueue
        // 代码
        Deque<String> adq =  new ArrayDeque<>();
        adq.push("12");
        adq.offer("12");
        // als.clear();
        System.out.print(adq);


        // 双列集合
        // HashMap LinkedHashMap TreeMap

        Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
    }
}