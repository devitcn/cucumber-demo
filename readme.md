# Cucumber演示项目

Cucumber是一个简单的提升自动化测试代码质量的方法。用cucumber实现的测试用例既能够自动化的运行，也能够方便修改数据。

Cucumber的长处：

与RF、Gauge之类的BDD测试工具相比更轻量，可以嵌入到主编程语言中实现步骤代码。

与JUNIT相比，测试数据直观，借用了BDD的优势，这样可以让自动化测试活起来，让测试专家修改调整用例数据。

文本化的用例描述，辅助实例化需求讨论。

## 目录说明

项目使用maven做build，采用了默认的目录规范。

包功能介绍（cn.devit.demo.cucumber）：

- cn.devit.demo.cucumber
    - biz 存放业务代码，属于待测试的业务代码
    - simple 演示cucumber的简单用法
    - spring 演示cucumber 中如何依赖spring容器
        - service 模拟业务服务类和spring配置
    - extend1 使用spring拆分step类。

- src/main/resources
    - features cucumber feature 文件
    - application.xml 模拟主业务的spring配置文件

## 支持工具

###Eclipse

https://github.com/cucumber/cucumber-eclipse

### IDEA

idea社区版

## 设计实现cucumber测试

### 设计原则

实现cucumber测试和所有的自动化测试一样，有一个固定的套路。就是3A原则，每个测试都要分成三个阶段。

#### Given

在given阶段，又称为setup阶段，或者arrange，需要完成：

环境清理，测试数据准，资源连接等等

一致性断言，提前发现出现测试数据错误



#### When

When阶段，又称为call，assign，实际调用被测系统或者接口，需要完成：

背景信息检查，given中常常会省略测试无关的数据，这些数据在调用之前需要准备好

构造测试数据，根据前文参数和背景信息组装测试对象、报文

调用被测系统，获得返回结果

断言调用成功

将测试数据存档，供后面引用

查询其他数据，如更新后的db数据，日志，其他异步结果（等待）

#### Then

Then阶段又称为assert，这个阶段主要做结果的验证。需要完成：

断言接口的返回值

- 必须包含某个字符串
- 语法格式

    提示：通常不宜做全文精确匹配（因为接口的返回值往往含有时间戳等随机变化的成分），或者将变化的部分去除以后来匹配

断言后端数据的变化符合预期

- 简单的数量变化，时间变化
- 严格的字段变化

    提示：在编写步骤的时候避免体现技术实现。严格的字段变化应当封装成为业务指标，避免一个字段一个字段比较（严重的业务耦合），

### 举例

#### 浏览器

在一切顺利的情况下

    场景：打开百度首页
    * 启动搜狗浏览器  
    * 访问https://www.baidu.com
    * 返回HTTP200
    * 包含<title>百度一下，你就知道 </title>

这个测试符合3A原则，可以分成三个步骤

- 启动……属于given
- 访问……属于when
- 返回……，BODY……属于then

但是要注意一点，断言的两步偏重技术性验证。如果我们是专门用来做HTTP接口的协议验证。这样是没有问题的。如果我们的验证主体是浏览器的行为，那么这两句话的可读性就要打折扣了。测试用例要体现的是业务用语。同样的结果，我们可以稍作修改使之更容易理解。

    * 返回HTTP200
    * 包含<title>百度一下，你就知道 </title>
    #改成
    * 网页正常打开
    * 标题是：百度一下，你就知道

也有人会觉得“网页正常打开”这句话的行为不明确，这个就需要在实例化需求讨论的过程中明确定义“网页正常打开”的含义，使之成为需求的一部分，或者大家均认可“HTTP200”就是需求的用词也可以

    * HTTP200
    * 标题是：百度一下，你就知道

