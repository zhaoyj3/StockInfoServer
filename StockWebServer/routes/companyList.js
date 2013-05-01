var netClient  = require("../lib/stockService/netClient");
var log=require('../lib/stockService/logger').getLogger("system");
/*
 * GET home page.
 */

exports.companyList = function(req, res){
    socket=new netClient.NetClient();
    //socket=netClient.getInstance();
    socket.start(function(){
        var buffer = new Buffer(0);
        log.info('now connect server success!');
//        console.log('now connect server success!');
        socket.client.on('data', function (data) {
            buffer = buffer_add(buffer,data);
            if (buffer_find_body(buffer) == -1) return;
            socket.client.write('quit!\r\n');
            var msg=buffer.toString('utf8',0,(buffer.length-4));
            res.end(msg);
            log.debug('recvice companyList data:' +msg);
//            console.log('recvice companyList data:' +msg);
        });
        socket.client.write('/companyList\r\n');
    });
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