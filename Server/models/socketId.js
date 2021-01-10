let SocketIds = module.exports = {
  array: [],
  add: function (city, client) {
    SocketIds.array.push({city: city, client: client});
  }
}