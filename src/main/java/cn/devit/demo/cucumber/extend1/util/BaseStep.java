package cn.devit.demo.cucumber.extend1.util;

import static org.hamcrest.Matchers.notNullValue;


import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import cn.devit.demo.cucumber.spring.service.Dao;
import cn.devit.demo.cucumber.spring.service.SpringConfig1;
import cucumber.api.java.en.Given;

@ContextConfiguration(classes = SpringConfig1.class)
public class BaseStep {

    @Autowired
    Dao dao;

    private String cardById;

    @Given("^清理数据库$")
    public void cleanDb() {
        dao.deleteAll();
    }

    @Given("^查询id=(\\w+)$")
    public void cleanDb(String id) {
        cardById = dao.getCardById(id);
        assertThat(cardById, notNullValue());
    }

    public String getCardById() {
        return cardById;
    }

}
