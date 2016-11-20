(function() {
    'use strict';

    angular
        .module('lowCostTripApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            'checklist-model',
            'uiGmapgoogle-maps',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();

angular.module('lowCostTripApp').controller("checkCtrl",['$scope',function($scope){
    $scope.map = { center: { latitude: 45, longitude: -73 }, zoom: 8 };
    $scope.roles = [
        {id: 1, text: 'guest'},
        {id: 2, text: 'user'},
        {id: 3, text: 'customer'},
        {id: 4, text: 'admin'}
    ];
    $scope.user = {
        roles: [2, 4]
    };
    $scope.checkAll = function() {
        $scope.user.roles = $scope.roles.map(function(item) { return item.id; });
    };
    $scope.uncheckAll = function() {
        $scope.user.roles = [];
    };
    $scope.checkFirst = function() {
        $scope.user.roles.splice(0, $scope.user.roles.length);
        $scope.user.roles.push(1);
    };
}]);
