# 项目部署使用说明

本文档提供了项目的多种部署方式，包括本地运行、Nginx转发等配置，以及在CentOS 7下的详细部署步骤。

## 项目简介

这是一个基于Java开发的Web项目，使用了嵌入式Tomcat作为服务器。项目可以通过两种方式运行：
1. **直接运行嵌入式Tomcat**：不依赖外部Tomcat，通过`Main.java`直接启动
2. **部署到独立Tomcat**：将项目打包为`api.war`部署到独立的Tomcat服务器

## 一、本地运行方式

### 1.1 直接运行嵌入式Tomcat

#### 步骤1：编译项目

```bash
# 进入项目根目录
cd d:\wwwroot\github\easyJavaWeb\easy_java

# 编译项目
mvn clean package
```

#### 步骤2：启动嵌入式Tomcat

```bash
# 使用Main.java启动嵌入式Tomcat（默认端口8080）
java -cp "target/classes;target/api/WEB-INF/lib/*" com.tsjy.dwlgxy.Main
```

#### 步骤3：验证运行

在浏览器访问：`http://localhost:8080/home`

### 1.2 部署到本地Tomcat

#### 步骤1：编译项目

```bash
cd d:\wwwroot\github\easyJavaWeb\easy_java
mvn clean package
```

#### 步骤2：部署到Tomcat

1. 将`target/api.war`复制到本地Tomcat的`webapps`目录
2. 启动Tomcat
3. Tomcat会自动解压`api.war`到`api`目录

#### 步骤3：验证运行

在浏览器访问：`http://localhost:8080/api/home`

## 二、Nginx转发配置

### 2.1 配置说明

Nginx作为反向代理，可以将HTTP请求转发到项目的嵌入式Tomcat或独立Tomcat。

### 2.2 情况一：不安装独立Tomcat（使用嵌入式Tomcat）

使用`nginx_config_no_tomcat.conf`配置文件，主要配置：

```nginx
location / {
    # 转发到嵌入式Tomcat（端口8080）
    proxy_pass http://127.0.0.1:8080;
    # 其他代理配置...
}
```

### 2.3 情况二：安装独立Tomcat

使用`nginx_config_with_tomcat.conf`配置文件，主要配置：

```nginx
location / {
    # 转发到独立Tomcat的api应用（端口8080）
    proxy_pass http://127.0.0.1:8080/api/;
    # 其他代理配置...
}
```

## 三、CentOS 7下的部署

### 3.1 安装Nginx

详细步骤请参考：`nginx_install_config_centos7.md`

主要步骤：
1. 安装EPEL仓库
2. 安装Nginx
3. 启动Nginx并设置开机自启
4. 配置Nginx转发规则

### 3.2 安装Tomcat（可选）

详细步骤请参考：`tomcat_install_config_centos7.md`

主要步骤：
1. 安装Java环境
2. 下载并安装Tomcat 9
3. 配置Tomcat服务
4. 部署`api.war`文件

## 四、完整部署流程

### 4.1 方式一：CentOS 7 + Nginx + 嵌入式Tomcat

#### 步骤1：准备项目

1. 在本地编译项目：`mvn clean package`
2. 将编译后的项目文件上传到服务器：`scp -r easy_java/ user@server_ip:/opt/`

#### 步骤2：安装Nginx

```bash
# 更新系统
 sudo yum update -y

# 安装EPEL仓库
 sudo yum install epel-release -y

# 安装Nginx
 sudo yum install nginx -y

# 启动Nginx并设置开机自启
 sudo systemctl start nginx
 sudo systemctl enable nginx
```

#### 步骤3：配置Nginx

```bash
# 上传Nginx配置文件
 scp nginx_config_no_tomcat.conf user@server_ip:/tmp/

# 替换默认配置
 sudo cp /tmp/nginx_config_no_tomcat.conf /etc/nginx/nginx.conf

# 检查配置是否正确
 sudo nginx -t

# 重新加载Nginx
 sudo systemctl reload nginx
```

#### 步骤4：运行项目

```bash
# 进入项目目录
 cd /opt/easy_java

# 启动嵌入式Tomcat（可以使用nohup后台运行）
 nohup java -cp "target/classes;target/api/WEB-INF/lib/*" com.tsjy.dwlgxy.Main > app.log 2>&1 &
```

#### 步骤5：验证部署

在浏览器访问：`http://server_ip/home`

