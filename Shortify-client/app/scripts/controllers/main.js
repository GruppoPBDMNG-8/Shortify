'use strict';

/**
 * @ngdoc function
 * @name shortifyClientApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the shortifyClientApp
 */
angular.module('shortifyClientApp')
  .controller('MainCtrl',['$scope','$routeParams','$http','deviceDetector', function ($scope,$routeParams, $http, deviceDetector ) {
    var Userdata = deviceDetector;
    $scope.showAlerts = false;
    $scope.showSuccess = false;
    $scope.longURL = '';
    $scope.customURL = '';

    $scope.createURL = function() {
      $scope.showAlerts = false;
      $scope.showSuccess = false;
      if ($scope.myForm.longURL.$valid && $scope.myForm.customURL.$valid ) {

      $http.post('http://localhost:4567/inserturl',
        {
          longURL: $scope.longURL,
          customURL: $scope.customURL,
          userAgent: Userdata.browser,
          os: Userdata.os
        })
        .then(function (response) {
          $scope.showSuccess = true;
          $scope.successMessage = response.data;
        })   // success
        .catch(function (response) {
          $scope.showAlerts = true;
          $scope.alertMessage = response.data.error;
        }); // error
      }
      };
  }])
  .directive('httpPrefix', function() {
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function(scope, element, attrs, controller) {
      function ensureHttpPrefix(value) {
        // Need to add prefix if we don't have http:// prefix already AND we don't have part of it
        if(value && !/^(https?):\/\//i.test(value) && 'http://'.indexOf(value) !== 0 && 'https://'.indexOf(value) !== 0 ) {
          controller.$setViewValue('http://' + value);
          controller.$render();
          return 'http://' + value;
        }
        else {
          return value;
        }
      }
      controller.$formatters.push(ensureHttpPrefix);
      controller.$parsers.splice(0, 0, ensureHttpPrefix);
    }
  };
});
