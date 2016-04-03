angular.module('meimarie')
    //.config(function ($routeProvider) {
    //    $routeProvider.when('/', {
    //        templateUrl: 'index.html',
    //        controller: 'index'
    //    }).when('/stats', {
    //        templateUrl: 'stats.html',
    //        controller: 'stats'
    //    }).otherwise('/');
    //})
    .controller('lisi', ['$scope', '$http', function ($scope, $http) {

        $scope.greetings = {de: 'Hallo', en: 'Hi'};

        //$http.get('/api/greeting', function (response) {
        //    $scope.greetings = response.data;
        //});
    }]);
//.controller('stats', ['$scope', '$http', function ($scope, $http) {
//    $http.get('/api/stats', function (response) {
//        $scope.greetings = response.data;
//    });
//}]);

