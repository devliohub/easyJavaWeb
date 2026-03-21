# CentOS 7 SELinux配置指南

## 1. 检查SELinux状态

首先检查SELinux是否已启用：

```bash
getenforce
```

输出可能是以下三种状态之一：
- `Enforcing`: SELinux已启用并正在强制执行策略
- `Permissive`: SELinux已启用但仅记录警告，不强制执行策略
- `Disabled`: SELinux已禁用

## 2. SELinux基本配置

### 2.1 临时切换到Permissive模式（仅用于测试）

如果在配置过程中遇到问题，可以临时切换到Permissive模式进行测试：

```bash
sudo setenforce 0
```

**注意：测试完成后请务必切换回Enforcing模式！**

```bash
sudo setenforce 1
```

### 2.2 永久禁用SELinux（不推荐）

如果需要永久禁用SELinux，可以编辑`/etc/selinux/config`文件：

```bash
sudo vi /etc/selinux/config
```

将`SELINUX=enforcing`修改为`SELINUX=disabled`，然后重启系统。

**注意：永久禁用SELinux会降低系统安全性，不推荐在生产环境中使用！**

## 3. 允许Nginx连接到Tomcat

### 3.1 允许HTTP服务网络连接

Nginx作为HTTP服务，需要允许它连接到网络服务（如Tomcat）：

```bash
sudo setsebool -P httpd_can_network_connect 1
```

参数说明：
- `-P`: 永久生效（重启后保持设置）
- `httpd_can_network_connect`: 允许httpd服务连接到网络

### 3.2 允许Nginx连接到特定端口

如果需要允许Nginx连接到特定端口（如Tomcat的8080端口），可以使用以下命令：

```bash
sudo semanage port -a -t http_port_t -p tcp 8080
```

参数说明：
- `-a`: 添加新规则
- `-t http_port_t`: 设置端口类型为http_port_t
- `-p tcp`: 指定协议为TCP

## 4. 设置文件上下文

确保项目文件具有正确的SELinux上下文：

```bash
sudo chcon -R --type=httpd_sys_content_t /path/to/your/project
```

参数说明：
- `-R`: 递归处理所有子目录和文件
- `--type=httpd_sys_content_t`: 设置文件类型为httpd_sys_content_t（HTTP服务可访问的内容）

## 5. 查看SELinux日志

如果遇到SEL相关的错误，可以查看SELinux日志进行排查：

```bash
sudo tail -f /var/log/audit/audit.log | grep AVC
```

或者使用`sealert`工具分析日志：

```bash
sudo sealert -a /var/log/audit/audit.log
```

## 6. 使用audit2allow修复SELinux问题

`audit2allow`工具可以根据SELinux日志自动生成允许规则：

### 6.1 安装audit2allow

```bash
sudo yum install -y policycoreutils-python
```

### 6.2 生成并应用规则

```bash
sudo grep nginx /var/log/audit/audit.log | audit2allow -M my-nginx
sudo semodule -i my-nginx.pp
```

## 7. 常见SELinux问题及解决方法

### 7.1 Nginx无法连接到Tomcat

**症状**：Nginx日志显示`connect() failed (111: Connection refused) while connecting to upstream`

**解决方案**：允许httpd服务网络连接
```bash
sudo setsebool -P httpd_can_network_connect 1
```

### 7.2 Nginx无法读取项目文件

**症状**：Nginx日志显示`Permission denied`错误

**解决方案**：设置正确的文件上下文
```bash
sudo chcon -R --type=httpd_sys_content_t /path/to/your/project
```

### 7.3 Tomcat无法访问某些资源

**症状**：Tomcat日志显示权限错误

**解决方案**：确保Tomcat进程有适当的SELinux上下文
```bash
sudo chcon -R --type=java_exec_t /path/to/tomcat/bin/java
```

## 8. 验证SELinux配置

使用以下命令验证SELinux配置是否正确：

```bash
# 检查httpd_can_network_connect状态
getsebool httpd_can_network_connect

# 检查端口上下文
semanage port -l | grep http_port_t

# 检查文件上下文
ls -Z /path/to/your/project
```
