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

angular.module('lowCostTripApp').
controller("checkCtrl",['$scope', '$log', 'uiGmapGoogleMapApi',
    function ($scope, $log, GoogleMapApi){
    angular.extend($scope, {
        map: {center:
        {
            latitude: 40.1451,
            longitude: -99.6680
        },
            zoom: 4
        },
        searchbox: {
            template:'searchbox.tpl.html',
            events:{
                places_changed: function (searchBox) {}
            }
        },
        options: {
            scrollwheel: false
        }
    });

    GoogleMapApi.then(function(maps) {
        maps.visualRefresh = true;
    });
}]);
