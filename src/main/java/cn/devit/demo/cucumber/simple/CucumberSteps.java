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

    private Frequency f;

    @cucumber.api.java.Before
    public void setup() {
        System.out.println("before");
    }

    @cucumber.api.java.After
    public void clear() {
        System.out.println("after");
    }

    @假如("你好：(.*)")
    public void hello(String name) {
        System.out.println("你好：" + name + "，欢迎光临。");
    }

    @假如("读文件(.*?)")
    public void read(String name) {
        System.out.println("read file" + name);
    }

    @假如("^入参 (\\w+)$")
    public void 入参(String input) throws Throwable {
        f = new Frequency(input);
    }

    @假如("^格式化结果是 (\\w+)$")
    public void 格式化结果是(String input) throws Throwable {
        if (!f.toString().equals(input)) {
            throw new RuntimeException(
                    "断言失败,应该是:" + input + "，结果却是" + f.toString());
        }
    }

    @假如("^班期(.*?)和班期(.*?)的交集是(.*?)$")
    public void 格式化结果是(String left, String right, List<Integer> split)
            throws Throwable {
        list.add(new Frequency(left));
        list.add(new Frequency(right));
        System.out.println(split);
        this.交集是(split);
    }

    List<Frequency> list = new ArrayList<Frequency>();

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
