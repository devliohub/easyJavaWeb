# CentOS 7 下 Tomcat 安装和配置步骤

## 一、安装 Java 环境

Tomcat 需要 Java 环境支持，先安装 Java：

### 1. 检查是否已安装 Java
```bash
java -version
```

### 2. 安装 Java 8（项目使用 Java 8）
```bash
# 安装 OpenJDK 8
sudo yum install java-1.8.0-openjdk-devel -y
```

### 3. 配置 Java 环境变量
```bash
# 查看 Java 安装路径
sudo update-alternatives --config java

# 编辑环境变量文件
sudo vi /etc/profile

# 在文件末尾添加以下内容（根据实际安装路径修改）
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk

export JRE_HOME=$JAVA_HOME/jre
export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin

# 使环境变量生效
source /etc/profile

# 验证环境变量
java -version
echo $JAVA_HOME
```

## 二、安装 Tomcat

### 1. 下载 Tomcat 9（与项目使用的版本一致）
```bash
# 创建安装目录
sudo mkdir -p /usr/local/tomcat

# 进入安装目录
cd /usr/local/tomcat

# 下载 Tomcat 9.0.26（项目 pom.xml 中指定的版本）
sudo wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.26/bin/apache-tomcat-9.0.26.tar.gz
```

### 2. 解压并重命名
```bash
# 解压
sudo tar -zxvf apache-tomcat-9.0.26.tar.gz

# 重命名（可选，方便管理）
sudo mv apache-tomcat-9.0.26 tomcat9

# 删除压缩包
sudo rm -f apache-tomcat-9.0.26.tar.gz
```

### 3. 创建 Tomcat 用户和组
```bash
# 创建 tomcat 组
sudo groupadd tomcat

# 创建 tomcat 用户并加入 tomcat 组
sudo useradd -g tomcat -d /usr/local/tomcat/tomcat9 -s /sbin/nologin tomcat
```

### 4. 设置权限
```bash
# 设置 Tomcat 目录所有权
sudo chown -R tomcat:tomcat /usr/local/tomcat/tomcat9/

# 设置 bin 目录下的脚本可执行权限
sudo chmod +x /usr/local/tomcat/tomcat9/bin/*.sh
```

### 5. 创建 Systemd 服务文件
```bash
# 创建 Tomcat 服务文件
sudo vi /etc/systemd/system/tomcat.service
```

在文件中添加以下内容：
```ini
[Unit]
Description=Apache Tomcat 9 Web Application Container
After=syslog.target network.target

[Service]
Type=forking

Environment=JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk
Environment=CATALINA_PID=/usr/local/tomcat/tomcat9/temp/tomcat.pid
Environment=CATALINA_HOME=/usr/local/tomcat/tomcat9
Environment=CATALINA_BASE=/usr/local/tomcat/tomcat9
Environment='CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC'
Environment='JAVA_OPTS=-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom'

ExecStart=/usr/local/tomcat/tomcat9/bin/startup.sh
ExecStop=/usr/local/tomcat/tomcat9/bin/shutdown.sh

User=tomcat
Group=tomcat
UMask=0007
RestartSec=10
Restart=always

[Install]
WantedBy=multi-user.target
```

### 6. 重载 Systemd 并启动 Tomcat
```bash
# 重载 Systemd 配置
sudo systemctl daemon-reload

# 启动 Tomcat
sudo systemctl start tomcat

# 设置开机自启
sudo systemctl enable tomcat
```

### 7. 验证 Tomcat 安装
```bash
# 检查 Tomcat 状态
sudo systemctl status tomcat

# 测试 Tomcat 是否正常运行（在浏览器访问http://server_ip:8080）
curl http://localhost:8080
```

## 三、配置 Tomcat

### 1. 配置 Tomcat 管理界面（可选）
```bash
# 编辑 Tomcat 用户配置文件
sudo vi /usr/local/tomcat/tomcat9/conf/tomcat-users.xml
```

在 `<tomcat-users>` 标签内添加以下内容：
```xml
<role rolename="admin-gui"/>
<role rolename="manager-gui"/>
<user username="admin" password="admin123" roles="admin-gui,manager-gui"/>
```

### 2. 允许远程访问管理界面（可选）
```bash
# 编辑 manager 上下文配置
sudo vi /usr/local/tomcat/tomcat9/webapps/manager/META-INF/context.xml

# 编辑 host-manager 上下文配置
sudo vi /usr/local/tomcat/tomcat9/webapps/host-manager/META-INF/context.xml
```

将以下内容注释掉（允许所有IP访问）：
```xml
<!--
<Valve className="org.apache.catalina.valves.RemoteAddrValve" 
       allow="127\.\d+\.\d+\.\d+|::1|0:0:0:0:0:0:0:1" />
-->
```

