module.exports = (sequelize, DataTypes) => {
    return sequelize.define('plant', {
        id: {
            primaryKey: true,
            type: DataTypes.INTEGER,
            autoIncrement: true
        },
        city: {
            type: DataTypes.STRING,
            allowNull: false
        },
        progress: {
            type: DataTypes.INTEGER,
        }
    });
}