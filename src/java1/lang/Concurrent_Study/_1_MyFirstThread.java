/*
 * 文件名：_1_MyFirstThread.java
 * 版权：Copyright 2007-2015 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： MyThread.java
 * 修改人：yunhai
 * 修改时间：2015年11月16日
 * 修改内容：新增
 */
package java1.lang.Concurrent_Study;

import org.junit.Test;

/**
 * 创建线程的两种方式。
 * 
 * @author yunhai
 */
public class _1_MyFirstThread {
    @Test
    public void byExtends() { // 继承Thread类
        System.out.println("主线程ID:" + Thread.currentThread().getId());
        MyThread myThread1 = new MyThread("Thread1");
        myThread1.start();
        MyThread myThread2 = new MyThread("Thread2");
        // 如果调用run方法，即相当于在主线程中执行run方法，跟普通的方法调用没有任何区别，此时并不会创建一个新的线程来执行定义的任务。
        myThread2.run(); // 先输出myThread2，后输出myThread1；说明新线程创建的过程不会阻塞主线程的后续执行.
        // myThread2.start();
    }

    /**
     * 这种方式必须将Runnable作为Thread类的参数，然后通过Thread的start方法来创建一个新线程来执行该子任务。
     * 
     * 如果直接调用Runnable的run方法的话，是不会创建新线程的，这根普通的方法调用没有任何区别。
     * 
     */
    @Test
    public void byRunnable() { // 实现Runnable接口
        System.out.println("子线程ID：" + Thread.currentThread().getId());
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();
    }

    /**
     * Thread类是实现了Runnable接口的.
     *
     */
    public class MyThread extends Thread {
        private String name;

        public MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("name:" + name + ", 子线程ID:" + Thread.currentThread().getId());
        }
    }

    public class MyRunnable implements Runnable {

        public MyRunnable() {
        }

        @Override
        public void run() {
            System.out.println("主线程ID:" + Thread.currentThread().getId());
        }
    }
}
