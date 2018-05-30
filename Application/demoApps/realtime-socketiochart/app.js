var express = require('express');
var app = require('express')();
var http = require('http').Server(app);
//creates a new socket.io instance attached to the http server.
var io = require('socket.io')(http);
//Include PubNub JavaScript SDK
var PubNub = require('pubnub');

//Provide the absolute path to the dist directory.
app.use(express.static('./dist'));

//On get request send 'index.html' page as a response.
app.get('/', function(req, res) {
   res.sendfile('index.html');
});

//Whenever someone connects this gets executed

io.on('connection', function (socket) {
    var strData;
//Instantiate a new Pubnub instance along with the subscribeKey
    pubnub = new PubNub({
        subscribeKey : 'sub-c-4377ab04-f100-11e3-bffd-02ee2ddab7fe'
    })
    //adding listener to pubnub
    pubnub.addListener({
        message: function(message) {
/*checking whether the message contains data for the ‘Apple’ category or not.*/
            if(message.message.symbol=='Apple'){
                        /*Creates a new date object from the specified message.timestamp.*/
                var x = new Date(message.message.timestamp);
//Converting the timestamp into a desired format. HH:MM:SS:MS
        var formatted =  (x.getHours()) + ':' + (x.getMinutes()) + ':' + (x.getSeconds()) + ':' + (x.getMilliseconds());
                       /*Here we are creating an object which will contain a timestamp as label and the ‘order_quantity’ as value.*/
                strData = {"label": formatted,
                           "value":message.message.order_quantity
                        }
                                               //sending data to the client
                socket.emit('news', strData);
            };
        }
    })
    console.log("Subscribing..");
//Subscribe the PubNub channel
    pubnub.subscribe({
        channels: ['pubnub-market-orders']
    });
});
//server listening on port 3000
http.listen(3000, function() {
   console.log('listening on *:3000');
});
