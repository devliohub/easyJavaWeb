# CentOS 7 防火墙配置指南

## 1. 检查防火墙状态

首先检查firewalld服务是否已启动：

```bash
sudo systemctl status firewalld
```

如果防火墙未启动，可以使用以下命令启动并设置为开机自启：

```bash
sudo systemctl start firewalld
sudo systemctl enable firewalld
```

## 2. 开放必要的端口

### 2.1 开放HTTP端口（80）

Nginx需要监听80端口以接收外部HTTP请求：

```bash
sudo firewall-cmd --permanent --add-port=80/tcp
```

### 2.2 开放嵌入式Tomcat端口（8080）

嵌入式Tomcat默认运行在8080端口，需要允许Nginx转发请求到该端口：

```bash
sudo firewall-cmd --permanent --add-port=8080/tcp
```

## 3. 重新加载防火墙规则

添加规则后，需要重新加载防火墙以使规则生效：

```bash
sudo firewall-cmd --reload
```

## 4. 验证端口开放情况

使用以下命令验证端口是否已正确开放：

```bash
sudo firewall-cmd --list-ports
```

你应该能看到类似以下输出：
```
80/tcp 8080/tcp
```

## 5. 临时关闭防火墙（仅用于测试）

如果在配置过程中遇到问题，可以临时关闭防火墙进行测试：

```bash
sudo systemctl stop firewalld
```

**注意：测试完成后请务必重新启动防火墙！**

```bash
sudo systemctl start firewalld
```

## 6. 防火墙高级配置（可选）

### 6.1 允许特定IP访问

如果需要限制只有特定IP可以访问8080端口（例如只允许Nginx服务器本身）：

```bash
sudo firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="127.0.0.1" port protocol="tcp" port="8080" accept'
```

### 6.2 允许Nginx服务

使用预定义的服务配置（可能需要先安装Nginx的firewalld配置）：

```bash
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https  # 如果使用HTTPS
```

## 7. 常见问题排查

- 如果防火墙规则不生效，请检查是否有其他安全软件（如SELinux）在运行
- 如果无法访问80端口，检查Nginx是否正在监听该端口：`sudo netstat -tlnp | grep 80`
- 如果Nginx无法转发到8080端口，检查Tomcat是否正在监听该端口：`sudo netstat -tlnp | grep 8080`
