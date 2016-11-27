(function() {
    'use strict';

    angular
        .module('lowCostTripApp')
        .controller('RestaurantReservationDeleteController',RestaurantReservationDeleteController);

    RestaurantReservationDeleteController.$inject = ['$uibModalInstance', 'entity', 'RestaurantReservation'];

    function RestaurantReservationDeleteController($uibModalInstance, entity, RestaurantReservation) {
        var vm = this;

        vm.restaurantReservation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RestaurantReservation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