### 4.2 方式二：CentOS 7 + Nginx + 独立Tomcat

#### 步骤1：准备项目

1. 在本地编译项目：`mvn clean package`
2. 将`target/api.war`上传到服务器：`scp target/api.war user@server_ip:/tmp/`

#### 步骤2：安装Java和Tomcat

```bash
# 安装Java 8
 sudo yum install java-1.8.0-openjdk-devel -y

# 安装Tomcat 9
# 详细步骤请参考tomcat_install_config_centos7.md
```

#### 步骤3：部署WAR包

```bash
# 复制WAR包到Tomcat的webapps目录
 sudo cp /tmp/api.war /usr/local/tomcat/tomcat9/webapps/

# 重启Tomcat
 sudo systemctl restart tomcat
```

#### 步骤4：安装Nginx

```bash
# 安装Nginx
 sudo yum install epel-release -y
 sudo yum install nginx -y
```

#### 步骤5：配置Nginx

```bash
# 上传Nginx配置文件
 scp nginx_config_with_tomcat.conf user@server_ip:/tmp/

# 替换默认配置
 sudo cp /tmp/nginx_config_with_tomcat.conf /etc/nginx/nginx.conf

# 检查配置是否正确
 sudo nginx -t

# 重新加载Nginx
 sudo systemctl reload nginx
```

#### 步骤6：验证部署

在浏览器访问：`http://server_ip/home`

## 五、防火墙配置

### 5.1 开放Nginx端口（80）

```bash
# 允许HTTP流量
 sudo firewall-cmd --permanent --add-service=http

# 重新加载防火墙规则
 sudo firewall-cmd --reload
```

### 5.2 开放Tomcat端口（8080，可选）

```bash
# 允许8080端口流量（如果需要直接访问Tomcat）
 sudo firewall-cmd --permanent --add-port=8080/tcp

# 重新加载防火墙规则
 sudo firewall-cmd --reload
```

## 六、常见问题及解决方案

### 6.1 端口被占用

```bash
# 查看端口占用情况
 sudo netstat -tlnp | grep 8080

# 终止占用端口的进程
 sudo kill -9 <进程ID>
```

### 6.2 Nginx转发失败

1. 检查Nginx配置是否正确：`sudo nginx -t`
2. 检查后端服务是否正在运行
3. 查看Nginx日志：`sudo tail -n 50 /var/log/nginx/error.log`

### 6.3 Tomcat部署失败

1. 检查WAR包权限：`sudo chown tomcat:tomcat /usr/local/tomcat/tomcat9/webapps/api.war`
2. 查看Tomcat日志：`sudo tail -n 100 /usr/local/tomcat/tomcat9/logs/catalina.out`
3. 检查Java环境：`java -version`

### 6.4 数据库连接失败

1. 检查`application.properties`中的数据库配置
2. 确保数据库服务正在运行
3. 检查数据库用户权限

## 七、优化建议

### 7.1 Nginx优化

```nginx
# 增加工作进程数
worker_processes  4;

# 增加连接数
worker_connections  2048;

# 启用压缩
 gzip on;
 gzip_comp_level 5;
 gzip_types text/plain text/css application/json application/javascript text/xml application/xml text/javascript;
```

### 7.2 Tomcat优化

```ini
# 内存优化
Environment='CATALINA_OPTS=-Xms1024M -Xmx2048M -server -XX:+UseParallelGC'

# 连接数优化
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443"
           maxThreads="200"
           minSpareThreads="25"
           maxSpareThreads="75"
           acceptCount="100" />
```

## 八、注意事项

1. **端口权限**：端口80需要管理员权限，如果使用普通用户运行，请修改为其他端口（如8080）
2. **防火墙配置**：确保防火墙已开放所需端口（80、8080等）
3. **权限设置**：确保服务运行用户对相关文件和目录有读写权限
4. **日志管理**：定期查看应用日志和服务器日志，及时发现问题
5. **安全配置**：生产环境建议使用HTTPS，并配置适当的安全策略

## 九、相关文件

- `nginx_config_no_tomcat.conf`：不安装Tomcat时的Nginx配置
- `nginx_config_with_tomcat.conf`：安装Tomcat时的Nginx配置
- `nginx_install_config_centos7.md`：CentOS 7下Nginx安装配置
- `tomcat_install_config_centos7.md`：CentOS 7下Tomcat安装配置

## 十、联系信息

如有问题，请联系项目维护人员。