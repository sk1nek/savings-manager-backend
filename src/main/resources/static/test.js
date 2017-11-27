angular.module('demo', [])
    .controller('Hello', function($scope, $http) {
        // $http.get('http://localhost:8080/api/test').
        // then(function(response) {
        //     $scope.greeting = response.data;
        // });

        var obj = {"title": "title", "details": "details", "isExpense": true, "value": 5000};


        $http.post('http://localhost:8080/api/user/addbalancechange', obj);
        console.log("XDD");
    });