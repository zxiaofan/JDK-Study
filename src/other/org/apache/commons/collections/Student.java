/*
 * 文件名：Student.java
 * 版权：Copyright 2007-2015 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： Student.java
 * 修改人：yunhai
 * 修改时间：2015年12月2日
 * 修改内容：新增
 */
package other.org.apache.commons.collections;

/**
 * module.
 * 
 * @author yunhai
 */
public class Student {
    private String name;

    private int age;

    public Student(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
