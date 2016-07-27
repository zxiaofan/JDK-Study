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
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

/**
 * 最近需将大量json转换为对应的JavaBean，网上有诸多在线转换工具，但是由于公司对代码要求checkStyle，故自己写个转换工具
 * 
 * 不喜欢重复且没技术含量的工作，要么沉沦要么改变。
 * 
 * 持续更新地址：https://github.com/zxiaofan/JDK-Study/tree/master/src/utils
 * 
 * @author yunhai
 */
public class JsonToJavaBean {
    public static void main(String[] args) {
        JsonToJavaBean.start();
    }

    public static void start() {
        System.out.println("请输入待转换的json文件的绝对路径：");
        // Scanner scanner = new Scanner(System.in);
        // String path = scanner.nextLine();
        String packageName = "";
        String path = "‪D:\\json.txt";
        toJavaBean(path, packageName);
        System.out.println("转换完成，请到" + outputPath + "查看转换结果！");
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

    private static Set<Bean> fields = new HashSet();

    /**
     * 转bean.
     * 
     * @param path
     * @param packageName
     */
    private static void toJavaBean(String path, String packageName) {
        String json = readTextFile(path, encode);
        if (!(json.startsWith("{") || json.endsWith("}"))) {
            throw new RuntimeException("不是标准的json文件:{...}"); // 暂不做过多校验
        }
        json = formatStr(json);
        // 第一个field
        int i = 0, j = 0, k = 0;
        while (i < json.length()) {
            i = json.indexOf("\"");
            j = json.indexOf("\":");
            k = json.indexOf(",");
            Bean bean = new Bean();
            bean.setFieldName(json.substring(i + 1, j));
            String beanValue = json.substring(j + 2, k);
            if (beanValue.startsWith("\"")) {
                bean.setFieldType(ObjType.String.getType());
            } else if (beanValue.startsWith("{")) {
                bean.setFieldType(ObjType.Defined.getType());
            } else if (beanValue.startsWith("[")) {
                bean.setFieldType(ObjType.List.getType());
            } else if (beanValue.contains(".")) {
                bean.setFieldType(ObjType.Double.getType());
            } else {
                bean.setFieldType(ObjType.Int.getType());
            }
        }
    }

    private static String readTextFile(String sFileName, String sEncode) {
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
    private static void createFile(String path) {
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
    private static String formatStr(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    /**
     * 输出路径.
     */
    private static String outputPath = "d:\\JsonToJavaBean\\";

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