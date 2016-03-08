'use strict';

/**
 * @ngdoc function
 * @name shortifyClientApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the shortifyClientApp
 */
angular.module('shortifyClientApp')
  .controller('AboutCtrl', function ($window, $http, $routeParams) {
    $http.get('http://localhost:4567/' + $routeParams.id )
      .then(function(response) {$window.location.href = response.data.redirectURL;})   // success
      .catch(function() {$window.location.href = '/';}); // error
  });
