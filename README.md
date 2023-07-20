

# 前言


# 一、项目概述
## 1.项目需求

> 实现材料仓库的管理：提供材料出入库管理等实用功能。 材料入库管理：材料检验入库、入库查询、入库类别按月统计；
> 材料出库管理：材料库存查询、材料出库、出库查询、出库类别按月统计； 辅助管理：仓库系统的人员管理、基础表的管理。

## 2.总述
此项目为Javaweb项目，前后端不分离，典型的单体架构，主要功能是对仓库转入转出等业务进行管理，并对数据进行可视化展示，同时有部分权限管理的功能

## 3.技术栈选择
前端：layui、jquery、echarts、thymeleaf模板引擎
后端：mysql、maven、tomcat、mybatis、springMVC、spring、SpringBoot、logback

## 4.环境介绍
数据库：mysql8.0
项目结构：maven
数据库连接池：Druid
前端框架：layui、jquery、echarts、thymeleaf模板引擎
后端框架：SpringBoot、SSM
语言：Java
jdk版本：8
编写的IDE：IDEA

## 5.效果图展示
为了更直观的展示项目，这里先放几张效果图


![在这里插入图片描述](https://img-blog.csdnimg.cn/202106172032283.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619111451760.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)



![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618154411464.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618154429912.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618154440361.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)


![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618154523115.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)




![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618154619372.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)



# 二、数据库设计
## 1.数据库模型设计概览
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618162351335.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

## 2.数据库表设计

### ①depository

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618163940592.png)




**描述：** 该表存储仓库的信息，比如仓库名称，仓库地址和仓库介绍



**表结构：**

| **序号** | **字段名** | **数据类型** | **主键** | **非空** | **默认值** | **描述** |
| -------- | ---------- | ------------ | -------- | -------- | ---------- | -------- |
| 1        | id         | INT(10)      | 是       | 是       |            |       仓库id   |
| 2        | dname      | VARCHAR(255) | 否       | 是       |            | 仓库名称 |
| 3        | address    | VARCHAR(255) | 否       | 是       |            | 仓库地址 |
| 4        | introduce  | VARCHAR(255) | 否       | 是       |            | 仓库介绍 |

 

### ②仓库调度记录（depository_record）

![](https://img-blog.csdnimg.cn/20210618165600496.png)




**描述：** 该表记录仓库调度的记录，同时该表也是数据也可以看做一条条申请信息。

 

**表结构：**

| **序号** | **字段名**     | **数据类型** | **主键** | **非空** | **默认值** | **描述**                                                     |
| -------- | -------------- | ------------ | -------- | -------- | ---------- | ------------------------------------------------------------ |
| 1        | id             | INT(10)      | 是       | 是       |            | 记录id                                                       |
| 2        | application_id | INT(10)      | 否       | 否       |            | 申请编号(暂时无用)                                           |
| 3        | mname          | VARCHAR(255) | 否       | 是       |            | 产品名称                                                     |
| 4        | depository_id  | INT(10)      | 否       | 是       |            | 调度的仓库id                                                 |
| 5        | type           | INT(10)      | 否       | 是       | 0          | 调度记录类型（0出库，1入库)                                  |
| 6        | quantity       | DOUBLE(22)   | 否       | 否       |            | 数量                                                         |
| 7        | price          | DOUBLE(22)   | 否       | 否       |            | 价格                                                         |
| 8        | state          | VARCHAR(255) | 否       | 否       |            | 状态（待审核/审核未通过，未入库/出库/检验不通过，待验收/已入库/已出库） |
| 9        | applicant_id   | INT(10)      | 否       | 否       |            | 申请人id                                                     |
| 10       | apply_remark   | VARCHAR(255) | 否       | 否       |            | 申请备注                                                     |
| 11       | apply_time     | DATETIME     | 否       | 否       |            | 申请时间                                                     |
| 12       | reviewer_id    | INT(10)      | 否       | 否       |            | 审核人id                                                     |
| 13       | review_remark  | VARCHAR(255) | 否       | 否       |            | 审核结果备注                                                 |
| 14       | review_time    | DATETIME     | 否       | 否       |            | 审核时间                                                     |
| 15       | review_pass    | INT(10)      | 否       | 否       |            | 审核是否通过，0表示未通过，1表示通过                         |
| 16       | checker_id     | INT(10)      | 否       | 否       |            | 验货人id                                                     |
| 17       | check_remark   | VARCHAR(255) | 否       | 否       |            | 验收备注                                                     |
| 18       | check_time     | DATETIME     | 否       | 否       |            | 出入库时间（验货时间）                                       |
| 19       | check_pass     | INT(10)      | 否       | 否       |            | 验收是否通过                                                 |

 

