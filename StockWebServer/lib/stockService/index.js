var HttpServer = require("./httpServer");
var netClient  = require("./netClient");

function StockService(){
  this.runHttpServer=function(app){
      server=new HttpServer();
      server.start(app);
  };
    this.runNetClient=function(config){
        netClient.init(config);
        client=new netClient.NetClient();
        //client=netClient.getInstance();
        client.start();
    };
}

exports = module.exports =StockService;


