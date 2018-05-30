//new  moduleimport
var express = require('express');
var http = require('http');
var socketIo = require('socket.io');
var path = require('path');
var fs = require('fs');
var ss = require('socket.io-stream');
//new express instace
var app = express();
//new http server instance
var server = http.Server(app);
//capture instance of socket io
var io = socketIo(server);

//serve simple index.html
app.get('/',function(req,res){
    res.sendFile(__dirname+'/index.html');
});

//add connect listener
io.on('connection',function(socket){
  //client connected
    console.log('one user connected '+socket.id);
ss(socket).on('file', (stream, data) =>{
  stream.pipe(fs.createWriteStream( Date.now() +'.txt'));
})

    // socket.on('accelerometer', function(data){
    //   //emit event to client
    //     socket.emit('accelerometer', data);
    //     console.log("Accelerometer: "+data);
    // });

    socket.on('disconnect',function(){
        console.log('one user disconnected '+socket.id);
    });
});

//open server for listening
server.listen(3000, function(){
    console.log('server listening on port 3000');
});



// //upon connection
// io.on('connection', function(socket) {
//   console.log('one user connected '+socket.id);
//   //emit event to client
//   socket.emit('hello', {
//     greeting: 'Hello Paul'
//   });
// });
