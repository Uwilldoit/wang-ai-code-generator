# AI零代码应用生成平台

## 用户模块

### 1、需求分析

对于用户模块，通常要具有下列功能：

- 用户注册
- 用户登录
- 用户注销
- 获取当前登录用户
- 用户权限控制
- 【管理员】管理用户

具体分析每个需求：

1. 用户注册：用户可以通过输入账号、密码、确认密码进行注册
2. 用户登录：用户可以通过输入账号和密码登录
3. 用户注销：用户可以退出登录
4. 获取当前登录用户：得到当前已经登录的用户信息（不用重复登录）
5. 用户权限控制：用户又分为普通用户和管理员，管理员拥有整个系统的最高权限，比如管理其他用户。后续可用考虑加入vip用户等。
6. 用户管理：仅管理员可用，可用对整个系统中的用户进行管理，比如搜索用户、删除用户



### 2、方案设计

- 库表设计
- 用户登录流程
- 如何对用户权限进行控制



#### 库表设计

库名：wang_ai_code_generator

表名：user（用户表）

##### 1、核心设计

用户表的核心是用户登录凭证（账号密码）和个人信息，SQL如下：

```sql
-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    editTime     datetime     default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    UNIQUE KEY uk_userAccount (userAccount),
    INDEX idx_userName (userName)
) comment '用户' collate = utf8mb4_unicode_ci;

```

说明：

1、editTime和updateTime的区别：editTime表示用户编辑个人信息的时间（需要业务代码来更新），而updateTime表示这条用户记录任何字段发生修改的时间（由数据库自动更新）。

2、给唯一值添加唯一键(唯一索引)，比如账号 userAccount,利用数据库天然防重复，同时可以增加查询效率。

3、给经常用于查询的字段添加索引，比如用户昵称 userName,可以增加查询效率。

##### 2、扩展设计

1)如果要实现会员功能，可以对表进行如下扩展:

1.给 userRole 字段新增枚举值 vip ，表示会员用户，可根据该值判断用户权限

2.新增会员过期时间字段，可用于记录会员有效期

3.新增会员兑换码字段，可用于记录会员的开通方式

4.新增会员编号字段，可便于定位用户并提供额外服务，并增加会员归属感

增加的SQL

```sql
vipExpireTime datetime     null comment '会员过期时间',
vipCode       varchar(128) null comment '会员兑换码',
vipNumber     bigint       null comment '会员编号'
```

2)如果要实现用户邀请功能，可以对表进行如下扩展:

1.新增 shareCode 分享码字段，用于记录每个用户的唯一邀请标识，可拼接到邀请网址后面，比如 https://mianshiya.com/?shareCode=xxx

2.新增 inviteUser 字段，用于记录该用户被哪个用户邀请了，可通过这个字段查询某用户邀请的用户列表。

对应的SQL如下

```sql
shareCode     varchar(20)  DEFAULT NULL COMMENT '分享码',
inviteUser    bigint       DEFAULT NULL COMMENT '邀请用户 id'
```



#### 用户登录流程

1. 建立初始会话
2. 登录成功，更新会话信息
3. 前端保存Cookie
4. 带Cookie的后续请求
5. 后端验证会话
6. 获取会话中存储的信息





## AI生成应用

### 实现AI应用生成（原生模式）

### SSE流式输出

LangChain4j + Reactor



#### 门面模式

为了统一管理生成和保存的逻辑，我决定使用门面模式这一设计模式。门面模式通过提供一个统一的高层接口来隐藏子系统的复杂性，让客户端只需要与这个简化的接口交互，而不需要了解内部的复杂实现细节。



### 代码优化：

在开发中，发现有大量重复代码，仔细分析后，决定使用以下优化策略：

- 解析器部分： 使用**策略模式**，不同类型的解析策略独立维护（**难点是不同解析策略的返回值不同**）
- 文件保存部分：使用**模板方法模式**，统一保存流程（**难点是不同保存方式的方法参数不同**）
- SSE流式处理部分：抽象出通用的流式处理逻辑（目前每种生成模式都写了 一套处理代码）

#### 策略模式（待补图）

策略模式定义了一系列算法，将每个算法封装起来，并让它们可以相互替换，使得算法的变化不会影响使用算法的代码，让项目更好维护和拓展。

#### 模板方法模式（待补图）

模板方法模式在抽象父类中定义了操作的标准流程，将一些具体实现步骤交给子类，使得子类可以在不改变流程的情况下对某个步骤进行定制。

#### 执行器模式（待补图）

正常情况下，可以通过工厂模式来创建不同的策略或模板方法，但由于每种生成模式的参数和返回值不同（HtmlCodeResult和MutilFileCodeResult），很难对通过工厂模式创建出来的对象进行统一的调用。

```java
public HtmlCodeResult parseCode(String codeContent) {}

public MultiFileCodeResult parseCode(String codeContent) {}

void saveFiles(HtmlCodeResult result, String baseDirPath) {}

void saveFiles(MultiFileCodeResult result, String baseDirPath) {}

```

对于方法参数不同的策略模式和模板方法模式，建议使用执行器模式（Executor）

执行器模式提供统一的执行入口来协调不同策略和模板的调用，特别适合处理参数类型不同但业务逻辑相似的场景，避免了工厂模式在处理不同参数类型时的局限性。



#### 混合模式（待补图）

最终，预期的代码架构是一种混合模式：

- 执行器模式： 提供统一的执行入口，根据生成类型执行不同的操作
- 策略模式：每种模式对应的解析方法单独作为一个类来维护
- 模板方法模式：抽象模板类定义了通用的文件保存流程，子类可以有自己的实现（比如多文件生成模式需要保存3个文件，而原生HTML模式只要保存1个文件）