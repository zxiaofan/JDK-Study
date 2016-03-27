/*
 * 文件名：LinkedList_Study.java
 * 版权：Copyright 2007-2016 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： LinkedList_Study.java
 * 修改人：xiaofan
 * 修改时间：2016年3月27日
 * 修改内容：新增
 */
package java1.util.list;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * @author xiaofan
 */
public class LinkedList_Study {
    @Test
    public void test() {
        List list = new LinkedList<>();
        list.add(null);
        list.add(null); // 允许null，且允许重复
        System.out.println(list); // [null, null]
        // contains调用int indexOf()方法，先判断入参是否是null，是则x.item == null，不是则equals比较，最后返回index或-1
        System.out.println(list.contains(null)); // true
        list.remove(null); // 只remove第一个元素
        list.add(1);
        list.remove((Integer) 1); // remove int本身
        list.add("a");
        list.add("b");
        Iterator ite = list.iterator();
        while (ite.hasNext()) {
            Object object = (Object) ite.next();
            System.out.println(object);
            list.remove("a"); // 迭代时remove抛ConcurrentModificationException
        }
    }
}
