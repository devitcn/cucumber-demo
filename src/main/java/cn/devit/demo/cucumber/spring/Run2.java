package cn.devit.demo.cucumber.spring;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;

@RunWith(cucumber.api.junit.Cucumber.class)
@CucumberOptions(
        features = "src/main/resources/features/with-spring.feature", 
        strict=true, glue = {
        "cn.devit.demo.cucumber.spring" })
public class Run2 {

}
