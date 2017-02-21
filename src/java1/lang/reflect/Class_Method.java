/*
 * 文件名：Class_Method.java
 * 版权：Copyright 2007-2017 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： Class_Method.java
 * 修改人：xiaofan
 * 修改时间：2017年2月21日
 * 修改内容：新增
 */
package java1.lang.reflect;

import java.lang.reflect.Method;

/**
 * 通过反射获取方法信息.
 * 
 * @author xiaofan
 */
public class Class_Method {
    public static void main(String[] args) {
        Class c1 = int.class;
        Class c2 = String.class;
        Class c3 = void.class;
        System.out.println(c1.getName()); // 类的全称（int）
        System.out.println(c2.getName()); // java.lang.String
        System.out.println(c2.getSimpleName()); // 不包含包名的类名称（String）
        System.out.println(c3.getName()); // void
        // 基本数据类型、void关键字，都存在类类型
        String str = "zxiaofan.com";
        System.out.println("=====打印  " + str.getClass().getName() + "  的类信息=====");
        printClassInfo(str);
    }

    /**
     * 打印任意类的信息（类的成员函数、成员变量）.
     * 
     */
    public static void printClassInfo(Object obj) {
        // 获取类信息，需先获取类的类类型
        Class c = obj.getClass();
        // 获取类名称
        System.out.println("类全称是：" + c.getName());
        // Method类，方法对象，一个成员对象就是一个Method对象；
        // getMethods()获取所有public的函数，包括父类继承而来的
        // c.getDeclaredMethods()获取所有该类自己声明的方法（所有访问类型）
        Method[] ms = c.getMethods(); // c.getDeclaredMethods()
        System.out.println("类方法如下：");
        for (int i = 0; i < ms.length; i++) {
            Method method = ms[i];
            // 方法返回值类型的类类型
            Class returnType = method.getReturnType();
            System.out.print("__" + returnType.getName() + " ");
            // 方法名称
            System.out.print(method.getName() + "(");
            // 获取参数类型（参数列表的类型的类类型）
            Class[] paramTypes = method.getParameterTypes();
            for (Class class1 : paramTypes) {
                System.out.print(class1.getName() + ",");
            }
            System.out.println(")");
        }
    }
}
