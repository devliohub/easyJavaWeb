# Java 应用运行说明

## Classpath 说明

在 Java 中，`classpath` 是告诉 JVM 从哪里加载类文件的路径集合。如果不指定 `classpath`，JVM 会使用默认的 `classpath`（通常是当前目录 `.`）。

对于这个项目，**必须显式指定 `classpath`**，原因如下：

1. **Main 类的位置**：`Main.class` 位于 `target/classes/` 目录下，而不是当前目录
2. **依赖库**：项目依赖了大量第三方库（如 Tomcat、数据库驱动、日志库等），这些库位于 `target/api/WEB-INF/lib/` 目录下的 JAR 文件中
3. **编译输出结构**：Maven 编译后的输出结构决定了需要指定多个路径来包含所有必要的类文件和依赖

## 正确的运行命令

您可以在原命令的基础上添加 JVM 参数，但必须保留 `classpath` 设置：

```bash
java -cp "target/classes;target/api/WEB-INF/lib/*" -Xms512m -Xmx2g -XX:+UseG1GC -Dfile.encoding=UTF-8 com.tsjy.dwlgxy.Main
```

## 命令说明

- `-cp "target/classes;target/api/WEB-INF/lib/*"`：指定类路径，包含编译后的类文件和所有依赖库
- `-Xms512m`：设置 JVM 初始堆大小为 512MB
- `-Xmx2g`：设置 JVM 最大堆大小为 2GB
- `-XX:+UseG1GC`：使用 G1 垃圾收集器
- `-Dfile.encoding=UTF-8`：设置文件编码为 UTF-8
- `com.tsjy.dwlgxy.Main`：主类的全限定名

## 其他运行方式

### 1. 使用 Maven 运行

如果使用 Maven，可以通过 `exec:java` 插件运行，它会自动处理类路径：

```bash
mvn exec:java -Dexec.mainClass="com.tsjy.dwlgxy.Main" -Dexec.args="-Xms512m -Xmx2g -XX:+UseG1GC -Dfile.encoding=UTF-8"
```

### 2. 使用打包后的 WAR 文件

如果项目已经打包为 `api.war`，可以部署到独立的 Tomcat 服务器上运行。

## 常见问题

### 问题 1：`Could not find or load main class com.tsjy.dwlgxy.Main`
- **原因**：JVM 找不到指定的主类
- **解决**：确保当前目录是项目根目录，并且 `classpath` 包含 `target/classes/` 目录

### 问题 2：`NoClassDefFoundError` 或 `ClassNotFoundException`
- **原因**：找不到依赖的类
- **解决**：确保 `classpath` 包含 `target/api/WEB-INF/lib/*` 以加载所有依赖库

### 问题 3：端口被占用
- **原因**：默认端口 8080 已被其他程序占用
- **解决**：可以通过 `-Dport=8081` 参数指定其他端口：
  ```bash
  java -cp "target/classes;target/api/WEB-INF/lib/*" -Dport=8081 com.tsjy.dwlgxy.Main
  ```