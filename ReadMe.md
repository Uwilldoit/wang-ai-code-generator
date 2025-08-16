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





## 应用模块

### 1、需求分析

之前实现的是单机版本，用户只能在本地生成代码文件，现在我们要将其升级为平台化系统，这意味需要支持多用户、应用管理、在线部署等功能。

需要的具体功能包括：

- 用户基础功能
- 创建应用
- 编辑应用信息
- 删除自己的应用
- 查看应用详细
- 分页查询自己的应用列表
- 分页查看精选应用列表
- 用户高级功能
- 实时查看应用效果（⭐）
- 应用部署（⭐）
- 管理功能
- 管理所有应用（删改查）
- 设置精选应用



### 2、方案设计

平台化改造的核心在于**建立完整的应用生命周期管理体系**。

#### 工作流程

用户在主页输入提示词后，系统会创建一个应用记录，然后跳转到对话页面与AI交互生成网站。生成完成后，用户可以预览效果，满意后进行部署，让网站真正对外提供服务。

这个流程看似简单，但涉及到数据存储、权限控制、文件管理、网站部署等多个技术环节。



#### 库表设计

应用表是整个项目的核心，需要记录应用的基本信息、生成配置、部署信息等。其中最关键的是deployKey字段，由于每个网站应用文件的部署都是隔离的（想象成沙箱），需要用唯一字段来区分，可以作为应用的存储和访问路径，而且为了便于访问，每个应用的访问路径不能太长。

这里我们参考美团NoCode 等平台的设计，将deployKey设置为6位英文数字组成的唯一标识符。



```sql
-- 应用表
create table app
(
    id           bigint auto_increment comment 'id' primary key,
    appName      varchar(256)                       null comment '应用名称',
    cover        varchar(512)                       null comment '应用封面',
    initPrompt   text                               null comment '应用初始化的 prompt',
    codeGenType  varchar(64)                        null comment '代码生成类型（枚举）',
    deployKey    varchar(64)                        null comment '部署标识',
    deployedTime datetime                           null comment '部署时间',
    priority     int      default 0                 not null comment '优先级',
    userId       bigint                             not null comment '创建用户id',
    editTime     datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    UNIQUE KEY uk_deployKey (deployKey), -- 确保部署标识唯一
    INDEX idx_appName (appName),         -- 提升基于应用名称的查询性能
    INDEX idx_userId (userId)            -- 提升基于用户 ID 的查询性能
) comment '应用' collate = utf8mb4_unicode_ci;

```

说明：

1. `priority` 优先级字段：我们约定99表示精选应用，这样可以在主页展示高质量的应用，避免用户看到大量测试内容。为什么用数字而不是用枚举呢？因为这样更利于扩展，比如约定999表示置顶，还可以根据数字灵活调整各个应用的具体展示顺序。
2. 添加索引：给deployKey、appName、userId三个经常用于作为查询条件的字段增加索引，提高查询性能。

注意，暂时不考虑将应用代码直接保存到数据库字段中，而是保存在文件系统里，这样可以**避免数据库和文件存储不一致**的问题，也便于后续扩展到对象存储等方案。



### 应用生成

#### 业务流程：

1. 用户在主页输入提示词创建应用（入库）
2. 获得应用id后跳转到对话页面
3. 系统自动使用初始提示词与AI对话生成网站代码

由于应用的生成过程和AI对话是绑定的，我们可以提供一个名为`chatToGenCode`的应用生成接口，调用之前开发的AI代码生成门面完成任务，并且流式返回给前端。

一定要确保生成的文件能够与应用正确关联，因此这次生成的网站目录名称不再是之前的codeType_雪花算法，而是codeGenType_appId，这样就能通过appId查数据库获取应用信息，再根据应用信息找到对应的网站目录了。

为什﻿么这里不用 dep﻿loyKey 作为‎网站目录名称呢？是希望能够区分部署环境和过程。



#### SSE流式接口优化

##### 解决空格丢失问题

前端使用EventSource对接目前的接口时，会出现空格丢生问题，解决方法是在后端封装数据，参考DeepSeek的做法，将原本的返回值封装到JSON中。甚至美团NoCode对内容进行了加密。

按照封装的思路，我们可以编写下列代码，将 Flux 额外封装成 ServerSentEvent，把原始数据放到 JSON 的 `d` 字段内：



##### 主动告诉前端生成完毕

在 SSE 中，当服务器关闭连接时，会触发客户端的 `onclose` 事件，这是前端判断流结束的标准方式。但是，`onclose`事件会在连接正常结束（服务器主动关闭）和异常中断（如网络问题）时都触发，前端就很难区分到底后端是正常响应了所有数据、还是异常中断了。

因此，我们最好在后端添加一个明确的 `done` 事件，这样可以更清晰地区分流的正常结束和异常中断。