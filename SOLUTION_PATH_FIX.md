# 路径问题修复解决方案

## 问题描述

在运行嵌入式Tomcat时，出现以下错误：

```
Caused by: java.lang.IllegalArgumentException: The directory specified by base and internal path [/home/wwwroot/bookan/easyJavaWeb/target/classes/\] does not exist.
    at org.apache.catalina.webresources.DirResourceSet.initInternal(DirResourceSet.java:265)
    at org.apache.catalina.util.LifecycleBase.init(LifecycleBase.java:136)
```

这是因为Main.java中使用了硬编码的相对路径来定位资源目录，当从不同目录运行时会导致路径错误。

## 解决方案

### 1. 修改Main.java文件

我修改了Main.java文件，使其能够动态获取classes目录的位置，而不是使用硬编码的相对路径：

```java
import java.io.File;
import java.net.URL;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class Main {

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(Integer.getInteger("port", 8080));
		tomcat.getConnector();
		
		// 获取当前类的路径，用于动态定位资源目录
		URL classUrl = Main.class.getProtectionDomain().getCodeSource().getLocation();
		File classesDir = new File(classUrl.toURI());
		System.out.println("Classes directory: " + classesDir.getAbsolutePath());
		
		// 项目根目录 = classes目录的父目录的父目录
		File projectDir = classesDir.getParentFile().getParentFile();
		System.out.println("Project directory: " + projectDir.getAbsolutePath());
		
		// Webapp目录
		File webappDir = new File(projectDir, "src/main/webapp");
		System.out.println("Webapp directory: " + webappDir.getAbsolutePath());
		
		Context ctx = tomcat.addWebapp("", webappDir.getAbsolutePath());
		WebResourceRoot resources = new StandardRoot(ctx);
		
		// 使用动态获取的classes目录
		resources.addPreResources(
				new DirResourceSet(resources, "/WEB-INF/classes", classesDir.getAbsolutePath(), "/"));
		ctx.setResources(resources);
		
		tomcat.start();
		tomcat.getServer().await();
	}
}
```

### 2. 在CentOS环境中重新编译项目

在CentOS环境中，进入项目目录并重新编译：

```bash
cd /home/wwwroot/bookan/easyJavaWeb/easy_java
mvn clean compile package
```

### 3. 使用修复后的启动脚本

使用修复后的`startup.sh`脚本启动Tomcat：

```bash
chmod +x /home/wwwroot/bookan/easyJavaWeb/startup.sh
cd /home/wwwroot/bookan/easyJavaWeb
./startup.sh
```

## 验证方法

1. 查看Tomcat日志，确认没有路径错误：

```bash
tail -f /home/wwwroot/bookan/easyJavaWeb/tomcat.log
```

2. 检查Tomcat是否成功启动：

```bash
ps aux | grep com.tsjy.dwlgxy.Main
```

3. 测试访问：

```bash
curl http://www.easy-java.comurl http://www.easy-java.com/sys/rolemenus
```

## 总结

通过修改Main.java文件使其动态获取资源目录路径，解决了从不同目录运行时的路径错误问题。现在，无论从哪个目录运行启动脚本，Tomcat都能正确找到所需的资源文件。