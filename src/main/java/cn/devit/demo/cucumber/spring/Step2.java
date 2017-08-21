package cn.devit.demo.cucumber.spring;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import cn.devit.demo.cucumber.spring.service.Dao;
import cn.devit.demo.cucumber.spring.service.SpringConfig1;
import cucumber.api.java.en.Given;

@ContextConfiguration(classes = SpringConfig1.class)
public class Step2 {

    @Autowired
    BaseStep baseStep;

    @Autowired
    Dao dao;

    @Given("^查询结果是：(\\w+)$")
    public void cleanDb(String value) {
        
        String result = baseStep.getCardById();
        assertThat(result, is(value));
    }

}
