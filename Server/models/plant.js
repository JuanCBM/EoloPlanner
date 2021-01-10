module.exports = (sequelize, Sequelize) => {
  return sequelize.define('plant', {
    id: {
      primaryKey: true,
      type: Sequelize.INTEGER,
      autoIncrement: true
    },
    city: {
      type: Sequelize.STRING,
      allowNull: false
    },
    progress: {
      type: Sequelize.INTEGER
    },
    planning: {
      type: Sequelize.STRING
    },
  });
}