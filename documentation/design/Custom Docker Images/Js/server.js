var http = require('http');
var fs = require('fs');
var path = require('path');

const PORT=8080; 

console.log('Server is starting........');
let handleRequest = (request, response) => 
{
	// Mime Content Type Map
  	const ctmap = 
  	{
    	'.ico': 'image/x-icon',
    	'.html': 'text/html',
    	'.js': 'text/javascript',
    	'.json': 'application/json',
    	'.css': 'text/css',
    	'.png': 'image/png',
    	'.jpg': 'image/jpeg',
    	'.gif': 'image/gif',
    	'.wav': 'audio/wav',
    	'.mp3': 'audio/mpeg',
    	'.svg': 'image/svg+xml',
    	'.pdf': 'application/pdf',
    	'.doc': 'application/msword'
  	};
  
  	// Resolve Request File, Path, and File Extension
    var filePath = request.url;
    if (filePath == '/')
    {
        filePath = __dirname + '/index.html';
    }
    else
    {
    	filePath = __dirname + filePath;	
    }
    var extname = path.extname(filePath);
	// console.log('Serving file........' + filePath + ' with Content-type of ' + ctmap[extname]);

	// Serve up the requested file
    response.writeHead(200, { 'Content-Type': + ctmap[extname] });
    fs.readFile(filePath, null, function (error, data) 
    {
        if (error) 
        {
            response.writeHead(404);
            response.write('File not found: ' + filePath);
        } 
        else 
        {
            response.write(data);
        }
        response.end();
    });
};

http.createServer(handleRequest).listen(PORT);
console.log('Server is running on port ' + PORT);