### 3. 修改 Tomcat 端口（可选）
```bash
# 编辑 server.xml 配置文件
sudo vi /usr/local/tomcat/tomcat9/conf/server.xml

# 查找并修改端口（默认8080）
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

## 四、部署 api.war

### 1. 复制 api.war 到 Tomcat 的 webapps 目录
```bash
# 将 api.war 上传到服务器（使用scp或其他方式）
scp target/api.war user@server_ip:/tmp/

# 复制到 Tomcat 的 webapps 目录
sudo cp /tmp/api.war /usr/local/tomcat/tomcat9/webapps/

# 设置权限
sudo chown tomcat:tomcat /usr/local/tomcat/tomcat9/webapps/api.war
```

### 2. 重启 Tomcat（自动解压部署）
```bash
sudo systemctl restart tomcat
```

### 3. 验证部署
```bash
# 查看 webapps 目录（应该会自动生成 api 目录）
sudo ls -la /usr/local/tomcat/tomcat9/webapps/

# 测试 API 是否可访问
curl http://localhost:8080/api/home
```

## 五、防火墙配置

允许 Tomcat 默认端口 8080 的流量：

```bash
# 允许 8080 端口
sudo firewall-cmd --permanent --add-port=8080/tcp

# 重新加载防火墙规则
sudo firewall-cmd --reload

# 查看已开放的端口
sudo firewall-cmd --list-all
```

## 六、Tomcat 常用命令

```bash
# 启动 Tomcat
sudo systemctl start tomcat

# 停止 Tomcat
sudo systemctl stop tomcat

# 重启 Tomcat
sudo systemctl restart tomcat

# 查看 Tomcat 状态
sudo systemctl status tomcat

# 查看 Tomcat 日志
sudo tail -n 100 /usr/local/tomcat/tomcat9/logs/catalina.out

# 实时查看 Tomcat 日志
sudo tail -f /usr/local/tomcat/tomcat9/logs/catalina.out

# 查看访问日志
sudo tail -n 100 /usr/local/tomcat/tomcat9/logs/localhost_access_log.`date +%Y-%m-%d`.txt
```

## 七、常见问题

### 1. Tomcat 无法启动
- 检查 Java 环境是否配置正确：`java -version`
- 检查端口是否被占用：`sudo netstat -tlnp | grep 8080`
- 查看 Tomcat 日志：`sudo tail -n 100 /usr/local/tomcat/tomcat9/logs/catalina.out`
- 检查 Tomcat 服务配置：`sudo systemctl status tomcat`

### 2. war 包无法自动解压
- 检查 war 包权限：`sudo chown tomcat:tomcat /usr/local/tomcat/tomcat9/webapps/api.war`
- 检查 Tomcat 用户是否有写入权限：`sudo chmod -R 755 /usr/local/tomcat/tomcat9/webapps/`
- 手动解压 war 包：`sudo unzip api.war -d api`

### 3. API 访问返回 404
- 检查 war 包是否正确部署：`sudo ls -la /usr/local/tomcat/tomcat9/webapps/api/`
- 检查访问路径是否正确：`http://localhost:8080/api/home`
- 查看 Tomcat 应用日志：`sudo tail -n 100 /usr/local/tomcat/tomcat9/logs/localhost.`date +%Y-%m-%d`.log`

### 4. 数据库连接失败
- 检查 application.properties 中的数据库配置是否正确
- 确保数据库服务正在运行且可访问
- 检查数据库用户权限

## 八、优化建议

### 1. 内存优化
修改 `/etc/systemd/system/tomcat.service` 中的 `CATALINA_OPTS` 配置：
```ini
Environment='CATALINA_OPTS=-Xms1024M -Xmx2048M -server -XX:+UseParallelGC -XX:MaxPermSize=256M'
```

### 2. 连接数优化
修改 `/usr/local/tomcat/tomcat9/conf/server.xml` 中的 Connector 配置：
```xml
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443"
           maxThreads="200"
           minSpareThreads="25"
           maxSpareThreads="75"
           acceptCount="100" />
```

### 3. 启用压缩
修改 `/usr/local/tomcat/tomcat9/conf/server.xml` 中的 Connector 配置：
```xml
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443"
           compression="on"
           compressionMinSize="2048"
           compressableMimeType="text/html,text/xml,text/plain,text/css,text/javascript,application/javascript" />
```

## 九、安全建议

1. **修改默认端口**：将 8080 端口改为其他端口
2. **禁用管理界面**：如果不需要，删除或禁用 manager 和 host-manager 应用
3. **设置强密码**：如果启用管理界面，使用强密码
4. **限制访问IP**：只允许特定IP访问管理界面
5. **定期更新**：定期更新 Tomcat 版本以修复安全漏洞
6. **使用 HTTPS**：配置 SSL/TLS 加密连接
