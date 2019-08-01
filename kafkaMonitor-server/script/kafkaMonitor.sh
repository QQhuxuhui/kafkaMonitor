export JAVA_HOME=$JAVA_HOME
#export JAVA_HOME=/usr/java/jdk1.8.0_211

FILE="kafkaMonitor-server.jar"
dir="../"
CONFIGPATH="$dir/conf"
LIB="$dir/lib"
MAIN_CLASS="com.ServiceApplication"
#加载配置文件
CLASSPATH=.:$CONFIGPATH:$dir$FILE
#加载jar
cmd=$1
for i in $LIB/*.jar;
do
  CLASSPATH=$CLASSPATH:$i
done
#生效
export CLASSPATH=.:${CLASSPATH}
psid=0

start() {
	if [ -f "$dir/vt.pid" ];then
        pid=`cat $dir/vt.pid`
        psid=`jps|grep -i "\<$pid\>"`
        if [ ! -z "$pid" ] && [[ ! -z "$psid" ]];then
          echo "================================"
		  echo "warn: $MAIN_CLASS already started! (pid=$psid)"
          echo "================================"
          exit 0
        fi
      fi
      echo "================================"
      echo "COMMAND start kafkaMonitor-server"
      echo "================================"
      echo -n "Starting $MAIN_CLASS ..."

	  #echo java -cp ${CLASSPATH}$dir$FILE $MAIN_CLASS
	  nohup $JAVA_HOME/bin/java -cp ${CLASSPATH}$dir$FILE $MAIN_CLASS>>run.out  2>&1 &
	  echo $!>$dir/vt.pid
	  pid=`cat $dir/vt.pid`
      if [ $pid -ne 0 ]; then
         echo "(pid=`cat $dir/vt.pid`) [OK]"
      else
         echo "[Failed]"
      fi
   #fi
}
stop(){
		pid=`cat $dir/vt.pid`
        psid=`jps|grep -i "\<$pid\>"`
		if [ ! -z "$pid" ] && [[ ! -z "$psid" ]];then
			kill -SIGTERM $pid         
		else
			echo "pid not found"
			exit 0
		fi
   if [ $pid -ne 0 ]; then
      echo -n "Stopping $RUN_NAME ...(pid=$pid) "
      su - $RUNNING_USER -c "kill -9 $pid"
      if [ $? -eq 0 ]; then
         echo "[OK]"
      else
         echo "[Failed]"
      fi
   else
      echo "================================"
      echo "warn: $MAIN_CLASS is not running"
      echo "================================"
   fi
}


case $cmd in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    stop
	sleep 2
    start
    ;;
  *)
    echo $"Usage: $0 {start|stop|restart}"
    exit 1
esac

