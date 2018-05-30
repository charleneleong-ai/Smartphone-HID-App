var express = require('express');
var socket = require('socket.io');

//App setup
var app = express();
var server = app.listen(3000, function(){
  console.log('listening to port 3000');
})

//static files
app.use(express.static('public'));

//Socket setup
var io = socket(server);

io.on('connection', function(socket){
  console.log('One user connected: ' + socket.id);
  socket.on('chat', function(data){
    io.sockets.emit('chat', data);
  });

});
