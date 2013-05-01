#!/bin/bash

DEPLIBPATH=../lib

CLASSPATH=.:../config:$DEPLIBPATH/aidmlib-monitor-1.0.0.jar:$DEPLIBPATH/aidmlib-web-1.0.0.jar:$DEPLIBPATH/jmxtools.jar:$DEPLIBPATH/commons-beanutils.jar:$DEPLIBPATH/commons-collections-3.1.jar:$DEPLIBPATH/commons-lang-2.4.jar:$DEPLIBPATH/commons-logging-1.1.1.jar:$DEPLIBPATH/log4j-1.2.15.jar:$DEPLIBPATH/dom4j-1.6.1.jar:$DEPLIBPATH/xstream-1.3.1.jar:$DEPLIBPATH/ezmorph-1.0.6.jar:$DEPLIBPATH/json-lib-2.4-jdk15.jar:StockInfoServer.jar


doClose=n
echo -n "Do you really want to shutdown the application?(y/n) "
read doClose


if [ "$doClose" == "y" ]; then
        echo "Closing application..."
        # call the monitor's consoleClient
java -Dmonitor -cp $CLASSPATH com.asiainfo.dm.common.monitor.ConsoleClient -s
else
        echo "Did not close & Quit"
fi

