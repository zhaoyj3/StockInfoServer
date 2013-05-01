var events = require('events');

var emitter = new events.EventEmitter();
var eventName="publish";
var isReady=false;
var connectCount=0;
var channel=new Channel();
function Channel(){
  this.addEventListeners=function(socket){
      connectCount++;
      var session_id = socket.id;
      socket.on("message",function(data){
          console.log("Received: " + data);
      });
      socket.on("disconnect",function(){
          connectCount--;
          console.log("client["+session_id+"] disconnect!");
      });
        emitter.on(eventName, function (msg) {
            console.log('[socket] send message to client:' + msg);
            if(connectCount>1){
            socket.broadcast.emit('stock', msg);
            }else{
             socket.emit('stock', msg);
            }
        });
      isReady=true;
  };
}

exports.publish=function(msg){
    if(isReady){
     emitter.emit(eventName,msg);
    }else{
     //console.log('listener is not ready!');
    }
  };

exports.getInstance=function(){
   return  channel;
};




