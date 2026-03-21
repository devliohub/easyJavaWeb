# Nginx转发嵌入式Tomcat连接失败解决方案

## 问题描述

在CentOS 7环境下，使用Nginx转发请求到嵌入式Tomcat时，Nginx日志显示以下错误：

```
2026/03/21 02:00:12 [error] 23483#23483: *34 connect() failed (111: Connection refused) while connecting to upstream, client: 192.168.10.5, server: www.easy-java.com, request: "GET /favicon.ico HTTP/1.1", upstream: "http://127.0.0.1:8080/favicon.ico", host: "www.easy-java.com", referrer: "http://www.easy-java.com/sys/rolemenus"
```

同时，在运行嵌入式Tomcat时出现以下错误：

```
错误: 找不到或无法加载主类 com.tsjy.dwlgxy.Main
原因: java.lang.ClassNotFoundException: com.tsjy.dwlgxy.Main
```

## 解决方案概述

这个问题涉及多个方面，需要从以下几个方面进行修复：

1. 检查嵌入式Tomcat配置
2. 修复启动脚本
3. 配置Nginx
4. 配置防火墙
5. 配置SELinux

## 具体修复步骤

### 1. 检查嵌入式Tomcat配置

#### 1.1 确认端口设置

检查`Main.java`文件中的端口配置：

```java
// 文件路径：easy_java/src/main/java/com/tsjy/dwlgxy/Main.java
public static void main(String[] args) throws Exception {
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(Integer.getInteger("port", 8080)); // 默认端口8080
    // ...
}
```

确保端口设置为`8080`（或其他未被占用的端口）。

#### 1.2 确认项目结构

确保项目目录结构正确，特别是`target`目录：

```
easy_java/
├── src/
│   └── main/
│       └── java/
│           └── com/tsjy/dwlgxy/Main.java
└── target/
    ├── classes/
    │   └── com/tsjy/dwlgxy/Main.class  # 编译后的主类
    └── api/
        └── WEB-INF/
            └── lib/                     # 依赖库
```

### 2. 修复启动脚本

使用提供的`startup.sh`脚本启动嵌入式Tomcat，该脚本包含详细的调试信息和错误检查：

```bash
#!/bin/bash

# 项目根目录（在CentOS 7环境下，根据实际情况调整）
PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

# 检查目录结构
echo "Debug: Project directory: $PROJECT_DIR"
echo "Debug: Checking classes directory..."
if [ -d "$PROJECT_DIR/easy_java/target/classes" ]; then
    echo "Debug: Classes directory exists"
    echo "Debug: Main.class location: $PROJECT_DIR/easy_java/target/classes/com/tsjy/dwlgxy/Main.class"
    if [ -f "$PROJECT_DIR/easy_java/target/classes/com/tsjy/dwlgxy/Main.class" ]; then
        echo "Debug: Main.class file exists"
    else
        echo "Error: Main.class file not found!"
        exit 1
    fi
else
    echo "Error: Classes directory not found!"
    exit 1
fi

echo "Debug: Checking lib directory..."
if [ -d "$PROJECT_DIR/easy_java/target/api/WEB-INF/lib" ]; then
    echo "Debug: Lib directory exists"
    echo "Debug: Lib files count: $(ls -1 "$PROJECT_DIR/easy_java/target/api/WEB-INF/lib" | wc -l)"
else
    echo "Error: Lib directory not found!"
    exit 1
fi

# 类路径配置 - 使用Linux风格的路径分隔符
CLASSPATH="$PROJECT_DIR/easy_java/target/classes:$PROJECT_DIR/easy_java/target/api/WEB-INF/lib/*"

# JVM参数
JVM_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC -Dfile.encoding=UTF-8"

# 主类
MAIN_CLASS="com.tsjy.dwlgxy.Main"

# 日志文件
LOG_FILE="$PROJECT_DIR/tomcat.log"

# 检查是否已在运行
if pgrep -f "$MAIN_CLASS" > /dev/null; then
    echo "Tomcat is already running."
    exit 1
fi

# 启动命令
echo "Starting Tomcat..."
echo "Starting Tomcat..." > "$LOG_FILE"
echo "Classpath: $CLASSPATH" >> "$LOG_FILE"
echo "Main class: $MAIN_CLASS" >> "$LOG_FILE"

# 使用exec来确保java进程能接收到信号
nohup java $JVM_OPTS -cp "$CLASSPATH" $MAIN_CLASS >> "$LOG_FILE" 2>&1 &

# 获取进程ID
PID=$!
echo "Tomcat started with PID: $PID"
echo "PID: $PID" >> "$LOG_FILE"
echo "Tomcat started. Log file: $LOG_FILE"
echo "To check status: tail -f $LOG_FILE"
echo "To stop: kill $PID"
```

