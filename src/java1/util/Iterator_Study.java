package java1.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Test;

/**
 * 
 * @author yunhai
 */
public class Iterator_Study {
    @Test
    public void Iterator() {
        Collection col = new HashSet();
        col.add("hello");
        col.add("Java");
        col.add("Abstract");
        Iterator it = col.iterator();
        while (it.hasNext()) {
            System.out.println(it.next() + ""); // Java Abstract hello
            it.remove();
            // 迭代时不能【有效】修改集合元素，报异常ConcurrentModificationException
            // 如果HashSet包含的是对象，则对象不能修改，但对象内的属性可以修改。
            // 当向hashSet中添加可变对象时，如果修改其对象，有可能导致该对象与集合内其他对象相同，导致HashSet无法准确访问该对象
            col.remove("Java"); // 此行不会报错，因为it.remove()首先移除的是"Java"
            // col.remove("hello");
        }
        System.out.println(col.size()); // 0
    }
}
