var express = require('express');
var http = require('http');
var socketio = require('socket.io');

var app = express();
var server = http.Server(app);
var websocket = socketio(server);
server.listen(3000, () => console.log('listening on *:3000'));

websocket.on('connection', function (socket) {
  console.log('A client just joined', socket.id);
  socket.on('update', () => io.emit('update'));
});

app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});
