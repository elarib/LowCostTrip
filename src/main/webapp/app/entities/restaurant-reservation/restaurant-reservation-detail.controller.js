(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .controller('RestaurantReservationDetailController', RestaurantReservationDetailController);

    RestaurantReservationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RestaurantReservation', 'User'];

    function RestaurantReservationDetailController($scope, $rootScope, $stateParams, previousState, entity, RestaurantReservation, User) {
        var vm = this;

        vm.restaurantReservation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('lowCostTripApp:restaurantReservationUpdate', function(event, result) {
            vm.restaurantReservation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
