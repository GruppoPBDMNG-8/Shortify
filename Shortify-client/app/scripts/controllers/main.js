'use strict';

/**
 * @ngdoc function
 * @name shortifyClientApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the shortifyClientApp
 */
angular.module('shortifyClientApp')
  .controller('MainCtrl',['$window','$scope','$routeParams','$http', function ($window, $scope,$routeParams, $http ) {

    const SERVER_ADDR = 'http://' + location.hostname + ':4567/';
    const loc = 'http://' + location.host + '/#/';

    $scope.exampleurlpath = loc + 'example+';
    $scope.urlpath = loc;
    $scope.showAlerts = false;
    $scope.showSuccess = false;
    $scope.showStats = false;
    $scope.showRedirectAlert = false;
    $scope.longURL = '';
    $scope.customURL = '';
    $scope.alertMessage = '';

    $scope.colours=['Blue','Red','Yellow'];


    //GET ROUTE FOR REDIRECT AND STATISTICS
    if ($routeParams.id){
      $http.get(SERVER_ADDR + $routeParams.id )
        .then(function(response) {//GET REDIRECT
          if(response.data.redirectURL){$window.location.href = response.data.redirectURL;}
          else {//GET STATISTICS
            $scope.showStats = true;
            $scope.realURL = response.data.longURL;
            $scope.creation = response.data.URLCreationDate;
            $scope.totalClicks = response.data.clicksSize;
            $scope.lastClick = response.data.LastClickDate + ' - ' + response.data.LastClickLocation;
            var UserAgentsNames = [];
            var UserAgentClicks = [];
            for (var key in response.data.UserAgentsClicks){
              var number = response.data.UserAgentsClicks[key];
              UserAgentsNames.push(key);
              UserAgentClicks.push(number);
            }
            $scope.labels1 =  UserAgentsNames;
            $scope.data1 = UserAgentClicks;

            var LocationNames = [];
            var LocationClicks = [];
            for (var key2 in response.data.UserLocationClicks){
              var number2 = response.data.UserLocationClicks[key2];
              LocationNames.push(key2);
              LocationClicks.push(number2);
            }
            $scope.labels2 =  LocationNames;
            $scope.data2 = LocationClicks;
          }
        }) // success
        .catch(function() {$window.location.href = '/';}); // ERROR
    }


    //POST ROUTE FOR NEW SHORT URL
    $scope.createURL = function() {
      $scope.showAlerts = false;
      $scope.showSuccess = false;
      $scope.showStats = false;

        if ($scope.myForm.longURL.$valid && $scope.myForm.customURL.$valid ) {
        $http.post(SERVER_ADDR + 'inserturl',
          {
            longURL: $scope.longURL,
            customURL: $scope.customURL
          })
          .then(function (response) {
            $scope.showSuccess = true;
            $scope.successMessage = loc + response.data.shortURL;
          })   // success
          .catch(function (response) {
            $scope.showAlerts = true;
            $scope.alertMessage = response.data.error;
          }); // error
          if($scope.alertMessage === ''){$scope.alertMessage = 'Server error, please try again';}
        }
      };
  }])
  //INSERT THE 'HTTP' STRING AT THE BEGINNING OF THE URL
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

