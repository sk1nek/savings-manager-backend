var app = angular.module('demo', [])

app.controller('Hello', function($scope, $http) {

    $scope.register = function(user){

        var username = user.username;
        var password = user.password;

        console.log(password);



        $http.post('/user/registration', user);
    }

});

// app.directive('username', function($q, $timeout) {
//     return {
//         require: 'ngModel',
//         link: function(scope, elm, attrs, ctrl) {
//             var usernames = ['Jim', 'John', 'Jill', 'Jackie'];
//
//             ctrl.$validators.username = function(modelValue, viewValue) {
//
//                 if(ctrl.$isEmpty(modelValue)){
//                     return false;
//                 }
//
//                if(modelValue.length > 8 && modelValue.length < 32){
//                    return true;
//                }
//
//                return false;
//             };
//         }
//     };
// });