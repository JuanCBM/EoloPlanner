const grpc = require('grpc');
const protoLoader = require('@grpc/proto-loader');

var packageDefinition = protoLoader.loadSync(__dirname + '/../WeatherService.proto',
    {
        keepCase: true,
        longs: String,
        enums: String,
        defaults: true,
        oneofs: true
    });

var weatherServiceProto = grpc.loadPackageDefinition(packageDefinition);

module.exports = weatherServiceProto.com.protocolos.eoloplanner.WeatherService;