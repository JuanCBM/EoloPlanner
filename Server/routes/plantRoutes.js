const socketIds = require('../models/socketId');
const db = require("../app/database");
const Plant = db.plants;

module.exports = (app, queue) => {

  app.get("/api/eoloplants/", (req, res) =>
      Plant.findAll().then((result) => res.json(result))
      .catch(function (err) {
            res.status(500).json({message: err.message})
      })
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