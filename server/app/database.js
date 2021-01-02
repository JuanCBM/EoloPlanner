const mysql = require('mysql2/promise');
const { Sequelize, DataTypes } = require('sequelize');
const database = "eoloplant";
const plantRoutes = require('../routes/plantRoutes');
const plantModel = require('../models/plant');
const queue = require('./queue');

async function initialize(app) {
    // create db if it doesn't already exist
    const connection = await mysql.createConnection({ host: "localhost", port: "3306", user: "root", password: "pass" });
    await connection.query(`CREATE DATABASE IF NOT EXISTS \`${database}\`;`);

    // connect to db
    const sequelize = new Sequelize(database, "root", "pass", { dialect: 'mysql' });

    // init models and add them to the exported db object
    let Plant = plantModel(sequelize, DataTypes);

    // Plant routes
    plantRoutes(app, Plant, queue);

    // sync all models with database
    await sequelize.sync();
}
module.exports.initialize = initialize;