## 3、产品信息记录（库存）（material）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618165646888.png)

 

 

**描述：** 产品信息记录（库存信息）

 

**表结构：**

| **序号** | **字段名**    | **数据类型** | **主键** | **非空** | **默认值** | **描述**   |
| -------- | ------------- | ------------ | -------- | -------- | ---------- | ---------- |
| 1        | id            | INT(10)      | 是       | 是       |            | 存储id     |
| 2        | depository_id | INT(10)      | 否       | 否       |            | 仓库名称   |
| 3        | mname         | VARCHAR(255) | 否       | 否       |            | 材料名称   |
| 4        | quantity      | DOUBLE(22)   | 否       | 否       |            | 数量       |
| 5        | price         | DOUBLE(22)   | 否       | 否       |            | 总金额     |
| 6        | type_id       | INT(10)      | 否       | 否       |            | 材料种类id |

 

## 4、material_type

![](https://img-blog.csdnimg.cn/20210618165636448.png)

 

 

**描述：** 材料种类，我对材料进行了分类，这样统计起来也方便很多，另外建一个表是为了防止以后可能会对材料类型做的补充，同时节省存储空间。

 

**表结构：**

| **序号** | **字段名** | **数据类型** | **主键** | **非空** | **默认值** | **描述** |
| -------- | ---------- | ------------ | -------- | -------- | ---------- | -------- |
| 1        | id         | INT(10)      | 是       | 是       |            | 类型id   |
| 2        | tname      | VARCHAR(255) | 否       | 是       |            | 类型名称 |
| 3        | introduce  | VARCHAR(255) | 否       | 否       |            | 类型介绍 |

 

## 5、notice

![](https://img-blog.csdnimg.cn/20210618165627709.png)




**描述：** 公告表，用于存储公告信息

 

**表结构：**

| **序号** | **字段名** | **数据类型** | **主键** | **非空** | **默认值** | **描述** |
| -------- | ---------- | ------------ | -------- | -------- | ---------- | -------- |
| 1        | id         | INT(10)      | 是       | 是       |            | 公告主键 |
| 2        | title      | VARCHAR(255) | 否       | 是       |            | 公告标题 |
| 3        | content    | VARCHAR(255) | 否       | 否       |            | 公告内容 |
| 4        | time       | DATETIME     | 否       | 是       |            | 发布时间 |

 

## 6、standing_book
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618165701826.png)




**描述：** 台账表，作业要求里有要台账表，但我觉得这和仓库调度表没啥区别，所以这个表我虽然建了，但实际并未使用。

 

**表结构：**

| **序号** | **字段名**    | **数据类型** | **主键** | **非空** | **默认值** | **描述**                                                     |
| -------- | ------------- | ------------ | -------- | -------- | ---------- | ------------------------------------------------------------ |
| 1        | id            | INT(10)      | 是       | 是       |            | 台账记录id                                                   |
| 2        | type          | INT(10)      | 否       | 是       |            | 0表示调入，1表示调出（外部）;2表示调入（退料），3表示调出（领料）（内部调用） |
| 3        | quantity      | INT(10)      | 否       | 是       | 0          | 数量                                                         |
| 4        | price         | INT(10)      | 否       | 是       | 0          | 总价                                                         |
| 5        | material_name | VARCHAR(255) | 否       | 是       |            | 材料名称                                                     |

 

## 7、transfer_record

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618165709346.png)




**描述：** 转移表，这个是用来关联转移操作的。

 

**表结构：**

| **序号** | **字段名** | **数据类型** | **主键** | **非空** | **默认值** | **描述**   |
| -------- | ---------- | ------------ | -------- | -------- | ---------- | ---------- |
| 1        | id         | INT(10)      | 是       | 是       |            | 转移记录id |
| 2        | from_id    | INT(10)      | 否       | 是       |            | 转出仓库记录id |
| 3        | to_id      | INT(10)      | 否       | 是       |            | 转入仓库记录id |

 

## 8、 user

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210618165717935.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)




**描述：** 用户表，这里的密码是经过加密存储的，所以就算黑客破解数据库了，那损失也不会太大。

 

**表结构：**

