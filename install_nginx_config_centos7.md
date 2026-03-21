# CentOS 7 下 Nginx 安装和配置步骤

## 一、安装 Nginx

### 1. 更新系统包
```bash
sudo yum update -y
```

### 2. 安装 EPEL 仓库（Nginx 不在默认仓库中）
```bash
sudo yum install epel-release -y
```

### 3. 安装 Nginx
```bash
sudo yum install nginx -y
```

### 4. 启动 Nginx 并设置开机自启
```bash
# 启动 Nginx
sudo systemctl start nginx

# 设置开机自启
sudo systemctl enable nginx
```

### 5. 验证 Nginx 安装
```bash
# 检查 Nginx 状态
sudo systemctl status nginx

# 测试 Nginx 是否正常运行（在浏览器访问服务器IP或本地访问http://localhost）
curl http://localhost
```

## 二、配置 Nginx

根据您是否安装独立 Tomcat，选择对应的配置文件：

### 情况一：不安装独立 Tomcat（使用项目内置嵌入式 Tomcat）

#### 1. 备份默认 Nginx 配置
```bash
sudo cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.bak
```

#### 2. 复制配置文件
将之前创建的 `nginx_config_no_tomcat.conf` 复制到服务器：
```bash
# 上传文件到服务器（使用scp或其他方式）
scp nginx_config_no_tomcat.conf user@server_ip:/tmp/

# 复制到 Nginx 配置目录
sudo cp /tmp/nginx_config_no_tomcat.conf /etc/nginx/nginx.conf
```

#### 3. 修改配置文件中的域名（可选）
```bash
sudo vi /etc/nginx/nginx.conf
# 将 your-domain.com 替换为您的实际域名
```

### 情况二：安装独立 Tomcat

#### 1. 备份默认 Nginx 配置
```bash
sudo cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.bak
```

#### 2. 复制配置文件
将之前创建的 `nginx_config_with_tomcat.conf` 复制到服务器：
```bash
# 上传文件到服务器（使用scp或其他方式）
scp nginx_config_with_tomcat.conf user@server_ip:/tmp/

# 复制到 Nginx 配置目录
sudo cp /tmp/nginx_config_with_tomcat.conf /etc/nginx/nginx.conf
```

#### 3. 修改配置文件中的域名（可选）
```bash
sudo vi /etc/nginx/nginx.conf
# 将 your-domain.com 替换为您的实际域名
```

## 三、验证 Nginx 配置

```bash
# 检查 Nginx 配置是否正确
sudo nginx -t
```

如果输出 "nginx: configuration file /etc/nginx/nginx.conf test is successful"，说明配置正确。

## 四、重新加载 Nginx

```bash
sudo systemctl reload nginx
```

## 五、防火墙设置（如果需要）

如果您的服务器启用了防火墙，需要允许 80 端口：

```bash
# 允许 HTTP 流量（80端口）
sudo firewall-cmd --permanent --add-service=http

# 重新加载防火墙规则
sudo firewall-cmd --reload
```

## 六、验证 Nginx 转发

根据您的配置情况，测试 Nginx 转发是否正常：

### 情况一：不安装独立 Tomcat
1. 确保项目的嵌入式 Tomcat 正在运行（端口 8080）
2. 在浏览器访问：http://your-domain.com

### 情况二：安装独立 Tomcat
1. 确保独立 Tomcat 正在运行（端口 8080）
2. 确保 api.war 已部署到 Tomcat
3. 在浏览器访问：http://your-domain.com

## 七、常见问题

### 1. Nginx 无法启动
- 检查端口是否被占用：`sudo netstat -tlnp | grep 80`
- 检查配置文件是否正确：`sudo nginx -t`
- 查看错误日志：`sudo tail -n 50 /var/log/nginx/error.log`

### 2. 转发失败
- 检查后端服务是否正在运行
- 检查防火墙是否允许后端端口的流量
- 查看 Nginx 访问日志：`sudo tail -n 50 /var/log/nginx/access.log`
- 查看 Nginx 错误日志：`sudo tail -n 50 /var/log/nginx/error.log`

### 3. 权限问题
- 确保 Nginx 可以读取配置文件：`sudo chown nginx:nginx /etc/nginx/nginx.conf`
- 确保 Nginx 可以写入日志文件：`sudo chown nginx:nginx /var/log/nginx/*`

## 八、Nginx 常用命令

```bash
# 启动 Nginx
sudo systemctl start nginx

# 停止 Nginx
sudo systemctl stop nginx

# 重启 Nginx
sudo systemctl restart nginx

# 重新加载配置（不重启服务）
sudo systemctl reload nginx

# 查看 Nginx 状态
sudo systemctl status nginx

# 禁用 Nginx 开机自启
sudo systemctl disable nginx

# 启用 Nginx 开机自启
sudo systemctl enable nginx
```
