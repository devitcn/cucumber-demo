#language: zh-CN
功能: 与spring整合来编写步骤

  背景: 
    * 清理数据库

  场景: 场景1
    * 查询id=1
    * 查询结果是：1X

  场景大纲: 场景2
    * 查询id=<input>
    * 查询结果是：<expect>

    例子: 
      | input | expect |
      |    71 |    71X |
      |   172 |   172X |
