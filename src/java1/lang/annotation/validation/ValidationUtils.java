package java1.lang.annotation.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;

import java1.lang.annotation.SpecialParameterizedType;

/**
 * @author github.zxiaofan.com
 *
 *         用于参数校验
 */
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
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

    static Gson gson = new Gson();

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
     * 长度处理.
     * 
     * @param obj
     *            obj
     * @return obj
     * @throws Exception
     *             Exception.
     */
    public static Object stringCut(Object obj) throws Exception {
        if (null == obj) {
            return null;
        }
        // 处理集合
        if (obj instanceof List) {
            List<Class> listRealClass = getRealClass(obj);
            List<Object> listNew = (List<Object>) obj.getClass().newInstance();
            List<Object> listOld = (List) obj; // gson.fromJson(gson.toJson(obj), List.class);
            for (int i = 0; i < listOld.size(); i++) {
                Object objNew = listOld.get(i);
                objNew = gson.fromJson(gson.toJson(objNew), listRealClass.get(i));
                objNew = stringCut(objNew);
                listNew.add(objNew);
            }
            obj = listNew;
        } else if (obj instanceof Set) {
            List<Class> listRealClass = getRealClass(obj);
            Set<Object> setNew = (Set<Object>) obj.getClass().newInstance();
            Set<Object> setOld = (Set) obj; // gson.fromJson(gson.toJson(obj), Set.class);
            for (Object set : setOld) {
                int i = 0;
                Object objNew = gson.fromJson(gson.toJson(set), listRealClass.get(i));
                objNew = stringCut(objNew);
                setNew.add(objNew);
                i++;
            }
            obj = setNew;
        } else if (obj instanceof Map) {
            Map<?, ?> mapClass = getRealClassOfMap((Map<?, ?>) obj);
            Map<Object, Object> mapNew = (Map<Object, Object>) obj.getClass().newInstance();
            Map<?, ?> mapOld = (Map<?, ?>) obj; // gson.fromJson(gson.toJson(obj), Map.class);
            for (Entry<?, ?> set : mapOld.entrySet()) {
                Iterator ite = mapClass.entrySet().iterator();
                Entry entry = null;
                if (ite.hasNext()) {
                    entry = (Entry) ite.next();
                    Object objKey = stringCut(set.getKey());
                    // objKey = gson.fromJson(set.getKey().toString(), (Class) entry.getKey());
                    objKey = stringCut(objKey);
                    Object objValue = stringCut(set.getValue());
                    // objValue = gson.fromJson(gson.toJson(set.getValue()), (Class) entry.getValue());
                    mapNew.put(objKey, objValue);
                }
            }
            obj = mapNew;
        } else { // Object 或 泛型T
            cutObject(obj);
        }
        return obj;
    }

    /**
     * 处理泛型Object.
     * 
     * @param obj
     *            obj
     * @param field
     *            field
     * @param genericName
     *            genericName
     * @throws IllegalAccessException
     *             IllegalAccessException
     */
    private static void dealGenericObj(Object obj, Field field, String genericName) throws IllegalAccessException {
        List<Class> listGenericClass = getGenericClass(obj, genericName);
        field.setAccessible(true);
        Object objGen = gson.fromJson(gson.toJson(field.get(obj)), listGenericClass.get(0)); // field.get(obj)获取泛型对象
        try {
            objGen = stringCut(objGen);
        } catch (Exception e) {
            e.printStackTrace();
        }
        field.set(obj, objGen);
    }

    /**
     * 该Object内部属性含有泛型注解.
     * 
     * @param field
     *            field
     * @return isGeneric
     */
    private static String isGeneric(Field field) {
        Annotation[] classAnnotation = field.getAnnotations();
        StringCut stringCut = null;
        for (Annotation annotation : classAnnotation) {
            if (annotation.annotationType().getName().equals(StringCut.class.getName())) {
                stringCut = (StringCut) annotation;
                if (!"".equals(stringCut.isGeneric())) {
                    return stringCut.isGeneric();
                }
            }
        }
        return "";
    }

    /**
     * Object有StringCut注解.
     * 
     * @param obj
     *            obj
     * @return hasAnno
     */
    private static boolean hasAnnoOfStringCut(Object obj) {
        Annotation[] classAnnotation = obj.getClass().getAnnotations();
        boolean isTooLong = false;
        for (Annotation annotation : classAnnotation) {
            Class annotationType = annotation.annotationType();
            if (annotationType.getName().equals(StringCut.class.getName())) {
                isTooLong = true;
                break;
            }
        }
        return isTooLong;
    }

    /**
     * 处理非集合类Object，包括泛型.
     * 
     * @param obj
     *            obj
     */
    private static void cutObject(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        String claName = obj.getClass().getName();
        for (Field field : fields) {
            try {
                String isGeneric = isGeneric(field);
                if (!"".equals(isGeneric)) {
                    dealGenericObj(obj, field, isGeneric);
                } else {
                    dealBasicObject(obj, claName, field);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Class> getGenericClass(Object obj, String genericName) throws IllegalAccessException {
        List<Class> listGenericClass = new ArrayList<>();
        Field[] fds = obj.getClass().getDeclaredFields();
        for (Field fd : fds) {
            fd.setAccessible(true);
            if (genericName.equals(fd.getName())) {
                listGenericClass.add(fd.get(obj).getClass());
            }
        }
        return listGenericClass;
    }

    private static List<Class> getRealClass(Object obj) throws IllegalAccessException {
        List<Class> listRealClass = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        if (obj instanceof List) {
            for (Field fd : fields) {
                fd.setAccessible(true);
                if ("elementData".equals(fd.getName())) {
                    Object[] objs = (Object[]) fd.get(obj);
                    for (Object object : objs) {
                        if (null != object) {
                            listRealClass.add(object.getClass());
                        }
                    }
                }
            }
        } else if (obj instanceof Set) {
            for (Field fd : fields) {
                if ("map".equals(fd.getName())) {
                    fd.setAccessible(true);
                    Map<?, ?> map = (Map<?, ?>) fd.get(obj);
                    for (Object object : map.keySet()) {
                        if (null != object) {
                            listRealClass.add(object.getClass());
                        }
                    }
                }
            }
        } else {
            listRealClass.add(obj.getClass());
        }
        return listRealClass;
    }

    private static Map<Object, Object> getRealClassOfMap(Map<?, ?> obj) throws IllegalAccessException {
        Map<Object, Object> map = new HashMap();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field fd : fields) {
            fd.setAccessible(true);
            if ("table".equals(fd.getName())) {
                Object key = null;
                Object value = null;
                for (Entry<?, ?> entry : obj.entrySet()) {
                    if (null != entry) {
                        Class keyClass = getRealClass(entry.getKey()).get(0);
                        Type typeKey = null;
                        if (entry.getKey() instanceof List) {
                            typeKey = new SpecialParameterizedType(List.class, keyClass);
                        } else if (entry.getKey() instanceof Set) {
                            typeKey = new SpecialParameterizedType(Set.class, keyClass);
                        } else if (entry.getKey() instanceof Map) {
                            typeKey = new SpecialParameterizedType(Map.class, keyClass);
                        }
                        Class valueClass = getRealClass(entry.getValue()).get(0);
                        Type typeValue = null;
                        if (entry.getValue() instanceof List) {
                            typeValue = new SpecialParameterizedType(List.class, keyClass);
                        } else if (entry.getValue() instanceof Set) {
                            typeValue = new SpecialParameterizedType(Set.class, keyClass);
                        } else if (entry.getValue() instanceof Map) {
                            typeValue = new SpecialParameterizedType(Map.class, keyClass);
                        }
                        if (null != typeKey) {
                            key = typeKey;
                        } else {
                            key = keyClass;
                        }
                        if (null != typeValue) {
                            value = typeValue;
                        } else {
                            value = valueClass;
                        }
                        map.put(key, value);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 处理基础Object.
     * 
     * @param obj
     *            参数
     * @param claName
     *            类名
     * @param field
     *            属性
     * @throws Exception
     *             e
     */
    private static void dealBasicObject(Object obj, String claName, Field field) throws Exception {
        boolean isTooLong = hasAnnoOfStringCut(obj);
        if (!isTooLong) {
            return;
        }
        Date date;
        Object value;
        String fieldName;
        Class type;
        type = field.getType();
        fieldName = field.getName();
        Annotation[] annotations = field.getAnnotations();
        field.setAccessible(true);
        value = field.get(obj);
        if (type.isPrimitive() || String.class.getName().equals(type.getName())) {
            for (Annotation an : annotations) {
                if (an.annotationType().getName().equals(StringCut.class.getName())) {
                    dealTooLong(obj, claName, field, value, fieldName, type, an);
                }
            }
        } else {
            value = stringCut(value);
            field.set(obj, value);
        }
    }

    private static Class getRealClass(Field field) {
        Type fc = field.getGenericType();
        ParameterizedType pt = null;
        try {
            if (fc instanceof ParameterizedType) {
                pt = (ParameterizedType) fc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Class genericClazz = null;
        if (null != pt) {
            genericClazz = (Class) pt.getActualTypeArguments()[0];
        }
        return genericClazz;
    }

    /**
     * 处理含有@StringCut的属性，超长则截取.
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
    private static void dealTooLong(Object obj, String claName, Field field, Object value, String fieldName, Class type, Annotation an) {
        StringCut stringCut = (StringCut) an;
        if (String.class.equals(type)) {
            if (null != value && value.toString().length() > stringCut.maxLength() && stringCut.maxLength() != 0) {
                try {
                    // WriteLogBusi.writeLocalLog(fieldName, value.toString());
                    if (stringCut.isKeepHead()) {
                        field.set(obj, value.toString().substring(0, stringCut.maxLength()));
                    } else {
                        field.set(obj, value.toString().substring(value.toString().length() - stringCut.maxLength()));
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // WriteLogBusi.writeLocalLog(fieldName, e.getMessage() + e.toString());
                }
            }
            if (null != value && value.toString().length() < stringCut.minLength()) {
                int addLength = stringCut.minLength() - value.toString().length();
                String addValue = "";
                while (addValue.length() < addLength) {
                    addValue += stringCut.completion();
                }
                addValue = addValue.substring(0, addLength);
                try {
                    // WriteLogBusi.writeLocalLog(fieldName, value.toString());
                    if (stringCut.isKeepHead()) {
                        field.set(obj, value.toString() + addValue);
                    } else {
                        field.set(obj, addValue + value.toString());
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // WriteLogBusi.writeLocalLog(fieldName, e.getMessage() + e.toString());
                }
            }
        }
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
