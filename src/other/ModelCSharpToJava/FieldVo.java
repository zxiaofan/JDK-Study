/*
 * 文件名：FieldVo.java
 * 版权：Copyright 2007-2016 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： FieldVo.java
 * 修改人：yunhai
 * 修改时间：2016年3月28日
 * 修改内容：新增
 */
package other.ModelCSharpToJava;

/**
 * 字段信息
 * 
 * @author yunhai
 */
public class FieldVo {
    /**
     * 类名.
     */
    public String className;

    /**
     * 字段类型.
     */
    public String type;

    /**
     * 字段名-小写.
     */
    public String fieldNameLower;

    /**
     * 字段名-大写.
     */
    public String fieldNameUpper;

    /**
     * get.
     */
    public String getters;

    /**
     * set.
     */
    public String setters;

    /**
     * 类的相对路径.
     */
    public String relativePath;

    /**
     * 字段描述.
     */
    public String fieldDesc;

    /**
     * 设置className.
     * 
     * @return 返回className
     */
    public String getClassName() {
        return className;
    }

    /**
     * 获取className.
     * 
     * @param className
     *            要设置的className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 设置type.
     * 
     * @return 返回type
     */
    public String getType() {
        return type;
    }

    /**
     * 获取type.
     * 
     * @param type
     *            要设置的type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 设置fieldNameLower.
     * 
     * @return 返回fieldNameLower
     */
    public String getFieldNameLower() {
        return fieldNameLower;
    }

    /**
     * 获取fieldNameLower.
     * 
     * @param fieldNameLower
     *            要设置的fieldNameLower
     */
    public void setFieldNameLower(String fieldNameLower) {
        this.fieldNameLower = fieldNameLower;
    }

    /**
     * 设置fieldNameUpper.
     * 
     * @return 返回fieldNameUpper
     */
    public String getFieldNameUpper() {
        return fieldNameUpper;
    }

    /**
     * 获取fieldNameUpper.
     * 
     * @param fieldNameUpper
     *            要设置的fieldNameUpper
     */
    public void setFieldNameUpper(String fieldNameUpper) {
        this.fieldNameUpper = fieldNameUpper;
    }

    /**
     * 设置getters.
     * 
     * @return 返回getters
     */
    public String getGetters() {
        return getters;
    }

    /**
     * 获取getters.
     * 
     * @param getters
     *            要设置的getters
     */
    public void setGetters(String getters) {
        this.getters = getters;
    }

    /**
     * 设置setters.
     * 
     * @return 返回setters
     */
    public String getSetters() {
        return setters;
    }

    /**
     * 获取setters.
     * 
     * @param setters
     *            要设置的setters
     */
    public void setSetters(String setters) {
        this.setters = setters;
    }

    /**
     * 设置relativePath.
     * 
     * @return 返回relativePath
     */
    public String getRelativePath() {
        return relativePath;
    }

    /**
     * 获取relativePath.
     * 
     * @param relativePath
     *            要设置的relativePath
     */
    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    /**
     * 设置fieldDesc.
     * 
     * @return 返回fieldDesc
     */
    public String getFieldDesc() {
        return fieldDesc;
    }

    /**
     * 获取fieldDesc.
     * 
     * @param fieldDesc
     *            要设置的fieldDesc
     */
    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

}
