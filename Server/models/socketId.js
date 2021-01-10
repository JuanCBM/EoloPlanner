var SocketIds = module.exports = {
  array: [],
  add: function (city, client) {
    SocketIds.array.push({city: city, client: client});
  },
  remove: function (city) {
    //TODO: Remove from array in a best way
    SocketIds.array = SocketIds.array.filter(item => item.city !== city);
    for (let i = 0; i < SocketIds.array.length; i++) {
      if (SocketIds.array[i].city === city) {
        SocketIds.array.splice(i, 1);
      }
    }
  },
  getClient: function (city) {
    return SocketIds.array.filter(item => item.city === city)[0];
  }
}