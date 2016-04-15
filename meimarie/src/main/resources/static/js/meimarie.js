angular.module('meimarie', ['ngRoute', 'angular-loading-bar', 'ngAnimate', 'chart.js'])
    .config(function ($routeProvider) {
        $routeProvider.when('/transactions', {
            templateUrl: '../transactions.html',
            controller: 'transactionController'
        }).when('/stats', {
            templateUrl: '../statistics.html',
            controller: 'statisticsController'
        }).otherwise('/transactions');
    })

    // -------------------------- TRANSACTION PAGE CONTROLLER --------------------------

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
        }, function () {
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
    }])

    // -------------------------- STATS PAGE CONTROLLER --------------------------

    .controller('statisticsController', ['$scope', '$http', '$filter', function ($scope, $http, $filter) {
        $scope.stats = {descriptive: {}};

        $scope.loadSumsPerInterval = function (interval, transType) {
            $http.get("/api/stats/aggregation/sums_per_interval/" + transType + "/" + interval).then(function (resp) {
                var sumsPerInterval = resp.data;
                $scope.chartSeries = ['Sum of Transactions'];
                var dateFormat = 'dd-MM-yyyy';
                if (interval == "WEEK") {
                    dateFormat = "'KW'ww yyyy";
                } else if (interval == "MONTH") {
                    dateFormat = "LLLL yyyy";
                } else if (interval == "YEAR") {
                    dateFormat = "yyyy";
                }

                if (transType == 'spendings') {
                    $scope.chartDataSpendings = [new Array(sumsPerInterval.length)];
                    $scope.chartLabelsSpendings = new Array(sumsPerInterval.length);
                    for (i = 0; i <  sumsPerInterval.length; i++) {
                        $scope.chartDataSpendings[0][i] = sumsPerInterval[i].sum;
                        $scope.chartLabelsSpendings[i] = $filter('date')(sumsPerInterval[i].interval, dateFormat);
                    }
                } else {
                    $scope.chartDataWithdrawals = [new Array(sumsPerInterval.length)];
                    $scope.chartLabelsWithdrawals = new Array(sumsPerInterval.length);
                    for (i = 0; i <  sumsPerInterval.length; i++) {
                        $scope.chartDataWithdrawals[0][i] = sumsPerInterval[i].sum;
                        $scope.chartLabelsWithdrawals[i] = $filter('date')(sumsPerInterval[i].interval, dateFormat);
                    }
                }

                console.log(JSON.stringify(sumsPerInterval));
            }, function () {
                console.log("Failed to load bar chart data for " + transType + ".");
            });
        };

        //init data
        $scope.loadSumsPerInterval("MONTH", 'spendings');
        $scope.loadSumsPerInterval("MONTH", 'withdrawals');

        $scope.switchInterval = function(interval, transType) {
            $scope.loadSumsPerInterval(interval, transType);
        };

        $http.get("/api/stats/descriptive").then(function (resp) {
            $scope.stats.descriptive = resp.data;
            console.log(JSON.stringify($scope.stats.descriptive));
        }, function () {
            console.log("Failed to get the descriptive analysis.");
        });

        $http.get("/api/stats/top5").then(function (resp) {
            $scope.stats.top5 = resp.data;
            console.log(JSON.stringify($scope.stats.top5));
        }, function () {
            console.log("Failed to get the top 5 elements.");
        });

    }]);
