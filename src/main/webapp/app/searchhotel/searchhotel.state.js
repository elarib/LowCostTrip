(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('search-hotel', {
            parent: 'app',
            url: '/search-hotel',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/searchhotel/searchhotel.html',
                    controller: 'SearchHotelController',
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
