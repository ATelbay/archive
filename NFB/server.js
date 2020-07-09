var db = require('./db');
var log = require('./logger')(module);
db.connect();

var User = require('./user');

function run() {
  var vasya = new User("Vasya");
  var petya = new User("Petya");
  vasya.hello(petya);

  console.log(db.getPhrase("Run successful"));
}

if (module.parent) {
  exports.run = run;
} else {
  run();
}
