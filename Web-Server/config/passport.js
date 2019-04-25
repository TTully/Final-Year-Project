var passport = require('passport');
var User = require('../models/user');
var LocalStrategy = require('passport-local').Strategy;

// We store the user in the Session by using user.id
passport.serializeUser(function(user, done) {
  done(null, user.id);
});

passport.deserializeUser(function(id, done) {
  User.findById(id, function(err, user) {
    done(err, user);
  });
});

// Create new User
passport.use('local.signup', new LocalStrategy({
  usernameField: 'username',
  passwordField: 'password',
  passReqToCallback: true

}, function(req, username, password, done) {
  //Validate the passed username and passwordField
  req.checkBody('username', 'Invalid username').notEmpty().isLength({min:5});
  req.checkBody('password', 'Invalid password').notEmpty().isLength({min:6});
  var errors = req.validationErrors();
  if (errors) {
    var messages = [];
    errors.forEach(function(error) {
      messages.push(error.msg);
    });
    return done(null, false, req.flash('error', messages))
  }

  // find a user by username
  User.findOne({'username': username}, function(err, user) {
    if (err) {
      return done(err);
    }
    if (user) {
      return done(null, false, {message: 'Username is already in use'});
    }
    var newUser = new User();
    newUser.username = username;
    newUser.password = newUser.encryptPassword(password);
    newUser.save(function(err, result) {
      if (err) {
        return done(err);
      }
      return done (null, newUser);
    });
  });
}));

// Create new User
passport.use('local.signin', new LocalStrategy({
  usernameField: 'username',
  passwordField: 'password',
  passReqToCallback: true

}, function(req, username, password, done) {
  //Validate the passed username and passwordField
  req.checkBody('username', 'Invalid username').notEmpty();
  req.checkBody('password', 'Invalid password').notEmpty();
  var errors = req.validationErrors();
  if (errors) {
    var messages = [];
    errors.forEach(function(error) {
      messages.push(error.msg);
    });
    return done(null, false, req.flash('error', messages))
  }

    // find a user by username
    User.findOne({'username': username}, function(err, user) {
      if (err) {
        return done(err);
      }
      if (!user) {
        return done(null, false, {message: 'No User Found!'});
      }
      if (!user.validPassword(password)) {
        return done(null, false, {message: 'Wrong password.'})
      }
        return done (null, user);
      });
}));
