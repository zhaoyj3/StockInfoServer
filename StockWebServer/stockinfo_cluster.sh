#! /bin/sh
NODE_ENV=production
DAEMON="node cluster.js"
NAME=StockInfo
DESC=StockInfo
PIDFILE="stockInfo_cluster.pid"
case "$1" in
start)
echo "Starting $DESC: "
nohup $DAEMON > /dev/null &
echo $! 
echo $! > $PIDFILE
echo "Starting $NAME finish."
;;
stop)
echo "Stopping $DESC: "
cat $PIDFILE | while read pid
do
   echo $pid
   kill $pid
done

rm $PIDFILE
echo "Stopping $NAME finish."
;;
esac
exit 0
