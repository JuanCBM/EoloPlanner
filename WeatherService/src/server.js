const grpc = require('grpc');
const WeatherService = require('./interface');
const weatherServiceImpl = require('./weatherService');

const server = new grpc.Server();

server.addService(WeatherService.service, weatherServiceImpl);

server.bind('localhost:9090', grpc.ServerCredentials.createInsecure());

console.log('gRPC server running at http://localhost:9090');

server.start();