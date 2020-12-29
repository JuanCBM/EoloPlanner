async function initialize(app) {

    app.ws('/plantNotifications', function (ws, req) { });
}
module.exports.initialize = initialize;