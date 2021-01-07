const amqp = require('amqplib/callback_api');
const db = require('../app/database');
const CONN_URL = 'amqp://guest:guest@localhost';

const socketIds = require('../models/socketId');


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

            channel.consume(notificationsQueue, async function (msg) {
                console.log("Message:", msg.content.toString());
                let plant = JSON.parse(msg.content.toString());
                await updateDatabase(JSON.parse(msg.content));
                    //if (plant.progress === 100) {
                        wss.clients.forEach(function (client) {
                            //if (client.id === socketIds.getClient(plant.id).client) {
                                console.log('Client:' + client);
                                client.send(msg.content.toString());
                            //}
                        });
                    //} else {
                        // let ws = await wss.clients.get(plant.id);
                        // if (ws) ws.send(plant);
                    //}

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
    console.log("publishToQueue: '" + message + "'");
    creationChannel.sendToQueue(createPlantRequestQueue, Buffer.from(message));
};

function updateDatabase(plantInfo) {
    db.Plant.update(
        {progress: plantInfo.progress, planning: plantInfo.planning},
        {where: {id: plantInfo.id}})
}

module.exports.initialize = initialize;
module.exports.sendMessageToQueue = sendMessageToQueue;