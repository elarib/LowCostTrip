(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('restaurant-reservation', {
            parent: 'entity',
            url: '/restaurant-reservation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lowCostTripApp.restaurantReservation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/restaurant-reservation/restaurant-reservations.html',
                    controller: 'RestaurantReservationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('restaurantReservation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('restaurant-reservation-detail', {
            parent: 'entity',
            url: '/restaurant-reservation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lowCostTripApp.restaurantReservation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/restaurant-reservation/restaurant-reservation-detail.html',
                    controller: 'RestaurantReservationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('restaurantReservation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RestaurantReservation', function($stateParams, RestaurantReservation) {
                    return RestaurantReservation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'restaurant-reservation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('restaurant-reservation-detail.edit', {
            parent: 'restaurant-reservation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restaurant-reservation/restaurant-reservation-dialog.html',
                    controller: 'RestaurantReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RestaurantReservation', function(RestaurantReservation) {
                            return RestaurantReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('restaurant-reservation.new', {
            parent: 'restaurant-reservation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restaurant-reservation/restaurant-reservation-dialog.html',
                    controller: 'RestaurantReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                idResto: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('restaurant-reservation', null, { reload: 'restaurant-reservation' });
                }, function() {
                    $state.go('restaurant-reservation');
                });
            }]
        })
        .state('restaurant-reservation.edit', {
            parent: 'restaurant-reservation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restaurant-reservation/restaurant-reservation-dialog.html',
                    controller: 'RestaurantReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RestaurantReservation', function(RestaurantReservation) {
                            return RestaurantReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('restaurant-reservation', null, { reload: 'restaurant-reservation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('restaurant-reservation.delete', {
            parent: 'restaurant-reservation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/restaurant-reservation/restaurant-reservation-delete-dialog.html',
                    controller: 'RestaurantReservationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RestaurantReservation', function(RestaurantReservation) {
                            return RestaurantReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('restaurant-reservation', null, { reload: 'restaurant-reservation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
