# Nginx转发到嵌入式Tomcat连接失败解决方案

## 错误分析

错误信息：
```
2026/03/21 02:00:12 [error] 23483#23483: *34 connect() failed (111: Connection refused) while connecting to upstream, client: 192.168.10.5, server: www.easy-java.com, request: "GET /favicon.ico HTTP/1.1", upstream: "http://127.0.0.1:8080/favicon.ico", host: "www.easy-java.com", referrer: "http://www.easy-java.com/sys/rolemenus"
```

**核心问题**：Nginx无法连接到上游服务器`http://127.0.0.1:8080`，错误码111表示连接被拒绝。

## 排查步骤

### 1. 检查嵌入式Tomcat是否正在运行

```bash
# 登录到CentOS 7服务器
ssh user@www.easy-java.com

# 检查Java进程是否在运行
ps aux | grep java
```

如果没有看到Java进程，说明嵌入式Tomcat没有启动。

### 2. 检查Tomcat监听的端口

```bash
# 检查8080端口是否被监听
ss -tlnp | grep 8080
# 或使用netstat（如果已安装）
netstat -tlnp | grep 8080
```

如果没有输出，说明Tomcat没有在8080端口监听。

### 3. 检查防火墙设置

```bash
# 查看防火墙规则
firewall-cmd --list-all

# 检查是否允许8080端口
firewall-cmd --list-ports
```

如果8080端口没有开放，需要添加防火墙规则：

```bash
# 添加8080端口到防火墙
firewall-cmd --permanent --add-port=8080/tcp

# 重新加载防火墙规则
firewall-cmd --reload
```

### 4. 检查Tomcat启动日志

查看嵌入式Tomcat的启动日志，寻找错误信息：

```bash
# 假设Tomcat启动时日志输出到app.log
cat app.log
# 或查看最新日志
tail -n 100 app.log
```

### 5. 检查启动命令和classpath

确保嵌入式Tomcat的启动命令正确，特别是classpath设置：

```bash
# 正确的启动命令示例
java -cp "target/classes:target/api/WEB-INF/lib/*" -Dfile.encoding=UTF-8 com.tsjy.dwlgxy.Main
```

## 解决方案

### 方案1：启动嵌入式Tomcat

如果Tomcat没有运行，启动它：

```bash
# 进入项目目录
cd /path/to/easyJavaWeb/easy_java

# 启动嵌入式Tomcat（后台运行）
nohup java -cp "target/classes:target/api/WEB-INF/lib/*" -Dfile.encoding=UTF-8 com.tsjy.dwlgxy.Main > app.log 2>&1 &
```

### 方案2：检查端口配置

确保Main.java中配置的端口是8080：

```java
// Main.java中的端口配置
tomcat.setPort(Integer.getInteger("port", 8080));
```

如果需要修改端口，可以通过系统属性指定：

```bash
# 使用8081端口启动
nohup java -cp "target/classes:target/api/WEB-INF/lib/*" -Dport=8081 -Dfile.encoding=UTF-8 com.tsjy.dwlgxy.Main > app.log 2>&1 &
```

然后修改Nginx配置文件中的 upstream 地址。

### 方案3：检查防火墙设置

确保防火墙允许本地连接到8080端口：

```bash
# 临时开放8080端口（立即生效，重启后失效）
firewall-cmd --add-port=8080/tcp

# 永久开放8080端口
firewall-cmd --permanent --add-port=8080/tcp
firewall-cmd --reload
```

### 方案4：检查SELinux设置

如果CentOS 7启用了SELinux，可能会阻止Nginx连接到Tomcat：

```bash
# 查看SELinux状态
getenforce

# 临时关闭SELinux（测试用）
setenforce 0

# 如果关闭后正常，可以配置SELinux允许Nginx连接网络
setsebool -P httpd_can_network_connect 1
```

### 方案5：检查Nginx配置

确保Nginx配置文件中的upstream地址正确：

```nginx
# /etc/nginx/nginx.conf 中的相关配置
location / {
    proxy_pass http://127.0.0.1:8080;
    # 其他代理设置...
}
```

检查配置后重新加载Nginx：

```bash
sudo nginx -t
sudo systemctl reload nginx
```

## 验证解决方案

1. **检查Tomcat是否运行**：
   ```bash
   ps aux | grep java
   ```

2. **检查端口监听**：
   ```bash
   ss -tlnp | grep 8080
   ```

3. **测试本地连接**：
   ```bash
   curl http://127.0.0.1:8080/home
   ```

4. **测试Nginx转发**：
   在浏览器访问：`http://www.easy-java.com/home`

## 常见问题及解决方案

### 问题1：Tomcat启动后立即退出

**原因**：classpath错误或依赖缺失

**解决方案**：检查classpath设置，确保包含所有必要的依赖：

```bash
java -cp "target/classes:target/api/WEB-INF/lib/*" com.tsjy.dwlgxy.Main
```

### 问题2：端口被占用

**原因**：8080端口已被其他程序占用

**解决方案**：
1. 查找占用端口的进程：`lsof -i :8080` 或 `netstat -tlnp | grep 8080`
2. 终止占用端口的进程：`kill -9 <PID>`
3. 或使用其他端口启动Tomcat：`-Dport=8081`

### 问题3：Nginx 403 Forbidden

**原因**：权限问题或索引文件缺失

**解决方案**：
1. 检查Nginx运行用户权限
2. 确保Tomcat应用有默认索引页

## 完整部署流程回顾

1. **编译项目**：
   ```bash
   mvn clean compile
   ```

2. **启动嵌入式Tomcat**：
   ```bash
   nohup java -cp "target/classes:target/api/WEB-INF/lib/*" -Dfile.encoding=UTF-8 com.tsjy.dwlgxy.Main > app.log 2>&1 &
   ```

3. **配置Nginx**：
   ```bash
   sudo cp nginx_config_no_tomcat.conf /etc/nginx/nginx.conf
   sudo nginx -t
   sudo systemctl reload nginx
   ```

4. **设置防火墙**：
   ```bash
   sudo firewall-cmd --permanent --add-service=http
   sudo firewall-cmd --permanent --add-port=8080/tcp
   sudo firewall-cmd --reload
   ```

5. **验证部署**：
   ```bash
   curl http://www.easy-java.com/home
   ```

## 总结

Nginx连接被拒绝的问题通常由以下原因导致：
1. 后端服务（嵌入式Tomcat）未运行
2. 端口配置错误或被占用
3. 防火墙或SELinux阻止连接
4. classpath设置错误导致服务无法正常启动

通过逐步排查上述问题，应该能够解决Nginx转发失败的问题。