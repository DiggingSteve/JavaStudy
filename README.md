#以下安装都将在docker 进行
# mysql 安装
~ docker pull mysql:5.7
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
