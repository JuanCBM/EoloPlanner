let id = 0;

function createUniqueId() {
    id++;
    return id;
}

let users = [];

async function initialize(app, wss) {

    app.ws('/plantNotifications', function (ws, req) {
        let newId = createUniqueId();
        ws.id = newId;

        wss.clients.forEach(function (client) {
            if (client.id === id) {
                console.log('Enviamos mensaje a Client:' + client.id);
                client.send(`{"socketId": ${id}}`);
            }
        });

        ws.on('close', function (ws) {
            const removeIndex = users.indexOf(ws);
            users.splice(removeIndex, 1);
        });
    });
}

module.exports.initialize = initialize;