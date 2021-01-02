const amqp = require('amqplib/callback_api');

const CONN_URL = 'amqp://guest:guest@localhost';

const createPlantRequestQueue = 'eoloplantCreationRequests';
const notificationsQueue = 'eoloplantCreationProgressNotifications';

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
                durable: false
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
                durable: false
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
    let opts = { headers: { 'Content-Type': 'application/json'}};

    console.log("publishToQueue: '" + message + "'");
    creationChannel.sendToQueue(createPlantRequestQueue, Buffer.from(JSON.stringify(message)), opts);
};

module.exports.initialize = initialize;
module.exports.sendMessageToQueue = sendMessageToQueue;