var http     = require("http");
var fs       = require("fs");
var io       = require("socket.io");
var channel  = require("./channel");
var log=require('./logger').getLogger("system");

function HttpServer(){
    this.init=function(app){
        this.httpServer = http.createServer(app);
        this.io = io.listen(this.httpServer);
        this.io.sockets.on("connection", function(socket){
            log.info('now http client is connecting!');
//            console.log('now http client is connecting!');
             channel.getInstance().addEventListeners(socket);
        });
    };

    this.start=function(app){
        this.init(app);
        this.httpServer.listen(app.get('port'));
        log.info('now http server listen port['+app.get('port')+']!');
//        console.log('now http server listen port['+app.get('port')+']!');
    }
}

exports = module.exports =HttpServer;
