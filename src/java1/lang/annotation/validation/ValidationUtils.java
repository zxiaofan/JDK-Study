package java1.lang.annotation.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author github.zxiaofan.com
 *
 *         用于参数校验
 */
@SuppressWarnings({"rawtypes", "unused"})
public final class ValidationUtils {

    /**
     * 添加参数注释.
     */
    private static final String REGEXMAIL = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";

    /**
     * 添加参数注释.
     */
    private static final String REGEXPHONE = "^[1][3-8]+\\d{9}$|^0\\d{2,3}-\\d{5,9}$|0\\d{7,12}$";

    /**
     * 添加参数注释.
     */
    private static final String REGEXTEL = "^0\\d{2,3}-{0,1}\\d{5,9}$|0\\d{7,12}$";

    /**
     * 添加参数注释.
     */
    private static final String REGEXPOSTALCODE = "^\\d{6}$";

    /**
     * 构造函数.
     * 
     */
    private ValidationUtils() {
        throw new RuntimeException("this is a util class,can not instance!");
    }

    /**
     * 参数校验.
     * 
     * @param obj
     *            obj
     * @return obj
     * @throws Exception
     *             e
     */
    public static String validate(Object obj) throws Exception {
        List<String> list = new ArrayList<String>();
        if (null == obj) {
            return "参数为null";
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        long now = System.currentTimeMillis();
        String claName = obj.getClass().getName();
        for (Field field : fields) {
            realValidate(obj, list, now, claName, field);
        }

        return list.isEmpty() ? null : packaging(list);
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @return 结果
     */
    private static String packaging(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(s).append(';');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 添加方法注释.
     * 
     * @param obj
     *            参数
     * @param list
     *            结果
     * @param now
     *            当前时间
     * @param claName
     *            类名
     * @param field
     *            属性
     * @throws Exception
     *             e
     */
    private static void realValidate(Object obj, List<String> list, long now, String claName, Field field) throws Exception {
        Null nullAnnotation;
        NotNull notNull;
        AssertFalse assertFalse;
        AssertTrue assertTrue;
        Email email;
        FutureTime futureTime;
        StringLength stringLength;
        PastTime pastTime;
        Pattern pattern;
        Phone phone;
        PostalCode postalCode;
        Tel tel;
        NotNullAndEmpty nullAndEmpty;
        Number number;
        ToLower lower;
        ToUpper upper;
        Date date;
        Object value;
        String fieldName;
        Class type;
        boolean isNotNull;
        type = field.getType();
        fieldName = field.getName();
        Annotation[] annotations = field.getAnnotations();
        field.setAccessible(true);
        value = field.get(obj);
        isNotNull = field.isAnnotationPresent(NotNull.class);
        for (Annotation an : annotations) {
            if (an.annotationType().getName().equals(Null.class.getName())) {
                validateNull(list, claName, field, value, fieldName, type, an);
            } else if (an.annotationType().getName().equals(NotNull.class.getName())) {
                validateNotNull(list, claName, field, value, fieldName, type, an);
            } else if (an.annotationType().getName().equals(AssertFalse.class.getName())) {
                validateAssertFalse(list, claName, field, value, fieldName, type, isNotNull, an);
            } else if (an.annotationType().getName().equals(AssertTrue.class.getName())) {
                validateAssertTrue(list, claName, field, value, fieldName, type, isNotNull, an);
            } else if (an.annotationType().getName().equals(Email.class.getName())) {
                validateEmail(list, claName, field, value, fieldName, type, isNotNull, an);
            } else if (an.annotationType().getName().equals(FutureTime.class.getName())) {
                validateFutureTime(list, now, claName, field, value, fieldName, type, isNotNull, an);
            } else if (an.annotationType().getName().equals(StringLength.class.getName())) {
                validateStringLength(list, claName, field, value, fieldName, type, isNotNull, an);
            } else if (an.annotationType().getName().equals(PastTime.class.getName())) {
                validatePastTime(list, now, claName, field, value, fieldName, type, isNotNull, an);
            } else if (an.annotationType().getName().equals(Pattern.class.getName())) {
                validatePattern(list, claName, field, value, fieldName, type, isNotNull, an);
            } else if (an.annotationType().getName().equals(Phone.class.getName())) {
                validatePhone(list, claName, field, value, fieldName, type, isNotNull, an);
            } else if (an.annotationType().getName().equals(PostalCode.class.getName())) {
                validatePastalCode(list, claName, field, value, fieldName, type, isNotNull, an);
            } else if (an.annotationType().getName().equals(Tel.class.getName())) {
                validateTel(list, claName, field, value, fieldName, type, isNotNull, an);
            } else if (an.annotationType().getName().equals(NotNullAndEmpty.class.getName())) {
                validateNotNullAndEmpty(list, claName, field, value, fieldName, type, an);
            } else if (an.annotationType().getName().equals(Number.class.getName())) {
                validateNumber(obj, claName, field, value, fieldName, type, an);
            } else if (an.annotationType().getName().equals(ToLower.class.getName())) {
                validateLower(obj, claName, field, value, fieldName, type, an);
            } else if (an.annotationType().getName().equals(ToUpper.class.getName())) {
                validateUpper(obj, claName, field, value, fieldName, type, an);
            }
        }
        field.setAccessible(false);
        nullAnnotation = null;
        notNull = null;
        assertFalse = null;
        assertTrue = null;
        email = null;
        futureTime = null;
        stringLength = null;
        number = null;
        pastTime = null;
        pattern = null;
        phone = null;
        postalCode = null;
        tel = null;
        nullAndEmpty = null;
    }

    /**
     * 添加方法注释.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @throws Exception
     *             e
     */
    private static void validateUpper(Object obj, String claName, Field field, Object value, String fieldName, Class type, Annotation an) throws Exception {
        ToUpper upper = (ToUpper) an;
        if (String.class.equals(type)) {
            if (null != value) {
                field.set(obj, value.toString().toUpperCase());
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.Upper只能使用在java.lang.String等类型上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @throws Exception
     *             e
     */
    private static void validateLower(Object obj, String claName, Field field, Object value, String fieldName, Class type, Annotation an) throws Exception {
        ToLower lower = (ToLower) an;
        if (String.class.equals(type)) {
            if (null != value) {
                field.set(obj, value.toString().toLowerCase());
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.Lower只能使用在java.lang.String等类型上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param obj
     *            对象
     * @param an
     *            注解
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @throws Exception
     *             e
     */
    private static void validateNumber(Object obj, String claName, Field field, Object value, String fieldName, Class type, Annotation an) throws Exception {
        Number number;
        number = (Number) an;
        if (byte.class.equals(type) || Byte.class.equals(type) || short.class.equals(type) || Short.class.equals(type) || int.class.equals(type) || Integer.class.equals(type)
                || long.class.equals(type) || Long.class.equals(type)) {
            if (null == value) {
                field.set(obj, number.defaultValue());
            } else {
                double d = (double) value;
                if (d > number.max() || d < number.min()) {
                    field.set(obj, number.overstep());
                }
            }
        } else if (float.class.equals(type) || Float.class.equals(type) || double.class.equals(type) || Double.class.equals(type)) {
            if (null == value) {
                field.set(obj, number.defaultValue());
            } else {
                double d = (double) value;
                if (d > number.max() || d < number.min()) {
                    field.set(obj, number.overstep());
                }
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.Number只能使用在byte/short/int/long/double/float等类型上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateNotNullAndEmpty(List<String> list, String claName, Field field, Object value, String fieldName, Class type, Annotation an) throws Exception {
        NotNullAndEmpty nullAndEmpty;
        nullAndEmpty = (NotNullAndEmpty) an;
        if (String.class.equals(type)) {
            if (null == value || "".equals(value.toString().trim())) {
                list.add("参数:[" + field.getName() + "]不能为null或者空字符串");
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.NotNullAndEmpty只能使用在java.lang.String上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @param an
     *            注解
     * @param isNotNull
     *            不能为空
     * @throws Exception
     *             e
     */
    private static void validateTel(List<String> list, String claName, Field field, Object value, String fieldName, Class type, boolean isNotNull, Annotation an) throws Exception {
        Tel tel;
        tel = (Tel) an;
        if (String.class.equals(type)) {
            if (isNotNull) {
                if (null == value || !value.toString().matches(REGEXTEL)) {
                    list.add("参数:[" + field.getName() + "]必须为座机;格式:" + REGEXTEL);
                }
            } else {
                if (null != value && !value.toString().matches(REGEXTEL)) {
                    list.add("参数:[" + field.getName() + "]必须为座机;格式:" + REGEXTEL);
                }
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.Tel只能使用在java.lang.String上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @param an
     *            注解
     * @param isNotNull
     *            不能为空
     * @throws Exception
     *             e
     */
    private static void validatePastalCode(List<String> list, String claName, Field field, Object value, String fieldName, Class type, boolean isNotNull, Annotation an) throws Exception {
        PostalCode postalCode;
        postalCode = (PostalCode) an;
        if (String.class.equals(type)) {
            if (isNotNull) {
                if (null == value || !value.toString().matches(REGEXPOSTALCODE)) {
                    list.add("参数:[" + field.getName() + "]必须为邮编;格式:" + REGEXPOSTALCODE);
                }
            } else {
                if (null != value && !value.toString().matches(REGEXPOSTALCODE)) {
                    list.add("参数:[" + field.getName() + "]必须为邮编;格式:" + REGEXPOSTALCODE);
                }
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.PostalCode只能使用在java.lang.String上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            a
     * @param claName
     *            a
     * @param field
     *            a
     * @param value
     *            a
     * @param fieldName
     *            aa
     * @param type
     *            a
     * @param isNotNull
     *            a
     * @param an
     *            a
     * @throws Exception
     *             a
     */
    private static void validatePhone(List<String> list, String claName, Field field, Object value, String fieldName, Class type, boolean isNotNull, Annotation an) throws Exception {
        Phone phone;
        phone = (Phone) an;
        if (String.class.equals(type)) {
            if (isNotNull) {
                if (null == value || !value.toString().matches(REGEXPHONE)) {
                    list.add("参数:[" + field.getName() + "]必须为手机号,格式:" + REGEXPHONE);
                }
            } else {
                if (null != value && !value.toString().matches(REGEXPHONE)) {
                    list.add("参数:[" + field.getName() + "]必须为手机号,格式:" + REGEXPHONE);
                }
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.Phone只能使用在java.lang.String上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            fa
     * @param claName
     *            a
     * @param field
     *            a
     * @param value
     *            a
     * @param fieldName
     *            a
     * @param type
     *            a
     * @param isNotNull
     *            a
     * @param an
     *            a
     * @throws Exception
     *             a
     */
    private static void validatePattern(List<String> list, String claName, Field field, Object value, String fieldName, Class type, boolean isNotNull, Annotation an) throws Exception {
        Pattern pattern;
        pattern = (Pattern) an;
        if (String.class.equals(type)) {
            if (isNotNull) {
                if (null == value || !value.toString().matches(pattern.value())) {
                    list.add("参数:[" + field.getName() + "]必须满足格式:" + pattern.value());
                }
            } else {
                if (null != value && !value.toString().matches(pattern.value())) {
                    list.add("参数:[" + field.getName() + "]必须满足格式:" + pattern.value());
                }
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.Pattern只能使用在java.lang.String上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            a
     * @param claName
     *            a
     * @param field
     *            a
     * @param value
     *            a
     * @param fieldName
     *            a
     * @param type
     *            a
     * @param isNotNull
     *            aa
     * @param an
     *            a
     * @throws Exception
     *             a
     */
    private static void validateStringLength(List<String> list, String claName, Field field, Object value, String fieldName, Class type, boolean isNotNull, Annotation an) throws Exception {
        StringLength stringLength;
        stringLength = (StringLength) an;
        if (String.class.equals(type)) {
            if (isNotNull) {
                if (null == value || value.toString().length() < stringLength.min() || value.toString().length() > stringLength.max()) {
                    list.add("参数:[" + field.getName() + "]长度必须在" + stringLength.min() + "和" + stringLength.max() + "之间");
                }
            } else {
                if (null != value && (value.toString().length() < stringLength.min() || value.toString().length() > stringLength.max())) {
                    list.add("参数:[" + field.getName() + "]长度必须在" + stringLength.min() + "和" + stringLength.max() + "之间");
                }
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.StringLength只能使用java.util.String上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @param an
     *            注解
     * @param isNotNull
     *            不能为空
     * @param now
     *            当前毫秒
     * @throws Exception
     *             e
     */
    private static void validatePastTime(List<String> list, long now, String claName, Field field, Object value, String fieldName, Class type, boolean isNotNull, Annotation an) throws Exception {
        PastTime pastTime;
        Date date;
        pastTime = (PastTime) an;
        date = (Date) value;
        if (Date.class.equals(type)) {
            if (isNotNull) {
                if (null == value || date.getTime() >= now) {
                    list.add("参数:[" + field.getName() + "]必须是过去某个时间");
                }
            } else {
                if (null != value && date.getTime() >= now) {
                    list.add("参数:[" + field.getName() + "]必须是过去某个时间");
                }
            }

        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.PastTime只能使用在java.util.Date上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @param an
     *            注解
     * @param isNotNull
     *            不能为空
     * @param now
     *            当前毫秒
     * @throws Exception
     *             e
     */
    private static void validateFutureTime(List<String> list, long now, String claName, Field field, Object value, String fieldName, Class type, boolean isNotNull, Annotation an) throws Exception {
        FutureTime futureTime;
        Date date;
        futureTime = (FutureTime) an;
        date = (Date) value;
        if (Date.class.equals(type)) {
            if (isNotNull) {
                if (null == value || date.getTime() <= now) {
                    list.add("参数:[" + field.getName() + "]必须是将来某个时间");
                }
            } else {
                if (null != value && date.getTime() <= now) {
                    list.add("参数:[" + field.getName() + "]必须是将来某个时间");
                }
            }

        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.FutureTime只能使用在java.util.Date上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @param an
     *            注解
     * @param isNotNull
     *            不能为空
     * @throws Exception
     *             e
     */
    private static void validateEmail(List<String> list, String claName, Field field, Object value, String fieldName, Class type, boolean isNotNull, Annotation an) throws Exception {
        Email email;
        email = (Email) an;
        if (String.class.equals(type)) {
            if (isNotNull) {
                if (null == value || !value.toString().matches(REGEXMAIL)) {
                    list.add("参数:[" + field.getName() + "]必须是邮箱地址;格式为:" + REGEXMAIL);
                }
            } else {
                if (null != value && !value.toString().matches(REGEXMAIL)) {
                    list.add("参数:[" + field.getName() + "]必须是邮箱地址;格式为:" + REGEXMAIL);
                }
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.Email只能使用在java.lang.String上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @param an
     *            注解
     * @param isNotNull
     *            不能为空
     * @throws Exception
     *             e
     */
    private static void validateAssertTrue(List<String> list, String claName, Field field, Object value, String fieldName, Class type, boolean isNotNull, Annotation an) throws Exception {
        AssertTrue assertTrue;
        assertTrue = (AssertTrue) an;
        if (boolean.class.equals(type) || Boolean.class.equals(type)) {
            if (isNotNull) {
                if (null == value || !Boolean.parseBoolean(value.toString())) {
                    list.add("参数:[" + field.getName() + "]只能为true");
                }
            } else {
                if (null != value && !Boolean.parseBoolean(value.toString())) {
                    list.add("参数:[" + field.getName() + "]只能为true");
                }
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.AssertTrue只能使用在boolean或者java.lang.Boolean上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @param an
     *            注解
     * @param isNotNull
     *            不能为空
     * @throws Exception
     *             e
     */
    private static void validateAssertFalse(List<String> list, String claName, Field field, Object value, String fieldName, Class type, boolean isNotNull, Annotation an) throws Exception {
        AssertFalse assertFalse;
        assertFalse = (AssertFalse) an;
        if (boolean.class.equals(type) || Boolean.class.equals(type)) {
            if (isNotNull) {
                if (null == value || Boolean.parseBoolean(value.toString())) {
                    list.add("参数:[" + field.getName() + "]只能为false");
                }
            } else {
                if (null != value && Boolean.parseBoolean(value.toString())) {
                    list.add("参数:[" + field.getName() + "]只能为false");
                }
            }
        } else {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.AssertFalse只能使用在boolean或者java.lang.Boolean上");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateNotNull(List<String> list, String claName, Field field, Object value, String fieldName, Class type, Annotation an) throws Exception {
        NotNull notNull;
        notNull = (NotNull) an;
        if (int.class.equals(type) || long.class.equals(type) || short.class.equals(type) || byte.class.equals(type) || char.class.equals(type) || float.class.equals(type) || double.class.equals(type)
                || boolean.class.equals(type)) {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.NotNull不能使用在基本数据类型上");
        }
        if (null == value) {
            list.add("参数:[" + field.getName() + "]不能为null");
        }
    }

    /**
     * 添加方法注释.
     * 
     * @param list
     *            结果
     * @param claName
     *            类型
     * @param field
     *            属性
     * @param value
     *            属性值
     * @param fieldName
     *            属性名称
     * @param type
     *            类型
     * @param an
     *            注解
     * @throws Exception
     *             e
     */
    private static void validateNull(List<String> list, String claName, Field field, Object value, String fieldName, Class type, Annotation an) throws Exception {
        Null nullAnnotation;
        if (int.class.equals(type) || long.class.equals(type) || short.class.equals(type) || byte.class.equals(type) || char.class.equals(type) || float.class.equals(type) || double.class.equals(type)
                || boolean.class.equals(type)) {
            throw new Exception(claName + "类中字段[" + fieldName + "]注解:validation.Null不能使用在基本数据类型上");
        }
        nullAnnotation = (Null) an;
        if (null != value) {
            list.add("参数:[" + field.getName() + "]必须为null");
        }
    }
}
