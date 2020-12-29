let express = require('express');
let bodyParser = require('body-parser');
let app = express();
let expressWs = require('express-ws')(app);
const PORT = 3000;

const db = require('./app/database');
const socket = require('./app/server');
const queue = require('./app/queue');

let wss = expressWs.getWss('/notifications');

db.initialize(app);
socket.initialize(app);
queue.initialize(wss)

app.use(express.static('public'));
app.use(bodyParser.json());

app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`));