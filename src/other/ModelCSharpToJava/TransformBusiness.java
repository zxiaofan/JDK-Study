/*
 * 文件名：TransformBusiness.java
 * 版权：Copyright 2007-2016 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： TransformBusiness.java
 * 修改人：yunhai
 * 修改时间：2016年3月28日
 * 修改内容：新增
 */
package other.ModelCSharpToJava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author yunhai
 */
public class TransformBusiness {
    /**
     * 所有待转换文件绝对路径.
     */
    List<String> filePaths = new ArrayList<>();

    private static String encode = "utf-8";

    /**
     * 换行.
     */
    private static String rn = "\r\n";

    private static String tab = "    ";

    private static String desc1 = tab + "/**" + rn + tab + " * "; // 字段描述-开始部分

    private static String desc2 = "." + rn + tab + " */" + rn + tab;

    private static String author = "/**" + rn + " * @author yunhai(default)" + rn + " */" + rn;

    private static String importList = "import java.util.List;\r\n";

    private static String importBigDecimal = "import java.math.BigDecimal;\r\n";

    private static String importMap = "import java.util.Map;\r\n";

    private static String importDate = "import java.util.Date;\r\n";

    /**
     * import 包.
     */
    private static String importUtil = "import java.util.*;\r\n";

    /**
     * Map<相对路径，字段信息集合>.
     */
    private Map<String, List<FieldVo>> mapData = new HashMap();

    /**
     * 待转换的目录.
     */
    private static String originPath = "";

    /**
     * packageName包名.
     */
    private static String packageName = "";

    /**
     * package默认加入该路径.
     */
    private static String packagePath = "model.vo";

    /**
     * 输出路径.
     */
    private static String outputPath = "d:\\model\\vo";

    /**
     * 执行转换.
     * 
     * @param path
     *            绝对路径.
     * @param packageName1
     *            包名.
     */
    public void transform(String path, String packageName1) {
        originPath = path;
        packageName = packageName1;
        getAllFilePath(path);
        buildOriginData(filePaths);
        createJavaModel(mapData);
    }

