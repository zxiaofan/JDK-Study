
package java1.lang.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.gson.Gson;

import java1.lang.annotation.validation.AssertFalse;
import java1.lang.annotation.validation.Email;
import java1.lang.annotation.validation.ToUpper;
import java1.lang.annotation.validation.ValidationUtils;

/**
 * 自定义注解，参数验证
 * 
 * @author zxiaofan
 */
public class DIYAnnotation_Study {
    Gson gson = new Gson();

    @Test
    public void testDIYAnno() {
        AnnoVo vo = new AnnoVo();
        String param = "I am about to change Upper";
        String validate = null;
        try {
            vo.setGuestEmail("hi@zxiaofan.com");
            vo.setToUpper(param);
            vo.setBoolFalse(false); // true将报错,java.lang.AssertionError: expected null, but was:<参数:[boolFalse]只能为false>
            validate = ValidationUtils.validate(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(validate); // 为空则参数正确
        assertEquals(param.toUpperCase(), vo.getToUpper());
    }
}

class AnnoVo {
    @Email
    private String guestEmail;

    @ToUpper
    private String toUpper;

    @AssertFalse
    private boolean boolFalse;

    /**
     * 设置guestEmail.
     * 
     * @return 返回guestEmail
     */
    public String getGuestEmail() {
        return guestEmail;
    }

    /**
     * 获取guestEmail.
     * 
     * @param guestEmail
     *            要设置的guestEmail
     */
    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    /**
     * 设置toUpper.
     * 
     * @return 返回toUpper
     */
    public String getToUpper() {
        return toUpper;
    }

    /**
     * 获取toUpper.
     * 
     * @param toUpper
     *            要设置的toUpper
     */
    public void setToUpper(String toUpper) {
        this.toUpper = toUpper;
    }

    /**
     * 设置boolFalse.
     * 
     * @return 返回boolFalse
     */
    public boolean isBoolFalse() {
        return boolFalse;
    }

    /**
     * 获取boolFalse.
     * 
     * @param boolFalse
     *            要设置的boolFalse
     */
    public void setBoolFalse(boolean boolFalse) {
        this.boolFalse = boolFalse;
    }

}