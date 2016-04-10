//(function() {
angular.module('meimarie', ['ngRoute', 'angular-loading-bar', 'ngAnimate'])
    .config(function ($routeProvider) {
        $routeProvider.when('/transactions', {
            templateUrl: '../transactions.html',
            controller: 'transactionController'
        }).when('/stats', {
            templateUrl: '../statistics.html',
            controller: 'statisticsController'
        }).otherwise('/transactions');
    })
    .controller('transactionController', ['$scope', '$http', function ($scope, $http) {

        $scope.transactions = [];
        $scope.transaction = "";

        $scope.keyEvent = function (event) {
            if (event.keyCode === 13 && event.altKey === false) {
                addTransaction();
            }
        };

        $scope.getTransactionCss = function (type) {
            var css = {CA: 'icon-cash', CC: 'icon-credit-card', WT: 'icon-wire-transfer'};
            return css[type];
        };

        $scope.getAmountCss = function (amount) {
            return amount < 0 ? 'cash-expense' : 'cash-deposit';
        };

        $http.get("/api/transaction").then(function (resp) {
            console.log("loading data...");
            $scope.transactions = resp.data.content;
        }, function (resp) {
            console.log("Failed to load transactions.");
        });

        var addTransaction = function () {
            var trans = $scope.transaction;
            var date = parseDate(trans.match(/\d+\.\d+/i)[0]);
            console.log(date);
            var amount = parseFloat(trans.match(/-?\d+,\d+/i)[0]);
            var tags = trans.match(/#.+\b/gi);
            var transType = trans.match(/CC|WT|CA/i)[0];

            var payload = {date: date, amount: amount, tags: tags, transactionType: transType};
            console.log("Adding transaction " + JSON.stringify(payload));

            $http.post("/api/transaction", JSON.stringify(payload)).then(function (resp) {
                $scope.transactions.unshift(payload);
                $scope.transaction = "";
                console.log("Successfully transmitted ", JSON.stringify(payload));
                console.log(resp);
            }, function (resp) {
                console.log("Failed to transmit", JSON.stringify(payload));
                console.log(resp);
            });
        };


        var parseDate = function (strDate) {
            var dateElements = strDate.split(".");
            var day = dateElements[0];
            var month = dateElements[1];
            return new Date(Date.UTC(2016, parseInt(month) - 1, parseInt(day)));
        };


        //$http.get('/api/greeting', function (response) {
        //    $scope.greetings = response.data;
        //});
    }])
    .controller('statisticsController', ['$scope', '$http', function ($scope, $http) {
        //$http.get('/api/stats', function (response) {
        //    $scope.greetings = response.data;
        //});
    }]);

//})();