const amqp = require('amqplib/callback_api');

const CONN_URL = 'amqp://guest:guest@localhost';

const createPlantRequestQueue = 'plantCreationRequests';
const notificationsQueue = 'plantCreationProgressNotifications';

let notificationChannel = null;
let creationChannel = null;

async function initialize(wss) {
    amqp.connect(CONN_URL, async function (err, conn) {

        console.log("Conection with RabbitMQ");
        notificationChannel = await conn.createChannel(function (error, channel) {
            if (error) {
                throw error;
            }
            channel.assertQueue(notificationsQueue, {
                durable: true
            });

            channel.consume(notificationsQueue, function (msg) {

                console.log("Message:", msg.content.toString());
                wss.clients.forEach(function (client) {
                    console.log('Client:' + client);
                    client.send(msg.content.toString());
                });

            }, { noAck: true }
            );
        });

        creationChannel = await conn.createChannel(function (error, channel) {
            if (error) {
                throw error;
            }
            channel.assertQueue(createPlantRequestQueue, {
                durable: true
            });
        });
    });
}

process.on('exit', (code) => {
    notificationChannel.close();
    creationChannel.close();
    console.log(`Closing rabbitmq channel`);
});



const sendMessageToQueue = (message) => {

    console.log("publishToQueue: '" + message + "'");
    creationChannel.sendMessageToQueue(createPlantRequestQueue, Buffer.from(message));
};

module.exports.initialize = initialize;
module.exports.sendMessageToQueue = sendMessageToQueue;