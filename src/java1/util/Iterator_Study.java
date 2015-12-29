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
            col.remove("Java"); // 此行不会报错，因为it.remove()首先移除的是"Java"
            // col.remove("hello");
        }
        System.out.println(col.size()); // 0
    }
}
