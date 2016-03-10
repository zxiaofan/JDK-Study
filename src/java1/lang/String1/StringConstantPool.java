/*
 * 文件名：StringConstantPool.java
 * 版权：Copyright 2007-2016 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： StringConstantPool.java
 * 修改人：yunhai
 * 修改时间：2016年3月10日
 * 修改内容：新增
 */
package java1.lang.String1;

import org.junit.Test;

/**
 * String 常量池
 * 
 * 字符串常量池(String pool, String intern pool, String保留池) 是Java堆内存中一个特殊的存储区域,
 * 
 * 当创建一个String对象时,假如此字符串值已经存在于常量池中,则不会创建一个新的对象,而是引用已经存在的对象。
 * 
 * d=b+c:先执行StringBuilder的拼接，相当于new了一下，虽然值相等，但内存地址已变.
 * 
 * 当Java能直接使用字符串直接量（包括在编译时就计算出来的字符串值时，如String e = "张" + "三";），JVM就会使用常量池来管理这些字符串。
 * 
 * @author yunhai
 */
public class StringConstantPool {
    @Test
    public void test() {
        String a = "张三";
        String b = "张";
        String c = "三";
        String d = b + c;
        System.out.println(a == d); // false
        String e = "张" + "三";
        System.out.println(a == e); // true
    }
}
