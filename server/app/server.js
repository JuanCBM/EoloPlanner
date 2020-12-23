var express = require('express');
var app = express();

var expressWs = require('express-ws')(app);
const uuidv1 = require('uuid/v1');

var amqp = require('amqplib');

const CONN_URL = 'amqp://guest:guest@localhost';
const QUEUE_NAME = 'plantsProgress';

var sendToQueue = require('./producer').sendMessageToQueue;

var id, websocket, plantProgress;
var plantCompleted = true;

app.use(function (req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header(
        'Access-Control-Allow-Headers',
        'Origin, X-Requested-With, Content-Type, Accept',
    );
    next();
});
app.use(express.json());

app.post('/plants', function (req, res) {
    var text = req.body.text;
    console.log('/plant endpoint executed');
    console.log('Executing plant with text: ', text);

    if (plantCompleted) {
        id = uuidv1();

        res.json({ id: id, text: text, progress: 0, completed: false });

        sendToQueue(text);
    } else {
        res.send('plant is not completed. Wait until complete');
    }
    res.end();
});

app.get('/plants/:id', (req, res, next) => {
    console.log('/test/id endpoint executed');
    var idParam = req.params.id;
    if (id === idParam) {
        res.json({ progress: plantProgress, id: id, completed: plantCompleted });
    } else {
        res.json({ message: 'PLANT NOT FOUND' });
    }
    res.end();
});

app.ws('/allPlants', async (ws, req) => {
    console.log('User connected');
    //Aqui metemos todo eltema de websocket con la consiguiente grabada en l mySql
    websocket = ws;
});

function connectToRabbitMQ() {
    amqp.connect(CONN_URL).then(conn => {
        conn.createChannel().then(ch => {
            ch.consume(QUEUE_NAME, msg => {
                if (msg !== null) {
                    console.log(msg.content.toString());
                    let content = JSON.parse(msg.content);
                    plantCompleted = content.completed;
                    plantProgress = content.progress;
                    if (websocket != null) {
                        if (plantCompleted) {
                            websocket.send(content.result);
                            websocket = null
                        } else {
                            websocket.send(plantProgress + '%');
                        }
                    }
                }
            }, { noAck: true }
            );
        }).catch(err => reject(err));
    });
}

app.listen(8080, () => {
    connectToRabbitMQ();

    console.log('......................');
    console.log('Listening in port 8080');
});