    /**
     * 生成Java model.
     * 
     * @param map
     */
    private void createJavaModel(Map<String, List<FieldVo>> map) {
        createFile("d:\\model");
        createFile(outputPath);
        for (Entry<String, List<FieldVo>> entry : map.entrySet()) {
            String relativePath = entry.getKey();
            if (!"".equals(relativePath)) {
                relativePath = relativePath.substring(0, relativePath.lastIndexOf("\\"));
            }
            List<FieldVo> fieldVos = entry.getValue();
            if (fieldVos.isEmpty()) {
                continue;
            }
            StringBuffer buffer = new StringBuffer();
            if (!"".equals(packageName) && packageName != null) {
                packageName += ".";
            }
            String relativePathPackage = relativePath.replaceAll("\\\\", ".");
            // if (relativePathPackage.contains(".")) {
            // relativePathPackage = relativePathPackage.substring(relativePathPackage.lastIndexOf("."));
            // }
            buffer.append("package " + packageName + packagePath + relativePathPackage + ";" + rn + rn);
            buffer.append(importBigDecimal);
            buffer.append(importDate);
            buffer.append(importList);
            buffer.append(importMap);
            buffer.append(author);
            buffer.append("public class " + fieldVos.get(0).getClassName() + " {" + rn);
            // 字段定义
            for (FieldVo vo : fieldVos) {
                buffer.append(desc1);
                buffer.append(vo.getFieldDesc());
                buffer.append(desc2);
                buffer.append("public " + vo.getType() + " " + vo.getFieldNameLower() + ";" + rn);
            }
            // 字段get、set
            for (FieldVo vo : fieldVos) {
                // get
                buffer.append(desc1);
                buffer.append("获取" + vo.getFieldDesc() + "." + rn + tab + " *" + rn);
                buffer.append("     * @return 返回" + vo.getFieldDesc());
                buffer.append(desc2);
                buffer.append("public " + vo.getType() + " get" + vo.getFieldNameLower() + "() {" + rn);
                buffer.append(tab + tab + "return " + vo.getFieldNameLower() + ";" + rn + tab + "}" + rn);
                // set
                buffer.append(desc1);
                buffer.append("设置" + vo.getFieldDesc() + "." + rn + tab + " *" + rn);
                buffer.append("     * @param " + vo.getFieldNameLower() + rn + tab);
                buffer.append(" *       要设置的" + vo.getFieldNameLower());
                buffer.append(desc2);
                buffer.append("public void set" + vo.getFieldNameUpper() + "(" + vo.getType() + " " + vo.getFieldNameLower() + ") {" + rn);
                buffer.append(tab + tab + "this." + vo.getFieldNameLower() + " = " + vo.getFieldNameLower() + ";" + rn + tab + "}" + rn);
            }
            buffer.append("}" + rn);
            relativePath = relativePath.replaceAll("\\.", "\\\\");
            String finalPath = outputPath + "\\" + relativePath; // + fieldVos.get(0).getClassName() + ".java";
            if (!"".equals(relativePath)) {
                createFile(finalPath);
            }
            finalPath = outputPath + "\\" + relativePath + "\\" + fieldVos.get(0).getClassName() + ".java";
            try {
                FileWriter fw = new FileWriter(finalPath);
                fw.write(buffer.toString());
                fw.close();
                // OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), encode);
                // out.write(buffer.toString());
                // out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
     * 构造Java model原始信息.
     * 
     * @param Paths
     *            所有待转换文件绝对路径.
     */
    private void buildOriginData(List<String> Paths) {
        String fileTxt = ""; // 文本内容
        String fileClassname = ""; // 类名
        String fieldDesc = ""; // 字段描述
        String fieldNameUpper = ""; // 字段名-大写开头
        String type = ""; // 类型
        String relativePath = ""; // 相对路径
        for (String absolutePath : Paths) {
            relativePath = absolutePath.replace(originPath, "");
            List<FieldVo> fieldVos = new ArrayList<>(); // 字段信息集合
            fileClassname = absolutePath.substring(absolutePath.lastIndexOf("\\") + 1, absolutePath.lastIndexOf("."));
            fileTxt = readTextFile(absolutePath, encode);
            if (!fileTxt.contains("namespace")) {
                continue;
            }
            fileTxt = fileTxt.substring(fileTxt.indexOf("namespace"));
            fileTxt = fileTxt.replaceAll("#region public members", "");
            // Start 多匹配模式
            StringBuffer pattern = new StringBuffer();
            pattern.append("<summary>.*?(?<desc>.*?)?</summary>");
            pattern.append(".*?public\\s(?<type>.*?)?\\s");
            pattern.append("(?<fieldname>.*?)?\\{");
            Matcher matcher = Pattern.compile(pattern.toString()).matcher(fileTxt);
            while (matcher.find()) {
                FieldVo vo = new FieldVo();
                fieldDesc = matcher.group("desc").replaceAll("/", "").trim();
                type = matcher.group("type").trim();
                fieldNameUpper = matcher.group("fieldname").trim();
                if (!"class".equals(type) && !fieldNameUpper.endsWith(")") && !"".equals(fieldNameUpper)) {
                    vo.setClassName(fileClassname);
                    vo.setFieldNameUpper(fieldNameUpper);
                    type = typeTrans(type);
                    vo.setType(type);
                    // vo.setRelativePath(relativePath);
                    vo.setFieldNameLower(upperConvertToLower(fieldNameUpper));
                    vo.setFieldDesc(fieldDesc);
                    fieldVos.add(vo);
                }
            }
            // End 多匹配模式
            mapData.put(relativePath, fieldVos);
        }
    }

    /**
     * C#的数据类型转为Java.
     * 
     * replace()方法防止List<string>形式
     * 
     * @param type
     *            字段类型
     * @return Java类型
     */
    private String typeTrans(String type) {
        if (type.contains("?")) {
            type = type.replaceAll("\\?", "");
        }
        if (type.contains("string")) {
            type = type.replaceAll("string", "String");
        } else if (type.contains("DateTime")) {
            type = type.replaceAll("DateTime", "Date");
        } else if (type.contains("bool")) {
            type = type.replaceAll("bool", "boolean");
        } else if (type.contains("decimal")) {
            type = type.replaceAll("decimal", "BigDecimal");
        } else if (type.contains("object")) {
            type = type.replaceAll("object", "Object");
        }
        if ("enum".equals(type)) {
            type = "int";
        }
        if (type.contains("<int>")) {
            type = type.replaceAll("<int>", "<Integer>");
        }
        return type;
    }

    /**
     * C#字段大写开头转为小写开头.
     * 
     * @param fieldNameUpper
     * @return
     */
    private String upperConvertToLower(String fieldNameUpper) {
        if ("".equals(fieldNameUpper)) {
            return "";
        }
        String result = fieldNameUpper.replaceFirst(fieldNameUpper.substring(0, 1), fieldNameUpper.substring(0, 1).toLowerCase());
        return result;
    }

    /**
     * 所有待转换文件绝对路径.
     * 
     * @param path
     * @return
     */
    private void getAllFilePath(String path) {
        File root = new File(path);
        File[] files = root.listFiles();
        if (files == null) {
            filePaths.add(path);
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归调用
                getAllFilePath(file.getAbsolutePath());
                // System.out.println(filePath + "目录下所有子目录及其文件" + file.getAbsolutePath());
            } else {
                // System.out.println(filePath + "目录下所有文件" + file.getAbsolutePath());
                if (file.getAbsolutePath().endsWith(".cs")) {
                    filePaths.add(file.getAbsolutePath());
                }
            }
        }

    }

    public static String readTextFile(String sFileName, String sEncode) {
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
     * 去空格去换行.
     *
     * @param str
     *            str
     * @return str
     */
    public String formatStr(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }
}
