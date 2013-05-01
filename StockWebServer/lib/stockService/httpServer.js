var http     = require("http");
var fs       = require("fs");
var io       = require("socket.io");
var channel  = require("./channel");

function HttpServer(){
    this.init=function(app){
        this.httpServer = http.createServer(app);
        this.io = io.listen(this.httpServer);
        this.io.sockets.on("connection", function(socket){
            console.log('now http client is connecting!');
             channel.getInstance().addEventListeners(socket);
//            emitter.on('message', function (msg) {
//                console.log('[socket] send message to client:' + msg);
//                socket.broadcast.emit('stock', msg);
//
//            });
        });
    };

    this.start=function(app){
        this.init(app);
        this.httpServer.listen(app.get('port'));
        console.log('now http server listen port['+app.get('port')+']!');
    }
}

exports = module.exports =HttpServer;
