(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .controller('RestaurantReservationController', RestaurantReservationController);

    RestaurantReservationController.$inject = ['$scope', '$state', 'RestaurantReservation'];

    function RestaurantReservationController ($scope, $state, RestaurantReservation) {
        var vm = this;
        
        vm.restaurantReservations = [];

        loadAll();

        function loadAll() {
            RestaurantReservation.query(function(result) {
                vm.restaurantReservations = result;
            });
        }
    }
})();
