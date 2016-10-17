package java1.lang.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;

import java1.lang.annotation.validation.ValidationUtils;

/**
 * 自定义注解，参数验证
 * 
 * @author zxiaofan
 */
@SuppressWarnings("unchecked")
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

    @Test
    public void testStringCutBasic() throws Exception {
        AnnoVo vo = new AnnoVo();
        // 单一实体转换
        String param = "123456";
        vo.setCutName(param);
        vo.setGuestEmail("email");
        ValidationUtils.stringCut(vo);
        assertEquals(vo.getCutName().length(), 5);
    }

    @Test
    public void testStringCutListSet() throws Exception {
        AnnoVo vo = new AnnoVo();
        // 单一实体转换
        String param = "123456";
        // List转换
        List<AnnoVo> list = new ArrayList<>();
        vo.setCutName(param);
        list.add(vo);
        AnnoVo vo2 = new AnnoVo();
        vo2.setCutName("234567");
        list.add(vo2);
        list = (List<AnnoVo>) ValidationUtils.stringCut(list);
        String listvo = gson.toJson(list);
        System.out.println(listvo);
        // Set
        Set<AnnoVo> set = new HashSet<>();
        set.add(vo);
        set.add(vo2);
        set = (Set<AnnoVo>) ValidationUtils.stringCut(set);
        System.out.println(gson.toJson(set));
        Set<String> set2 = new HashSet<>();
        set2.add("set1");
        set2.add("set2");
        set2 = (Set<String>) ValidationUtils.stringCut(set2);
        System.out.println(gson.toJson(set2));

        // Map
        Map<String, AnnoVo> map = new HashMap<>();
        map.put("k1", vo);
        map.put("k2", vo2);
        map = (Map<String, AnnoVo>) ValidationUtils.stringCut(map);
        System.out.println(gson.toJson(map));
        // 复杂map
        Map<List<String>, List<AnnoVo>> map2 = new HashMap<>();
        List<String> list1 = new ArrayList<>();
        list1.add("111");
        List<AnnoVo> list2 = new ArrayList<>();
        vo.setCutName("123456");
        list2.add(vo);
        map2.put(list1, list2);
        map2 = (Map<List<String>, List<AnnoVo>>) ValidationUtils.stringCut(map2);
        System.out.println(gson.toJson(map2));
    }

    @Test
    public void testStringCutGeneric() throws Exception {
        AnnoVo vo = new AnnoVo();
        // 单一实体转换
        String param = "123456";
        // 泛型转换
        Request<AnnoVo> request = new Request<>();
        vo.setCutName(param);
        request.setObj(vo);
        request = (Request<AnnoVo>) ValidationUtils.stringCut(request);
        System.out.println(gson.toJson(request));

        // 复杂泛型
        Request<List<AnnoVo>> request2 = new Request<>();
        List<AnnoVo> list = new ArrayList<>();
        vo.setCutName("123456");
        vo.setGuestEmail("email");
        list.add(vo);
        request2.setObj(list);
        list = (List<AnnoVo>) ValidationUtils.stringCut(list);
        System.out.println(gson.toJson(list));
    }

    @Test
    public void testFuzaList() throws Exception {
        Request<AnnoListVo> request = new Request<>();
        AnnoListVo listAnno = new AnnoListVo();
        List<AnnoVo> listAnnovos = new ArrayList<>();
        AnnoVo vo = new AnnoVo();
        String param = "123456";
        vo.setCutName(param);
        listAnnovos.add(vo);
        listAnno.setListAnnovos(listAnnovos);
        request.setObj(listAnno);
        request = (Request<AnnoListVo>) ValidationUtils.stringCut(request);
        System.out.println(gson.toJson(request));
        System.out.println(gson.toJson(listAnno) + ">>>");
        listAnno = (AnnoListVo) ValidationUtils.stringCut(listAnno);
        System.out.println(gson.toJson(listAnno));
    }
}
