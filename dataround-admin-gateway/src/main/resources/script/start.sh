#!/usr/bin/env bash
BIN_PATH=$(dirname $0)
BASE_PATH=$(cd $(dirname $BIN_PATH); pwd)

echo "Prepare to start dataround gateway"
LOG_PATH=$BASE_PATH/logs
if [ ! -d $LOG_PATH ]; then
  mkdir -p $LOG_PATH
fi

if [ -z "$JAVA_HOME" ]; then
  echo "JAVA_HOME is not set"
  exit 1
else
  echo "JAVA_HOME is set to: $JAVA_HOME"
fi
JAVA_OPTS="-Xms256m -Xmx2048m" 
#JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
# Find the gateway jar file
JAR_FILE=$(find $BASE_PATH/lib -name "dataround-gateway-*.jar")
if [ -z "$JAR_FILE" ]; then
    echo "Error: dataround-gateway-*.jar not found in $BASE_PATH/lib"
    exit 1
fi
nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar $JAR_FILE \
      --spring.config.location=$BASE_PATH/conf/application.yaml >> $LOG_PATH/dataround-gateway.log 2>&1 &
echo "Dataround gateway started."