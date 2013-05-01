#!/bin/sh

DEPLIBPATH=../lib

CLASSPATH=.:../config:$DEPLIBPATH/aidmlib-monitor-1.0.0.jar:$DEPLIBPATH/aidmlib-web-1.0.0.jar:$DEPLIBPATH/jmxtools.jar:$DEPLIBPATH/commons-beanutils.jar:$DEPLIBPATH/commons-collections-3.1.jar:$DEPLIBPATH/commons-lang-2.4.jar:$DEPLIBPATH/commons-logging-1.1.1.jar:$DEPLIBPATH/log4j-1.2.15.jar:$DEPLIBPATH/dom4j-1.6.1.jar:$DEPLIBPATH/xstream-1.3.1.jar:$DEPLIBPATH/ezmorph-1.0.6.jar:$DEPLIBPATH/json-lib-2.4-jdk15.jar:StockInfoServer.jar

java -Xms512M -Xmx2048M -cp $CLASSPATH com.guangfa.stockinfoserver.AppServer runAsDaemon &

