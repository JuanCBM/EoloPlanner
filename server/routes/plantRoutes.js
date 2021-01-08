const socketIds = require('../models/socketId');

module.exports = (app, Plant, queue) => {

  app.get("/plants/", (req, res) =>
      Plant.findAll().then((result) => res.json(result))
  );

  app.post("/plants/", (req, res) =>
      Plant.create({
        city: req.body.city
      }).then((result) => {
        res.json({
          id: result.id,
          city: result.city,
          progress: 0,
          completed: false,
          planning: null
        });
        socketIds.add(result.id, req.headers.socketid);
        queue.sendMessageToQueue(
            JSON.stringify({id: result.id, city: result.city}));
      }).catch(function (err) {
        console.log(err);
        //TODO: Cambiar el error o vercomo lo solventamos
        res.json(err);
      })
  );

  // TODO: DELETE???
  app.delete("/plants/:city", (req, res) =>
      Plant.destroy({
        where: {
          city: req.params.city
        }
      }).then((result) => res.json(result))
  );
}