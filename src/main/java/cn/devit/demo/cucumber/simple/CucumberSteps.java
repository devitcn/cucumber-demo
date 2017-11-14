package cn.devit.demo.cucumber.simple;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.devit.demo.cucumber.biz.Frequency;
import cucumber.api.DataTable;
import cucumber.api.java.zh_cn.假如;

public class CucumberSteps {

    List<Frequency> list = new ArrayList<Frequency>();

    @cucumber.api.java.Before
    public void setup() {
        System.out.println("before");
    }

    @cucumber.api.java.After
    public void clear() {
        System.out.println("after");
    }

    @假如("^输入 (\\w+)$")
    public void 入参(String input) throws Throwable {
        list.add(new Frequency(input));
    }

    @假如("^规范化结果是 (\\w+)$")
    public void 格式化结果是(String input) throws Throwable {
        if (!list.get(0).toString().equals(input)) {
            throw new RuntimeException(
                    "断言失败,应该是:" + input + "，结果却是" + list.get(0).toString());
        }
    }

    @假如("^给出班期列表$")
    public void 班期列表(DataTable table) throws Throwable {
        List<Map<String, String>> mapList = table.asMaps(String.class,
                String.class);
        for (Map<String, String> map : mapList) {
            //“班期”二字是从feature文件中表格的表头这一行取得
            list.add(new Frequency(map.get("班期")));
        }
    }

    @假如("^它们的交集是(.*?)$")
    public void 交集是(List<Integer> split) throws Throwable {

        Set<Integer> set = new HashSet<Integer>(list.get(0).set());
        for (Frequency item : list) {
            set.retainAll(item.set());
        }
        assertThat(set, containsInAnyOrder(
                (Integer[]) split.toArray(new Integer[split.size()])));
    }
}
