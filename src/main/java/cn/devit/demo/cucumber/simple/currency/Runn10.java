package cn.devit.demo.cucumber.simple.currency;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;

@RunWith(cucumber.api.junit.Cucumber.class)
@CucumberOptions(
        //可以是feature文件、包含feature的文件夹
        features = "src/main/resources/features/currency.feature", 
        //严格模式，ignore会变成failure
        strict=true,
        //step代码的包
        glue = {
        "cn.devit.demo.cucumber.simple.currency" })
public class Runn10 {

}
