/*
 * 文件名：TimerStudy.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： TimerStudy.java
 * 修改人：zxiaofan
 * 修改时间：2017年5月3日
 * 修改内容：新增
 */
package java1.util.Timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.Test;

/**
 * Timer.
 * 
 * TimerTask抛出的了未检查异常则会导致Timer线程终止，同时Timer也不会重新恢复线程的执行
 * 
 * <1>schedule()：间隔时间固定，上次任务执行完毕后，延迟delay再执行下一次任务
 * 
 * <2>scheduleAtFixedRate()：执行频率固定，不管上次任务执行完毕与否，都在指定间隔后执行本次任务
 * 
 * @author zxiaofan
 */
public class TimerStudy {
    /**
     * 添加字段注释.
     */
    Timer timerDefault = new Timer();

    /**
     * 添加字段注释.
     */
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    /**
     * 延迟.
     */
    private long delay = 1000L;

    /**
     * 间隔.
     */
    private long period = 1000L;

    /**
     * 执行时间单位间隔.
     */
    private long executeTime = 1000L;

    /**
     * timerDefault.
     * 
     */
    @Test
    public void testDefault() {
        printNow();
        TimerTask task = new TaskRunnable("延迟指定时间执行任务...");
        timerDefault.schedule(task, delay); // schedule(TimerTask task, long delay)：以当前时间为准，延迟delay毫秒后执行Task
        Date firstTime = null;
        try {
            firstTime = format.parse("2015-11-12 00:11:22.3");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sleep(1000);
        // TimerTask不能重用
        task = new TaskRunnable("指定时间执行任务...");
        timerDefault.schedule(task, firstTime); // schedule(TimerTask task, Date time)：在指定时间执行任务，如果时间已过期立即执行任务
        sleep(1000);
        //
        task = new TaskRunnable("延迟delay，间隔period执行任务...");
        timerDefault.schedule(task, delay, 2 * period);
        sleep(1000);
        //
        task = new TaskRunnable("...从指定时间开始，间隔period执行任务...");
        timerDefault.schedule(task, firstTime, 4 * period); // 若时间已过期立即执行任务
        sleep(100000);
    }

    /**
     * timerDefault.
     * 
     * 待确认.
     */
    @Test
    public void testScheduleAtFixedRate() {
        printNow();
        TimerTask task = new TaskRunnable("延迟delay，间隔period执行任务...scheduleAtFixedRate频率固定...", 8 * executeTime);
        timerDefault.scheduleAtFixedRate(task, 2 * delay, 2 * period); // executeTime>period，实际间隔时间为executeTime？why不是period，费解
        // Date firstTime = null;
        // try {
        // firstTime = format.parse("2015-11-12 00:11:22.3");
        // } catch (ParseException e) {
        // e.printStackTrace();
        // }
        // timerDefault.scheduleAtFixedRate(task, firstTime, 3000L); // 指定firstTime时间后，每次间隔period开始执行任务（firstTime过期则立即执行）
        // TimerTask task2 = new TaskRunnable("延迟delay，间隔period执行任务...schedule间隔固定...", 4 * executeTime);
        // timerDefault.schedule(task2, 2 * delay, 2 * period); // executeTime>period，则时间间隔为executeTime？费解
        sleep(100000);
    }

    /**
     * sleep，确保任务已执行.
     * 
     * @param sleep
     *            sleep
     */
    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * printNow.
     * 
     */
    private void printNow() {
        System.out.println("now:" + format.format(new Date()));
    }
}
