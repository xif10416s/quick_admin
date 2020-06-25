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
#####  流程图 定义
* 负责人指定相关
  * <startEvent activiti:initiator="applyUserId">
    * 配合identityservice.setAuthenticatedUserId绑定，不用${}指定
  * <userTask activiti:candidateGroups="人事">
  * <userTask activiti:assignee="${applyuserid}">
    * 默认会使用initiator的参数
* 参考文章
  * https://blog.csdn.net/ryuenkyo/article/details/83008641
  * https://segmentfault.com/a/1190000005924648
  * https://www.geek-share.com/detail/2717346909.html


###### bean分层
* entity
* model

###### redis -- 暂时没用
* 短信验证码保存--失效时间
* 图片验证码

###### activiti
* process目录初始化数据：
  * act_re_deployment（部署信息） ， 一次部署一条记录
  * act_re_procdef（工作流定义信息）， 一个bpmn文件一条记录
  * act_ge_bytearray（上传资源文件信息）

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
* 系统管理  -- 清理无用代码
  * 用户管理
  * 角色管理
  * 菜单管理
  * 部门管理（部门角色没有）
  * 我的部门
  * 职务管理
  * 数据字典
  * 分类字典
  * 系统通告
  * 通讯录
  * 定时任务
* 流程管理  -- 待完善
  * 流程部署
  * 流程实例
  * 历史实例
* OA办公
  * 工单申请
    * 请假申请 -- DEMO
  * 代办任务
