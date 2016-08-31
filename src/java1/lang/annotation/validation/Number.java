package java1.lang.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxiaofan
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Number {
    /**
     * 添加方法注释.
     * 
     * @return
     */
    double min() default Double.MIN_VALUE;

    /**
     * 添加方法注释.
     * 
     * @return
     */
    double max() default Double.MAX_VALUE;

    /**
     * 添加方法注释.
     * 
     * @return
     */
    long defaultValue() default 0;

    /**
     * 不在最大值最小值之间取这个值.
     * 
     * @return
     */
    long overstep() default 0;

}
