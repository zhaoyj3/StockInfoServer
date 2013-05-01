var log4js = require('log4js');
log4js.configure('log4js.json', {
	reloadSecs: 300
});

exports.getLogger=function(category){
	return log4js.getLogger(category);
	
}