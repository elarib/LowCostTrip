(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('search-resto', {
            parent: 'app',
            url: '/search-resto',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/searchResto/searchResto.html',
                    controller: 'SearchRestoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
