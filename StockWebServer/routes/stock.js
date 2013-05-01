var netClient  = require("../lib/stockService/netClient");
/*
 * GET home page.
 */

exports.stockList = function(req, res){
    var stockName=req.param("stock");
    socket=new netClient.NetClient();
    //socket=netClient.getInstance();
    socket.start(function(){
        var buffer = new Buffer(0);
        console.log('now connect server success!');
        socket.client.on('data', function (data) {

//            res.writeHead(200, {'Content-Type': 'text/plain'});
            //java的writeUTF发送过来的数据,多两个字节,是数据的长度
            //res.end(data.substring(2));

            buffer = buffer_add(buffer,data);
            if (buffer_find_body(buffer) == -1) return;
            socket.client.write('quit!\r\n');
            var msg=buffer.toString('utf8',0,(buffer.length-4));
            res.end(msg);
            console.log('recvice stock data:' +msg);
        });
        socket.client.write('/stock/'+stockName+'\r\n');
    });
    console.log("req param is :"+stockName);
};

/**
 * 两个buffer对象加起来
 */
function buffer_add(buf1,data)
{
    var re = new Buffer(buf1.length + data.length);
    buf1.copy(re);
    re.write(data,buf1.length);
    return re;
}

/**
 * 从缓存中找到结束标记("\r\n\r\n")的位置
 */
function buffer_find_body(b)
{
    for(var i=0,len=b.length-3;i<len;i++)
    {
        if (b[i] == 0x0d && b[i+1] == 0x0a && b[i+2] == 0x0d && b[i+3] == 0x0a)
        {
            return i+4;
        }
    }
    return -1;
}