package cn.devit.demo.cucumber.extend1.publ;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import cn.devit.demo.cucumber.extend1.util.BaseStep;
import cucumber.api.java.en.Given;

public class Step2 extends BaseStep {


    @Given("^查询结果是：(\\w+)$")
    public void cleanDb(String value) {
        String result = getCardById();
        assertThat(result, is(value));
    }

}
