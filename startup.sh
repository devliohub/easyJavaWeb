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
echo "Starting Tomcat with classpath: $CLASSPATH"
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