使用以下命令运行脚本：

```bash
chmod +x startup.sh
./startup.sh
```

### 3. 配置Nginx

使用提供的Nginx配置文件，确保转发设置正确：

```nginx
# 文件路径：nginx_config_no_tomcat.conf

# 全局配置
worker_processes  1;
worker_rlimit_nofile 65535;

events {
    worker_connections  1024;
}

# HTTP配置
http {
    include       mime.types;
    default_type  application/octet-stream;

    # 日志配置
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;
    error_log   /var/log/nginx/error.log   error;

    # 性能优化配置
    sendfile        on;
    tcp_nopush      on;
    tcp_nodelay     on;
    keepalive_timeout  65;
    gzip  on;

    # 服务器配置
    server {
        # 监听80端口（HTTP）
        listen       80;
        # 服务器域名
        server_name  www.easy-java.com;

        # 字符集设置
        charset utf-8;

        # 转发配置
        location / {
            # 转发到本地8080端口（嵌入式Tomcat运行端口）
            proxy_pass http://127.0.0.1:8080;
            
            # 代理设置
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            
            # 长连接设置
            proxy_http_version 1.1;
            proxy_set_header Connection "";
            
            # 超时设置
            proxy_connect_timeout 60s;
            proxy_read_timeout 60s;
            proxy_send_timeout 60s;
        }

        # 静态资源优化
        location ~* \.(jpg|jpeg|png|gif|css|js|ico|svg|woff|woff2|ttf|eot)$ {
            proxy_pass http://127.0.0.1:8080;
            expires 30d;
            add_header Cache-Control "public, no-transform";
        }

        # 错误页面
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   /usr/share/nginx/html;
        }
    }
}
```

将配置文件复制到Nginx配置目录并重启Nginx：

```bash
sudo cp nginx_config_no_tomcat.conf /etc/nginx/conf.d/easy-java.conf
sudo systemctl restart nginx
```

### 4. 配置防火墙

开放必要的端口：

```bash
# 开放HTTP端口（80）
sudo firewall-cmd --permanent --add-port=80/tcp

# 开放嵌入式Tomcat端口（8080）
sudo firewall-cmd --permanent --add-port=8080/tcp

# 重新加载防火墙规则
sudo firewall-cmd --reload
```

验证端口开放情况：

```bash
sudo firewall-cmd --list-ports
```

### 5. 配置SELinux

允许Nginx连接到Tomcat：

```bash
# 允许httpd服务网络连接
sudo setsebool -P httpd_can_network_connect 1

# 允许Nginx连接到8080端口
sudo semanage port -a -t http_port_t -p tcp 8080
```

## 验证方法

### 1. 检查Tomcat运行状态

```bash
# 查看Tomcat进程
ps aux | grep com.tsjy.dwlgxy.Main

# 查看Tomcat日志
tail -f tomcat.log

# 检查端口监听
sudo netstat -tlnp | grep 8080
```

### 2. 检查Nginx状态

```bash
# 查看Nginx状态
sudo systemctl status nginx

# 检查Nginx日志
tail -f /var/log/nginx/error.log
```

### 3. 测试访问

使用浏览器或curl命令测试访问：

```bash
curl http://www.easy-java.comurl http://www.easy-java.com/sys/rolemenus
```

## 常见问题排查

### 1. Tomcat无法启动

**问题**：`Error: Main.class file not found!`

**解决方案**：确保项目已正确编译：

```bash
cd easy_java
mvn clean compile package
```

### 2. Nginx无法连接到Tomcat

**问题**：`connect() failed (111: Connection refused) while connecting to upstream`

**解决方案**：
1. 检查Tomcat是否正在运行
2. 检查防火墙是否开放了8080端口
3. 检查SELinux是否允许Nginx连接到Tomcat

### 3. 403 Forbidden错误

**问题**：访问时出现403 Forbidden错误

**解决方案**：检查文件权限和SELinux上下文：

```bash
sudo chcon -R --type=httpd_sys_content_t /path/to/your/project
```

## 总结

通过以上步骤，您应该能够解决Nginx转发嵌入式Tomcat时出现的连接拒绝错误。主要修复点包括：

1. 确保嵌入式Tomcat配置正确（端口设置、类路径配置）
2. 使用正确的启动脚本启动Tomcat
3. 配置Nginx正确转发请求
4. 开放必要的防火墙端口
5. 配置SELinux允许Nginx与Tomcat通信

如果您仍然遇到问题，请查看相关日志文件（Tomcat日志、Nginx日志、SELinux日志）以获取更多信息。