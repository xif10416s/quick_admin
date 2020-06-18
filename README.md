####  快速后台系统开发框架
*  参考 https://github.com/zhangdaiscott/jeecg-boot 项目
*  简单重构一下

#####  已完成功能清单
*   


#####  框架选型
*   spring boot 2.3.0.RELEASE
*   swagger2  2.9.2 
*   MyBatis Plus 3.3.1 
*   Apache Shiro 1.5.2  + jjwt 3.10.2
*   


#### 开发环境
#####  后端开发环境
* idea插件
  * alibaba  java coding guidelines
  * lombok
  * maven helper
  * mybaitsx ， MyBatis Log Plugin 
* jdk 1.8 

#####  前端开发环境

##### 说明
######  权限配置
*  权限配置有url的用url , 没有的在perms中指定

####  swagger地址
* http://localhost:31100/admin/swagger-ui.html

#### 工作流引擎activiti 接入
* application.yml 
  * spring.autoconfigure.exclude: org.activiti.spring.boot.SecurityAutoConfiguration
* ActivityConfig

###### bean分层
* entity
* model

##### TODO
###### 系统结构
* 代码重构
* 日志
* 异常
* 错误码
* TOKEN 刷新
* DIct自动添加 自定匹配值 -- 已完成
* ISysUserService * SysUserServiceImpl @Transaction不生效
。。。

###### 功能模块

