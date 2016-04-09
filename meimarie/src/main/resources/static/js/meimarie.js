//(function() {
angular.module('meimarie', [])
    //.config(function ($routeProvider) {
    //    $routeProvider.when('/', {
    //        templateUrl: 'index.html',
    //        controller: 'index'
    //    }).when('/stats', {
    //        templateUrl: 'stats.html',
    //        controller: 'stats'
    //    }).otherwise('/');
    //})
    .controller('index', ['$scope', '$http', function ($scope, $http) {

        $scope.transactions = [];
        $scope.transaction = "";

        $scope.keyEvent = function(event) {
          if (event.keyCode === 13 && event.altKey === false) {
              addTransaction();
          }
        };

        $http.get("/api/transaction").then(function(resp) {
            $scope.transactions = resp.data;
        }, function(resp) {
            console.log("Failed to load transactions.");
        });

        var addTransaction = function() {
            var trans = $scope.transaction;
            var strDate = trans.match(/\d+\.\d+/i)[0];
            var dateElements = strDate.split(".");
            var day = dateElements[0];
            var month = dateElements[1];
            var date = new Date(Date.UTC(2016, parseInt(month) - 1, parseInt(day)));
            console.log(date);
            var amount = parseFloat(trans.match(/-?\d+,\d+/i)[0]);
            var tags = trans.match(/#\w+/gi);
            var transType = trans.match(/CC|WT|CA|CW/i)[0];

            var payload = { date: date, amount: amount, tags: tags, transactionType: transType };
            console.log("Adding transaction " + JSON.stringify(payload));

            $http.post("/api/transaction", JSON.stringify(payload)).then(function(resp) {
                $scope.transactions.unshift(payload);
                $scope.transaction = "";
                console.log("Successfully transmitted ", JSON.stringify(payload));
                console.log(resp);
            }, function(resp) {
                console.log("Failed to transmit", JSON.stringify(payload));
                console.log(resp);
            });
        };









        //$http.get('/api/greeting', function (response) {
        //    $scope.greetings = response.data;
        //});
    }]);
//.controller('stats', ['$scope', '$http', function ($scope, $http) {
//    $http.get('/api/stats', function (response) {
//        $scope.greetings = response.data;
//    });
//}]);

//})();