//app.listen(process.env.PORT, process.env.IP);

var http = require('http');
var express = require('express');
var socket = require('socket.io');

var app = express();

var server = http.createServer(app).listen((process.env.PORT), function(){
  console.log("Express server on");
});

var io = socket.listen(server);

console.log("Express server listening on port %d", server.address().port)

app.get('/', function (req, res) {
  res.end('hello');
});

io.sockets.on('connection', function(socket) {
  socket.on('echo', function(data, callback) {
    //socket.emit('echo back', data);
    callback(data);
  });

    socket.on('message', function(data) {
        console.log(data)
        socket.emit('myevetnt', data);
    });

});
