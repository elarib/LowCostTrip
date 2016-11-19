(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .controller('HotelReservationDetailController', HotelReservationDetailController);

    HotelReservationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HotelReservation', 'User'];

    function HotelReservationDetailController($scope, $rootScope, $stateParams, previousState, entity, HotelReservation, User) {
        var vm = this;

        vm.hotelReservation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('lowCostTripApp:hotelReservationUpdate', function(event, result) {
            vm.hotelReservation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
