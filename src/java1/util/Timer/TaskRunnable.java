/*
 * 文件名：TaskRunnable.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： TaskRunnable.java
 * 修改人：zxiaofan
 * 修改时间：2017年5月3日
 * 修改内容：新增
 */
package java1.util.Timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 * 
 * @author zxiaofan
 */
public class TaskRunnable extends TimerTask {
    /**
     * 添加字段注释.
     */
    private String name;

    /**
     * 模拟执行时间.
     */
    private long executeTime = 0;

    /**
     * 构造函数.
     * 
     */
    public TaskRunnable() {
        super();
    }

    /**
     * 构造函数.
     * 
     * @param name
     *            name
     * @param executeTime
     *            executeTime
     */
    public TaskRunnable(String name, long executeTime) {
        super();
        this.name = name;
        this.executeTime = executeTime;
    }

    /**
     * 构造函数.
     * 
     * @param name
     *            name
     */
    public TaskRunnable(String name) {
        super();
        this.name = name;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void run() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            System.err.println("run[" + name + "]:" + format.format(new Date()));
            Thread.sleep(executeTime);
        } catch (Exception e) { // TimerTask抛出的了未检查异常则会导致Timer线程终止，同时Timer也不会重新恢复线程的执行
            e.printStackTrace();
        }
    }

}
