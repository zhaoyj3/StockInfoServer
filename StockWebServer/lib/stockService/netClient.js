var net = require("net");
var channel  = require("./channel");
var log=require('./logger').getLogger("system");

var conf={};
function NetClient(){
    this.init=function(callback){
        var connectListener=function(){
            log.info('now connect server success!');
//            console.log('now connect server success!');
            self.write('request send data now!\r\n');
            self.on('data', function (data) {
                //java的writeUTF发送过来的数据,多两个字节,是数据的长度
                  channel.publish(data.substring(2));
                  log.debug('recv data:' + data.substring(2));
//                console.log('recv data:' + data.substring(2));

            });
        };
        connectListener=this.connectListener=callback||connectListener;
        var self=this.client = new net.Socket();
        this.client.setEncoding('utf8');
        this.client.on('error',  function (error) {
            if (error.code = 'ECONNREFUSED') {
                setTimeout(function () {
                    //清除之前的连接监听事件
                    self.removeListener("connect",connectListener);
                    //重新请求连接
                    self.connect(conf.port, conf.ip, connectListener);
                }, 1000);

            } else {
                log.error('socket connect occur error:',error);
//                console.log('error:' + error);
            }

        });
        this.client.on('close', function () {
            log.debug('Connection closed');
//            console.log('Connection closed');
        });

    };

    this.start=function(callback){
        this.init(callback);
        this.client.connect(conf.port,conf.ip, this.connectListener);
        log.info('client try to connect stockServer['+conf.port+':'+conf.ip+']');
    } ;
}

//exports = module.exports =NetClient;
exports.init=function(config){
    conf.port= parseInt(config.port || 9999);
    conf.ip = config.ip ||'127.0.0.1';
};
exports.NetClient=NetClient;
