let express = require('express');
let bodyParser = require('body-parser');
let app = express();
let expressWs = require('express-ws')(app);
const PORT = 3000;

const plantRoutes = require('./routes/plantRoutes');
const server = require('./app/server');
const queue = require('./app/queue');
const db = require("./app/database");

let wss = expressWs.getWss('/notifications');

db.sequelize.sync({force: true}).then(() => {
  console.log("Drop and re-sync db.");
});

server.initialize(app, wss);
queue.initialize(wss)

app.use(express.static('client'));
app.use(bodyParser.json());

app.listen(PORT, () => console.log(`Server listening on port: ${PORT}`));
plantRoutes(app, queue);