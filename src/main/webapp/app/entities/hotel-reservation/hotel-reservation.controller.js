(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .controller('HotelReservationController', HotelReservationController);

    HotelReservationController.$inject = ['$scope', '$state', 'HotelReservation'];

    function HotelReservationController ($scope, $state, HotelReservation) {
        var vm = this;
        
        vm.hotelReservations = [];

        loadAll();

        function loadAll() {
            HotelReservation.query(function(result) {
                vm.hotelReservations = result;
            });
        }
    }
})();
