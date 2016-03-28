package other.ModelCSharpToJava;

import java.util.Scanner;

import org.junit.Test;

/*
 * 文件名：StartTransform.java
 * 版权：Copyright 2007-2016 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： StartTransform.java
 * 修改人：yunhai
 * 修改时间：2016年3月28日
 * 修改内容：新增
 */

/**
 * 本工具源于最近项目需要将C#的model转为Java，且公司有CheckStyle，
 * 
 * 考虑到copy、paste效率太低，且对技术没有任何提升；
 * 
 * 以后接手的C#项目会越来越多，写个工具实现一键转换的必要性也就相当高了。
 * 
 * 开始转换
 * 
 * @author yunhai
 */
public class StartTransform {
    /**
     * 转换实现类.
     */
    TransformBusiness trans = new TransformBusiness();

    @Test
    public void start() {
        System.out.println("请输入待修改的文件（夹）的绝对路径：");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        System.out.println("请输入包名：");
        String packageName = scanner.nextLine();
        trans.transform(path, packageName);
    }

}
