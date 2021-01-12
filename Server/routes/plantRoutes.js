const socketIds = require('../models/socketId');
const db = require("../app/database");
const Plant = db.plants;
// TODO: [Correccion] Podria ser necesario introducir un servicio que realice las operaciones.
module.exports = (app, queue) => {

  app.get("/api/eoloplants/", (req, res) =>
      Plant.findAll().then((result) => res.json(result))
      .catch(function (err) {
            res.status(500).json({message: err.message})
      })
  );

  app.get("/api/eoloplants/:id", (req, res) =>
      Plant.findOne({
        where: {
          id: req.params.id
        }
      }).then(function (result) {
        if (!result) {
          res.status(404).send("Not found eolicplant at id:" + req.params.id);
        } else {
          res.json(result).status(200);
        }
      })
  );

  app.delete("/api/eoloplants/:id", (req, res) =>
      Plant.destroy({
        where: {
          id: req.params.id
        }
      }).then((result) => res.json(result))
  );

  app.post("/api/eoloplants/", (req, res) =>
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
        res.status(404).json({message: err.message})
      })
  );

}