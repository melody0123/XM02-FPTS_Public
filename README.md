# XM02-FPTS

**02组项目：金融产品交易系统（Financial product optimization trading system， FPTS）**

开发成员：

|                            姓名                             |   角色   |
| :---------------------------------------------------------: | :------: |
|        [Guoxi Zhang](https://github.com/Guoxi-Zhang)        | 项目经理 |
|           [m31ody](https://github.com/melody0123)           | 项目成员 |
|             [laybxc](https://github.com/laybxc)             | 项目成员 |
|          [LiZiYang3](https://github.com/LiZiYang3)          | 项目成员 |
| [BackdreamPassenger](https://github.com/BackdreamPassenger) | 项目成员 |
|         [liyicheng1](https://github.com/liyicheng1)         | 项目成员 |

## 项目部署流程

1. 在idea中打开该项目，等待maven自动加载完成所有依赖。

2. 点击右上角绿色三角，运行该项目

	![image-20230112171513392](README.assets/image-20230112171513392.png)

3. 当控制台出现下述字样，项目运行成功

	![image-20230112171625780](README.assets/image-20230112171625780.png)

4. 在浏览器输入：localhost:80 访问项目

	![image-20230112171703440](README.assets/image-20230112171703440.png)

	

	

## 合作开发流程（面向开发者）

使用github 的Collaborators 模式共同在一个仓库中开发。

建议下载[github desktop](https://desktop.github.com/)。

### 流程简述

1. 克隆项目到自己本机

  ![image-20221104184253349](README.assets/image-20221104184253349.png)

  选择合适的路径克隆。

  

2. 在本机编写代码，所有改动均会被记录到github desktop中，如下图：

  ![image-20221104184718351](README.assets/image-20221104184718351.png)

3. **在本机测试无误后，**添加描述信息（summary,Description）后，点击Commit to master进行提交

  ![image-20221104185058195](README.assets/image-20221104185058195.png)

4. 点击Push origin，提交到github远程仓库

  ![image-20221104185327087](README.assets/image-20221104185327087.png)

5. 完成



### 注意事项

1. ~~目前数据库已经采用辛卫老师的远程数据库，需要对数据库版本进行一定的修改，修改方法如下：~~

   ~~将mysql数据库版本改为5.0.4~~

1. 采用远程数据库，需要将mysql版本改回8.0.31

	![](README.assets/20221123213658.png)

	![image-20221130094918819](README.assets/image-20221130094918819.png)

	

### 参考资料

1. [[Git & GitHub] 怎么团队合作多人开发项目](https://blog.csdn.net/dietime1943/article/details/81391835)

2. [github Desktop桌面版使用基本教程](https://www.jianshu.com/p/1e45b93bd593)
3. [github中fork,clone,push,pull request的简单理解](https://blog.csdn.net/cvper/article/details/79035664)