| **序号** | **字段名**    | **数据类型** | **主键** | **非空** | **默认值** | **描述**                                           |
| -------- | ------------- | ------------ | -------- | -------- | ---------- | -------------------------------------------------- |
| 1        | id            | INT(10)      | 是       | 是       |            | 用户id                                             |
| 2        | uname         | VARCHAR(255) | 否       | 是       |            | 用户名称                                           |
| 3        | authority     | VARCHAR(255) | 否       | 否       |            | 表示权限等级（游客/员工/审核员/仓管员/系统管理员） |
| 4        | pwd           | VARCHAR(255) | 否       | 否       |            | 用户登录密码（数据库存储的是加密后的）             |
| 5        | sex           | VARCHAR(255) | 否       | 是       |            | 性别                                               |
| 6        | depository_id | INT(10)      | 否       | 否       |            | 负责仓库，序号表示仓库id，0表示全部仓库            |
| 8        | email         | VARCHAR(255) | 否       | 否       |            | 邮箱                                               |
| 7        | entry_date    | DATE         | 否       | 是       |            | 入职日期                                           |
| 9        | phone         | VARCHAR(255) | 否       | 否       |            | 手机号                                             |

 

# 四、功能设计与展示
### ①出入库申请流程
参与出入库申请的角色有三个——发起申请的普通用户，审核申请的审核人，仓库验收的仓管员。
普通用户发起申请（制单）->审核人审核申请->相应仓管员验收货物->入库或出库

用户能发起三种类型的申请——出库，入库，转移
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619143112227.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/2021061914312943.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)


### ②出入库管理
#### 1.出入库查询
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619141555407.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

用户可以查看出入库申请记录，同时也可以选择**开始日期，仓库，材料名称**等来进行自己期望的查询，并可以点击**详情**查看详细信息。

> 注：这里表格的数据并不是一次全部给前端，而是前端根据自己的需求分页获取


![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619141814247.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

也可以对记录进行 **（批量）删除**，
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619141901951.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

也可以进行**排序筛选**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619141952136.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619142025973.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

同时也支持对当前数据进行**导出打印**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619142048555.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619142127608.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)


![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619142104291.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

#### 2.可视化展示
出入库的信息将会以图表的信息展现出来，可以给管理者一个更直观的感受
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619142906853.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619143034187.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)



### ③库存管理
#### 1.库存查询
这个和上面类似，不过这里因为字段较少就没有设置详情，同时为了库存安全，这里并未提供增删改功能，只能供用户查询信息。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619143410935.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

不过也提供**筛选、导出和打印**的功能
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619143706890.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619143724101.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/2021061914373995.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

#### 2.可视化展示
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619143916276.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)


### ④其他管理
这里我只写了仓库增加和材料类型增加
#### 1.材料种类添加
![在这里插入图片描述](https://img-blog.csdnimg.cn/202106191441383.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

#### 2.仓库创建
![](https://img-blog.csdnimg.cn/20210619144213820.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

## 3.个人管理
### ①我的任务
此处会显示登录用户的**未完成任务**和**已完成任务**。

> 注：这里采取流式加载来懒加载数据

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619145349527.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

点击加载更多会向服务器请求另外所需数据，如果没有则会显示“没有更多了”

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619145513976.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

点击未完成任务，则会进入审核/验收页面
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619145716326.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619145739377.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

审核的话需要指定对应仓库的仓管员负责验收任务，同时写下备注，点击审核通过或者不通过。
验收只要写备注以及验收通过或者不通过。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619150212383.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)


### ②我的申请
在这里可以查看自己提交的申请

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619150320994.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

点击可以查看申请所处的流程阶段，实时查看自己的申请状况。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619150409937.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/2021061915042685.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

### ③个人信息管理
在这个页面，用户可以查看和修改自己的非敏感信息。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619150543720.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)


![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619150606999.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

如果你要对你的绑定邮箱和密码进行修改，则需要进行额外的流程
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619150711775.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619150715563.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

## 4.网站管理
### ①公告
在这里可以发布公告
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619150835312.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

同时在首页可以查看对应公告
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619150923743.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619150933817.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

### ②人员管理
系统管理员可以查看对应的人员信息
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619151104245.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

可以筛选查询需要的信息
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619151134964.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

可以（批量）删除用户信息，也可以添加用户信息
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619151232896.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

或者编辑修改用户信息
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619151248461.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)

还可以导出打印用户信息
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210619151348713.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ2MTAxODY5,size_16,color_FFFFFF,t_70)