var db = require('../db');
var log = require('../logger')(module);

function User(name) {
  this.name = name;
}

User.prototype.hello = function(who) {
  console.log(db.getPhrase("Hello")
  + ", "  + who.name);
};

module.exports = User;