实现步骤的伪代码

    class Step {
    
        Handler handler;
        
        @Given("^启动(搜狗|谷歌)?浏览器$")
        public void openBorswer(String type) {
            if (handler != null) {
                handler.close();
            }
            switch (type) {
            case "搜狗":
                handler = exec("sogou");
                break;
            case "谷歌":
            default:
                handler = exec("chrome");
                break;
            }
            wait(3s);
            assert handler !=null;
        }
        
        Page page;
        
        @Given("^访问(.*?)$")
        public void openUrl(String url) {
            page =handler.open(url);
            wait(2s)
            assert page !=null;
        }
        
        @Given("^网页正常打开$")
        public void assertStatusCode() {
            assert page.header.statusCode == 200;
        }
        
        @Given("^标题是：(.*?)$")
        public void assertBodyContains(String text) {
            assert page.document.title == text;
        }
        
    }   


从实现代码中可以看出：

- feature步骤和实现代码是通过注解中的正则表达式关联起来的，正则匹配则步骤确定
- cucumber负责将正则表达式中的group和方法的参数对应起来，在调用方法式做好类型转换
- 步骤不是独立存在的，前后之间存在输入输出依赖，需要保存中间结果
- 每一步会做些必要条件验证，前一步的成功时后面执行的基础
- 通过优化正则表达式，可以实现步骤参数化（打开不同的浏览器）

## 重用代码

不能直接扩展step类。`cucumber.runtime.CucumberException: You're not allowed to extend classes that define Step Definitions or hooks. `

如果想将step分割成多个文件，可以使用spring容器来管理step类

或者使用ThreadLocal变量来存储中间状态。

  
  
## Support

###网址

GITHUB： https://github.com/cucumber

官网：https://cucumber.io/

###和Spring集成


在POM中添加依赖

    <dependency>
      <groupId>info.cukes</groupId>
      <artifactId>cucumber-spring</artifactId>
      <version>1.2.5</version>
    </dependency>

就能够在step类上使用`@ContextConfiguration`来引入spring配置，实现bean注入。参见cn.devit.demo.cucumber.spring里面的例子。

需要注意一点：这个模块依赖的是spring4，如果你用pring4，不会有问题。如果在用的是spring3，那么他会报一个错误：

    java.lang.IncompatibleClassChangeError: Found class org.springframework.test.context.TestContext, but interface was expected
      at cucumber.runtime.java.spring.CucumberTestContextManager.getContext(SpringFactory.java:215)
      at cucumber.runtime.java.spring.CucumberTestContextManager.<init>(SpringFactory.java:207)
      at cucumber.runtime.java.spring.SpringFactory.start(SpringFactory.java:102)


这时有两种选择：

1. 不用`@ContextConfiguration`，改用cucumber.xml：

cucumber-spring就是默认他会在classpath中找一个cucumber.xml的spring配置文件，如果有就会加载，这种情况下是不会报错误的。

    cat src/main/resources/cucumber.xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
      xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.1.xsd">
      <context:annotation-config></context:annotation-config>
      <!-你的其他配置-->
    </beans>

有个缺点，这个配置只能有一份。

2. 下载源码，修改依赖，重新编译一份cucumber-spring

从github下载cucumber-jvm#1.2.5的源码，里面有一个文件夹spring，是个pom项目。

打开`spring/pom.xml`编辑，将原来1.2.5，换一个名字比如1.2.5.SPR3，然后添加一个properties

添加属性：

    <properties>
      <spring.version>3.2.5.RELEASE</spring.version>
    </properties>
对应的修改版本号，和依赖的版本号
    
    <version>1.2.5.SPR3</version>
    <dependency>
        <groupId>info.cukes</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>1.2.5</version>
    </dependency>
    <dependency>
        <groupId>info.cukes</groupId>
        <artifactId>cucumber-core</artifactId>
        <version>1.2.5</version>
    </dependency>
    <dependency>
        <groupId>info.cukes</groupId>
        <artifactId>cucumber-junit</artifactId>
        <version>1.2.5</version>
        <scope>test</scope>
    </dependency>    

编译，打包（有部分测试一定不过）

    mvn install -DskipTests=true
  
也可以将修改过的包上传的内部的nexus里面共享给他人。


