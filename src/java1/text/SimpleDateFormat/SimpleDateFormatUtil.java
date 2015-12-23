/*
 * 文件名：SimpleDateFormatUtil.java
 * 版权：Copyright 2007-2015 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SimpleDateFormatUtil.java
 * 修改人：yunhai
 * 修改时间：2015年12月23日
 * 修改内容：新增
 */
package java1.text.SimpleDateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 静态的simpleDateFormat实例，避免创建大量对象。
 * 
 * @author yunhai
 */
public class SimpleDateFormatUtil {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String format(Date date) {
        return sdf.format(date);
    }

    public static Date parse(String source) throws ParseException {
        return sdf.parse(source);
    }

    /**
     * 同步SimpleDateFormat对象以适应多线程.
     * 
     * @param date
     * @return
     */
    public static String formatSync(Date date) {
        synchronized (sdf) {
            return sdf.format(date);
        }
    }

    public static Date parseSync(String source) throws ParseException {
        synchronized (sdf) {
            return sdf.parse(source);
        }
    }

    /**
     * 使用ThreadLocal解决SimpleDateFormat不同步问题.
     */
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static String formatLocal(Date date) {
        return threadLocal.get().format(date);
    }

    public static Date parseLocal(String source) throws ParseException {
        return threadLocal.get().parse(source);
    }
}
