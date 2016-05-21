angular.module("app", []);

angular.module("app").controller("PersonsController", ["$scope", "$http", function($scope, $http) {
    $scope.rows = [
        {firstName: "Dan", lastName: "Oprita"},
        {firstName: "Daniel", lastName: "Sarbe"},
        {firstName: "Ovidiu", lastName: "Petridean"}
    ];

    $scope.makeAjaxCall = function() {
        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/employees'
        }).then(function successCallback(response) {
            console.debug(response)
            $scope.rows = response.data._embedded.employees;
        })
    };

    $scope.makeAjaxCall();

    $scope.r2 = "R2";

    $scope.makeAjaxCall2 = function() {
        $http({
            method: 'GET',
            url: 'http://localhost:8080/analyze'
        }).then(function successCallback(response) {
            console.debug(response)
            $scope.r2 = response.data.sentences;
        })
    };
    $scope.makeAjaxCall2();
}]);
