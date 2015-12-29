/*
 * 文件名：EnumSet_Study.java
 * 版权：Copyright 2007-2015 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： EnumSet_Study.java
 * 修改人：yunhai
 * 修改时间：2015年12月29日
 * 修改内容：新增
 */
package java1.util.set;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;

import org.junit.Test;

/**
 * EnumSet
 * 
 * ①add-null抛空指针异常，判断是否包含null和removenull无异常；remove-null时返回false。
 * 
 * ②内部以位向量形式存储，紧凑高效，so占用内存小且运行效率高(尤其是批量操作，如containsAll、retainAll)
 * 
 * @author yunhai
 */
enum Season {
    Spring, summer, fail, winter
}

public class EnumSet_Study {
    @Test
    public void init() {
        // 创建一个EnumSet集合，元素是Season枚举的所有枚举值
        EnumSet set = EnumSet.allOf(Season.class);
        System.out.println(set); // [Spring, summer, fail, winter]
        // 创建一个集合元素是Season枚举值的空EnumSet
        set = EnumSet.noneOf(Season.class);
        System.out.println(set); // []
        set.add(Season.summer);
        System.out.println(set); // [summer]
        // 指定枚举值
        EnumSet set2 = EnumSet.of(Season.Spring, Season.summer);
        System.out.println(set2); // [Spring, summer]
        // 从from到to的所有枚举值组成的集合
        set2 = EnumSet.range(Season.summer, Season.winter);
        System.out.println(set2); // [summer, fail, winter]
        // set2+set3=Season全部枚举值
        EnumSet set3 = EnumSet.complementOf(set2);
        System.out.println(set3); // [Spring]
    }

    @Test
    public void copy() {
        EnumSet set = EnumSet.of(Season.Spring, Season.summer);
        EnumSet set2 = EnumSet.copyOf(set);
        System.out.println(set2); // [Spring, summer]
        Collection col = new HashSet();
        col.add(Season.Spring);
        col.add(Season.fail);
        set2 = EnumSet.copyOf(col);
        System.out.println(set2); // [Spring, fail]
        col.add("Smile");
        // copy collection的元素创建EnumSet集合时，必须保证collection的元素全为同一个枚举类的枚举值
        set2 = EnumSet.copyOf(col); // 异常ClassCastException
    }
}
