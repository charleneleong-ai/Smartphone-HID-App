//new  moduleimport
var express = require('express'),
    http = require('http'),
    socketIo = require('socket.io'),
    port = process.env.PORT || 3000,
    app = express(),
    server = http.Server(app),
    io = socketIo(server),
    path = require('path');


//serve simple index.html
// app.get('/',function(req,res){
//     res.sendFile(__dirname+'/public/index.html');
// })
// app.use(express.static(path.join(__dirname, 'public')));
app.use(express.static(path.join(__dirname, 'public')));

//add connect listener
io.on("connection", socket => {
  console.log('one user connected '+socket.id);

socket.on('orientation', function(data){
  // setInterval(function(){
    socket.broadcast.emit('orientation', data);
    // console.log(typeof(data.x));
    console.log("Orientation Server: xValue: "+ data.x+" yValue: "+data.y+" zValue: "+data.z);
  // }, 1000);
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
