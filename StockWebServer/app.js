
/**
 * Module dependencies.
 */
var express = require('express')
  , routes = require('./routes')
  , stock = require('./routes/stock')
  , company = require('./routes/companyList')
  , path = require('path')
  , http = require('http')
 , StockService = require('./lib/stockService');


//端口属性配置
var port=8888;
var netCLientConfig = {port: 9999,ip: '127.0.0.1'};

var app = express();
app.configure(function(){
// all environments
app.set('port', port);
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

});

// Routes
app.get('/', routes.index);
app.get('/stockinfo/:stock', stock.stockList);
app.get('/companyList', company.companyList);

stockService=new StockService();
if (!module.parent) {
    stockService.runHttpServer(app);
    stockService.runNetClient(netCLientConfig);
        console.log("Express server listening on port %d in %s mode", app.get('port'),
            app.settings.env);
}


exports.startApp=function(httpPort,netClientConfig){
    app.set('port', httpPort||8888);
    var config=netClientConfig||{port: 9999,ip: '127.0.0.1'};
    stockService.runHttpServer(app);
    stockService.runNetClient(netCLientConfig);
    console.log("Express server listening on port %d in %s mode", app.get('port'),
        app.settings.env);
};



