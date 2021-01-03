let express = require('express');
let bodyParser = require('body-parser');
let app = express();
let expressWs = require('express-ws')(app);
const PORT = 3000;

const db = require('./app/database');
const server = require('./app/server');
const queue = require('./app/queue');

let wss = expressWs.getWss('/notifications');

db.initialize(app);
server.initialize(app, wss);
queue.initialize(wss)

app.use(express.static('client'));
app.use(bodyParser.json());

app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`));