执行以下命令获得源代码：git clone git://github.com/zhaoyj3/StockInfoServer.git；

本系统股市模拟系统，分为两个子系统：StockInfoServer股市行情服务器和StockWebServer股市Web服务器，见同名两个目录。


--------------------------------------------------
特别注意：

由于时间比较仓促，git获得源码测试没能在linux环境进行。
在window环境中，git下载的的脚本文件是不能在unix环境执行的。
会出现类似以下代码：
/bin/sh^M: bad interpreter:
说明文件的换行符是windows的换行符，unix不能识别。
解决办法是：用editplus等编辑软件把文档换行格式转换成unix的格式即可。

或者

从我发邮件的邮件附件中替换原脚本文件即可。
-----------------------------------------------------------------------


基于安装环境的不同，安装文档分为Windows版和Unix版本。


若你的系统支持office,请看readme/Windows文件夹的安装手册文件，按如下步骤执行：

1、安装StockWebServer服务器,  参考《StockInfoServer1.0.0安装手册.doc》的第三章软件安装；

2、安装StockInfoServer服务器,  参考《StockWebServer1.0.0安装手册.doc》的第三章软件安装；

3、启动StockInfoServer服务器,参考《StockInfoServer1.0.0安装手册.doc》的第四章4.1节运行及终止程序；

4、启动StockWebServer服务器,参考《StockWebServer1.0.0安装手册.doc》的第四章4.1 节运行及终止程序；

5、启动完成！去Chrome浏览器看看,默认地址是http://localhost:8888/


若你的系统不支持office,请看readme/Unix文件夹的安装手册文件，按如下步骤执行：

1、安装StockWebServer服务器,  参考《StockInfoServer1.0.0_install.txt》的第三章软件安装；

2、安装StockInfoServer服务器,  参考《StockWebServer1.0.0_install.txt》的第三章软件安装；

3、启动StockInfoServer服务器,参考《StockInfoServer1.0.0_install.txt》的第四章4.1节运行及终止程序；

4、启动StockWebServer服务器,参考《StockWebServer1.0.0_install.txt》的第四章4.1 节运行及终止程序；

5、启动完成！去Chrome浏览器看看,默认地址是http://localhost:8888/