/*
 * 文件名：JsonToJavaBean.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： JsonToJavaBean.java
 * 修改人：yunhai
 * 修改时间：2016年7月20日
 * 修改内容：新增
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

/**
 * 最近需将大量json转换为对应的JavaBean，网上有诸多在线转换工具，但是由于公司对代码要求checkStyle，故自己写个转换工具
 * 
 * 持续更新地址：https://github.com/zxiaofan/JDK-Study/tree/master/src/utils
 * 
 * @author yunhai
 */
public class JsonToJavaBean {
    static JsonToJavaBean start = new JsonToJavaBean();

    public static void main(String[] args) {
        start.start();
    }

    public void start() {
        // System.out.println("请输入待转换的json文件的绝对路径：");
        // Scanner scanner = new Scanner(System.in);
        // String path = scanner.nextLine();
        String packageName = "";
        String path = "D:\\json.txt";
        String json = readTextFile(path, encode);
        toJavaBean(json, packageName);
        System.out.println("转换完成，请到" + outputPath + "查看转换结果！");
        createFile(outputPath);
        try {
            String[] cmd = new String[5];
            cmd[0] = "cmd";
            cmd[1] = "/c";
            cmd[2] = "start";
            cmd[3] = " ";
            cmd[4] = outputPath;
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String encode = "utf-8";

    private static Gson gson = new Gson();

    private static Map<String, List<Bean>> fields = new HashMap<>();

    /**
     * 转bean.
     * 
     * @param json
     * @param packageName
     */
    private void toJavaBean(String json, String packageName) {
        if (!(json.startsWith("{") || json.endsWith("}"))) {
            throw new RuntimeException("不是标准的json文件:{...}"); // 暂不做过多校验
        }
        json = formatStr(json);
        List<Bean> beans = new ArrayList<>();
        buildOrignBean(json, beans, "Root");
        System.out.println(gson.toJson(fields));
    }

    public void buildOrignBean(String json, List<Bean> beans, String className) {
        Map<String, Object> map = gson.fromJson(json, Map.class);
        Iterator<Entry<String, Object>> itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            Bean bean = new Bean();
            Entry<String, Object> entry = itr.next();
            String k = entry.getKey();
            bean.setFieldName(k);
            Object v = entry.getValue();
            if (v == null || v.toString().equals("[]")) {
                itr.remove();
                continue;
            }
            if (v instanceof Integer) {
                bean.setFieldType(ObjType.Int.getType());
                entry.setValue("Integer");
            } else if (v instanceof BigDecimal) {
                bean.setFieldType(ObjType.Double.getType());
                entry.setValue("Double");
            } else if (v instanceof Double) {
                bean.setFieldType(ObjType.Double.getType());
                entry.setValue("Double");
            } else if (v instanceof String) {
                bean.setFieldType(ObjType.String.getType());
                entry.setValue("String");
            } else {
                String childJson = v.toString();
                if (childJson.startsWith("{")) {
                    String childName = k;
                    entry.setValue(childName);
                    bean.setFieldName(k);
                    bean.setFieldType(ObjType.Defined.getType());
                    List<Bean> newBeans = new ArrayList<>();
                    buildOrignBean(childJson, newBeans, k);
                    fields.put(k, newBeans);
                } else if (childJson.startsWith("[")) {
                    bean.setFieldName(k);
                    bean.setFieldType(ObjType.List.getType());

                    childJson = getAllFieldOfList(childJson);
                    System.out.println(childJson);
                    Map<String, Object> childMap = gson.fromJson(childJson, Map.class);
                    List<Bean> newBeans = new ArrayList<>();
                    buildOrignBean(gson.toJson(childMap), newBeans, k);
                } else {
                    entry.setValue("String");
                }
            }
            beans.add(bean);
        }
        fields.put(className, beans);
    }

    /**
     * TODO 添加方法注释.
     * 
     * @param childJson
     * @return
     */
    private String getAllFieldOfList(String childJson) {
        childJson = childJson.substring(1, childJson.length() - 1);
        return null;
    }

    private String readTextFile(String sFileName, String sEncode) {
        StringBuffer sbStr = new StringBuffer();
        try {
            File ff = new File(sFileName);
            InputStreamReader read = new InputStreamReader(new FileInputStream(ff), sEncode);
            BufferedReader ins = new BufferedReader(read);
            String dataLine = "";
            while (null != (dataLine = ins.readLine())) {
                sbStr.append(dataLine);
                // sbStr.append("/r/n");
            }
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sbStr.toString();
    }

    /**
     * 新建文件（夹）.
     * 
     * @param path
     *            路径
     */
    private void createFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (path.contains(".")) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // 递归创建文件夹，保证该路径所有文件夹都被创建
                createFile(path.substring(0, path.lastIndexOf("\\")));
                file.mkdir();
            }
        }
    }

    /**
     * 去空格去换行.
     *
     * @param str
     *            str
     * @return str
     */
    private String formatStr(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
            // json = json.replace(":\".*?\"(?=[,}])", ":\"" + typeString + "\"");
            p = Pattern.compile(":\".*?\"(?=[,}])");
            m = p.matcher(str);
            str = m.replaceAll(":\"" + typeString + "\"");

        }
        return str;
    }

    /**
     * 输出路径.
     */
    private static String outputPath = "d:\\JsonToJavaBean\\";

    private static String typeString = "typeString";

    private static Stack<String> stack = new Stack<>();
}

class Bean {
    private String fieldName;

    private String fieldType;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

}

enum ObjType {
    String("String"), Int("int"), Float("float"), Date("Date"), Double("double"), BigDecimal("Bigdecimal"), List("List"), Map("Map"), Set("Set"), Defined("Defined");

    private String type;

    private ObjType(java.lang.String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}