var express = require('express');
var router = express.Router();
var csrf = require('csurf');
var passport = require('passport');

var csrfProtection = csrf();
router.use(csrfProtection);

/* GET home page. */
router.get('/', isLoggedIn, function(req, res, next) {
  res.render('index');
});

router.get('/logout', isLoggedIn, function(req, res, next) {
  req.logout();
  res.redirect('/user/signin');
});

/*
router.get('/user/signup', function(req, res, next) {
  var messages = req.flash('error');
  res.render('user/signup', {csrfToken: req.csrfToken(), messages: messages, hasErrors: messages.length > 0 });
});
*/

router.get('/user/signin', function(req, res, next) {
  var messages = req.flash('error');
  res.render('user/signin', {csrfToken: req.csrfToken(), messages: messages, hasErrors: messages.length > 0 });
});

/*
router.post('/user/signup', passport.authenticate('local.signup', {
  successRedirect: '/',
  failureRedirect: '/user/signup',
  failureFlash: true
}));
*/

router.post('/user/signin', passport.authenticate('local.signin', {
  successRedirect: '/',
  failureRedirect: '/user/signin',
  failureFlash: true
}));

module.exports = router;

function isLoggedIn(req, res, next) {
  if (req.isAuthenticated()) {
     return next();
  }
  res.redirect('/user/signin')
}
