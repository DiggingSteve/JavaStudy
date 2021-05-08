#以下安装都将在docker 进行
# mysql 安装
~ docker pull mysql:5.7 \n
sudo docker run -d -p 3306:3306 --privileged=true -v /docker/mysql/conf/my.cnf:/etc/my.cnf -v /docker/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 --name mysql mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_general_ci
参数说明:

run　run 是运行一个容器
-d　 表示后台运行
-p　　表示容器内部端口和服务器端口映射关联
--privileged=true　设值MySQL 的root用户权限, 否则外部不能使用root用户登陆
-v /docker/mysql/conf/my.cnf:/etc/my.cnf 将服务器中的my.cnf配置映射到docker中的/docker/mysql/conf/my.cnf配置
-v /docker/mysql/data:/var/lib/mysql　　同上,映射数据库的数据目录, 避免以后docker删除重新运行MySQL容器时数据丢失
-e MYSQL_ROOT_PASSWORD=123456　　　设置MySQL数据库root用户的密码
--name mysql　　　　 设值容器名称为mysql
mysql:5.7　　表示从docker镜像mysql:5.7中启动一个容器
--character-set-server=utf8mb4 --collation-server=utf8mb4_general_ci 设值数据库默认编码

# redis 安装
mkdir /usr/local/redis
下载配置文件到指定目录

wget -P  /usr/local/redis http://download.redis.io/redis-stable/redis.conf
修改配置文件

vi /usr/local/redis/redis.conf
进入文件命令模式后，输入‘/bind 127.0.0.1’查找关键字找到对应的行，输入‘N’查找下一个
找到所在行后输入字母‘i’ 进入编辑模式，在第一位字母前面加上‘#’注释行。
按‘ESC’进入命令行模式输入‘:wq’ 保存文件并退出，完成配置文件的编辑
配置	说明
bind 127.0.0.1	限制redis只能本地访问，#注释后所有Ip都可以访问

docker run -d --name redis -p 6379:6379 -v /usr/local/redis/redis.conf:/etc/redis/redis.conf redis redis-server /etc/redis/redis.conf --appendonly yes --requirepass 'test'

# rabbitmq
RabbitMQ安装
下载rabbitmq3.7.15的docker镜像：
docker pull rabbitmq:3.7.15
Copy to clipboardErrorCopied
使用如下命令启动RabbitMQ服务：
docker run -p 5672:5672 -p 15672:15672 --name rabbitmq \
-d rabbitmq:3.7.15
Copy to clipboardErrorCopied
进入容器并开启管理功能：
docker exec -it rabbitmq /bin/bash
rabbitmq-plugins enable rabbitmq_management
Copy to clipboardErrorCopied


开启防火墙：
firewall-cmd --zone=public --add-port=15672/tcp --permanent
firewall-cmd --reload
Copy to clipboardErrorCopied
访问地址查看是否安装成功：http://192.168.3.101:15672


输入账号密码并登录：guest guest

创建帐号并设置其角色为管理员：mall mall



创建一个新的虚拟host为：/mall


点击mall用户进入用户配置页面


给mall用户配置该虚拟host的权限

# Elasticsearch

docker pull elasticsearch:7.6.2
Copy to clipboardErrorCopied
修改虚拟内存区域大小，否则会因为过小而无法启动:
sysctl -w vm.max_map_count=262144
Copy to clipboardErrorCopied
使用如下命令启动Elasticsearch服务：
docker run -p 9200:9200 -p 9300:9300 --name elasticsearch \
-e "discovery.type=single-node" \
-e "cluster.name=elasticsearch" \
-v /mydata/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
-v /mydata/elasticsearch/data:/usr/share/elasticsearch/data \
-d elasticsearch:7.6.2
Copy to clipboardErrorCopied
启动时会发现/usr/share/elasticsearch/data目录没有访问权限，只需要修改/mydata/elasticsearch/data目录的权限，再重新启动即可；
chmod 777 /mydata/elasticsearch/data/
Copy to clipboardErrorCopied
安装中文分词器IKAnalyzer，并重新启动：
docker exec -it elasticsearch /bin/bash
#此命令需要在容器中运行
elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.6.2/elasticsearch-analysis-ik-7.6.2.zip
docker restart elasticsearch
Copy to clipboardErrorCopied
开启防火墙：
firewall-cmd --zone=public --add-port=9200/tcp --permanent
firewall-cmd --reload

访问会返回版本信息：http://192.168.3.101:9200

# idea 添加注释
Idea 添加注释：类注释、方法注释
类注释
方法注释
类注释
File–Setting–Editor–File and Code Templates–Class:
注释模板：
/**
 *
 *
 *@description: 
 *@author: Andy
 *@time: ${DATE} ${TIME}
 * 
 */
1
2
3
4
5
6
7
8
操作截图：

效果：

方法注释
为了获取参数信息，我们需要使用 “ Live Templates” 。

创建 Live Templates 分组

File–Setting–Live Templates。


创建 Template

我们上一步创建了 Andy 分组，现在我们将在 Andy 分组中创建 Template。

设置模板内容
模板缩写（Abbreviation）：例如，我们可以把它设置为 “a”。
模板描述（Description）：例如，我们可以设置为 “方法注释”。
模板内容：
/**
 *
 *
 * @description: 
$params$
 * @return: $return$
 * @author: Andy
 * @time: $date$ $time$
 */    
1
2
3
4
5
6
7
8
9

定义模板内容中引用的变量
我们上一步的截图中，点击 Edit variables 按钮可以弹出变量设置窗口。
这里，我们把参数变量 params 设置为一个自定义函数 groovyScript() 。groovyScript 函数调用 Idea 的 methodParameters() 函数获得参数数组，并进行格式化。
params 的值（即 groovyScript 函数）：
groovyScript("def result=''; def params=\"${_1}\".replaceAll('[\\\\[|\\\\]|\\\\s]', '').split(',').toList(); for(i = 0; i < params.size(); i++) {result+=' * @param ' + params[i] + ((i < params.size() - 1) ? '\\n' : '')}; return result", methodParameters())
1
其他变量的值，直接使用 Idea 的函数进行赋值。
date → date()
time → time()
return → methodReturnType()

定义模板的使用范围


效果：



————————————————
版权声明：本文为CSDN博主「Andy_Li_」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/liqing0013/article/details/84104419

