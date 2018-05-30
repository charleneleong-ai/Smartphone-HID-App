//new  moduleimport
var express = require('express');
var http = require('http');
var socketIo = require('socket.io');

var port = process.env.PORT || 3000;
// var index = require("./routes/index");

//new express instace
var app = express();
// app.use(index);
//new http server instance
var server = http.Server(app);
//capture instance of socket io
var io = socketIo(server);

//serve simple index.html
app.get('/',function(req,res){
    res.sendFile(__dirname+'/index.html');
})

//add connect listener
io.on("connection", socket => {
  console.log('one user connected '+socket.id);

socket.on('accelerometer', function(data){
  setInterval(function(){
    socket.broadcast.emit('accelerometer', data);
    console.log("Accelerometer Server: "+data);
  }, 10000);
});

  socket.on('disconnect',function(){
      console.log('one user disconnected '+socket.id);
  });
});



//open server for listening
server.listen(port, () => console.log('Listening on port '+port));

// //upon connection
// io.on('connection', function(socket) {
//   console.log('one user connected '+socket.id);
//   //emit event to client
//   socket.emit('hello', {
//     greeting: 'Hello Paul'
//   });
// });
