(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hotel-reservation', {
            parent: 'entity',
            url: '/hotel-reservation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lowCostTripApp.hotelReservation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hotel-reservation/hotel-reservations.html',
                    controller: 'HotelReservationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hotelReservation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hotel-reservation-detail', {
            parent: 'entity',
            url: '/hotel-reservation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'lowCostTripApp.hotelReservation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hotel-reservation/hotel-reservation-detail.html',
                    controller: 'HotelReservationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hotelReservation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HotelReservation', function($stateParams, HotelReservation) {
                    return HotelReservation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hotel-reservation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hotel-reservation-detail.edit', {
            parent: 'hotel-reservation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hotel-reservation/hotel-reservation-dialog.html',
                    controller: 'HotelReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HotelReservation', function(HotelReservation) {
                            return HotelReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hotel-reservation.new', {
            parent: 'hotel-reservation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hotel-reservation/hotel-reservation-dialog.html',
                    controller: 'HotelReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                idHotel: null,
                                checkIn: null,
                                checkOut: null,
                                pricePerNight: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hotel-reservation', null, { reload: 'hotel-reservation' });
                }, function() {
                    $state.go('hotel-reservation');
                });
            }]
        })
        .state('hotel-reservation.edit', {
            parent: 'hotel-reservation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hotel-reservation/hotel-reservation-dialog.html',
                    controller: 'HotelReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HotelReservation', function(HotelReservation) {
                            return HotelReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hotel-reservation', null, { reload: 'hotel-reservation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hotel-reservation.delete', {
            parent: 'hotel-reservation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hotel-reservation/hotel-reservation-delete-dialog.html',
                    controller: 'HotelReservationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HotelReservation', function(HotelReservation) {
                            return HotelReservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hotel-reservation', null, { reload: 'hotel-reservation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
